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
			console.log("somebody reconnecting");
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
			console.log(mission);
			missions[mission.id] = mission;
			var usr = users[e.ownerId];
			mission.addUser(usr);
			for (var i in users){
				var s = users[i].socket;
				s.emit("new mission success", {
					id: mission.id,
					type : mission.type,
					owner_id : e.ownerId,
					owner_name : usr.name
				});
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
			mission.addUser(user);
			socket.emit("join mission success", {
				id: mission.id,
				user : user.id
			});
		}
	});
	
	socket.on('chat', function(e){
		if(!e || !e.mission_id || !e.user_id || !e.type || !e.message){
			makingAMistake(socket, 'chat');
		}else{
			var mission = missions[e.room_id];
			var user_name = users[e.user_id].name;
			var msg = {
				message : e.message,
				type : e.type,
				name: user_name,
				user_id : e.user_id
			};
			mission.addChatlog(msg);
			msg.mission_id = e.mission_id;
			for (var i in mission.users) {
				var s = mission.users[i].socket;
				s.emit('chat success', msg);
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

	socket.on('get chatlog', function(e){
		if(!e || !e.mission_id){
			makingAMistake(socket, "get chat log")
		}
		var mission = missions[e.mission_id];
		socket.emit('get chat log success', {
			chatlog: mission.getChatLog(),
			metadata: mission.getMetaData()
		});
	});
});


console.log("Hello, chatserver is listening on port 9595");