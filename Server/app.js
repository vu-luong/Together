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


io.sockets.on('connection', function(socket){
	console.log(socket.id + " connected");
	socket.on('login', function(e){
		// console.log("on login");
		console.log(e.id, e.name);
		if(!e || !e.name || !e.id){
			makingAMistake(socket, "login");
		} else if (users[e.id] != undefined) {
			users[e.id].socket = socket;
			socket.emit("login success", {
				id : e.id
			});
		} else {
			var user = new User(e.id, e.name, socket);
			users[e.id] = user;
			console.log(e.id + " " + e.name);
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
					tmp.push(missions[i].getMetaData());
				}
			}
			socket.emit('get mission success', {
				missions: tmp
			});
		}
	});

	socket.on('new mission', function(e){
		if(!e || !e.name || !e.owner_id){
			makingAMistake(socket, "new mission");
		}else{
			console.log("new mission success");
			var misson = new Mission(e.name);
			missions[mission.id] = mission;
			var usr = users[e.owner_id];;
			mission.addUser(usr);
			console.log("number of users " +  users.length);
			for (var i in users){
				var s = users[i].socket;
				s.emit("new mission success", {
					id: mission.id,
					name: mission.name,
					user: users[i].getMetaData()
				});
			}
		}
	});

	socket.on('join mission', function(e){

		if(!e || !e.user_id){
			makingAMistake(socket, "join mission");
		}else{
			console.log(e.user_id + " entered room " + e.room_id);
			var room = rooms[e.room_id];
			room.addUserById(e.user_id);
			socket.emit("join room success", {
				id: room.id
			});
		}
	});
	socket.on('leave mission', function(e){

	});
	socket.on('chat', function(e){
		if(!e || !e.room_id || !e.user_id || !e.type || !e.message){
			makingAMistake(socket, "chat");
		}else{
			var room = rooms[e.room_id];
			var user_name = users[e.user_id].name;

			for (var i in room.userRoom) {

				var s = room.userRoom[i].socket;
				s.emit("chat", {
					message : e.message,
					type : e.type,
					url : e.url,
					name: user_name,
					user_id : e.user_id,
					room_id : e.room_id
				})
			}

		}
	});

	socket.on('generate words', function(e){
		var noOfWord = 5; // 1-3-1
		if(e && e.noOfWord){
			noOfWord = e.noOfWord;
		}
		words = require('./words.js');
		var rs = [];
		rs = rs.concat(words.get('easy',1));
		rs = rs.concat(words.get('normal',3));
		rs = rs.concat(words.get('hard',1));
		return rs;
	});

	socket.on('disconnect', function(){
		console.log(socket.id+" disconnect");
	});
});

console.log("Hello, chatserver is listening on port 9595");