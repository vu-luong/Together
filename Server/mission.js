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
	this.currentWordIdx = 0;
	this.currentAnswered_currentWord = {};
	this.result = {};

	this.started = false;
	this.finished = false;

	this.users = {};
	this.pending = {};
	this.missionlog = new Array();
	var _self = this;

	this.addUser = function(usr){
		if (_self.users[usr.id] != undefined) {
			console.log(_self.users[usr.id].name + " already in mission");
			return false;
		}
		_self.users[usr.id] = usr;
		_self.numUsers ++;
		_self.currentAnswered_currentWord[usr.id] = 0;
		_self.result[usr.id] = {};
		if(_self.hasPending(usr.id)){
			delete _self.pending[usr.id];
		}
		return true;
	}
	this.addToPending = function(usr){
		if (_self.users[usr.id] != undefined || _self.pending[usr.id] != undefined) {
			console.log(_self.users[usr.id].name + " already in mission or already in pending");
			return false;
		}
		_self.pending[usr.id] = usr;
		return true;
	}
	this.removeUserById = function(id){
		delete _self.currentAnswered_currentWord[id];
		delete _self.users[id];
	}
	this.hasUser = function(id){
		if(_self.users[id] != undefined) return true;
		else return false;
	}
	this.hasPending = function(id){
		if(_self.pending[id] != undefined) return true;
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
			numUsers: _self.numUsers,
			finished: _self.finished
		}
	}
	this.getMissionlog = function(){
		return this.missionlog;
	}
	/*
		message
		type
		name
		user_id
		user_avatar

		id
		clap
	*/
	this.addMissionlog = function(msg){
		msg.clap = 0;
		_self.missionlog.push(msg);
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

	this.getCurrentWord = function(){
		return _self.words[_self.currentWordIdx];
	}

	this.nextWord = function(){
		_self.currentAnswered_currentWord = {};
		for(var i in _self.users){
			_self.currentAnswered_currentWord[i] = 0;
		}
		if(_self.currentWordIdx <= _self.words.length-1)
			_self.currentWordIdx ++;
		return _self.getCurrentWord();
	}

	this.readyNextRound = function(){
		var cdt = 1;
		for(var i in _self.currentAnswered_currentWord){
			if(_self.currentAnswered_currentWord[i] == 0){
				cdt = 0;
				break;
			}
		}
		return cdt;
	}

	this.moveOn = function(id, answer){
		if(_self.currentAnswered_currentWord[id] > 3){
		}else{
			if(answer == _self.getCurrentWord())
				_self.result[id][_self.getCurrentWord()] = 10;

			_self.currentAnswered_currentWord[id] ++;
		}
		console.log("move on:",_self.currentAnswered_currentWord);
		if(_self.readyNextRound() && (_self.currentWordIdx < (_self.words.length-1) ) ){
			_self.nextWord();
		}else if(_self.readyNextRound() && (_self.currentWordIdx == (_self.words.length-1) ) ){
			_self.finished = true;
		}
	}

	this.isFinished = function(){
		console.log("_self.currentWordIdx: ",_self.currentWordIdx);
		console.log("_self.readyNextRound(): ",_self.readyNextRound());
		if( (_self.currentWordIdx >= (_self.words.length-1) ) && _self.readyNextRound()){
			return true;
		}
		return false;
	}

	this.clap = function(log_idx){
		_self.missionlog[log_idx].clap ++;
	}

	return this;
}