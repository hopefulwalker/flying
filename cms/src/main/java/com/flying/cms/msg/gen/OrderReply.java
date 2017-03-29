/* Generated SBE (Simple Binary Encoding) message codec */
package com.flying.cms.msg.gen;

import uk.co.real_logic.sbe.codec.java.*;

import java.io.UnsupportedEncodingException;

public class OrderReply
{
    public static final int BLOCK_LENGTH = 20;
    public static final int TEMPLATE_ID = 2;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final OrderReply parentMessage = this;
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

    public OrderReply wrapForEncode(final DirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = BLOCK_LENGTH;
        this.actingVersion = SCHEMA_VERSION;
        limit(offset + actingBlockLength);

        return this;
    }

    public OrderReply wrapForDecode(
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

    public static int requestNoId()
    {
        return 1;
    }

    public static String requestNoMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long requestNoNullValue()
    {
        return -9223372036854775808L;
    }

    public static long requestNoMinValue()
    {
        return -9223372036854775807L;
    }

    public static long requestNoMaxValue()
    {
        return 9223372036854775807L;
    }

    public long requestNo()
    {
        return CodecUtil.int64Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderReply requestNo(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int retCodeId()
    {
        return 2;
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
        return CodecUtil.int32Get(buffer, offset + 8, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderReply retCode(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int cntrNoId()
    {
        return 3;
    }

    public static String cntrNoMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte cntrNoNullValue()
    {
        return (byte)0;
    }

    public static byte cntrNoMinValue()
    {
        return (byte)32;
    }

    public static byte cntrNoMaxValue()
    {
        return (byte)126;
    }

    public static int cntrNoLength()
    {
        return 8;
    }

    public byte cntrNo(final int index)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 12 + (index * 1));
    }

    public void cntrNo(final int index, final byte value)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 12 + (index * 1), value);
    }

    public static String cntrNoCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getCntrNo(final byte[] dst, final int dstOffset)
    {
        final int length = 8;
        if (dstOffset < 0 || dstOffset >= dst.length)
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 12, dst, dstOffset, length);
        return length;
    }

    public String getCntrNo() throws UnsupportedEncodingException
    {
        final byte[] dst = new byte[8];
        CodecUtil.charsGet(buffer, this.offset + 12, dst, 0, dst.length);
        return new String(dst,"UTF-8").trim();
    }

    public OrderReply putCntrNo(final byte[] src, final int srcOffset)
    {
        final int length = 8;
        if (srcOffset < 0 || srcOffset >= src.length)
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }
        CodecUtil.charsPut(buffer, this.offset + 12, src, srcOffset, length);
        return this;
    }

    public OrderReply putCntrNo(final String cntrNo) throws UnsupportedEncodingException
    {
        byte[] src = cntrNo.getBytes("UTF-8");
        CodecUtil.charsPut(buffer, this.offset + 12, src, 0, 8);
        return this;
    }
}
