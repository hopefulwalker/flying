/**
 * Created by Walker.Zhang on 2015/5/20.
 * Revision History:
 * Date          Who              Version      What
 * 2015/5/20     Walker.Zhang     0.1.0        Created.
 */
package com.flying.common.msg.codec.generator;

import com.flying.common.msg.codec.anno.CnvInfo;
import com.flying.common.msg.codec.anno.Name;
import com.flying.common.msg.codec.anno.ReplyFields;
import com.flying.common.msg.codec.anno.Fields;
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

public class CodeGenerator {
    private static final Logger logger = LoggerFactory.getLogger(CodeGenerator.class);

    private static final short CNV_GET_HEADER_INFO = 1;
    private static final short CNV_REQUEST_2_BYTES = 101;
    private static final short CNV_BYTES_2_REQUEST = 102;
    private static final short CNV_REPLY_2_BYTES = 201;
    private static final short CNV_BYTES_2_REPLY = 202;

    private static final String FILE_POSTFIX = ".mvel";
    private static final String PACKAGE_TEMPLATE = "com.flying.common.msg.codec.generator.pkgdef";
    private static final String CLASS_TEMPLATE = "com.flying.common.msg.codec.generator.classdef";
    private static final String ATTR_TEMPLATE = "com.flying.common.msg.codec.generator.attrdef";

    private static final String GET_HEADER_INFO = "com.flying.common.msg.codec.generator.methodgetheaderinfo";

    private static final String REQUEST_2_BYTES = "com.flying.common.msg.codec.generator.methodrequest2bytes";
    private static final String BYTES_2_REQUEST = "com.flying.common.msg.codec.generator.methodbytes2request";

    private static final String BYTES_2_REPLY_COLLECTION = "com.flying.common.msg.codec.generator.methodbytes2replycollection";
    private static final String BYTES_2_REPLY_DTO = "com.flying.common.msg.codec.generator.methodbytes2replydto";
    private static final String REPLY_COLLECTION_2_BYTES = "com.flying.common.msg.codec.generator.methodreplycollection2bytes";
    private static final String REPLY_DTO_2_BYTES = "com.flying.common.msg.codec.generator.methodreplydto2bytes";
    private static final String REPLY_BASIC_2_BYTES = "com.flying.common.msg.codec.generator.methodreplybasic2bytes";

    private static final String INDENT = "    ";

    private static final String KEY_NOW = "now";
    private static final String KEY_PKG_NAME = "pkgName";
    private static final String KEY_CLASS_NAME = "className";
    private static final String KEY_RETURN_KIND = "retKind";
    private static final String KEY_RETURN_KIND_NAME = "retKindName";
    private static final String KEY_DTO_CLASS = "ClassObject_DTO";
    private static final String KEY_EXCEPTION = "exception";
    private static final String KEY_HEADER_CLASS_NAME = "headerClass";
    private static final String KEY_VAR_HEADER = "varHeader";
    private static final String KEY_REPLY_CLASS_NAME = "replyClass";
    private static final String KEY_VAR_REPLY = "varReply";
    private static final String KEY_REQUEST_CLASS_NAME = "requestClass";
    private static final String KEY_VAR_REQUEST = "varRequest";
    private static final String KEY_MSGTYPE_CLASS_NAME = "msgTypeClass";
    private static final String KEY_VAR_FIELDS = "varFields";
    private static final String KEY_VAR_BYTES = "varBytes";
    private static final String KEY_STRING_CONTAINED = "stringContained";
    private static final String KEY_VAR_DTOS = "varDTOs";
    private static final String KEY_MSGDTO_CLASS_NAME = "msgDTOClass";
    private static final String KEY_VAR_MSGDTO = "varMsgDTO";
    private static final String KEY_VAR_DTO = "varDTO";
    private static final String KEY_DTO_CLASS_NAME = "dtoClass";
    private static final String KEY_VAR_DTOFIELDS = "varDTOFields";
    private static final String KEY_REPLY2BYTES_PARA_KIND = "paraKind";
    private static final String KEY_VAR_RETURN_CODE = "varRetCode";

    private static final String PARA_KIND_BASIC = "basic";
    private static final String PARA_KIND_DTO = "dto";
    private static final String PARA_KIND_COLLECTION = "collection";

