package com.cqyuanye.common;

import com.cqyuanye.common.dispatcher.Event;
import com.cqyuanye.common.dispatcher.EventHandler;

/**
 * Created by yuanye on 2016/4/23.
 */
public interface Service {

    void start();
    void shutdown();
    void handle(Event e);

    default EventHandler defaultHandler(){
        return (e)->handle(e);
    }
}
