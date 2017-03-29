/* Generated SBE (Simple Binary Encoding) message codec */
package com.flying.monitor.msg.gen;

import uk.co.real_logic.sbe.codec.java.*;

import java.io.UnsupportedEncodingException;

@GroupOrder({ServerQueryReply.Servers.class})
public class ServerQueryReply
{
    public static final int BLOCK_LENGTH = 4;
    public static final int TEMPLATE_ID = 4;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final ServerQueryReply parentMessage = this;
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

    public ServerQueryReply wrapForEncode(final DirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = BLOCK_LENGTH;
        this.actingVersion = SCHEMA_VERSION;
        limit(offset + actingBlockLength);

        return this;
    }

    public ServerQueryReply wrapForDecode(
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

    public ServerQueryReply retCode(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    private final Servers servers = new Servers();

    public static long serversId()
    {
        return 2;
    }

    public Servers servers()
    {
        servers.wrapForDecode(parentMessage, buffer, actingVersion);
        return servers;
    }

    public Servers serversCount(final int count)
    {
        servers.wrapForEncode(parentMessage, buffer, count);
        return servers;
    }

    public static class Servers implements Iterable<Servers>, java.util.Iterator<Servers>
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeEncoding dimensions = new GroupSizeEncoding();
        private ServerQueryReply parentMessage;
        private DirectBuffer buffer;
        private int blockLength;
        private int actingVersion;
        private int count;
        private int index;
        private int offset;

        public void wrapForDecode(
            final ServerQueryReply parentMessage, final DirectBuffer buffer, final int actingVersion)
        {
            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit(), actingVersion);
            blockLength = dimensions.blockLength();
            count = dimensions.numInGroup();
            this.actingVersion = actingVersion;
            index = -1;
            parentMessage.limit(parentMessage.limit() + HEADER_SIZE);
        }

        public void wrapForEncode(final ServerQueryReply parentMessage, final DirectBuffer buffer, final int count)
        {
            this.parentMessage = parentMessage;
            this.buffer = buffer;
            actingVersion = SCHEMA_VERSION;
            dimensions.wrap(buffer, parentMessage.limit(), actingVersion);
            dimensions.blockLength((int)163);
            dimensions.numInGroup((short)count);
            index = -1;
            this.count = count;
            blockLength = 163;
            parentMessage.limit(parentMessage.limit() + HEADER_SIZE);
        }

        public static int sbeHeaderSize()
        {
            return HEADER_SIZE;
        }

        public static int sbeBlockLength()
        {
            return 163;
        }

        public int actingBlockLength()
        {
            return blockLength;
        }

        public int count()
        {
            return count;
        }

        @Override
        public java.util.Iterator<Servers> iterator()
        {
            return this;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext()
        {
            return (index + 1) < count;
        }

        @Override
        public Servers next()
        {
            if (index + 1 >= count)
            {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + blockLength);
            ++index;

            return this;
        }

        public static int uuidId()
        {
            return 3;
        }

        public static String uuidMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static byte uuidNullValue()
        {
            return (byte)0;
        }

        public static byte uuidMinValue()
        {
            return (byte)32;
        }

        public static byte uuidMaxValue()
        {
            return (byte)126;
        }

        public static int uuidLength()
        {
            return 36;
        }

        public byte uuid(final int index)
        {
            if (index < 0 || index >= 36)
            {
                throw new IndexOutOfBoundsException("index out of range: index=" + index);
            }

            return CodecUtil.charGet(buffer, this.offset + 0 + (index * 1));
        }

        public void uuid(final int index, final byte value)
        {
            if (index < 0 || index >= 36)
            {
                throw new IndexOutOfBoundsException("index out of range: index=" + index);
            }

            CodecUtil.charPut(buffer, this.offset + 0 + (index * 1), value);
        }

        public static String uuidCharacterEncoding()
        {
            return "UTF-8";
        }

        public int getUuid(final byte[] dst, final int dstOffset)
        {
            final int length = 36;
            if (dstOffset < 0 || dstOffset >= dst.length)
            {
                throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
            }

            CodecUtil.charsGet(buffer, this.offset + 0, dst, dstOffset, length);
            return length;
        }

        public String getUuid() throws UnsupportedEncodingException
        {
            final byte[] dst = new byte[36];
            CodecUtil.charsGet(buffer, this.offset + 0, dst, 0, dst.length);
            return new String(dst,"UTF-8").trim();
        }

        public Servers putUuid(final byte[] src, final int srcOffset)
        {
            final int length = 36;
            if (srcOffset < 0 || srcOffset >= src.length)
            {
                throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
            }
            CodecUtil.charsPut(buffer, this.offset + 0, src, srcOffset, length);
            return this;
        }

        public Servers putUuid(final String uuid) throws UnsupportedEncodingException
        {
            byte[] src = uuid.getBytes("UTF-8");
            CodecUtil.charsPut(buffer, this.offset + 0, src, 0, 36);
            return this;
        }

        public static int regionId()
        {
            return 4;
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

            return CodecUtil.charGet(buffer, this.offset + 36 + (index * 1));
        }

        public void region(final int index, final byte value)
        {
            if (index < 0 || index >= 16)
            {
                throw new IndexOutOfBoundsException("index out of range: index=" + index);
            }

            CodecUtil.charPut(buffer, this.offset + 36 + (index * 1), value);
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

            CodecUtil.charsGet(buffer, this.offset + 36, dst, dstOffset, length);
            return length;
        }

        public String getRegion() throws UnsupportedEncodingException
        {
            final byte[] dst = new byte[16];
            CodecUtil.charsGet(buffer, this.offset + 36, dst, 0, dst.length);
            return new String(dst,"UTF-8").trim();
        }

        public Servers putRegion(final byte[] src, final int srcOffset)
        {
            final int length = 16;
            if (srcOffset < 0 || srcOffset >= src.length)
            {
                throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
            }
            CodecUtil.charsPut(buffer, this.offset + 36, src, srcOffset, length);
            return this;
        }

        public Servers putRegion(final String region) throws UnsupportedEncodingException
        {
            byte[] src = region.getBytes("UTF-8");
            CodecUtil.charsPut(buffer, this.offset + 36, src, 0, 16);
            return this;
        }

        public static int serviceTypeId()
        {
            return 5;
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
            return CodecUtil.int16Get(buffer, offset + 52, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public Servers serviceType(final short value)
        {
            CodecUtil.int16Put(buffer, offset + 52, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }

        public static int nameId()
        {
            return 6;
        }

        public static String nameMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static byte nameNullValue()
        {
            return (byte)0;
        }

        public static byte nameMinValue()
        {
            return (byte)32;
        }

        public static byte nameMaxValue()
        {
            return (byte)126;
        }

        public static int nameLength()
        {
            return 64;
        }

        public byte name(final int index)
        {
            if (index < 0 || index >= 64)
            {
                throw new IndexOutOfBoundsException("index out of range: index=" + index);
            }

            return CodecUtil.charGet(buffer, this.offset + 54 + (index * 1));
        }

        public void name(final int index, final byte value)
        {
            if (index < 0 || index >= 64)
            {
                throw new IndexOutOfBoundsException("index out of range: index=" + index);
            }

            CodecUtil.charPut(buffer, this.offset + 54 + (index * 1), value);
        }

        public static String nameCharacterEncoding()
        {
            return "UTF-8";
        }

        public int getName(final byte[] dst, final int dstOffset)
        {
            final int length = 64;
            if (dstOffset < 0 || dstOffset >= dst.length)
            {
                throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
            }

            CodecUtil.charsGet(buffer, this.offset + 54, dst, dstOffset, length);
            return length;
        }

        public String getName() throws UnsupportedEncodingException
        {
            final byte[] dst = new byte[64];
            CodecUtil.charsGet(buffer, this.offset + 54, dst, 0, dst.length);
            return new String(dst,"UTF-8").trim();
        }

        public Servers putName(final byte[] src, final int srcOffset)
        {
            final int length = 64;
            if (srcOffset < 0 || srcOffset >= src.length)
            {
                throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
            }
            CodecUtil.charsPut(buffer, this.offset + 54, src, srcOffset, length);
            return this;
        }

        public Servers putName(final String name) throws UnsupportedEncodingException
        {
            byte[] src = name.getBytes("UTF-8");
            CodecUtil.charsPut(buffer, this.offset + 54, src, 0, 64);
            return this;
        }

        public static int endpointId()
        {
            return 7;
        }

        public static String endpointMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static byte endpointNullValue()
        {
            return (byte)0;
        }

        public static byte endpointMinValue()
        {
            return (byte)32;
        }

        public static byte endpointMaxValue()
        {
            return (byte)126;
        }

        public static int endpointLength()
        {
            return 32;
        }

        public byte endpoint(final int index)
        {
            if (index < 0 || index >= 32)
            {
                throw new IndexOutOfBoundsException("index out of range: index=" + index);
            }

            return CodecUtil.charGet(buffer, this.offset + 118 + (index * 1));
        }

        public void endpoint(final int index, final byte value)
        {
            if (index < 0 || index >= 32)
            {
                throw new IndexOutOfBoundsException("index out of range: index=" + index);
            }

            CodecUtil.charPut(buffer, this.offset + 118 + (index * 1), value);
        }

        public static String endpointCharacterEncoding()
        {
            return "UTF-8";
        }

        public int getEndpoint(final byte[] dst, final int dstOffset)
        {
            final int length = 32;
            if (dstOffset < 0 || dstOffset >= dst.length)
            {
                throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
            }

            CodecUtil.charsGet(buffer, this.offset + 118, dst, dstOffset, length);
            return length;
        }

        public String getEndpoint() throws UnsupportedEncodingException
        {
            final byte[] dst = new byte[32];
            CodecUtil.charsGet(buffer, this.offset + 118, dst, 0, dst.length);
            return new String(dst,"UTF-8").trim();
        }

        public Servers putEndpoint(final byte[] src, final int srcOffset)
        {
            final int length = 32;
            if (srcOffset < 0 || srcOffset >= src.length)
            {
                throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
            }
            CodecUtil.charsPut(buffer, this.offset + 118, src, srcOffset, length);
            return this;
        }

        public Servers putEndpoint(final String endpoint) throws UnsupportedEncodingException
        {
            byte[] src = endpoint.getBytes("UTF-8");
            CodecUtil.charsPut(buffer, this.offset + 118, src, 0, 32);
            return this;
        }

        public static int workersId()
        {
            return 8;
        }

        public static String workersMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static int workersNullValue()
        {
            return -2147483648;
        }

        public static int workersMinValue()
        {
            return -2147483647;
        }

        public static int workersMaxValue()
        {
            return 2147483647;
        }

        public int workers()
        {
            return CodecUtil.int32Get(buffer, offset + 150, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public Servers workers(final int value)
        {
            CodecUtil.int32Put(buffer, offset + 150, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }

        public static int stateIdId()
        {
            return 9;
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
            return CodecUtil.int8Get(buffer, offset + 154);
        }

        public Servers stateId(final byte value)
        {
            CodecUtil.int8Put(buffer, offset + 154, value);
            return this;
        }

        public static int reportTimeId()
        {
            return 10;
        }

        public static String reportTimeMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        public static long reportTimeNullValue()
        {
            return -9223372036854775808L;
        }

        public static long reportTimeMinValue()
        {
            return -9223372036854775807L;
        }

        public static long reportTimeMaxValue()
        {
            return 9223372036854775807L;
        }

        public long reportTime()
        {
            return CodecUtil.int64Get(buffer, offset + 155, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public Servers reportTime(final long value)
        {
            CodecUtil.int64Put(buffer, offset + 155, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }
    }
}
