package com.carry.pr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        while (true) {

            logger.info("info...3333333333333333333333333333333");
            logger.error("error...333333333323213");
            logger.warn("warn...2133333333333333333333333");
            Thread.sleep(1000);
        }

    }
}
