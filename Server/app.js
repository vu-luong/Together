var Mission = require('./mission.js');
var User = require('./user.js');
var missions = {};
var users = {};

var io = require("socket.io").listen(9595);
io.sockets.on("connection", function(socket){
	/*
	 * handle login event
	 */
	socket.on('login', function(identity){
		
	});
	
	/*
	 * handle web socket instability 
	 */
	socket.on('relogin', function(identity){
	});
	
	socket.on('get mission', function(identity){
	});
	
	socket.on('new mission', function(identity){
		var mission = new Mission(identity.name)
//		console.log('new id: ',mission.id,'; name: ', identity.name);
		missions[mission.id] = mission;
//		for(var m in missions){
//			console.log(m,' - ',missions[m].id,' - ',missions[m].name);
//		}
	});
	
	socket.on('join mission', function(){
	});
});