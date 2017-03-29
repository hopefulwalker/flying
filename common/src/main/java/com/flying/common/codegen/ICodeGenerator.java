/**
 * Created by Walker.Zhang on 2015/7/21.
 * Revision History:
 * Date          Who              Version      What
 * 2015/7/21     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.codegen;

public interface ICodeGenerator {
    String generate(Class clazz);

    String getClassName(Class clazz);
}
