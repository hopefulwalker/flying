/**
 * Created by Walker.Zhang on 2015/4/1.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/1     Walker.Zhang     0.1.0        Created.
 */

package com.flying.util.common;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class DictionaryTest {

    @Test
    public void testGetString() throws Exception {
        assertEquals("1", Dictionary.getString(UUID.randomUUID().toString(), (short) 1));
    }
}