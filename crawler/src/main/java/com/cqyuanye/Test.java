package com.cqyuanye;

import com.cqyuanye.common.AsynDispatcher;
import com.cqyuanye.common.Dispatcher;
import com.cqyuanye.crawler.http.GetKuwoListEvent;
import com.cqyuanye.crawler.http.HttpEvent;
import com.cqyuanye.crawler.http.HttpService;
import com.cqyuanye.crawler.parser.ParserEvent;
import com.cqyuanye.crawler.parser.ParserService;

/**
 * Created by yuanye on 2016/4/23.
 */
public class Test {

    public static void run(){
        Dispatcher dispatcher = new AsynDispatcher(5);

        HttpService httpService = new HttpService(dispatcher);
        ParserService parserService = new ParserService(dispatcher);

        dispatcher.registerEventHandler(HttpEvent.class,(event) ->httpService.handle((HttpEvent)event));
        dispatcher.registerEventHandler(ParserEvent.class,(event) -> parserService.handle((ParserEvent)event));

        httpService.start();
        parserService.start();

        GetKuwoListEvent event = new GetKuwoListEvent("http://www.kuwo.cn/artist/contentMusicsAjax?artistId=385&pn=1&rn=10000");

        dispatcher.handle(event);
    }

    public static void main(String[] args) {
        run();
    }
}
