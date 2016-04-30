package com.cqyuanye.crawler.http;

import com.cqyuanye.common.dispatcher.Dispatcher;
import com.cqyuanye.common.Service;
import com.cqyuanye.common.dispatcher.Event;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

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
        for (int i = 0; i < WORKER_NUM; i++) {
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
            getEvents.put((HttpEvent) e);
        } catch (InterruptedException e1) {
            //do nothing
        }
    }

    private class CrawlerThread extends Thread {

        private CloseableHttpClient client;

        @Override
        public void run() {

            client = HttpClients.createDefault();

            while (!Thread.interrupted()) {

                HttpEvent event;
                try {
                    event = getEvents.poll(5, TimeUnit.SECONDS);
                    if (event != null) {
                        try {
                            String url = event.url();
                            HttpGet get = new HttpGet(url);
                            CloseableHttpResponse res = client.execute(get);

                            if (res.getStatusLine().getStatusCode() == 200) {
                                StringBuilder sb = new StringBuilder();
                                InputStream is = res.getEntity().getContent();

                                byte[] buf = new byte[1024];
                                int read;
                                while ((read = is.read(buf)) > 0) {
                                    sb.append(new String(buf, 0, read, "utf-8"));
                                }
                                event.callback().onSuccess(sb.toString(), event);
                            }else{
                                System.out.println(res.getStatusLine().getStatusCode());
                            }
                            res.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            event.callback().onFailed(e.getMessage(), event);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            try {
                client.close();
            } catch (IOException e) {
                //do nothing
            }
        }
    }
}
