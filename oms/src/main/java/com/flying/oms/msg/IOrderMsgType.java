/*
 Created by Walker.Zhang on 2015/3/15.
 Revision History:
 Date          Who              Version      What
 2015/3/15     Walker.Zhang     0.1.0        Created.
 2017/6/2      Walker.Zhang     0.3.7        Rebuild the asynchronous communication engine.
*/
package com.flying.oms.msg;

public interface IOrderMsgType {
    short OrderRequest = 1;
    short OrderReply = 2;
}
