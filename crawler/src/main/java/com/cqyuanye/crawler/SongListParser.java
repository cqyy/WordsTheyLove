package com.cqyuanye.crawler;

import com.cqyuanye.common.dispatcher.Dispatcher;
import com.cqyuanye.common.dispatcher.Event;
import com.cqyuanye.crawler.LrcHttpEvent;
import com.cqyuanye.crawler.http.Callback;
import com.cqyuanye.crawler.http.HttpEvent;
import com.cqyuanye.crawler.parser.Parser;
import com.cqyuanye.crawler.parser.ParserEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kali on 2016/4/24.
 */
public class SongListParser implements Parser {

    private final Dispatcher dispatcher;

    public SongListParser(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }


    @Override
    public void parse(ParserEvent event) {
        String html = event.html();
        String regex = "<a href=\"(http://[\\w\\d./\\?=]+)\".*>([^>]+)</a>";
        Pattern pattern = Pattern.compile(regex);

        String[] lines = html.split("\n");

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String songUrl = matcher.group(1);

                HttpEvent httpEvent = new LrcHttpEvent(songUrl, new Callback() {
                    @Override
                    public void onSuccess(String html, Event event) {
                        //TODO
                    }

                    @Override
                    public void onFailed(String err, Event event) {
                        //TODO
                    }
                });
                dispatcher.handle(httpEvent);
            }
        }
    }
}
