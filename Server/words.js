var exports = module.exports = {};
module.exports = {
	init: function(){
		init();
	},
	get : function(type, howmany){
		return getFromStack(words[type], howmany);
	}
}
words = {};
words = {
	'easy':[
	'a','b','c'
	],	
	'normal':[
	'd','e','f'
	],
	'hard':[
	'g','h','j'
	]
}
function init(){
	var fs = require('fs');
	fs.readFile('res/data/Word_Beginner.txt', function(err, data){
		if(err) throw err;
		words['easy'] = data.toString().toLowerCase().split("\n");
		console.log("Easy words loaded");
	});
	fs.readFile('res/data/Word_Intermediate.txt', function(err, data){
		if(err) throw err;
		words['normal'] = data.toString().toLowerCase().split("\n");
		console.log("Normal words loaded");
	});
	fs.readFile('res/data/Word_advanced.txt', function(err, data){
		if(err) throw err;
		words['hard'] = data.toString().toLowerCase().split("\n");
		console.log("Hard words loaded");
	});
}
function getFromStack(stack, howmany){
	var rs = [];
	for(var i=0;i<howmany;i++){
		var txt = stack[parseInt(Math.random()*stack.length)];
		while(rs.indexOf(txt) != -1){
			txt = stack[parseInt(Math.random()*stack.length)];
		}
		rs.push(txt);
	}
	return rs;
}