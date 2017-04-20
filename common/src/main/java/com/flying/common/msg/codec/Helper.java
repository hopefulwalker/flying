/*
 Created by Walker.Zhang on 2015/3/31.
 Revision History:
 Date          Who              Version      What
 2015/3/31     Walker.Zhang     0.1.0        Created.
 2017/4/14     Walker.Zhang     0.3.2        Refactor to rebuild the codec system.*
*/
package com.flying.common.msg.codec;

import com.flying.common.IReturnCode;
import com.flying.common.service.ServiceException;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IClientEngine;
import com.flying.framework.messaging.event.IMsgEvent;
import com.flying.framework.messaging.event.impl.MsgEvent;
import com.flying.framework.messaging.event.impl.MsgEventInfo;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;


public class Helper {
    public static byte[] request(IClientEngine engine, int timeout, byte[] bytes) throws ServiceException {
        IMsgEvent requestEvent = new MsgEvent(IMsgEvent.ID_REQUEST, engine, new MsgEventInfo(bytes));
        IMsgEvent replyEvent = engine.request(requestEvent, timeout);
        return buildReplyBytes(engine, timeout, replyEvent);
    }

    public static byte[] buildReplyBytes(IClientEngine engine, int timeout, IMsgEvent replyEvent) throws ServiceException {
        switch (replyEvent.getId()) {
            case IMsgEvent.ID_REPLY_SUCCEED:
                return replyEvent.getInfo().getByteArray();
            case IMsgEvent.ID_REPLY_TIMEOUT:
                throw new ServiceException(IReturnCode.TIMEOUT, buildExceptionMessage(engine, timeout, replyEvent));
            case IMsgEvent.ID_REPLY_UNSUPPORTED:
                throw new ServiceException(ServiceException.UNSUPPORTED_SERVICE, buildExceptionMessage(engine, timeout, replyEvent));
            case IMsgEvent.ID_REPLY_FAILED:
                throw new ServiceException(IReturnCode.UNKNOWN_FAILURE, buildExceptionMessage(engine, timeout, replyEvent));
            default:
                StringBuilder sb = new StringBuilder("EventId:").append("[").append(replyEvent.getId()).append("]");
                throw new ServiceException(IReturnCode.INVALID_EVENT, sb.toString());
        }
    }

    public static DirectBuffer request(IClientEngine engine, int timeout, DirectBuffer requestBuffer) throws ServiceException {
        IMsgEvent requestEvent = new MsgEvent(IMsgEvent.ID_REQUEST, engine, new MsgEventInfo(requestBuffer.byteArray()));
        IMsgEvent replyEvent = engine.request(requestEvent, timeout);
        return buildReplyBuffer(engine, timeout, replyEvent);
    }

    private static DirectBuffer buildReplyBuffer(IClientEngine engine, int timeout, IMsgEvent replyEvent) throws ServiceException {
        switch (replyEvent.getId()) {
            case IMsgEvent.ID_REPLY_SUCCEED:
                return new UnsafeBuffer(replyEvent.getInfo().getByteArray());
            case IMsgEvent.ID_REPLY_TIMEOUT:
                throw new ServiceException(IReturnCode.TIMEOUT, buildExceptionMessage(engine, timeout, replyEvent));
            case IMsgEvent.ID_REPLY_UNSUPPORTED:
                throw new ServiceException(ServiceException.UNSUPPORTED_SERVICE, buildExceptionMessage(engine, timeout, replyEvent));
            case IMsgEvent.ID_REPLY_FAILED:
                throw new ServiceException(IReturnCode.UNKNOWN_FAILURE, buildExceptionMessage(engine, timeout, replyEvent));
            default:
                StringBuilder sb = new StringBuilder("EventId:").append("[").append(replyEvent.getId()).append("]");
                throw new ServiceException(IReturnCode.INVALID_EVENT, sb.toString());
        }
    }

    private static String buildExceptionMessage(IClientEngine engine, int timeout, IMsgEvent replyEvent) {
        StringBuilder stringBuilder = new StringBuilder("Endpoints:");
        for (IEndpoint endpoint : engine.getEndpoints()) {
            stringBuilder.append("[").append(endpoint.asString()).append("]");
        }
        stringBuilder.append("Timeout:").append("[").append(timeout).append("]");
        stringBuilder.append("EventId:").append("[").append(replyEvent.getId()).append("]");
        return stringBuilder.toString();
    }

    public static void sendMsg(IClientEngine engine, byte[] bytes) {
        IMsgEvent requestEvent = new MsgEvent(IMsgEvent.ID_REQUEST, engine, new MsgEventInfo(bytes));
        engine.sendMsg(requestEvent);
    }

    public static void sendMsg(IClientEngine engine, DirectBuffer requestBuffer) throws ServiceException {
        IMsgEvent requestEvent = new MsgEvent(IMsgEvent.ID_REQUEST, engine, new MsgEventInfo(requestBuffer.byteArray()));
        engine.sendMsg(requestEvent);
    }

//    public static DirectBuffer recvMsg(IClientEngine engine, int timeout) throws ServiceException {
//        IMsgEvent replyEvent = engine.recvMsg(timeout);
//        return Helper.buildReplyBuffer(engine, timeout, replyEvent);
//    }

    public static byte[] recvMsg(IClientEngine engine, int timeout) throws ServiceException {
        IMsgEvent replyEvent = engine.recvMsg(timeout);
        return Helper.buildReplyBytes(engine, timeout, replyEvent);
    }

    public static void checkMsgType(short expected, short actual) throws ServiceException {
        if (expected != actual) {
            throw new ServiceException(ServiceException.MISMATCH_REPLY, "expected:" + expected + "actual:" + actual);
        }
    }

    public static void checkRetCode(int expected, int actual, long requestNo) throws ServiceException {
        if (expected != actual) {
            throw new ServiceException(actual, "requestNO=" + requestNo);
        }
    }
}
