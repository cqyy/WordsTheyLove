package com.cqyuanye.common.dispatcher;

/**
 * Created by yuanye on 2016/4/23.
 */
public interface Dispatcher {

    void handle(Event event);

    void registerEventHandler(Class eventType,EventHandler handler);

    EventHandler getHandler(Event event);
}
