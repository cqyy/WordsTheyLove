package com.cqyuanye.crawler.lrc;

import com.cqyuanye.common.dispatcher.Dispatcher;
import com.cqyuanye.common.dispatcher.Event;
import com.cqyuanye.common.dispatcher.EventHandler;
import com.cqyuanye.crawler.ListParserEvent;
import com.cqyuanye.crawler.SongListHttpEvent;
import com.cqyuanye.crawler.http.Callback;

/**
 * Created by yuanye on 2016/4/25.
 */
public class CrawlerLrcEventHandler implements EventHandler {

    private final String baseURL = "http://www.kuwo.cn/artist/contentMusicsAjax?artistId=${singerID}&pn=1&rn=10000";
    private final Dispatcher dispatcher;

    public CrawlerLrcEventHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle(Event event) {
        if (! (event instanceof CrawlerLrcEvent)){
            System.err.println("Error event for " + CrawlerLrcEvent.class.getCanonicalName() + " ,need for CrawlerLrcEvent,found "
                    + event.getClass().getCanonicalName());
        }

        CrawlerLrcEvent ce = (CrawlerLrcEvent)event;

        SongListHttpEvent se = new SongListHttpEvent(baseURL.replace("${singerID}", ce.singerId()), new Callback() {
            @Override
            public void onSuccess(String html, Event event) {
                ListParserEvent le = new ListParserEvent(html);
                dispatcher.handle(le);
            }

            @Override
            public void onFailed(String err, Event event) {
                System.err.println("Parse Error");
            }
        });

        dispatcher.handle(se);
    }
}
