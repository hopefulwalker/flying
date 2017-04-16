/**
 * Created by Walker.Zhang on 2015/4/14.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/14     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.msg.codec.generator;

import com.flying.util.common.ObjectUtils;

import java.io.*;

public class OutputManager {
    private final File outputDir;

    /**
     * Create a new {@link uk.co.real_logic.sbe.generation.OutputManager} for generating Java source files into a given package.
     *
     * @param baseDirectoryName for the generated source code.
     * @param packageName       for the generated source code relative to the baseDirectoryName.
     * @throws IOException if an error occurs during output.
     */
    public OutputManager(final String baseDirectoryName, final String packageName) throws IOException {
        ObjectUtils.checkNotNull(baseDirectoryName);
        ObjectUtils.checkNotNull(packageName);

        final String dirName =
                (baseDirectoryName.endsWith("" + File.separatorChar) ? baseDirectoryName : baseDirectoryName + File.separatorChar) +
                        packageName.replace('.', File.separatorChar);

        outputDir = new File(dirName);
        if (!outputDir.exists()) {
            if (!outputDir.mkdirs()) {
                throw new IllegalStateException("Unable to create directory: " + dirName);
            }
        }
    }

    /**
     * Create a new output which will be a Java source file in the given package.
     * <p>
     * The {@link Writer} should be closed once the caller has finished with it. The Writer is
     * buffer for efficient IO operations.
     *
     * @param name the name of the Java class.
     * @return a {@link Writer} to which the source code should be written.
     */
    public Writer createOutput(final String name) throws IOException {
        final File targetFile = new File(outputDir, name + ".java");
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
    }
}
