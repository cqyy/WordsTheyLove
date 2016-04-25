package com.cqyuanye.crawler;

import com.cqyuanye.crawler.fs.FSEvent;

/**
 * Created by yuanye on 2016/4/25.
 */
public class LrcFSEvent extends FSEvent {

    private final String filename;
    private final String content;

    public LrcFSEvent(String filename, String content) {
        this.filename = filename;
        this.content = content;
    }


    @Override
    public String filename() {
        return filename;
    }

    @Override
    public byte[] content() {
        return content.getBytes();
    }
}
