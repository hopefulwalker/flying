/*
 Created by Walker on 2017/4/15.
 Revision History:
 Date          Who              Version      What
 2017/4/15      Walker           0.1.0        Created. 
*/
package com.flying.monitor.model;

import com.flying.common.msg.codec.anno.Fields;

import java.lang.reflect.Field;

public class Test {
    public static void main(String args[]) {
        Class clazz = String.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }
    }
}
