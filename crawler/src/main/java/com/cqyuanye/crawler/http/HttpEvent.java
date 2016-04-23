package com.cqyuanye.crawler.http;

import com.cqyuanye.common.Event;

/**
 * Created by yuanye on 2016/4/23.
 */
public interface HttpEvent extends Event{

    String url();

    Enum contentType();
}
