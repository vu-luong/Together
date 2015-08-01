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

	this.users = new Array();
	this.missionlog = new Array();
	var _self = this;

	this.addUser = function(usr){
		if (_self.users[usr.id] != undefined) {
			console.log(_self.users[usr.id].name + " already in mission");
			return false;
		}
		_self.users[usr.id] = usr;
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
			minUsers: _self.minUsers,
			words: _self.words,
			owner_id: _self.owner,
			owner_name: _self.users[_self.owner].name
		}
	}
	this.getChatLog = function(){
		return this.missionlog;
	}
	/*
		message : e.message,
		type : e.type,
		name: user_name,
		user_id : e.user_id
	*/
	this.addChatlog = function(msg){
		_self.chatlog.push(msg);
	}

	console.log("defined");
	return this;
}