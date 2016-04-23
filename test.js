var jsdom = require('jsdom')
var request = require('request');

request({uri: 'http://www.xiami.com/artist/2177'}, (error, response, body) => {
    if(error && response.statusCode != 200)
{
    console.log('Error');
}else{
    jsdom.env(body, (errors, window) => {
        var $ = require('jquery')(window)
        console.log($("td.song_name a").text());
})}
})
