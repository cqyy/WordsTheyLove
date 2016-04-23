package com.cqyuanye.crawler.http;

import com.cqyuanye.common.Dispatcher;
import com.cqyuanye.common.Service;
import com.cqyuanye.crawler.parser.ParseKuwoListEvent;
import com.cqyuanye.crawler.parser.ParseKuwoLrcEvent;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by yuanye on 2016/4/23.
 */
public class HttpService implements Service {

    private final int WORKER_NUM = 5;

    private final BlockingQueue<HttpEvent> getEvents = new LinkedBlockingDeque<>();
    private final List<Thread> workers = new ArrayList<>(WORKER_NUM);
    private final Dispatcher dispatcher;

    public HttpService(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void start() {
        for (int i = 0; i < WORKER_NUM; i++){
            Thread thread = new CrawlerThread();
            workers.add(thread);
            thread.start();
        }
    }

    @Override
    public void shutdown() {
        workers.forEach(Thread::interrupt);
    }

    public void handle(HttpEvent event){
        try {
            getEvents.put(event);
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    private class CrawlerThread extends Thread{

        private CloseableHttpClient client;

        @Override
        public void run() {

            client = HttpClients.createDefault();

            while (!Thread.currentThread().isInterrupted()){
                try {
                    HttpEvent event = getEvents.take();
                    String url = event.url();
                    HttpGet get = new HttpGet(url);
                    HttpResponse res = client.execute(get);

                    if (res.getStatusLine().getStatusCode() == 200){
                        StringBuilder sb = new StringBuilder();
                        InputStream is = res.getEntity().getContent();

                        byte[] buf = new byte[1024];
                        int read;
                        while ( (read = is.read(buf)) > 0){
                            sb.append(new String(buf,0,read,"utf-8"));
                        }

                        if(event.contentType() == HttpEventType.KUWO_LIST){
                            dispatcher.handle(new ParseKuwoListEvent(sb.toString()));
                        }else if (event.contentType() == HttpEventType.KUWO_LRC){
                            dispatcher.handle(new ParseKuwoLrcEvent(sb.toString()));
                        }else {
                            //should not get here
                            assert false;
                        }
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
