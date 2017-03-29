/*
 Created by Walker.Zhang on 2015/3/15.
 Revision History:
 Date          Who              Version      What
2015/3/15     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.oms.msg;

public interface IOrderMsgType {
    public final static short Q_PLACE_ORDER = 1;
    public final static short OrderRequest = 1;
    public final static short P_PLACE_ORDER = 2;
    public final static short OrderReply = 2;
}
