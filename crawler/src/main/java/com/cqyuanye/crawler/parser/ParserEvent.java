package com.cqyuanye.crawler.parser;

import com.cqyuanye.common.Event;

/**
 * Created by yuanye on 2016/4/23.
 */
public interface ParserEvent extends Event{

    String html();
    Enum   type();
}
