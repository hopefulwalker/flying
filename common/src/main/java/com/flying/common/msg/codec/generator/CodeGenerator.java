/*
 Created by Walker on 2017/4/14.
 Revision History:
 Date          Who              Version      What
 2015/4/14     Walker.Zhang     0.1.0        Created.
 2017/4/14     Walker.Zhang     0.3.2        Refactor to support SBE 1.6.2.
*/
package com.flying.common.msg.codec.generator;

import com.flying.common.msg.codec.anno.CodecInfo;
import com.flying.common.msg.codec.anno.DefaultInfo;
import com.flying.common.msg.codec.anno.Fields;
import com.flying.common.msg.codec.anno.Name;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;

class CodeGenerator {
    private static final Logger logger = LoggerFactory.getLogger(CodeGenerator.class);
    ////// Start of refactor constances.
    private static final String MVEL_FILE_POSTFIX = ".mvel";
    private static final String MVEL_PACKAGE_TEMPLATE = "com.flying.common.msg.codec.generator.pkgdef";
    private static final String MVEL_CLASS_TEMPLATE = "com.flying.common.msg.codec.generator.classdef";
    private static final String MVEL_GET_HEADER_INFO = "com.flying.common.msg.codec.generator.methodgetheaderinfo";
    private static final String MVEL_ENCODE_MSG = "com.flying.common.msg.codec.generator.methodencodemsg";
    private static final String MVEL_GET_BODY_DECODER = "com.flying.common.msg.codec.generator.methodgetbodydecoder";
    private static final String MVEL_DECODE_MSG = "com.flying.common.msg.codec.generator.methoddecodemsg";

    private static final String KEY_CLASS_NAME = "className";
    private static final String KEY_NOW = "now";
    private static final String KEY_PKG_NAME = "pkgName";
    private static final String KEY_EXCEPTION = "exception";

    private static final String KEY_HEADER_DECODER_CLASS_NAME = "headerDecoderClass";
    private static final String KEY_VAR_HEADER_DECODER = "varHeaderDecoder";
    private static final String VALUE_HEADER_DECODER = "headerDecoder";

    private static final String KEY_HEADER_ENCODER_CLASS_NAME = "headerEncoderClass";
    private static final String KEY_VAR_HEADER_ENCODER = "varHeaderEncoder";
    private static final String VALUE_HEADER_ENCODER = "headerEncoder";

    private static final String KEY_BODY_DECODER_CLASS_NAME = "bodyDecoderClass";
    private static final String KEY_VAR_BODY_DECODER = "varBodyDecoder";
    private static final String VALUE_BODY_DECODER = "bodyDecoder";

    private static final String KEY_BODY_ENCODER_CLASS_NAME = "bodyEncoderClass";
    private static final String KEY_VAR_BODY_ENCODER = "varBodyEncoder";
    private static final String VALUE_BODY_ENCODER = "bodyEncoder";
    private static final String KEY_BODY_CLASS_NAME = "bodyClass";
    // Element related keys.
    private static final String KEY_ELEMENT_EXISTS = "elementExists";
    private static final String KEY_ELEMENT_ENCODER_CLASS_NAME = "elementEncoderClass";
    private static final String KEY_ELEMENT_DECODER_CLASS_NAME = "elementDecoderClass";
    private static final String KEY_VAR_ELEMENT_ENCODER = "varElementEncoder";
    private static final String KEY_VAR_ELEMENT_DECODER = "varElementDecoder";
    private static final String KEY_ELEMENT_CLASS_NAME = "elementClass";
    private static final String KEY_ELEMENT_CLASS = "elementClassObject";
    private static final String KEY_VAR_ELEMENT_FIELDS = "varElementFields";
    private static final String KEY_VAR_ELEMENTS = "varElements";
    private static final String KEY_VAR_ELEMENT = "varElement";

    // method return related constants
    private static final String KEY_RETURN_TYPE = "retType";
    private static final String VALUE_RETURN_TYPE_PRIMITIVE = "primitive";
    private static final String VALUE_RETURN_TYPE_ARRAY = "array";
    private static final String VALUE_RETURN_TYPE_STRING = "String";
    private static final String VALUE_RETURN_TYPE_COLLECTION = "collection";
    private static final String VALUE_RETURN_TYPE_VALUE_OBJECT = "valueObject";
    private static final String KEY_RETURN_TYPE_STRING = "retTypeString";

