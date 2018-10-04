package com.hzy.log;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LogHelper {
    public static Logger logger = LogManager.getLogger(LogHelper.class);
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            logger.warn(i+": warn test line...");
            logger.error(i+": error test line...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
