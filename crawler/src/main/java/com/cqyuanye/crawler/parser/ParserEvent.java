package com.cqyuanye.crawler.parser;

import com.cqyuanye.common.dispatcher.Event;

/**
 * Created by yuanye on 2016/4/23.
 */
public abstract class ParserEvent implements Event{

    public abstract String html();

}
