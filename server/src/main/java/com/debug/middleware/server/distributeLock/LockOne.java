package com.debug.middleware.server.distributeLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockOne {
    private static final Logger log = LoggerFactory.getLogger(LockOne.class);

    public static void main(String[] args) {
        Thread tAdd = new Thread(new LockThread(100));
        Thread tSub = new Thread(new LockThread(-100));

        tAdd.start();
        tSub.start();
    }
}

class LockThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(LockThread.class);

    private int count;

    public LockThread(int count) {
        this.count = count;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {

                //synchronized (this){
                synchronized (SysConstant.amount){
                    SysConstant.amount = SysConstant.amount + count;
                    log.info("this time amount: {}", SysConstant.amount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



