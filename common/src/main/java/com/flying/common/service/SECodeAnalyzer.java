/**
 * Created by Walker.Zhang on 2015/5/14.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/14     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.service;

import com.flying.common.IReturnCode;
import com.flying.util.common.Dictionary;

public class SECodeAnalyzer {
    public static String analyze(int code) {
        int serviceType = Math.abs(code) / ServiceException.EXCEPTION_DIGITS;
        int infoCode = code % ServiceException.EXCEPTION_DIGITS;
        String codeMsg;
        if (Math.abs(infoCode) <= IReturnCode.ABS_MAX_CODE) {
            codeMsg = Dictionary.getString(IReturnCode.class, infoCode);
        } else if (Math.abs(infoCode) <= ServiceException.ABS_MAX_CODE) {
            codeMsg = Dictionary.getString(ServiceException.class, infoCode);
        } else {
            codeMsg = Dictionary.getString(getExceptionFullName(serviceType), infoCode);
        }
        return "Service:" + Dictionary.getString(IServiceType.class, serviceType) + ",Code:" + codeMsg;
    }

    private static String getExceptionFullName(int serviceType) {
        return Dictionary.getString(SECodeAnalyzer.class, serviceType);
    }
}