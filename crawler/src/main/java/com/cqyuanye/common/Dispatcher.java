package com.cqyuanye.common;

/**
 * Created by yuanye on 2016/4/23.
 */
public interface Dispatcher {

    void handle(Event event);

    void registerEventHandler(Class eventType,EventHandler handler);

    EventHandler getHandler(Event event);
}
