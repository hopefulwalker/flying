/* Generated SBE (Simple Binary Encoding) message codec */
package com.flying.monitor.msg.gen;

import uk.co.real_logic.sbe.codec.java.*;

import java.io.UnsupportedEncodingException;

public class ServerQueryRequest
{
    public static final int BLOCK_LENGTH = 18;
    public static final int TEMPLATE_ID = 3;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final ServerQueryRequest parentMessage = this;
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

    public ServerQueryRequest wrapForEncode(final DirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = BLOCK_LENGTH;
        this.actingVersion = SCHEMA_VERSION;
        limit(offset + actingBlockLength);

        return this;
    }

    public ServerQueryRequest wrapForDecode(
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

    public static int regionId()
    {
        return 1;
    }

    public static String regionMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte regionNullValue()
    {
        return (byte)0;
    }

    public static byte regionMinValue()
    {
        return (byte)32;
    }

    public static byte regionMaxValue()
    {
        return (byte)126;
    }

    public static int regionLength()
    {
        return 16;
    }

    public byte region(final int index)
    {
        if (index < 0 || index >= 16)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 0 + (index * 1));
    }

    public void region(final int index, final byte value)
    {
        if (index < 0 || index >= 16)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 0 + (index * 1), value);
    }

    public static String regionCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getRegion(final byte[] dst, final int dstOffset)
    {
        final int length = 16;
        if (dstOffset < 0 || dstOffset >= dst.length)
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 0, dst, dstOffset, length);
        return length;
    }

    public String getRegion() throws UnsupportedEncodingException
    {
        final byte[] dst = new byte[16];
        CodecUtil.charsGet(buffer, this.offset + 0, dst, 0, dst.length);
        return new String(dst,"UTF-8").trim();
    }

    public ServerQueryRequest putRegion(final byte[] src, final int srcOffset)
    {
        final int length = 16;
        if (srcOffset < 0 || srcOffset >= src.length)
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }
        CodecUtil.charsPut(buffer, this.offset + 0, src, srcOffset, length);
        return this;
    }

    public ServerQueryRequest putRegion(final String region) throws UnsupportedEncodingException
    {
        byte[] src = region.getBytes("UTF-8");
        CodecUtil.charsPut(buffer, this.offset + 0, src, 0, 16);
        return this;
    }

    public static int serviceTypeId()
    {
        return 2;
    }

    public static String serviceTypeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static short serviceTypeNullValue()
    {
        return (short)-32768;
    }

    public static short serviceTypeMinValue()
    {
        return (short)-32767;
    }

    public static short serviceTypeMaxValue()
    {
        return (short)32767;
    }

    public short serviceType()
    {
        return CodecUtil.int16Get(buffer, offset + 16, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public ServerQueryRequest serviceType(final short value)
    {
        CodecUtil.int16Put(buffer, offset + 16, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
