var exports = module.exports = {};
module.exports = function(name){
	return words;
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
words.get = function(type, howmany){
	return getFromStack(words[type]);
}
function getFromStack(stack, howmany){
	var rs = [];
	for(var i=0;i<howmany;i++){
		var txt = stack[parseInt(Math.random()*stack.length)];
		while(rs.indexOf(txt) == -1){
			txt = stack[parseInt(Math.random()*stack.length)];
		}
		rs.push(txt);
	}
	return rs;
}