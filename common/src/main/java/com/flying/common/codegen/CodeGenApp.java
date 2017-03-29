/**
 * Created by Walker.Zhang on 2015/7/21.
 * Revision History:
 * Date          Who              Version      What
 * 2015/7/21     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.codegen;

import java.io.Writer;

public class CodeGenApp {

    private static final String OUTPUT_DIR = "output.dir";
    private static final String CODE_GEN_CLASS = "code.generator.class";

    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.format("Usage: %s <filenames> ...\n", CodeGenApp.class.getName());
            System.exit(-1);
        }
        for (final String fullName : args) {
            Class clazz = null;
            try {
                clazz = Class.forName(fullName);
            } catch (ClassNotFoundException e) {
                System.out.format("Usage: %s class not found...\n", fullName);
                System.exit(-1);
            }
            ICodeGenerator generator = getCodeGenerator();
            if (generator == null) {
                System.out.format("Error: %s is not a code generator or could not load.\n", fullName);
                System.exit(-1);
            }
            try {
                OutputManager outputManager = new OutputManager(System.getProperty(OUTPUT_DIR, "."), clazz.getPackage().getName());
                try (final Writer out = outputManager.createOutput(generator.getClassName(clazz))) {
                    out.append(generator.generate(clazz));
                }
            } catch (Exception e) {
                System.out.format("Exception occurs when generating %s serializer class ...\n", fullName);
                e.printStackTrace();
                System.exit(-1);
            }
            System.out.format("Succeed to generate file for interface %s ...\n", fullName);
        }
    }

    public static ICodeGenerator getCodeGenerator() {
        String generatorClassName = System.getProperty(CODE_GEN_CLASS);
        if (generatorClassName == null) return null;
        Class clazz;
        Object generator;
        try {
            clazz = Class.forName(generatorClassName);
            generator = clazz.newInstance();
            if (generator instanceof ICodeGenerator) return (ICodeGenerator) generator;
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
