package com.cqyuanye.crawler.http;

import com.cqyuanye.common.dispatcher.Event;

/**
 * Created by kali on 2016/4/24.
 */
public interface Callback {

    void onSuccess(String html,Event event);
    void onFailed(String err,Event event);
}
