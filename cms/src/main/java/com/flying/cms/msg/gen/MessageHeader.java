/* Generated SBE (Simple Binary Encoding) message codec */
package com.flying.cms.msg.gen;

import uk.co.real_logic.sbe.codec.java.*;

import java.io.UnsupportedEncodingException;

public class MessageHeader
{
    private DirectBuffer buffer;
    private int offset;
    private int actingVersion;

    public MessageHeader wrap(final DirectBuffer buffer, final int offset, final int actingVersion)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingVersion = actingVersion;
        return this;
    }

    public int size()
    {
        return 22;
    }

    public static int blockLengthNullValue()
    {
        return 65535;
    }

    public static int blockLengthMinValue()
    {
        return 0;
    }

    public static int blockLengthMaxValue()
    {
        return 65534;
    }

    public int blockLength()
    {
        return CodecUtil.uint16Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public MessageHeader blockLength(final int value)
    {
        CodecUtil.uint16Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int templateIdNullValue()
    {
        return 65535;
    }

    public static int templateIdMinValue()
    {
        return 0;
    }

    public static int templateIdMaxValue()
    {
        return 65534;
    }

    public int templateId()
    {
        return CodecUtil.uint16Get(buffer, offset + 2, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public MessageHeader templateId(final int value)
    {
        CodecUtil.uint16Put(buffer, offset + 2, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int schemaIdNullValue()
    {
        return 65535;
    }

    public static int schemaIdMinValue()
    {
        return 0;
    }

    public static int schemaIdMaxValue()
    {
        return 65534;
    }

    public int schemaId()
    {
        return CodecUtil.uint16Get(buffer, offset + 4, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public MessageHeader schemaId(final int value)
    {
        CodecUtil.uint16Put(buffer, offset + 4, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int versionNullValue()
    {
        return 65535;
    }

    public static int versionMinValue()
    {
        return 0;
    }

    public static int versionMaxValue()
    {
        return 65534;
    }

    public int version()
    {
        return CodecUtil.uint16Get(buffer, offset + 6, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public MessageHeader version(final int value)
    {
        CodecUtil.uint16Put(buffer, offset + 6, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static long timestampNullValue()
    {
        return -9223372036854775808L;
    }

    public static long timestampMinValue()
    {
        return -9223372036854775807L;
    }

    public static long timestampMaxValue()
    {
        return 9223372036854775807L;
    }

    public long timestamp()
    {
        return CodecUtil.int64Get(buffer, offset + 8, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public MessageHeader timestamp(final long value)
    {
        CodecUtil.int64Put(buffer, offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int sourceIPNullValue()
    {
        return -2147483648;
    }

    public static int sourceIPMinValue()
    {
        return -2147483647;
    }

    public static int sourceIPMaxValue()
    {
        return 2147483647;
    }

    public int sourceIP()
    {
        return CodecUtil.int32Get(buffer, offset + 16, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public MessageHeader sourceIP(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 16, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static short msgTypeNullValue()
    {
        return (short)-32768;
    }

    public static short msgTypeMinValue()
    {
        return (short)-32767;
    }

    public static short msgTypeMaxValue()
    {
        return (short)32767;
    }

    public short msgType()
    {
        return CodecUtil.int16Get(buffer, offset + 20, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public MessageHeader msgType(final short value)
    {
        CodecUtil.int16Put(buffer, offset + 20, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
