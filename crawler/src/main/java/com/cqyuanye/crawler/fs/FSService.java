package com.cqyuanye.crawler.fs;

import com.cqyuanye.common.Service;
import com.cqyuanye.common.dispatcher.Event;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuanye on 2016/4/24.
 */
public class FSService implements Service {

    private String basePath;
    private final int ThreadNum = 3;
    private final BlockingQueue<FSEvent> events = new LinkedBlockingDeque<>();
    private final List<Thread> writers = new ArrayList<>(ThreadNum);

    public FSService(String basePath) {
        this.basePath = basePath;

        File path = new File(basePath);
        if (!path.exists()){
            if (!path.mkdirs()){
                System.err.println("Could not could create dir: " + basePath);
            }
        }
    }

    @Override
    public void start() {
        for (int i = 0; i < ThreadNum;i++){
            WriterThread writer = new WriterThread();
            writers.add(writer);
            writer.start();
        }
    }

    @Override
    public void shutdown() {
        writers.forEach(Thread::interrupt);
    }

    @Override
    public void handle(Event e) {
        try {
            events.put((FSEvent)e);
        } catch (InterruptedException e1) {
            //do nothing
        }
    }


    private class WriterThread extends Thread{

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()){

                FSEvent event;
                OutputStream os = null;
                try {
                    event = events.poll(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    continue;
                }

                try{
                    File file = new File(basePath,event.filename());
                    if (!file.exists()){
                        if(!file.createNewFile()){
                            System.err.println("Create file failed. " + file.getAbsolutePath());
                            continue;
                        }
                    }
                    os = new FileOutputStream(file);
                    byte[] content = event.content();
                    os.write(content);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (os != null){
                        try {
                            os.close();
                        } catch (IOException e) {
                            //do nothing
                        }
                    }
                }
            }
        }
    }
}
