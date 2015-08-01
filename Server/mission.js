var exports = module.exports = {};
module.exports = function(name){
	return new Mission(name);
}
var mission_id = 0;
function newMissionId(){
	mission_id ++;
	return mission_id;
}

function Mission(name){
	this.id = newMissionId();
	this.name = name;
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
}