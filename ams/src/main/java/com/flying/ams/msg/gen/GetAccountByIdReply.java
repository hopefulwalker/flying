/* Generated SBE (Simple Binary Encoding) message codec */
package com.flying.ams.msg.gen;

import uk.co.real_logic.sbe.codec.java.*;

import java.io.UnsupportedEncodingException;

public class GetAccountByIdReply
{
    public static final int BLOCK_LENGTH = 47;
    public static final int TEMPLATE_ID = 2;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final GetAccountByIdReply parentMessage = this;
    private DirectBuffer buffer;
    private int offset;
    private int limit;
    private int actingBlockLength;
    private int actingVersion;

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "";
    }

    public int offset()
    {
        return offset;
    }

    public GetAccountByIdReply wrapForEncode(final DirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = BLOCK_LENGTH;
        this.actingVersion = SCHEMA_VERSION;
        limit(offset + actingBlockLength);

        return this;
    }

    public GetAccountByIdReply wrapForDecode(
        final DirectBuffer buffer, final int offset, final int actingBlockLength, final int actingVersion)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

        return this;
    }

    public int size()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        buffer.checkLimit(limit);
        this.limit = limit;
    }

    public static int retCodeId()
    {
        return 1;
    }

    public static String retCodeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int retCodeNullValue()
    {
        return -2147483648;
    }

    public static int retCodeMinValue()
    {
        return -2147483647;
    }

    public static int retCodeMaxValue()
    {
        return 2147483647;
    }

    public int retCode()
    {
        return CodecUtil.int32Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public GetAccountByIdReply retCode(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int aidId()
    {
        return 2;
    }

    public static String aidMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long aidNullValue()
    {
        return -9223372036854775808L;
    }

    public static long aidMinValue()
    {
        return -9223372036854775807L;
    }

    public static long aidMaxValue()
    {
        return 9223372036854775807L;
    }

    public long aid()
    {
        return CodecUtil.int64Get(buffer, offset + 4, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public GetAccountByIdReply aid(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 4, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int typeId()
    {
        return 3;
    }

    public static String typeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static short typeNullValue()
    {
        return (short)-32768;
    }

    public static short typeMinValue()
    {
        return (short)-32767;
    }

    public static short typeMaxValue()
    {
        return (short)32767;
    }

    public short type()
    {
        return CodecUtil.int16Get(buffer, offset + 12, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public GetAccountByIdReply type(final short value)
    {
        CodecUtil.int16Put(buffer, offset + 12, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int custIdId()
    {
        return 4;
    }

    public static String custIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long custIdNullValue()
    {
        return -9223372036854775808L;
    }

    public static long custIdMinValue()
    {
        return -9223372036854775807L;
    }

    public static long custIdMaxValue()
    {
        return 9223372036854775807L;
    }

    public long custId()
    {
        return CodecUtil.int64Get(buffer, offset + 14, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public GetAccountByIdReply custId(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 14, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int parentIdId()
    {
        return 5;
    }

    public static String parentIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long parentIdNullValue()
    {
        return -9223372036854775808L;
    }

    public static long parentIdMinValue()
    {
        return -9223372036854775807L;
    }

    public static long parentIdMaxValue()
    {
        return 9223372036854775807L;
    }

    public long parentId()
    {
        return CodecUtil.int64Get(buffer, offset + 22, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public GetAccountByIdReply parentId(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 22, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int statusIdId()
    {
        return 6;
    }

    public static String statusIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte statusIdNullValue()
    {
        return (byte)-128;
    }

    public static byte statusIdMinValue()
    {
        return (byte)-127;
    }

    public static byte statusIdMaxValue()
    {
        return (byte)127;
    }

    public byte statusId()
    {
        return CodecUtil.int8Get(buffer, offset + 30);
    }

    public GetAccountByIdReply statusId(final byte value)
    {
        CodecUtil.int8Put(buffer, offset + 30, value);
        return this;
    }

    public static int createTimeId()
    {
        return 7;
    }

    public static String createTimeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long createTimeNullValue()
    {
        return -9223372036854775808L;
    }

    public static long createTimeMinValue()
    {
        return -9223372036854775807L;
    }

    public static long createTimeMaxValue()
    {
        return 9223372036854775807L;
    }

    public long createTime()
    {
        return CodecUtil.int64Get(buffer, offset + 31, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public GetAccountByIdReply createTime(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 31, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int updateTimeId()
    {
        return 8;
    }

    public static String updateTimeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long updateTimeNullValue()
    {
        return -9223372036854775808L;
    }

    public static long updateTimeMinValue()
    {
        return -9223372036854775807L;
    }

    public static long updateTimeMaxValue()
    {
        return 9223372036854775807L;
    }

    public long updateTime()
    {
        return CodecUtil.int64Get(buffer, offset + 39, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public GetAccountByIdReply updateTime(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 39, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
