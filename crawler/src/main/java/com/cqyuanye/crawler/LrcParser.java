package com.cqyuanye.crawler;

import com.cqyuanye.common.dispatcher.Dispatcher;
import com.cqyuanye.common.dispatcher.Event;
import com.cqyuanye.common.dispatcher.EventHandler;
import com.cqyuanye.crawler.fs.FSEvent;
import com.cqyuanye.crawler.parser.ParserEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by kali on 2016/4/24.
 */
public class LrcParser implements EventHandler {

    private final Dispatcher dispatcher;

    public LrcParser(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }


    @Override
    public void handle(Event event) {
        ParserEvent pe = (ParserEvent)event;

        Document doc = Jsoup.parse(pe.html());
        String name = doc.select("#lrcName").first().text();
        String singer = doc.select(".artist span a").first().text();
        String lrc;
        StringBuilder sb = new StringBuilder();
        for(Element element :doc.select(".lrcItem")){
            sb.append(element.text());
            sb.append("\r\n");
        }

        lrc = sb.toString();

        if (name != null && name.trim().length() > 0
                && singer != null && singer.trim().length() > 0
                && lrc.trim().length() > 0){

            String filename = name + "_" + singer + ".txt";

            FSEvent fsEvent = new LrcFSEvent(filename,lrc);
            dispatcher.handle(fsEvent);
        }else{
            System.out.println("Parse event " + pe + " failed.");
        }
    }
}
