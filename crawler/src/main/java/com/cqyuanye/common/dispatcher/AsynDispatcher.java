package com.cqyuanye.common.dispatcher;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by yuanye on 2016/4/23.
 */
public class AsynDispatcher implements Dispatcher {

    private final static int DEFAULT_HANDLER_THREAD_NUM = 5;

    //private final BlockingQueue<Event> eventBlockingQueue = new LinkedBlockingDeque<>();
    private final Map<Class,EventHandler> handlers= new ConcurrentHashMap<>();
    private final ExecutorService executor;

    //private volatile boolean shutdown = false;

    public AsynDispatcher(int threads){
        executor = Executors.newFixedThreadPool(threads);
    }

    public AsynDispatcher(){
        this(DEFAULT_HANDLER_THREAD_NUM);
    }

    @Override
    public void handle(Event event){

        executor.execute(new EventHandleThread(event));
    }

    @Override
    public void registerEventHandler(Class eventType, EventHandler handler) {
        handlers.put(eventType,handler);
    }

    @Override
    public EventHandler getHandler(Event event) {
        return handlers.get(event.eventType());
    }

    public void shutdown(){
      //  shutdown = true;
        executor.shutdown();
    }


    private class EventHandleThread implements Runnable{

        private final Event event;

        private EventHandleThread(Event event) {
            this.event = event;
        }

        @Override
        public void run() {
            EventHandler handler = getHandler(event);
            if (handler !=null){
                handler.handle(event);
            }else{
                System.out.println("Could not find handler for event: " + event.eventType().getCanonicalName());
            }
        }
    }
}
