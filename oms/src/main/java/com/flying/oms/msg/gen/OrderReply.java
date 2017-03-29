/* Generated SBE (Simple Binary Encoding) message codec */
package com.flying.oms.msg.gen;

import uk.co.real_logic.sbe.codec.java.*;

import java.io.UnsupportedEncodingException;

public class OrderReply
{
    public static final int BLOCK_LENGTH = 86;
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

    public OrderReply retCode(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int extNoId()
    {
        return 2;
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
        return CodecUtil.int64Get(buffer, offset + 4, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderReply extNo(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 4, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int oidId()
    {
        return 3;
    }

    public static String oidMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long oidNullValue()
    {
        return -9223372036854775808L;
    }

    public static long oidMinValue()
    {
        return -9223372036854775807L;
    }

    public static long oidMaxValue()
    {
        return 9223372036854775807L;
    }

    public long oid()
    {
        return CodecUtil.int64Get(buffer, offset + 12, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderReply oid(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 12, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int acctIdId()
    {
        return 4;
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
        return CodecUtil.int64Get(buffer, offset + 20, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderReply acctId(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 20, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int exchIdId()
    {
        return 5;
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
        return CodecUtil.int16Get(buffer, offset + 28, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderReply exchId(final short value)
    {
        CodecUtil.int16Put(buffer, offset + 28, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int sectCodeId()
    {
        return 6;
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

        return CodecUtil.charGet(buffer, this.offset + 30 + (index * 1));
    }

    public void sectCode(final int index, final byte value)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 30 + (index * 1), value);
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

        CodecUtil.charsGet(buffer, this.offset + 30, dst, dstOffset, length);
        return length;
    }

    public String getSectCode() throws UnsupportedEncodingException
    {
        final byte[] dst = new byte[6];
        CodecUtil.charsGet(buffer, this.offset + 30, dst, 0, dst.length);
        return new String(dst,"UTF-8").trim();
    }

    public OrderReply putSectCode(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset >= src.length)
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }
        CodecUtil.charsPut(buffer, this.offset + 30, src, srcOffset, length);
        return this;
    }

    public OrderReply putSectCode(final String sectCode) throws UnsupportedEncodingException
    {
        byte[] src = sectCode.getBytes("UTF-8");
        CodecUtil.charsPut(buffer, this.offset + 30, src, 0, 6);
        return this;
    }

    public static int bsSideIdId()
    {
        return 7;
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
        return CodecUtil.int8Get(buffer, offset + 36);
    }

    public OrderReply bsSideId(final byte value)
    {
        CodecUtil.int8Put(buffer, offset + 36, value);
        return this;
    }

    public static int priceId()
    {
        return 8;
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
        return CodecUtil.doubleGet(buffer, offset + 37, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderReply price(final double value)
    {
        CodecUtil.doublePut(buffer, offset + 37, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int qtyId()
    {
        return 9;
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
        return CodecUtil.int64Get(buffer, offset + 45, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderReply qty(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 45, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int cntrNoId()
    {
        return 10;
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

        return CodecUtil.charGet(buffer, this.offset + 53 + (index * 1));
    }

    public void cntrNo(final int index, final byte value)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 53 + (index * 1), value);
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

        CodecUtil.charsGet(buffer, this.offset + 53, dst, dstOffset, length);
        return length;
    }

    public String getCntrNo() throws UnsupportedEncodingException
    {
        final byte[] dst = new byte[8];
        CodecUtil.charsGet(buffer, this.offset + 53, dst, 0, dst.length);
        return new String(dst,"UTF-8").trim();
    }

    public OrderReply putCntrNo(final byte[] src, final int srcOffset)
    {
        final int length = 8;
        if (srcOffset < 0 || srcOffset >= src.length)
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }
        CodecUtil.charsPut(buffer, this.offset + 53, src, srcOffset, length);
        return this;
    }

    public OrderReply putCntrNo(final String cntrNo) throws UnsupportedEncodingException
    {
        byte[] src = cntrNo.getBytes("UTF-8");
        CodecUtil.charsPut(buffer, this.offset + 53, src, 0, 8);
        return this;
    }

    public static int bizDateId()
    {
        return 11;
    }

    public static String bizDateMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int bizDateNullValue()
    {
        return -2147483648;
    }

    public static int bizDateMinValue()
    {
        return -2147483647;
    }

    public static int bizDateMaxValue()
    {
        return 2147483647;
    }

    public int bizDate()
    {
        return CodecUtil.int32Get(buffer, offset + 61, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderReply bizDate(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 61, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int stateIdId()
    {
        return 12;
    }

    public static String stateIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte stateIdNullValue()
    {
        return (byte)-128;
    }

    public static byte stateIdMinValue()
    {
        return (byte)-127;
    }

    public static byte stateIdMaxValue()
    {
        return (byte)127;
    }

    public byte stateId()
    {
        return CodecUtil.int8Get(buffer, offset + 65);
    }

    public OrderReply stateId(final byte value)
    {
        CodecUtil.int8Put(buffer, offset + 65, value);
        return this;
    }

    public static int stateEnteredCodeId()
    {
        return 13;
    }

    public static String stateEnteredCodeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int stateEnteredCodeNullValue()
    {
        return -2147483648;
    }

    public static int stateEnteredCodeMinValue()
    {
        return -2147483647;
    }

    public static int stateEnteredCodeMaxValue()
    {
        return 2147483647;
    }

    public int stateEnteredCode()
    {
        return CodecUtil.int32Get(buffer, offset + 66, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderReply stateEnteredCode(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 66, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int createTimeId()
    {
        return 14;
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
        return CodecUtil.int64Get(buffer, offset + 70, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderReply createTime(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 70, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int updateTimeId()
    {
        return 15;
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
        return CodecUtil.int64Get(buffer, offset + 78, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public OrderReply updateTime(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 78, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
