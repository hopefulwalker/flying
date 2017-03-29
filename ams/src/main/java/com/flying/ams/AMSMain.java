/*
 Created by Walker.Zhang on 2015/2/22.
 Revision History:
 Date          Who              Version      What
2015/2/22     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.ams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class AMSMain {
    private final static Logger logger = LoggerFactory.getLogger(AMSMain.class);

    private final static String CFG_FILE_NAME = "com/flying/ams/ams-config.xml";

    public static void main(String args[]) {
        try {
            ApplicationController appCtrl = new ApplicationController(CFG_FILE_NAME);
            boolean done = false;
            System.out.println("Start application...");
            appCtrl.start();
            printInfo();
            while (!done) {
                Scanner in = new Scanner(System.in);
                String input = in.next().trim();
                if (input.equals("1")) {
                    appCtrl.start();
                } else if (input.equals("2")) {
                    appCtrl.stop();
                } else if (input.equals("3")) {
                    appCtrl.exit();
                    break;
                }
                printInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInfo() {
        System.out.println("\nPlease input your directive:");
        System.out.println("1=start");
        System.out.println("2=stop");
        System.out.println("3=exit");
    }
}
