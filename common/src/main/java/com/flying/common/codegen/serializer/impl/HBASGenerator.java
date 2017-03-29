/**
 * Created by Walker.Zhang on 2015/7/21.
 * Revision History:
 * Date          Who              Version      What
 * 2015/7/21     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.codegen.serializer.impl;

import com.flying.common.codegen.ICodeGenerator;
import com.flying.common.codegen.serializer.anno.Serialization;
import org.apache.commons.lang3.StringUtils;
import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

public class HBASGenerator implements ICodeGenerator {
    private static final Logger logger = LoggerFactory.getLogger(HBASGenerator.class);
    private static final String FILE_POSTFIX = ".mvel";
    private static final String HAZELCAST_BAS_TEMPLATE = "com.flying.common.codegen.serializer.impl.hcbas";

    private static final String SBE_WHOLE_TEMPLATE = "com.flying.common.codegen.serializer.impl.sbewhole";

    private static final String SBE_BYTE_TEMPLATE = "com.flying.common.codegen.serializer.impl.sbebyte";
    private static final String SBE_SHORT_TEMPLATE = "com.flying.common.codegen.serializer.impl.sbeshort";
    private static final String SBE_INT_TEMPLATE = "com.flying.common.codegen.serializer.impl.sbeint";
    private static final String SBE_LONG_TEMPLATE = "com.flying.common.codegen.serializer.impl.sbelong";
    private static final String SBE_DOUBLE_TEMPLATE = "com.flying.common.codegen.serializer.impl.sbedouble";
    private static final String SBE_STRING_TEMPLATE = "com.flying.common.codegen.serializer.impl.sbestring";

    // common keys
    private static final String KEY_BO_NAME = "boName";
    private static final String KEY_BO_BAS_NAME = "boBASName";
    private static final String KEY_BO_SBE_NAME = "boSBEName";

    private static final String KEY_VAR_BO = "varBO";
    private static final String KEY_VAR_BO_BAS = "varBOBAS";
    private static final String KEY_VAR_BO_SBE = "varBOSBE";

    private static final String KEY_FIELD_NAME = "fieldName";
    private static final String KEY_BYTE_SEQUENCE = "byteSequence";
    private static final String KEY_STRING_BYTES = "stringBytes";

    private static final String KEY_BLOCK_LENGTH = "blockLength";
    private static final String KEY_SBE_GETSETMETHODS = "sbeGetSetMethods";

    private static final String KEY_SBE_CLASS_DEFINITION = "sbeClassDefinition";
    private static final String KEY_NOW = "now";
    private static final String KEY_PKG_NAME = "packageName";

    private static final String KEY_BAS_SET_BINARY_STATEMENT = "setBinaryStatement";
    private static final String KEY_BAS_SET_BO_STATEMENT = "setBOStatement";
    private static final String INDENT = "    ";
    private Map<String, Object> keys = new HashMap<>();

    @Override
    public String getClassName(Class clazz) {
        return clazz.getSimpleName() + "BAS";
    }

    @Override
    public String generate(Class clazz) {
        // prepare the common KEYs.
        keys.put(KEY_BO_NAME, clazz.getSimpleName());
        keys.put(KEY_VAR_BO, StringUtils.uncapitalize(clazz.getSimpleName()));
        keys.put(KEY_BO_BAS_NAME, getClassName(clazz));
        keys.put(KEY_VAR_BO_BAS, StringUtils.uncapitalize(getClassName(clazz)));
        keys.put(KEY_BO_SBE_NAME, clazz.getSimpleName() + "SBE");
        keys.put(KEY_VAR_BO_SBE, StringUtils.uncapitalize(clazz.getSimpleName() + "SBE"));

        // prepare the keys for hcbas
        keys.put(KEY_SBE_CLASS_DEFINITION, generateSBEClass(clazz));
        keys.put(KEY_NOW, new Date(System.currentTimeMillis()));
        keys.put(KEY_PKG_NAME, clazz.getPackage().getName());
        keys.put(KEY_BAS_SET_BINARY_STATEMENT, generateSetBinaryStatement(clazz));
        keys.put(KEY_BAS_SET_BO_STATEMENT, generateSetBOStatement(clazz));

        // evaluate the template and return the result.
        URL url = getUrl(HAZELCAST_BAS_TEMPLATE);
        String contents = null;
        try (InputStream inStream = url.openStream()) {
            contents = (String) TemplateRuntime.eval(inStream, null, keys);
        } catch (IOException e) {
            logger.error("Error in generate bas file!", e);
        }
        return contents;
    }

    private String generateSBEClass(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        int blockLength = 0;
        StringBuilder sbeGetSetMethods = new StringBuilder();
        for (int sequenceID = 1; sequenceID <= fields.length; sequenceID++) {
            for (Field field : fields) {
                if (!isSerialized(field)) continue;
                Serialization serialization = field.getAnnotation(Serialization.class);
                if (serialization.id() != sequenceID) continue;
                blockLength += generateSBEMethod(field, blockLength, sbeGetSetMethods);
                break;
            }
        }

        keys.put(KEY_BLOCK_LENGTH, blockLength);
        keys.put(KEY_SBE_GETSETMETHODS, sbeGetSetMethods.toString());

        URL url = getUrl(SBE_WHOLE_TEMPLATE);
        String contents = null;
        try (InputStream inStream = url.openStream()) {
            contents = (String) TemplateRuntime.eval(inStream, null, keys);
        } catch (IOException e) {
            logger.error("Error in generate sbe whole file!", e);
        }
        return contents;
    }

    private int generateSBEMethod(Field field, int byteSequence, StringBuilder sbeGetSetMethods) {
        String fieldTypeName = field.getType().getSimpleName();
        String fieldName = field.getName();
        keys.put(KEY_FIELD_NAME, fieldName);
        keys.put(KEY_BYTE_SEQUENCE, byteSequence);
        int occupiedBytes = 0;
        String template = null;
        switch (fieldTypeName) {
            case "byte":
                occupiedBytes = Byte.BYTES;
                template = SBE_BYTE_TEMPLATE;
                break;
            case "short":
                occupiedBytes = Short.BYTES;
                template = SBE_SHORT_TEMPLATE;
                break;
            case "int":
                occupiedBytes = Integer.BYTES;
                template = SBE_INT_TEMPLATE;
                break;
            case "long":
                occupiedBytes = Long.BYTES;
                template = SBE_LONG_TEMPLATE;
                break;
            case "double":
                occupiedBytes = Double.BYTES;
                template = SBE_DOUBLE_TEMPLATE;
                break;
            case "String":
                Serialization serialization = field.getAnnotation(Serialization.class);
                occupiedBytes = serialization.bytes();
                keys.put(KEY_STRING_BYTES, occupiedBytes);
                template = SBE_STRING_TEMPLATE;
                break;
        }

        URL url = getUrl(template);
        try (InputStream inStream = url.openStream()) {
            sbeGetSetMethods.append(TemplateRuntime.eval(inStream, null, keys));
        } catch (IOException e) {
            logger.error("Error in generate sbe getset methods!", e);
        }
        return occupiedBytes;
    }

    private String generateSetBinaryStatement(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder statement = new StringBuilder();
        boolean first = true;
        for (Field field : fields) {
            if (!isSerialized(field)) continue;
            if (first)
                first = false;
            else
                statement.append(".");
            //extNo(orderBO.getExtNo())
            statement.append(field.getName())
                    .append("(").append(keys.get(KEY_VAR_BO)).append(".get").append(StringUtils.capitalize(field.getName())).append("()").append(")");
        }
        return statement.toString();
    }

    private String generateSetBOStatement(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder statement = new StringBuilder();
        boolean first = true;
        for (Field field : fields) {
            if (!isSerialized(field)) continue;
            if (first)
                first = false;
            else
                statement.append(INDENT).append(INDENT);
            //orderBO.setOid(orderBOSBE.oid());
            statement.append(keys.get(KEY_VAR_BO)).append(".set").append(StringUtils.capitalize(field.getName()))
                    .append("(").append(keys.get(KEY_VAR_BO_SBE)).append(".").append(field.getName()).append("()").append(");\n");
        }
        return statement.toString();
    }

    private boolean isSerialized(Field field) {
        // It should not be a transient field and has annotation of Serialization.
        return !Modifier.isTransient(field.getModifiers()) && field.getAnnotation(Serialization.class) != null;
    }

    private URL getUrl(String template) {
        final ClassLoader loader = this.getClass().getClassLoader();
        // Next search for a Properties file.
        StringBuilder fileName = new StringBuilder();
        fileName.append(template.replace('.', '/')).append(FILE_POSTFIX);
        URL url = loader.getResource(fileName.toString());
        if (url == null) {
            throw new MissingResourceException("Missing Resource " + fileName, fileName.toString(), template);
        }
        return url;
    }
}
