var jsdom = require('jsdom')
var fs = require('fs')

//var html = '<html><body><h1>Hello World!</h1><p class="hello">Heya Big World!</body></html>'

fs.readFile('./test.html','utf-8',(error,html) =>{
	if (error) {
		console.log(error)
	}else{
		jsdom.env(html,function(errors,window){
			var $ = require('jquery')(window);
			console.log($('.song_name a').text())
			// $('.song_name').each( () =>{
			// 	console.log($(this).text())
			});
		
	}
});

