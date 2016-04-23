package com.cqyuanye.crawler.parser;

import com.cqyuanye.common.Dispatcher;
import com.cqyuanye.common.Service;
import com.cqyuanye.crawler.http.GetKuwoLrcEvent;
import com.cqyuanye.crawler.http.HttpEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yuanye on 2016/4/23.
 */
public class ParserService implements Service {

    private final int WORKER_NUM = 5;

    private final BlockingQueue<ParserEvent> events = new LinkedBlockingDeque<>();
    private final List<Thread> workers = new ArrayList<>(WORKER_NUM);
    private final Dispatcher dispatcher;

    private final EnumMap<ParserType, Parser> parsers = new EnumMap<>(ParserType.class);

    public ParserService(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        parsers.put(ParserType.KUWO_LIST, new ListParser());
        parsers.put(ParserType.KUWO_LRC, new LrcParser());
    }


    @Override
    public void start() {
        for (int i = 0; i < WORKER_NUM; i++) {
            ParseThread thread = new ParseThread();
            workers.add(thread);
            thread.start();
        }
    }

    @Override
    public void shutdown() {
        workers.forEach(java.lang.Thread::interrupt);
    }

    private class ParseThread extends Thread {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {

                    ParserEvent event = events.take();
                    Parser parser = parsers.get(event.type());
                    assert parser != null;
                    parser.parse(event.html());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void handle(ParserEvent event) {
        try {
            events.put(event);
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    private class ListParser implements Parser {
        @Override
        public void parse(String html) {
            String regex = "<a href=\"(http://[\\w\\d./\\?=]+)\".*>([^>]+)</a>";
            Pattern pattern = Pattern.compile(regex);

            String[] lines = html.split("\n");

            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String songUrl = matcher.group(1);
                    HttpEvent httpEvent = new GetKuwoLrcEvent(songUrl);
                    dispatcher.handle(httpEvent);
                }
            }
        }
    }

    private class LrcParser implements Parser {

        @Override
        public void parse(String html) {
            Document doc = Jsoup.parse(html);
            String name = doc.select("#lrcName").first().text();
            String singer = doc.select(".artist span a").first().text();
            String lrc;
            StringBuilder sb = new StringBuilder();
            for(Element element :doc.select(".lrcItem")){
                sb.append(element.text());
                sb.append("\n");
            }

            lrc = sb.toString();

            System.out.println("歌名：" + name);
            System.out.println("歌手：" + singer);
            System.out.println("歌词：\n " + lrc);
        }
    }
}
