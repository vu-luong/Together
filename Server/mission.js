var exports = module.exports = {};
module.exports = function(name){
	return new Mission(name);
}
var mission_id = 0;
function newMissionId(){
	mission_id ++;
	return mission_id;
}

function Mission(type,owner,minUsers,words){
	this.id = newMissionId();
	this.type = type;
	this.owner = owner;
	this.minUsers = minUsers;
	this.words = words;

	this.users = new Array();
	this.missionlog = new Array();
	var _self = this;

	this.addUser = function(usr){
		if (_self.users[id] != undefined) {
			console.log(_self.users[id].name + " already in room");
			return false;
		}
		_self.users[id] = usr;
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
			owner: _self.owner
		}
	}
}