/**
 * Created by Walker.Zhang on 2015/5/24.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/24     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.msg.converter;

public interface IMsgConverter {
    public short getMsgType(byte[] msg);
}
