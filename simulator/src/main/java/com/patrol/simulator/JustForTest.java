/**
 * Created by Walker.Zhang on 2015/7/21.
 * Revision History:
 * Date          Who              Version      What
 * 2015/7/21     Walker.Zhang     0.1.0        Created.
 */
package com.patrol.simulator;

import java.lang.reflect.Field;

public class JustForTest {
    public static void main(String args[]) {
        Field[] fields = City.class.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
            System.out.println(field.getType().getName());
            System.out.println(field.getType().getSimpleName());
        }
    }

    public static class City {
        private byte id;
        private double money;
        private String name;
    }
}

