var exports = module.exports = {};
module.exports = function(){
	return new User();
}

function User(name){
	this.name = name;
}