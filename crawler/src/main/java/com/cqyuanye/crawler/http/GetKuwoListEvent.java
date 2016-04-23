package com.cqyuanye.crawler.http;

/**
 * Created by yuanye on 2016/4/23.
 */
public class GetKuwoListEvent implements HttpEvent {

    private final String url;

    public GetKuwoListEvent(String url) {
        //String bashUrl = "http://www.kuwo.cn/artist/contentMusicsAjax?artistId=${id}&pn=1&rn=100";
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
        return HttpEventType.KUWO_LIST;
    }
}
