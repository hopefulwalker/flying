/**
 * This file is generated by code generation tools.
 * Generation time is Sun Jul 26 13:14:43 CST 2015
 */
package com.flying.oms.model;

import com.hazelcast.nio.serialization.ByteArraySerializer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class OrderBOBAS implements ByteArraySerializer<OrderBO> {
    @Override
    public int getTypeId() {
        return 1;
    }

    @Override
    public void destroy() {
    }

    @Override
    public byte[] write(OrderBO orderBO) throws IOException {
        DirectBuffer buffer = new DirectBuffer(new byte[OrderBOSBE.BLOCK_LENGTH]);
        OrderBOSBE orderBOSBE = new OrderBOSBE();
        orderBOSBE.wrapForEncode(buffer, 0).oid(orderBO.getOid()).extNo(orderBO.getExtNo()).acctId(orderBO.getAcctId()).exchId(orderBO.getExchId()).sectCode(orderBO.getSectCode()).bsSideId(orderBO.getBsSideId()).price(orderBO.getPrice()).qty(orderBO.getQty()).cntrNo(orderBO.getCntrNo()).bizDate(orderBO.getBizDate()).stateId(orderBO.getStateId()).stateEnteredCode(orderBO.getStateEnteredCode()).createTime(orderBO.getCreateTime()).updateTime(orderBO.getUpdateTime());
        return buffer.array();
    }

    @Override
    public OrderBO read(byte[] bytes) throws IOException {
        DirectBuffer buffer = new DirectBuffer(bytes);
        OrderBOSBE orderBOSBE = new OrderBOSBE();
        orderBOSBE.wrapForDecode(buffer, 0, OrderBOSBE.BLOCK_LENGTH);
        // Analyze the reply and build dto.
        OrderBO orderBO = new OrderBO();
        orderBO.setOid(orderBOSBE.oid());
        orderBO.setExtNo(orderBOSBE.extNo());
        orderBO.setAcctId(orderBOSBE.acctId());
        orderBO.setExchId(orderBOSBE.exchId());
        orderBO.setSectCode(orderBOSBE.sectCode());
        orderBO.setBsSideId(orderBOSBE.bsSideId());
        orderBO.setPrice(orderBOSBE.price());
        orderBO.setQty(orderBOSBE.qty());
        orderBO.setCntrNo(orderBOSBE.cntrNo());
        orderBO.setBizDate(orderBOSBE.bizDate());
        orderBO.setStateId(orderBOSBE.stateId());
        orderBO.setStateEnteredCode(orderBOSBE.stateEnteredCode());
        orderBO.setCreateTime(orderBOSBE.createTime());
        orderBO.setUpdateTime(orderBOSBE.updateTime());

        return orderBO;
    }

        public static class OrderBOSBE {
        public static final int BLOCK_LENGTH = 82;
        private DirectBuffer buffer;
        private int offset;
        private int limit;
        private int actingBlockLength;

        public OrderBOSBE wrapForEncode(final DirectBuffer buffer, final int offset) {
            this.buffer = buffer;
            this.offset = offset;
            this.actingBlockLength = BLOCK_LENGTH;
            limit(offset + actingBlockLength);
            return this;
        }

        public OrderBOSBE wrapForDecode(final DirectBuffer buffer, final int offset, final int actingBlockLength) {
            this.buffer = buffer;
            this.offset = offset;
            this.actingBlockLength = actingBlockLength;
            limit(offset + actingBlockLength);

            return this;
        }

        public void limit(final int limit) {
            buffer.checkLimit(limit);
            this.limit = limit;
        }

                public long oid() {
            return CodecUtil.int64Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public OrderBOSBE oid(final long value) {
            CodecUtil.int64Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }
        public long extNo() {
            return CodecUtil.int64Get(buffer, offset + 8, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public OrderBOSBE extNo(final long value) {
            CodecUtil.int64Put(buffer, offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }
        public long acctId() {
            return CodecUtil.int64Get(buffer, offset + 16, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public OrderBOSBE acctId(final long value) {
            CodecUtil.int64Put(buffer, offset + 16, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }
        public short exchId() {
            return CodecUtil.int16Get(buffer, offset + 24, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public OrderBOSBE exchId(final short value) {
            CodecUtil.int16Put(buffer, offset + 24, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }        public String sectCode() throws UnsupportedEncodingException {
            final byte[] dst = new byte[6];
            CodecUtil.charsGet(buffer, this.offset + 26, dst, 0, dst.length);
            return new String(dst, "UTF-8").trim();
        }

        public OrderBOSBE sectCode(final String value) throws UnsupportedEncodingException {
            if (value == null) return this;
            byte[] src = value.getBytes("UTF-8");
            CodecUtil.charsPut(buffer, this.offset + 26, src, 0, 6);
            return this;
        }        public byte bsSideId() {
            return CodecUtil.int8Get(buffer, offset + 32);
        }

        public OrderBOSBE bsSideId(final byte value) {
            CodecUtil.int8Put(buffer, offset + 32, value);
            return this;
        }
        public double price() {
            return CodecUtil.doubleGet(buffer, offset + 33, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public OrderBOSBE price(final double value) {
            CodecUtil.doublePut(buffer, offset + 33, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }
        public long qty() {
            return CodecUtil.int64Get(buffer, offset + 41, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public OrderBOSBE qty(final long value) {
            CodecUtil.int64Put(buffer, offset + 41, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }
        public String cntrNo() throws UnsupportedEncodingException {
            final byte[] dst = new byte[8];
            CodecUtil.charsGet(buffer, this.offset + 49, dst, 0, dst.length);
            return new String(dst, "UTF-8").trim();
        }

        public OrderBOSBE cntrNo(final String value) throws UnsupportedEncodingException {
            if (value == null) return this;
            byte[] src = value.getBytes("UTF-8");
            CodecUtil.charsPut(buffer, this.offset + 49, src, 0, 8);
            return this;
        }        public int bizDate() {
            return CodecUtil.int32Get(buffer, offset + 57, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public OrderBOSBE bizDate(final int value) {
            CodecUtil.int32Put(buffer, offset + 57, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }
        public byte stateId() {
            return CodecUtil.int8Get(buffer, offset + 61);
        }

        public OrderBOSBE stateId(final byte value) {
            CodecUtil.int8Put(buffer, offset + 61, value);
            return this;
        }
        public int stateEnteredCode() {
            return CodecUtil.int32Get(buffer, offset + 62, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public OrderBOSBE stateEnteredCode(final int value) {
            CodecUtil.int32Put(buffer, offset + 62, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }
        public long createTime() {
            return CodecUtil.int64Get(buffer, offset + 66, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public OrderBOSBE createTime(final long value) {
            CodecUtil.int64Put(buffer, offset + 66, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }
        public long updateTime() {
            return CodecUtil.int64Get(buffer, offset + 74, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public OrderBOSBE updateTime(final long value) {
            CodecUtil.int64Put(buffer, offset + 74, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }

    }

}