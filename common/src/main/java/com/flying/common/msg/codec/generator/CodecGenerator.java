/*
 Created by Walker on 2017/4/14.
 Revision History:
 Date          Who              Version      What
 2017/4/14     Walker           0.3.2        Created to support SBE 1.6.2.
*/
package com.flying.common.msg.codec.generator;

public class CodecGenerator {
    private static final String OUTPUT_DIR = "codec.output.dir";

    public static void main(String args[]) {
        if (args.length == 0) {
            System.err.format("Usage: %s <filenames>...\n", CodecGenerator.class.getName());
            System.exit(-1);
        }
        for (final String fullInterfaceName : args) {
            Class clazz = null;
            try {
                clazz = Class.forName(fullInterfaceName);
            } catch (ClassNotFoundException e) {
                System.err.format("Usage: %s class not found...\n", fullInterfaceName);
                System.exit(-1);
            }
            if (!clazz.isInterface()) {
                System.err.format("Usage: %s should be an interface...\n", fullInterfaceName);
                System.exit(-1);
            }
            try {
                CodeGenerator generator = new CodeGenerator();
                generator.generateImplClass(System.getProperty(OUTPUT_DIR, "."), clazz);
            } catch (Exception e) {
                System.err.format("Exception occurs when generating %s implementation class ...\n", fullInterfaceName);
                e.printStackTrace();
                System.exit(-1);
            }
            System.out.format("Succeed to generate file for interface %s ...\n", fullInterfaceName);
        }
    }
}
