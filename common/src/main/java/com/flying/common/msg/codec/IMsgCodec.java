/*
 Created by Walker.Zhang on 2015/5/24.
 Revision History:
 Date          Who              Version      What
 2015/5/24     Walker.Zhang     0.1.0        Created.
 2017/4/14     Walker.Zhang     0.3.2        Refactor to rebuild the codec system.
*/
package com.flying.common.msg.codec;

/**
 * The interface for codec. The codec should provide two kind of works:
 * 1. byte <-> message.
 */
public interface IMsgCodec {
    short getMsgType(byte[] msg);
}