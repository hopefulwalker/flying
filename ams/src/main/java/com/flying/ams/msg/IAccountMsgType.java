/*
 Created by Walker.Zhang on 2015/3/15.
 Revision History:
 Date          Who              Version      What
2015/3/15     Walker.Zhang     0.1.0        Created. 
 */
package com.flying.ams.msg;

public interface IAccountMsgType {
    public final static short Q_GET_ACCOUNT_BY_ID = 1;
    public final static short P_GET_ACCOUNT_BY_ID = 2;
    public final static short GetAccountByIdRequest = 1;
    public final static short GetAccountByIdReply = 2;
}
