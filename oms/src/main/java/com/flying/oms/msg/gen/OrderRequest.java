/* Generated SBE (Simple Binary Encoding) message codec */
package com.flying.oms.msg.gen;

import uk.co.real_logic.sbe.codec.java.*;

import java.io.UnsupportedEncodingException;

public class OrderRequest
{
    public static final int BLOCK_LENGTH = 41;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final OrderRequest parentMessage = this;
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

    public OrderRequest wrapForEncode(final DirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = BLOCK_LENGTH;
        this.actingVersion = SCHEMA_VERSION;
        limit(offset + actingBlockLength);

        return this;
    }

    public OrderRequest wrapForDecode(
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

    public static int extNoId()
    {
        return 1;
    }

    public static String extNoMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long extNoNullValue()
    {
        return -9223372036854775808L;
    }

    public static long extNoMinValue()
    {
        return -9223372036854775807L;
    }

    public static long extNoMaxValue()
    {
        return 9223372036854775807L;
    }

    public long extNo()
    {
        return CodecUtil.int64Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderRequest extNo(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int acctIdId()
    {
        return 2;
    }

    public static String acctIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long acctIdNullValue()
    {
        return -9223372036854775808L;
    }

    public static long acctIdMinValue()
    {
        return -9223372036854775807L;
    }

    public static long acctIdMaxValue()
    {
        return 9223372036854775807L;
    }

    public long acctId()
    {
        return CodecUtil.int64Get(buffer, offset + 8, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderRequest acctId(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int bsSideIdId()
    {
        return 3;
    }

    public static String bsSideIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte bsSideIdNullValue()
    {
        return (byte)-128;
    }

    public static byte bsSideIdMinValue()
    {
        return (byte)-127;
    }

    public static byte bsSideIdMaxValue()
    {
        return (byte)127;
    }

    public byte bsSideId()
    {
        return CodecUtil.int8Get(buffer, offset + 16);
    }

    public OrderRequest bsSideId(final byte value)
    {
        CodecUtil.int8Put(buffer, offset + 16, value);
        return this;
    }

    public static int exchIdId()
    {
        return 4;
    }

    public static String exchIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static short exchIdNullValue()
    {
        return (short)-32768;
    }

    public static short exchIdMinValue()
    {
        return (short)-32767;
    }

    public static short exchIdMaxValue()
    {
        return (short)32767;
    }

    public short exchId()
    {
        return CodecUtil.int16Get(buffer, offset + 17, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderRequest exchId(final short value)
    {
        CodecUtil.int16Put(buffer, offset + 17, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int sectCodeId()
    {
        return 5;
    }

    public static String sectCodeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte sectCodeNullValue()
    {
        return (byte)0;
    }

    public static byte sectCodeMinValue()
    {
        return (byte)32;
    }

    public static byte sectCodeMaxValue()
    {
        return (byte)126;
    }

    public static int sectCodeLength()
    {
        return 6;
    }

    public byte sectCode(final int index)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 19 + (index * 1));
    }

    public void sectCode(final int index, final byte value)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 19 + (index * 1), value);
    }

    public static String sectCodeCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getSectCode(final byte[] dst, final int dstOffset)
    {
        final int length = 6;
        if (dstOffset < 0 || dstOffset >= dst.length)
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 19, dst, dstOffset, length);
        return length;
    }

    public String getSectCode() throws UnsupportedEncodingException
    {
        final byte[] dst = new byte[6];
        CodecUtil.charsGet(buffer, this.offset + 19, dst, 0, dst.length);
        return new String(dst,"UTF-8").trim();
    }

    public OrderRequest putSectCode(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset >= src.length)
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }
        CodecUtil.charsPut(buffer, this.offset + 19, src, srcOffset, length);
        return this;
    }

    public OrderRequest putSectCode(final String sectCode) throws UnsupportedEncodingException
    {
        byte[] src = sectCode.getBytes("UTF-8");
        CodecUtil.charsPut(buffer, this.offset + 19, src, 0, 6);
        return this;
    }

    public static int priceId()
    {
        return 6;
    }

    public static String priceMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static double priceNullValue()
    {
        return Double.NaN;
    }

    public static double priceMinValue()
    {
        return 4.9E-324d;
    }

    public static double priceMaxValue()
    {
        return 1.7976931348623157E308d;
    }

    public double price()
    {
        return CodecUtil.doubleGet(buffer, offset + 25, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderRequest price(final double value)
    {
        CodecUtil.doublePut(buffer, offset + 25, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int qtyId()
    {
        return 7;
    }

    public static String qtyMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long qtyNullValue()
    {
        return -9223372036854775808L;
    }

    public static long qtyMinValue()
    {
        return -9223372036854775807L;
    }

    public static long qtyMaxValue()
    {
        return 9223372036854775807L;
    }

    public long qty()
    {
        return CodecUtil.int64Get(buffer, offset + 33, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderRequest qty(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 33, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
