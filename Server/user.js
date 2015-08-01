var exports = module.exports = {};
module.exports = function(id, name, avatar, socket){
	return new User(id, name, avatar, socket);
}

function User(id, name, avatar, socket){
	this.id = id;
	this.name = name;
	this.avatar = avatar;

	this.socket = socket;

	
	var _self = this;
	this.getMetaData = function(){
		return {
			'id': _self.id,
			'name': _self.name,
			'avatar': _self.avatar
		}
	};

	return this;
}