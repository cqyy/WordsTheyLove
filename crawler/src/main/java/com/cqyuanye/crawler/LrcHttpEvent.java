package com.cqyuanye.crawler;

import com.cqyuanye.crawler.http.Callback;
import com.cqyuanye.crawler.http.HttpEvent;

/**
 * Created by kali on 2016/4/24.
 */
public class LrcHttpEvent extends HttpEvent {

    private final String url;
    private final Callback hook;
    public LrcHttpEvent(String url,Callback hook) {
        this.url = url;
        this.hook = hook;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public Callback callback() {
        return hook;
    }
}
