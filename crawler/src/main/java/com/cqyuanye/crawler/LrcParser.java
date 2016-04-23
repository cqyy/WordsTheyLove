package com.cqyuanye.crawler;

import com.cqyuanye.crawler.http.Callback;
import com.cqyuanye.crawler.parser.Parser;
import com.cqyuanye.crawler.parser.ParserEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by kali on 2016/4/24.
 */
public class LrcParser implements Parser {
    @Override
    public void parse(ParserEvent event) {

        Document doc = Jsoup.parse(event.html());
        String name = doc.select("#lrcName").first().text();
        String singer = doc.select(".artist span a").first().text();
        String lrc;
        StringBuilder sb = new StringBuilder();
        for(Element element :doc.select(".lrcItem")){
            sb.append(element.text());
            sb.append("\n");
        }

        lrc = sb.toString();

        if (name != null && name.trim().length() > 0
                && singer != null && singer.trim().length() > 0
                && lrc.trim().length() > 0){

            //TODO change to real handling
            System.out.println("¸èÃû£º" + name);
            System.out.println("¸èÊÖ£º" + singer);
            System.out.println("¸è´Ê£º\n " + lrc);
        }else{
            System.out.println("Parse event " + event + " failed.");
        }
    }
}
