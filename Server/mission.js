var exports = module.exports = {};
module.exports = function(type,owner,minUsers,words){
	return new Mission(type,owner,minUsers,words);	
} 
var mission_id = 0;
function newMissionId(){
	mission_id ++;
	return mission_id;
}

function Mission(type,owner,minUsers,words){
	this.id = newMissionId();
	this.type = type;
	this.owner = owner; // id
	this.minUsers = minUsers;
	this.words = words;
	this.numUsers = 0;

	this.started = false;

	this.users = {};
	this.missionlog = new Array();
	var _self = this;

	this.addUser = function(usr){
		if (_self.users[usr.id] != undefined) {
			console.log(_self.users[usr.id].name + " already in mission");
			return false;
		}
		_self.users[usr.id] = usr;
		_self.numUsers ++;
		return true;
	}
	this.removeUserById = function(id){
		delete userRoom[id];
	}
	this.hasUser = function(id){
		if(_self.users[id] != undefined) return true;
		else return false;
	}
	this.getMetaData = function(){
		return {
			id : _self.id,
			type: _self.type,
			minUsers: _self.minUsers, // min number of users to start
			words: _self.words,
			owner_id: _self.owner,
			owner_name: _self.users[_self.owner].name,
			numUsers: _self.numUsers
		}
	}
	this.getChatLog = function(){
		return this.missionlog;
	}
	/*
		message
		type
		name
		user_id
		user_avatar
	*/
	this.addChatlog = function(msg){
		_self.chatlog.push(msg);
	}

	this.isStartable = function(){
		if(_self.started) return false;
		var l=0;
		for(var i in _self.users){
			l++;
		}
		if(l >= _self.minUsers){
			return true;
		}else{
			return false;
		}
	}

	console.log("defined");
	return this;
}