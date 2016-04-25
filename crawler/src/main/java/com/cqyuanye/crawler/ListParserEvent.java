package com.cqyuanye.crawler;

import com.cqyuanye.crawler.parser.ParserEvent;

/**
 * Created by yuanye on 2016/4/25.
 */
public class ListParserEvent extends ParserEvent {

    private final String html;

    public ListParserEvent(String html) {
        this.html = html;
    }


    @Override
    public String html() {
        return html;
    }

    @Override
    public Class eventType() {
        return ListParserEvent.class;
    }
}
