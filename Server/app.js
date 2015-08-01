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
			//console.log(socket.id+" somebody reconnecting");somebod
			if (users[e.id] != undefined) {
				users[e.id].socket = socket;
				// console.log("reconnected");
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
		if(!e || !e.mission_id || !e.user_id || !e.type || !e.message){
			makingAMistake(socket, 'chat');
		}else{
			var mission = missions[e.room_id];
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
			mission.addChatlog(msg);
			msg.mission_id = e.mission_id;
			for (var i in mission.users) {
				var s = mission.users[i].socket;
				s.emit('chat', msg);
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

	socket.on('get misionlog', function(e){
		if(!e || !e.mission_id){
			makingAMistake(socket, "get missionlog")
		}
		var mission = missions[e.mission_id];
		socket.emit('get missionlog success', {
			chatlog: mission.getChatLog(),
			metadata: mission.getMetaData()
		});
	});
});


console.log("Hello, chatserver is listening on port 9595");