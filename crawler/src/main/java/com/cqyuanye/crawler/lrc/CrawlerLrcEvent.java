package com.cqyuanye.crawler.lrc;

import com.cqyuanye.common.dispatcher.Event;

/**
 * Created by yuanye on 2016/4/25.
 */
public class CrawlerLrcEvent implements Event{

    private final String singerId;

    public CrawlerLrcEvent(String singerId) {
        this.singerId = singerId;
    }

    @Override
    public Class eventType() {
        return CrawlerLrcEvent.class;
    }

    public String singerId(){
        return singerId;
    }
}