    private static final String KEY_MSGTYPE_CLASS_NAME = "msgTypeClass";


    private static final String SBE_STRING_ENCODER = "Encoder";
    private static final String SBE_STRING_DECODER = "Decoder";

    private static final String KEY_VAR_MSG = "varMsg";

    private static final String KEY_VAR_FIELDS = "varFields";
    private static final String KEY_VAR_FIELD = "varField";
    private static final String INDENT = "    ";
    ////// End of refactor constances.
    private Set<String> imports = new HashSet<>();
    private DefaultInfo defaultInfo;

    void generateImplClass(String outputDirName, Class clazz) throws Exception {
        // set up defaults
        defaultInfo = (DefaultInfo) clazz.getAnnotation(DefaultInfo.class);
        OutputManager outputManager = new OutputManager(outputDirName, clazz.getPackage().getName());
        StringBuilder header = new StringBuilder();
        StringBuilder classDeclaration = new StringBuilder();
        StringBuilder methodDeclaration = new StringBuilder();

        header.append(generatePackage(clazz.getPackage().getName()));
        String simpleClassName = clazz.getSimpleName().replaceFirst("I", "");
        classDeclaration.append(generateClassDeclaration(simpleClassName));
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) methodDeclaration.append(generateMethod(method)).append("\n");
        try (final Writer out = outputManager.createOutput(clazz.getSimpleName().replaceFirst("I", ""))) {
            out.append(header).append("\n");
            out.append(generateImports()).append("\n");
            out.append(classDeclaration).append("\n");
            out.append(methodDeclaration).append("\n");
            out.append("}\n");
        }
    }

    private String generatePackage(final String packageName) {
        Map<String, Object> vars = new HashMap<>();
        vars.put(KEY_NOW, new Date(System.currentTimeMillis()));
        vars.put(KEY_PKG_NAME, packageName);
        return getMvelEvalResult(MVEL_PACKAGE_TEMPLATE, vars);
    }

    private String generateClassDeclaration(final String className) {
        Map<String, Object> vars = new HashMap<>();
        vars.put(KEY_CLASS_NAME, className);
        return getMvelEvalResult(MVEL_CLASS_TEMPLATE, vars);
    }

    private String generateImports() {
        StringBuilder sb = new StringBuilder();
        for (String statement : imports) {
            sb.append("import ").append(statement).append(";").append("\n");
        }
        return sb.toString();
    }

    private String generateMethod(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(INDENT).append(Modifier.toString(method.getModifiers()).replaceAll("abstract", ""));

        Map<String, Object> vars = new HashMap<>();
        sb.append(prepareReturnType(method, vars));
        sb.append(" ").append(method.getName()).append("(");
        Parameter[] parameter = method.getParameters();
        for (int i = 0; i < parameter.length; i++) {
            sb.append(generateParameter(parameter[i]));
            if (i < parameter.length - 1) sb.append(",");
        }
        sb.append(")");
        Class[] clazzs = method.getExceptionTypes();
        assert (clazzs.length <= 1);
        if (clazzs.length != 0) {
            String exception = generateException(clazzs[0]);
            vars.put(KEY_EXCEPTION, exception);
            sb.append(" throws ").append(exception);
        }
        sb.append("{\n").append(generateMethodBody(method, vars)).append("\n");
        sb.append(INDENT).append("}");
        return sb.toString();
    }

    private String prepareReturnType(Method method, Map<String, Object> vars) {
        String retType;
        Class returnClazz = method.getReturnType();
        String retTypeString = returnClazz.getSimpleName();
        if (returnClazz.isPrimitive()) {
            retType = VALUE_RETURN_TYPE_PRIMITIVE;
        } else if (returnClazz.isArray()) {
            retType = VALUE_RETURN_TYPE_ARRAY;
        } else if (returnClazz.getName().equals(String.class.getName())) {
            retType = VALUE_RETURN_TYPE_STRING;
        } else {
            addImports(returnClazz);
            Type retJDKType = method.getGenericReturnType();
            if (retJDKType instanceof Class) {
                retType = VALUE_RETURN_TYPE_VALUE_OBJECT;
                vars.put(KEY_ELEMENT_CLASS, returnClazz);
                vars.put(KEY_ELEMENT_CLASS_NAME, returnClazz.getSimpleName());
            } else if (retJDKType instanceof ParameterizedType) {
                retType = VALUE_RETURN_TYPE_COLLECTION;
                try {
                    returnClazz = Class.forName(((ParameterizedType) retJDKType).getActualTypeArguments()[0].getTypeName());
                    vars.put(KEY_ELEMENT_CLASS, returnClazz);
                    vars.put(KEY_ELEMENT_CLASS_NAME, returnClazz.getSimpleName());
                    retTypeString = retTypeString + "<" + returnClazz.getSimpleName() + ">";
                    addImports(returnClazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                retType = VALUE_RETURN_TYPE_VALUE_OBJECT;
                logger.error("Unsupported return type of method!, return VALUE_RETURN_TYPE_VALUE_OBJECT" + method.getName());
            }
        }
        vars.put(KEY_RETURN_TYPE_STRING, retTypeString);
        vars.put(KEY_RETURN_TYPE, retType);
        return retTypeString;
    }

    private String generateMethodBody(Method method, Map<String, Object> vars) {
        CodecInfo codecInfo = method.getAnnotation(CodecInfo.class);
        prepareHeaderCodec(codecInfo, vars);
        switch (codecInfo.type()) {
            case CodecInfo.ENCODE_MSG:
                prepareMsgType(codecInfo, vars);
                prepareBodyCodec(codecInfo, vars);
                prepareEncodeFields(method, vars);
                return getMvelEvalResult(MVEL_ENCODE_MSG, vars);
            case CodecInfo.GET_HEADER_INFO:
                prepareMsg(method, vars);
                prepareDecodeField(codecInfo, vars);
                return getMvelEvalResult(MVEL_GET_HEADER_INFO, vars);
            case CodecInfo.GET_BODY_DECODER:
                prepareMsg(method, vars);
                prepareBodyCodec(codecInfo, vars);
                return getMvelEvalResult(MVEL_GET_BODY_DECODER, vars);
            case CodecInfo.DECODE_MSG:
                prepareMsg(method, vars);
                prepareBodyCodec(codecInfo, vars);
                prepareDecodeFields(codecInfo, vars);
                return getMvelEvalResult(MVEL_DECODE_MSG, vars);
        }
        return null;
    }

    private void prepareMsg(Method method, Map<String, Object> vars) {
        Parameter[] paras = method.getParameters();
        if (paras.length == 1) {
            String name = paras[0].getAnnotation(Name.class).value();
            vars.put(KEY_VAR_MSG, name);
        }
    }

    private void prepareHeaderCodec(CodecInfo annotation, Map<String, Object> vars) {
        if (!annotation.headerDecoderClass().trim().isEmpty() || !defaultInfo.headerDecoderClass().trim().isEmpty()) {
            String decoderClass = annotation.headerDecoderClass().trim();
            if (decoderClass.isEmpty()) {
                decoderClass = defaultInfo.headerDecoderClass().trim();
            }
            vars.put(KEY_HEADER_DECODER_CLASS_NAME, getClassSimpleName(decoderClass, defaultInfo.headerCodecPackage()));
            vars.put(KEY_VAR_HEADER_DECODER, VALUE_HEADER_DECODER);
        }
        if (!annotation.headerEncoderClass().trim().isEmpty() || !defaultInfo.headerEncoderClass().trim().isEmpty()) {
            String encoderClass = annotation.headerEncoderClass().trim();
            if (encoderClass.isEmpty()) {
                encoderClass = defaultInfo.headerEncoderClass().trim();
            }
            vars.put(KEY_HEADER_ENCODER_CLASS_NAME, getClassSimpleName(encoderClass, defaultInfo.headerCodecPackage()));
            vars.put(KEY_VAR_HEADER_ENCODER, VALUE_HEADER_ENCODER);
        }
    }

    private void prepareBodyCodec(CodecInfo annotation, Map<String, Object> vars) {
        if (!annotation.bodyDecoderClass().trim().isEmpty()) {
            String codecSimpleName = getClassSimpleName(annotation.bodyDecoderClass(), defaultInfo.bodyCodecPackage());
            vars.put(KEY_BODY_DECODER_CLASS_NAME, codecSimpleName);
            vars.put(KEY_VAR_BODY_DECODER, VALUE_BODY_DECODER);
            vars.put(KEY_BODY_CLASS_NAME, StringUtils.removeEnd(codecSimpleName, SBE_STRING_DECODER));
        }
        if (!annotation.bodyEncoderClass().trim().isEmpty()) {
            String codecSimpleName = getClassSimpleName(annotation.bodyEncoderClass(), defaultInfo.bodyCodecPackage());
            vars.put(KEY_BODY_ENCODER_CLASS_NAME, codecSimpleName);
            vars.put(KEY_VAR_BODY_ENCODER, VALUE_BODY_ENCODER);
            vars.put(KEY_BODY_CLASS_NAME, StringUtils.removeEnd(codecSimpleName, SBE_STRING_ENCODER));
        }
    }

    private void prepareMsgType(CodecInfo annotation, Map<String, Object> vars) {
        if (!annotation.msgTypeClass().trim().isEmpty() || !defaultInfo.msgTypeClass().trim().isEmpty()) {
            String msgTypeClass = annotation.msgTypeClass().trim();
            if (msgTypeClass.isEmpty()) {
                msgTypeClass = defaultInfo.msgTypeClass().trim();
            }
            vars.put(KEY_MSGTYPE_CLASS_NAME, getClassSimpleName(msgTypeClass, defaultInfo.msgTypePackage()));
        }
    }

    private String getClassSimpleName(String codecClass, String packagePrefix) {
        try {
            Class codec;
            codecClass = codecClass.trim();
            if (!packagePrefix.trim().isEmpty() && !StringUtils.contains(codecClass, '.')) {
                codec = Class.forName(packagePrefix.trim() + "." + codecClass);
            } else {
                codec = Class.forName(codecClass);
            }
            addImports(codec);
            return codec.getSimpleName();
        } catch (ClassNotFoundException cnfe) {
            logger.error("Error in loading codec class", cnfe);
        }
        return null;
    }

    private void prepareDecodeField(CodecInfo codecInfo, Map<String, Object> vars) {
        if (!codecInfo.fields().isEmpty()) {
            vars.put(KEY_VAR_FIELD, codecInfo.fields());
        }
    }

    private void prepareEncodeFields(Method method, Map<String, Object> vars) {
        vars.put(KEY_ELEMENT_EXISTS, false);
        StringBuilder varFields = new StringBuilder();
        Parameter[] parameters = method.getParameters();
        for (Parameter para : parameters) {
            Fields fieldsAnnotation = para.getAnnotation(Fields.class);
            if (fieldsAnnotation == null) continue;
            String paraName = para.getAnnotation(Name.class).value();
            Class clazz = para.getType();
            // field directly, primitives or string
            if (clazz.isPrimitive() || clazz.getName().equals(String.class.getName())) {
                // If the fields contains nothing or more than 1 field, using @Name
                String fieldName = fieldsAnnotation.value().trim();
                if (fieldName.isEmpty() || fieldName.contains(Fields.SPLIT_CHAR)) {
                    fieldName = paraName;
                }
                varFields.append(".").append(fieldName).append("(").append(fieldName).append(")");
                continue;
            }
            // collection
            if (para.getParameterizedType() instanceof ParameterizedType) {
                try {
                    // elementEncoderClass, varElementEncoder, elementClass, varElements, varElement
                    vars.put(KEY_ELEMENT_ENCODER_CLASS_NAME, StringUtils.capitalize(fieldsAnnotation.elementEncoderClass()));
                    vars.put(KEY_VAR_ELEMENT_ENCODER, StringUtils.uncapitalize(fieldsAnnotation.elementEncoderClass()));
                    Class elementClazz = Class.forName(((ParameterizedType) para.getParameterizedType()).getActualTypeArguments()[0].getTypeName());
                    addImports(elementClazz);
                    vars.put(KEY_ELEMENT_CLASS_NAME, elementClazz.getSimpleName());
                    vars.put(KEY_VAR_ELEMENTS, paraName);
                    vars.put(KEY_VAR_ELEMENT, StringUtils.uncapitalize(elementClazz.getSimpleName()));
                    vars.put(KEY_ELEMENT_EXISTS, true);
                    vars.put(KEY_VAR_ELEMENT_FIELDS, buildVOFieldString(fieldsAnnotation.value().trim(), elementClazz, (String) vars.get(KEY_VAR_ELEMENT), null));
                } catch (ClassNotFoundException cnfe) {
                    logger.error("error occurs in handling ParameterizedType!", cnfe);
                }
                continue;
            }
            // bean
            varFields.append(buildVOFieldString(fieldsAnnotation.value().trim(), clazz, paraName, null));
        }
        // varFields
        vars.put(KEY_VAR_FIELDS, varFields.toString());
    }

    private void prepareDecodeFields(CodecInfo cnfInfo, Map<String, Object> vars) {
        // elementDecoderClass, varElementDecoder, elementClass, varElements, varElement, retTypeString, varElementFields
        vars.put(KEY_ELEMENT_DECODER_CLASS_NAME, cnfInfo.elementDecoderClass());
        String varElementDecoder = StringUtils.uncapitalize(cnfInfo.elementDecoderClass());
        vars.put(KEY_VAR_ELEMENT_DECODER, varElementDecoder);
        vars.put(KEY_VAR_ELEMENTS, StringUtils.uncapitalize((String) vars.get(KEY_ELEMENT_CLASS_NAME)) + "s");
        String varElement = StringUtils.uncapitalize((String) vars.get(KEY_ELEMENT_CLASS_NAME));
        vars.put(KEY_VAR_ELEMENT, varElement);
        String voDecoder;
        if (vars.get(KEY_RETURN_TYPE).equals(VALUE_RETURN_TYPE_COLLECTION)) {
            voDecoder = varElementDecoder;
        } else {
            voDecoder = (String) vars.get(KEY_VAR_BODY_DECODER);
        }
        vars.put(KEY_VAR_ELEMENT_FIELDS, buildVOFieldString(cnfInfo.fields().trim(), (Class) vars.get(KEY_ELEMENT_CLASS), varElement, voDecoder));
    }

    private String buildVOFieldString(String fieldNameString, Class clazz, String voName, String voDecoder) {
        String[] fieldNames = new String[0];
        if (!fieldNameString.isEmpty()) {
            fieldNames = StringUtils.split(fieldNameString.replaceAll(" ", ""), Fields.SPLIT_CHAR);
        }
        StringBuilder sb = new StringBuilder();
        for (Field field : clazz.getDeclaredFields()) {
            if (fieldNameString.isEmpty() || ArrayUtils.contains(fieldNames, field.getName())) {
                if (voDecoder == null) {
                    sb.append(".").append(field.getName()).append("(").append(voName)
                            .append(".get").append(StringUtils.capitalize(field.getName())).append("())");
                } else {
                    sb.append(voName).append(".set").append(StringUtils.capitalize(field.getName()))
                            .append("(").append(voDecoder).append(".").append(field.getName()).append("());");
                }
            }
        }
        return sb.toString();
    }

    private String generateException(Class clazz) {
        addImports(clazz);
        return clazz.getSimpleName();
    }

    private String generateParameter(Parameter parameter) {
        try {
            Class paraClass = parameter.getType();
            addImports(paraClass);
            String retKindName = parameter.getType().getSimpleName();
            if (parameter.getParameterizedType() instanceof ParameterizedType) {
                Type type = parameter.getParameterizedType();
                Class dtoClass = Class.forName(((ParameterizedType) type).getActualTypeArguments()[0].getTypeName());
                retKindName = retKindName + "<" + dtoClass.getSimpleName() + ">";

            }
            Name name = parameter.getAnnotation(Name.class);
            return retKindName + " " + name.value();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private URL getUrl(String template) {
        final ClassLoader loader = this.getClass().getClassLoader();
        // Next search for a Properties file.
        StringBuilder fileName = new StringBuilder();
        fileName.append(template.replace('.', '/')).append(MVEL_FILE_POSTFIX);
        URL url = loader.getResource(fileName.toString());
        if (url == null) {
            throw new MissingResourceException("Missing Resource " + fileName, fileName.toString(), template);
        }
        return url;
    }

    private void addImports(Class clazz) {
        if (!clazz.isPrimitive() && !clazz.isArray() && !clazz.getName().startsWith("java.lang")) {
            imports.add(clazz.getName());
        }
    }

    private String getMvelEvalResult(String template, Map<String, Object> vars) {
        URL url = getUrl(template);
        String contents = null;
        try (InputStream inStream = url.openStream()) {
            contents = (String) TemplateRuntime.eval(inStream, null, vars);
        } catch (IOException ioe) {
            logger.error("Error in eval mvel template!", ioe);
        }
        return contents;
    }
}