    private static final String RETURN_KIND_VOID = "void";
    private static final String RETURN_KIND_DTO = "dto";
    private static final String RETURN_KIND_PRIMITIVE = "primitive";
    private static final String RETURN_KIND_ARRAY = "array";
    private static final String RETURN_KIND_STRING = "String";
    private static final String RETURN_KIND_COLLECTION = "collection";

    private Set<String> imports = new HashSet<>();

    public void generateImplClass(String outputDirName, Class clazz) throws Exception {
        OutputManager outputManager = new OutputManager(outputDirName, clazz.getPackage().getName());
        StringBuilder header = new StringBuilder();
        StringBuilder classDeclaration = new StringBuilder();
        StringBuilder fieldDeclaration = new StringBuilder();
        StringBuilder methodDeclaration = new StringBuilder();

        header.append(generatePackage(clazz.getPackage().getName()));
        String simpleClassName = clazz.getSimpleName().replaceFirst("I", "");
        classDeclaration.append(generateClassDeclaration(simpleClassName));
        fieldDeclaration.append(generateFieldDeclaration(simpleClassName));
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) methodDeclaration.append(generateMethod(method)).append("\n");

        try (final Writer out = outputManager.createOutput(clazz.getSimpleName().replaceFirst("I", ""))) {
            out.append(header).append("\n");
            out.append(generateImports()).append("\n");
            out.append(classDeclaration).append("\n");
            out.append(fieldDeclaration).append("\n");
            out.append(methodDeclaration).append("\n");
            out.append("}\n");
        }
    }

    private String generatePackage(final String packageName) {
        URL url = getUrl(PACKAGE_TEMPLATE);
        Map<String, Object> vars = new HashMap<>();
        vars.put(KEY_NOW, new Date(System.currentTimeMillis()));
        vars.put(KEY_PKG_NAME, packageName);
        String contents = null;
        try (InputStream inStream = url.openStream()) {
            contents = (String) TemplateRuntime.eval(inStream, null, vars);
        } catch (IOException e) {
            logger.error("Error in generate file headerClass!", e);
        }
        return contents;
    }

    private String generateImports() {
        StringBuilder sb = new StringBuilder();
        for (String statement : imports) {
            sb.append("import ").append(statement).append(";").append("\n");
        }
        return sb.toString();
    }

    private String generateClassDeclaration(final String className) {
        URL url = getUrl(CLASS_TEMPLATE);
        Map<String, Object> vars = new HashMap<>();
        vars.put(KEY_CLASS_NAME, className);
        String contents = null;
        try (InputStream inStream = url.openStream()) {
            contents = (String) TemplateRuntime.eval(inStream, null, vars);
        } catch (IOException e) {
            logger.error("Error in generate file headerClass!", e);
        }
        return contents;
    }

    private String generateFieldDeclaration(String className) {
        URL url = getUrl(ATTR_TEMPLATE);
        Map<String, Object> vars = new HashMap<>();
        vars.put(KEY_CLASS_NAME, className);
        String contents = null;
        try (InputStream inStream = url.openStream()) {
            contents = (String) TemplateRuntime.eval(inStream, null, vars);
        } catch (IOException e) {
            logger.error("Error in generate file headerClass!", e);
        }
        return contents;
    }

    private String generateMethod(Method method) {
        Map<String, Object> vars = new HashMap<>();
        String retKind = RETURN_KIND_VOID;
        StringBuilder sb = new StringBuilder();
        sb.append(INDENT).append(Modifier.toString(method.getModifiers()).replaceAll("abstract", ""));
        String retKindName = method.getReturnType().getSimpleName();
        Class returnClass = method.getReturnType();
        if (returnClass.isPrimitive()) {
            if (!returnClass.getName().equals(RETURN_KIND_VOID)) retKind = RETURN_KIND_PRIMITIVE;
        } else if (returnClass.isArray()) {
            retKind = RETURN_KIND_ARRAY;
        } else if (returnClass.getName().equals("java.lang.String")) {
            retKind = RETURN_KIND_STRING;
        } else {
            retKind = RETURN_KIND_DTO;
            addImports(returnClass);
            Type retType = method.getGenericReturnType();
            if (retType instanceof ParameterizedType) {
                retKind = RETURN_KIND_COLLECTION;
                try {
                    returnClass = Class.forName(((ParameterizedType) retType).getActualTypeArguments()[0].getTypeName());
                    retKindName = retKindName + "<" + returnClass.getSimpleName() + ">";
                    addImports(returnClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            vars.put(KEY_DTO_CLASS, returnClass);
        }
        sb.append(retKindName);
        vars.put(KEY_RETURN_KIND_NAME, retKindName);
        vars.put(KEY_RETURN_KIND, retKind);
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

    private String generateMethodBody(Method method, Map<String, Object> vars) {
        CnvInfo cnvInfo = (CnvInfo) method.getAnnotations()[0];
        switch (cnvInfo.type()) {
            case CNV_GET_HEADER_INFO:
                prepareHeader(cnvInfo, vars);
                if (!cnvInfo.dtoFields().isEmpty()) {
                    vars.put(KEY_VAR_FIELDS, cnvInfo.dtoFields());
                }
                prepareVarBytes(method, vars);
                return generateGetHeaderInfo(method, vars);
            case CNV_REQUEST_2_BYTES:
                prepareHeader(cnvInfo, vars);
                prepareRequest(cnvInfo, vars);
                prepareMsgType(cnvInfo, vars);
                prepareRequestFields(method, vars);
                return generateRequest2Bytes(method, vars);
            case CNV_BYTES_2_REQUEST:
                prepareHeader(cnvInfo, vars);
                prepareVarBytes(method, vars);
                prepareRequest(cnvInfo, vars);
                return generateBytes2Request(method, vars);
            case CNV_REPLY_2_BYTES:
                prepareHeader(cnvInfo, vars);
                prepareReply(cnvInfo, vars);
                prepareMsgType(cnvInfo, vars);
                prepareReplyFields(method, vars);
                if (vars.get(KEY_REPLY2BYTES_PARA_KIND).equals(PARA_KIND_COLLECTION)) return generateReplyCollection2Bytes(method, vars);
                if (vars.get(KEY_REPLY2BYTES_PARA_KIND).equals(PARA_KIND_DTO)) return generateReplyDTO2Bytes(method, vars);
                if (vars.get(KEY_REPLY2BYTES_PARA_KIND).equals(PARA_KIND_BASIC)) return generateReplyBasic2Bytes(method, vars);
            case CNV_BYTES_2_REPLY:
                prepareHeader(cnvInfo, vars);
                prepareVarBytes(method, vars);
                prepareReply(cnvInfo, vars);
                prepareMsgType(cnvInfo, vars);
                if (vars.get(KEY_RETURN_KIND).equals(RETURN_KIND_DTO)) return generateBytes2ReplyDTO(method, vars);
                if (vars.get(KEY_RETURN_KIND).equals(RETURN_KIND_COLLECTION)) return generateBytes2ReplyCollection(method, vars);
                break;
        }
        return null;
    }

    private void prepareVarBytes(Method method, Map<String, Object> vars) {
        Parameter[] paras = method.getParameters();
        if (paras.length == 1) {
            String name = paras[0].getAnnotation(Name.class).value();
            vars.put(KEY_VAR_BYTES, name);
        }
    }

    private void prepareHeader(CnvInfo annotation, Map<String, Object> vars) {
        try {
            Class header;
            header = Class.forName(annotation.headerClass());
            assert header != null;
            addImports(header);
            vars.put(KEY_HEADER_CLASS_NAME, header.getSimpleName());
            vars.put(KEY_VAR_HEADER, "header");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void prepareRequest(CnvInfo annotation, Map<String, Object> vars) {
        try {
            Class request;
            request = Class.forName(annotation.requestClass());
            assert request != null;
            addImports(request);
            vars.put(KEY_REQUEST_CLASS_NAME, request.getSimpleName());
            vars.put(KEY_VAR_REQUEST, "request");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void prepareReply(CnvInfo annotation, Map<String, Object> vars) {
        try {
            Class reply;
            reply = Class.forName(annotation.replyClass());
            assert reply != null;
            addImports(reply);
            vars.put(KEY_REPLY_CLASS_NAME, reply.getSimpleName());
            vars.put(KEY_VAR_REPLY, "reply");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void prepareMsgType(CnvInfo annotation, Map<String, Object> vars) {
        try {
            Class msgType;
            msgType = Class.forName(annotation.msgTypeClass());
            assert msgType != null;
            addImports(msgType);
            vars.put(KEY_MSGTYPE_CLASS_NAME, msgType.getSimpleName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String generateGetHeaderInfo(Method method, Map<String, Object> vars) {
        URL url = getUrl(GET_HEADER_INFO);
        String contents = null;
        try (InputStream inStream = url.openStream()) {
            contents = (String) TemplateRuntime.eval(inStream, null, vars);
        } catch (IOException e) {
            logger.error("Error in generate file headerClass!", e);
        }
        return contents;
    }

    private String generateRequest2Bytes(Method method, Map<String, Object> vars) {
        URL url = getUrl(REQUEST_2_BYTES);
        String contents = null;
        try (InputStream inStream = url.openStream()) {
            contents = (String) TemplateRuntime.eval(inStream, null, vars);
        } catch (IOException e) {
            logger.error("Error in generate file headerClass!", e);
        }
        return contents;
    }

    private String generateBytes2Request(Method method, Map<String, Object> vars) {
        URL url = getUrl(BYTES_2_REQUEST);
        String contents = null;
        try (InputStream inStream = url.openStream()) {
            contents = (String) TemplateRuntime.eval(inStream, null, vars);
        } catch (IOException e) {
            logger.error("Error in generate file headerClass!", e);
        }
        return contents;
    }

    private String generateReplyCollection2Bytes(Method method, Map<String, Object> vars) {
        URL url = getUrl(REPLY_COLLECTION_2_BYTES);
        String contents = null;
        try (InputStream inStream = url.openStream()) {
            contents = (String) TemplateRuntime.eval(inStream, null, vars);
        } catch (IOException e) {
            logger.error("Error in generate file headerClass!", e);
        }
        return contents;
    }

    private String generateReplyDTO2Bytes(Method method, Map<String, Object> vars) {
        URL url = getUrl(REPLY_DTO_2_BYTES);
        String contents = null;
        try (InputStream inStream = url.openStream()) {
            contents = (String) TemplateRuntime.eval(inStream, null, vars);
        } catch (IOException e) {
            logger.error("Error in generate file headerClass!", e);
        }
        return contents;
    }

    private String generateReplyBasic2Bytes(Method method, Map<String, Object> vars) {
        URL url = getUrl(REPLY_BASIC_2_BYTES);
        String contents = null;
        try (InputStream inStream = url.openStream()) {
            contents = (String) TemplateRuntime.eval(inStream, null, vars);
        } catch (IOException e) {
            logger.error("Error in generate file headerClass!", e);
        }
        return contents;
    }

    private String generateBytes2ReplyCollection(Method method, Map<String, Object> vars) {
        // varBytes, exception, msgDTOClass, varMsgDTO, dtoClass, stringContained, varDTOFields, varDTOs
        CnvInfo annotation = (CnvInfo) method.getAnnotations()[0];
        String msgDTOClass = StringUtils.substringAfterLast(annotation.msgDTOClass(), ".");
        String varMsgDTO = StringUtils.uncapitalize(msgDTOClass);
        Class dtoClassObject = (Class) vars.get(KEY_DTO_CLASS);
        String dtoClass = dtoClassObject.getSimpleName();
        String varDTO = StringUtils.uncapitalize(dtoClass);
        String varDTOs = StringUtils.uncapitalize(dtoClass) + "s";
        boolean stringContained = false;
        StringBuilder varDTOFields = new StringBuilder();
        String[] fields = annotation.dtoFields().split(",");
        for (String field : fields) {
            String fieldName = field.trim();
            Field dtoField = null;
            try {
                dtoField = dtoClassObject.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            assert (dtoField != null);
            varDTOFields.append("                ");
            if (dtoField.getType().getName().equals("java.lang.String")) {
                stringContained = true;
                varDTOFields.append(varDTO).append(".set").append(StringUtils.capitalize(fieldName))
                        .append("(").append(varMsgDTO).append(".get").append(StringUtils.capitalize(fieldName)).append("());");
            } else {
                varDTOFields.append(varDTO).append(".set").append(StringUtils.capitalize(fieldName))
                        .append("(").append(varMsgDTO).append(".").append(fieldName).append("());");
            }
            varDTOFields.append("\n");
        }
        vars.put(KEY_MSGDTO_CLASS_NAME, msgDTOClass);
        vars.put(KEY_VAR_MSGDTO, varMsgDTO);
        vars.put(KEY_DTO_CLASS_NAME, dtoClass);
        vars.put(KEY_VAR_DTO, varDTO);
        vars.put(KEY_VAR_DTOS, varDTOs);
        vars.put(KEY_STRING_CONTAINED, stringContained);
        vars.put(KEY_VAR_DTOFIELDS, varDTOFields);
        URL url = getUrl(BYTES_2_REPLY_COLLECTION);
        String retSegments = null;
        try (InputStream inStream = url.openStream()) {
            retSegments = (String) TemplateRuntime.eval(inStream, null, vars);
        } catch (IOException e) {
            logger.error("Error in generate method return body!", e);
        }
        return retSegments;
    }

    private String generateBytes2ReplyDTO(Method method, Map<String, Object> vars) {
        // dtoClass, varDTO, stringContained, varDTOFields, exception
        CnvInfo annotation = (CnvInfo) method.getAnnotations()[0];
        Class dtoClassObject = (Class) vars.get(KEY_DTO_CLASS);
        String dtoClass = dtoClassObject.getSimpleName();
        String varDTO = StringUtils.uncapitalize(dtoClassObject.getSimpleName());
        boolean containString = false;
        StringBuilder varDTOFields = new StringBuilder();
        String[] fields = annotation.dtoFields().split(",");
        for (String field : fields) {
            String fieldName = field.trim();
            Field dtoField = null;
            try {
                dtoField = dtoClassObject.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            assert (dtoField != null);
            varDTOFields.append("        ");
            if (dtoField.getType().getName().equals("java.lang.String")) {
                containString = true;
                varDTOFields.append(varDTO).append(".set").append(StringUtils.capitalize(fieldName))
                        .append("(").append(vars.get(KEY_VAR_REPLY)).append(".get").append(StringUtils.capitalize(fieldName)).append("());");
            } else {
                varDTOFields.append(varDTO).append(".set").append(StringUtils.capitalize(fieldName))
                        .append("(").append(vars.get(KEY_VAR_REPLY)).append(".").append(fieldName).append("());");
            }
            varDTOFields.append("\n");
        }
        vars.put(KEY_DTO_CLASS_NAME, dtoClass);
        vars.put(KEY_VAR_DTO, varDTO);
        vars.put(KEY_STRING_CONTAINED, containString);
        vars.put(KEY_VAR_DTOFIELDS, varDTOFields);
        URL url = getUrl(BYTES_2_REPLY_DTO);
        String retSegments = null;
        try (InputStream inStream = url.openStream()) {
            //dtoClass, varDTO, stringContained, varDTOFields, exception
            retSegments = (String) TemplateRuntime.eval(inStream, null, vars);
        } catch (IOException e) {
            logger.error("Error in generate method return body!", e);
        }
        return retSegments;
    }

    private void prepareRequestFields(Method method, Map<String, Object> vars) {
        boolean stringContained = false;
        StringBuilder varFields = new StringBuilder();
        Parameter[] parameters = method.getParameters();
        for (Parameter para : parameters) {
            Fields fieldss = para.getAnnotation(Fields.class);
            if (fieldss == null) continue;
            String name = para.getAnnotation(Name.class).value();
            String[] fields = fieldss.value().split(",");
            for (String field : fields) {
                try {
                    String fieldName = field.trim();
                    Class paraClass = para.getType();
                    if (paraClass.isPrimitive()) {
                        varFields.append(".").append(fieldName)
                                .append("(").append(name).append(")");
                        continue;
                    }
                    if (paraClass.getSimpleName().equals("String")) {
                        varFields.append(".put").append(StringUtils.capitalize(fieldName))
                                .append("(").append(name).append(")");
                        stringContained = true;
                        continue;
                    }
                    if (para.getType().getDeclaredField(fieldName).getType().getSimpleName().equals("String")) {
                        // String field
                        varFields.append(".put").append(StringUtils.capitalize(fieldName))
                                .append("(").append(name)
                                .append(".get").append(StringUtils.capitalize(fieldName))
                                .append("())");
                        stringContained = true;
                    } else {
                        varFields.append(".").append(fieldName)
                                .append("(").append(name)
                                .append(".get").append(StringUtils.capitalize(fieldName))
                                .append("())");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // prepare vars
        vars.put(KEY_VAR_FIELDS, varFields.toString());
        vars.put(KEY_STRING_CONTAINED, stringContained);
    }

    private void prepareReplyFields(Method method, Map<String, Object> vars) {
        // varDTOs, msgDTOClass, varMsgDTO, varDTO, varDTOClass, varFields, varDTOFields
        boolean stringContained = false;
        StringBuilder varFields = new StringBuilder();
        StringBuilder varDTOFields = new StringBuilder();
        String paraKind = PARA_KIND_BASIC;
        Parameter[] parameters = method.getParameters();
        vars.put(KEY_VAR_RETURN_CODE, parameters[0].getAnnotation(Name.class).value());
        for (Parameter para : parameters) {
            ReplyFields replyFields = para.getAnnotation(ReplyFields.class);
            if (replyFields == null) continue;
            String name = para.getAnnotation(Name.class).value();
            String[] fields = replyFields.value().split(",");
            for (String field : fields) {
                try {
                    String fieldName = field.trim();
                    Class paraClass = para.getType();
                    if (paraClass.isPrimitive()) {
                        varFields.append(".").append(fieldName)
                                .append("(").append(name).append(")");
                        continue;
                    }
                    if (paraClass.getName().equals("java.lang.String")) {
                        varFields.append(".put").append(StringUtils.capitalize(fieldName))
                                .append("(").append(name).append(")");
                        stringContained = true;
                        continue;
                    }
                    if (para.getParameterizedType() instanceof ParameterizedType) {
                        paraKind = PARA_KIND_COLLECTION;
                        // DTO Collection
                        vars.put(KEY_VAR_DTOS, name);
                        String msgDTOClass = StringUtils.substringAfterLast(replyFields.msgDTOClass(), ".");
                        vars.put(KEY_MSGDTO_CLASS_NAME, msgDTOClass);
                        vars.put(KEY_VAR_MSGDTO, StringUtils.uncapitalize(msgDTOClass));
                        Class dtoClass = Class.forName(((ParameterizedType) para.getParameterizedType()).getActualTypeArguments()[0].getTypeName());
                        addImports(dtoClass);
                        vars.put(KEY_DTO_CLASS_NAME, dtoClass.getSimpleName());
                        vars.put(KEY_VAR_DTO, StringUtils.uncapitalize(dtoClass.getSimpleName()));
                        // build varDTOFields
                        if (dtoClass.getDeclaredField(fieldName).getType().getName().equals("java.lang.String")) {
                            // String field
                            varDTOFields.append(".put").append(StringUtils.capitalize(fieldName))
                                    .append("(").append(vars.get(KEY_VAR_DTO))
                                    .append(".get").append(StringUtils.capitalize(fieldName))
                                    .append("())");
                            stringContained = true;
                        } else {
                            varDTOFields.append(".").append(fieldName)
                                    .append("(").append(vars.get(KEY_VAR_DTO))
                                    .append(".get").append(StringUtils.capitalize(fieldName))
                                    .append("())");
                        }
                    } else {
                        // varBytes, replyClass, varReply, dtoClass, varDTO, stringContained, varDTOFields, exception
                        // varDTOFields.
                        paraKind = PARA_KIND_DTO;
                        Class dtoClass = para.getType();
                        addImports(dtoClass);
                        vars.put(KEY_DTO_CLASS_NAME, dtoClass.getSimpleName());
                        vars.put(KEY_VAR_DTO, StringUtils.uncapitalize(dtoClass.getSimpleName()));
                        // build varDTOFields
                        if (dtoClass.getDeclaredField(fieldName).getType().getName().equals("java.lang.String")) {
                            // String field
                            stringContained = true;
                            varDTOFields.append(".put").append(StringUtils.capitalize(fieldName)).append("(")
                                    .append(vars.get(KEY_VAR_DTO)).append(".get").append(StringUtils.capitalize(fieldName))
                                    .append("())");
                        } else {
                            varDTOFields.append(".").append(fieldName).append("(")
                                    .append(vars.get(KEY_VAR_DTO)).append(".get").append(StringUtils.capitalize(fieldName)).append("())");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // prepare vars
        vars.put(KEY_REPLY2BYTES_PARA_KIND, paraKind);
        vars.put(KEY_STRING_CONTAINED, stringContained);
        vars.put(KEY_VAR_FIELDS, varFields.toString());
        vars.put(KEY_VAR_DTOFIELDS, varDTOFields.toString());
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
        fileName.append(template.replace('.', '/')).append(FILE_POSTFIX);
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
}
