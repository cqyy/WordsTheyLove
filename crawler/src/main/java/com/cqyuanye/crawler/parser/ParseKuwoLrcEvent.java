package com.cqyuanye.crawler.parser;

/**
 * Created by yuanye on 2016/4/23.
 */
public class ParseKuwoLrcEvent implements ParserEvent {

    private final String html;

    public ParseKuwoLrcEvent(String html) {
        this.html = html;
    }

    @Override
    public String html() {
        return html;
    }

    @Override
    public Enum type() {
        return ParserType.KUWO_LRC;
    }

    @Override
    public Class eventType() {
        return ParserEvent.class;
    }
}
