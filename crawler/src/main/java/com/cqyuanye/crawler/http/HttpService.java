package com.cqyuanye.crawler.http;

import com.cqyuanye.common.dispatcher.Dispatcher;
import com.cqyuanye.common.Service;
import com.cqyuanye.common.dispatcher.Event;
import org.apache.http.HttpResponse;
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

    @Override
    public void handle(Event e) {
        try {
            getEvents.put((HttpEvent)e);
        } catch (InterruptedException e1) {
            //do nothing
        }
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

                HttpEvent event;
                try {
                    event = getEvents.take();
                } catch (InterruptedException e) {
                    if (Thread.currentThread().isInterrupted()){
                        break;
                    }
                    continue;
                }
                try {
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

                       event.callback().onSuccess(sb.toString(),event);
                    }
                } catch (IOException e) {
                    event.callback().onFailed(e.getMessage(),event);
                }
            }
        }
    }
}
