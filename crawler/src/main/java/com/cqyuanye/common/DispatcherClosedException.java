package com.cqyuanye.common;

/**
 * Created by yuanye on 2016/4/23.
 */
public class DispatcherClosedException extends Exception {

    public DispatcherClosedException(){
        super();
    }

    public DispatcherClosedException(String msg){
        super(msg);
    }
}
