package com.cqyuanye.crawler.fs;

import com.cqyuanye.common.dispatcher.Event;

/**
 * Created by yuanye on 2016/4/25.
 */
public abstract class FSEvent implements Event{

    @Override
    public Class eventType() {
        return FSEvent.class;
    }

    public abstract String filename();

    public abstract byte[] content();
}
