var Mission = require('./mission.js');
var User = require('./user.js');
var missions = {};
var users = {};

var io = require("socket.io").listen(9595);

function makingAMistake(socket, evt, message){
	if(!message) message = 'not enough info';
	socket.emit("service failure", {
		event: evt,
		reason: message
	});
}
words = require("./words.js");
words.init();


io.sockets.on('connection', function(socket){
	console.log(socket.id + " connected");
	socket.on('login', function(e){
		// console.log("on login");
		if(!e || !e.name || !e.id){
			makingAMistake(socket, "login");
		} else if (users[e.id] != undefined) {
			/*
			* login
			*/
			users[e.id].socket = socket;
			socket.emit("login success", {
				id : e.id
			});
		} else {
			/*
			* register
			*/
			var user = User(e.id, e.name, e.avatar, socket);
			users[e.id] = user;
			console.log("line34: ",e.id + " " + e.name + " "+socket.id);
			var tmp = new Array();
			console.log("user registered: "+user.getMetaData().id+", "+user.getMetaData().name);
			socket.emit("login success", {
				id: user.id
			});
		}
	});

	socket.on('relogin', function(e) {
		if (!e || !e.id) {
			makingAMistake(socket, "relogin");
		} else {
			console.log(socket.id+" somebody reconnecting");
			if (users[e.id] != undefined) {
				users[e.id].socket = socket;
				console.log("reconnected");
			}
		}
	});

	socket.on('get mission', function(e){
		if (!e || !e.user_id) {
			makingAMistake(socket, "get mission");
		} else {
			console.log("get mission success");
			tmp = new Array();
			for (var i in missions) {
				if(missions[i].hasUser(e.user_id)){
					var m = missions[i].getMetaData();
					var mdata = users[m.owner_id].getMetaData();
					m.owner_name = mdata.name;
					m.joined = true;
					tmp.push(m);
				}else if(missions[i].hasPending(e.user_id)){
					var m = missions[i].getMetaData();
					var mdata = users[m.owner_id].getMetaData();
					m.owner_name = mdata.name;
					m.joined = false;
					tmp.push(m);
				}
			}
			socket.emit('get mission success', {
				missions: tmp
			});
		}
	});

	socket.on('new mission', function(e){
		if(!e || !e.type || !e.ownerId || !e.minUsers || !e.words){
			makingAMistake(socket, "new mission");
		}else{
			console.log("new mission success");
			var mission = Mission(e.type,e.ownerId,e.minUsers,e.words);
			missions[mission.id] = mission;
			var usr = users[e.ownerId];
			mission.addUser(usr);
			var l = 0;
			for (var i in users){
				console.log("users["+i+"]:"+users[i].id+", "+users[i].name);
				l++;
			}
			console.log("users.length: "+l);
			for (var i in users){
				var s = users[i].socket;
				if(s == undefined){
					console.log("socket is null ", users[i].getMetaData());

				}else{
					s.emit("new mission success", mission.getMetaData());
					s.emit("mission created", mission.getMetaData());	
				}
			}
		}
	});

	socket.on('join mission', function(e){
		if(!e || !e.user_id || !e.mission_id){
			makingAMistake(socket, "join mission");
		}else{
			console.log(e.user_id + " entered mission " + e.mission_id);
			var mission = missions[e.mission_id];
			var user = users[e.user_id]
			if(mission.addUser(user)){
				var mUsers = mission.users;
				for(var usrId in mUsers){
					var usr = mUsers[usrId];
					if(usr != user){
						if(usr.socket){
							console.log("log log asldjaldjaklsdjldjlkasjdksaj");
							usr.socket.emit('people join mission', {
								mission_id : mission.id,
								newly_joined_id : user.id,
								newly_joined_name : user.name,
								newly_joined_avatar : user.avatar
							});
						}else{
							/* TODO: offline, do something */
							console.log(usr.id+" is offline");
						}
					}
				}
			}
			/*
			socket.emit("join mission success", {
				id: mission.id,
				user : user.id
			});
			*/

			if(mission.isStartable()){
				mission.started = true;
				for(var usr in mUsers){
					if(usr.socket){
						usr.socket.emit('mission start', {
							mission_id : mission.id
						});
					}else{
						/* TODO: offline, do something */
					}
				}
			}
		}
	});
	
	socket.on('chat', function(e){
		if(!e || !e.mission_id || !e.user_id || !e.type){
			makingAMistake(socket, 'chat');
		}else{
			var mission = missions[e.mission_id];
			var user_name = users[e.user_id].name;
			var user_avatar = users[e.user_id].avatar;
			var msg = {
				message : e.message,
				url: e.url,
				type : e.type,
				name: user_name,
				user_id : e.user_id,
				user_avatar : user_avatar
			};
			console.log('msg: '+e.message);
			mission.addMissionlog(msg);
			if(e.type == 100){
				for (var i in mission.users) {
					var s = mission.users[i].socket;
					if(s){
						msg.mission_id = e.mission_id;
						var correct = false;
						if(e.message == mission.getCurrentWord()){
							correct = true;
						}
						msg.correct = correct;
						s.emit('chat', msg);
					}
				}
			}else{
				for (var i in mission.users) {
					var s = mission.users[i].socket;
					if(s){
						msg.mission_id = e.mission_id;
						s.emit('chat', msg);
					}
				}
			}
			

			if(e.type == 100){ /* this is an answer */
				mission.moveOn(e.user_id, e.message);
				for(var i in mission.users){
					var usr = mission.users[i];
					if(usr.socket){
						var w = 0;
						for(var k in mission.currentAnswered_currentWord){
							if(mission.currentAnswered_currentWord[k] > 0) w++;
						}
						usr.socket.emit('get current word success', {
							word: mission.getCurrentWord(),
							currentAnswered : w,
							numUsers: mission.numUsers
						});
					}else{
						/* offline, do something! */
					}
				}
				if(mission.readyNextRound()){
					/* tell all user to move to the next round */
					for(var usrIdx in mission.users){
						var usr = mission.users[usrIdx];
						if(usr.socket){
							usr.socket.emit('push word', {
								word: mission.getCurrentWord()
							});
						}
					}
				}else if(mission.isFinished()){
					/* tell all user that this mission is finished */
					for(var usrIdx in mission.users){
						var usr = mission.users[usrIdx];
						if(usr.socket){
							var _result = mission.result;
							var final_result = {};
							for(var i in _result){
								final_result[i] = {
									"point": 0,
									"user_id" : i,
									"user_name" : mission.users[i].name,
									"user_avatar" : mission.users[i].avatar
								};
								for(var j in _result[i]){
									final_result[i].point += _result[i][j];
								}
							}
							usr.socket.emit('mission finished', {
								words: mission.words,
								result: final_result
							});
						}
					}
				}
			}
		}
	});

	socket.on('generate words', function(e){
		var noOfWord = 5; // 1-3-1
		if(e && e.noOfWord){
			noOfWord = e.noOfWord;
		}
		var words = require('./words.js');
		var rs = [];
		rs = rs.concat(words.get('easy',parseInt(noOfWord*0.2)));
		rs = rs.concat(words.get('normal',parseInt(noOfWord*0.6)));
		rs = rs.concat(words.get('hard',noOfWord - parseInt(noOfWord*0.2) - parseInt(noOfWord*0.6)));
		console.log(noOfWord," ",rs);
		socket.emit('generate words success', {
			"words" : rs
		});
	});

	socket.on('disconnect', function(){
		console.log(socket.id+" disconnect");
		for(var id in users){
			users[id].socket = undefined;
		}
	});

	socket.on('get all missions', function(){
		var tmp = [];
		for(var i in missions){
			tmp.push(missions[i].getMetaData());
		}
		socket.emit('get all missions success', {
			missions : tmp
		});
	});

	socket.on('get missionlog', function(e){
		console.log('socket.mission log');
		if(!e || !e.mission_id){
			makingAMistake(socket, "get missionlog")
		}else{
			console.log('get mission log');
			var mission = missions[e.mission_id];
			console.log("get missionlog: ",mission.getMissionlog());
			socket.emit('get missionlog success', {
				missionlog: mission.getMissionlog(),
				metadata: mission.getMetaData()
			});	
		}
	});

	socket.on('get current word', function(e){ /* get the current word to pronounce */
		if(!e || !e.mission_id){
			makingAMistake(socket, "get word");
		}else{
			var mission = missions[e.mission_id];
			var w = 0;
			for(var i in mission.currentAnswered_currentWord){
				if(mission.currentAnswered_currentWord[i] > 0) w++;
			}
			socket.emit('get current word success', {
				word: mission.getCurrentWord(),
				currentAnswered : w,
				numUsers: mission.numUsers
			});
		}
	});

	socket.on('invite', function(e){
		if(!e || !e.from_user_id || !e.to_user_id || !e.mission_id){
			makingAMistake(socket, "invite");
		}else{
			var from_user = users[e.from_user_id];
			var to_user = users[e.to_user_id];
			var mission = missions[e.mission_id];

			if(to_user.socket){
				to_user.socket.emit('invited', {
					from_user_name : from_user.name,
					from_user_avatar : from_user.avatar,
					mission_id : e.mission_id
				});
				mission.addToPending(to_user);
			}else{
				/* offline */
			}
		}
	})
});


console.log("Hello, chatserver is listening on port 9595");