package com.cqyuanye.crawler.http;

/**
 * Created by yuanye on 2016/4/23.
 */
public class GetKuwoLrcEvent implements HttpEvent {

    private final String url;

    public GetKuwoLrcEvent(String url) {
        this.url = url;
    }

    @Override
    public Class eventType() {
        return HttpEvent.class;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public Enum contentType() {
        return HttpEventType.KUWO_LRC;
    }
}
