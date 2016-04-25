package com.cqyuanye.crawler;

import com.cqyuanye.common.Service;
import com.cqyuanye.common.dispatcher.AsynDispatcher;
import com.cqyuanye.common.dispatcher.Dispatcher;
import com.cqyuanye.crawler.fs.FSEvent;
import com.cqyuanye.crawler.fs.FSService;
import com.cqyuanye.crawler.http.HttpEvent;
import com.cqyuanye.crawler.http.HttpService;
import com.cqyuanye.crawler.lrc.CrawlerLrcEvent;
import com.cqyuanye.crawler.lrc.CrawlerLrcEventHandler;

/**
 * Created by yuanye on 2016/4/25.
 */
public class Crawler {

    public static void main(String[] args) {
        Dispatcher dispatcher = new AsynDispatcher(4);

        Service http = new HttpService(dispatcher);
        Service fs = new FSService("E:\\lrc");

        dispatcher.registerEventHandler(CrawlerLrcEvent.class,new CrawlerLrcEventHandler(dispatcher));
        dispatcher.registerEventHandler(FSEvent.class,fs.defaultHandler());
        dispatcher.registerEventHandler(HttpEvent.class,http.defaultHandler());
        dispatcher.registerEventHandler(ListParserEvent.class,new SongListParser(dispatcher));
        dispatcher.registerEventHandler(LrcParserEvent.class,new LrcParser(dispatcher));

        fs.start();
        http.start();

        CrawlerLrcEvent ce =new CrawlerLrcEvent("385");
        dispatcher.handle(ce);
    }
}
