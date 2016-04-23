package com.cqyuanye.crawler.http;

import com.cqyuanye.common.dispatcher.Event;

/**
 * Created by yuanye on 2016/4/23.
 */
public abstract class HttpEvent implements Event{

    public abstract String url();

    public abstract Callback callback();

    @Override
    public Class eventType() {
        return HttpEvent.class;
    }
}
