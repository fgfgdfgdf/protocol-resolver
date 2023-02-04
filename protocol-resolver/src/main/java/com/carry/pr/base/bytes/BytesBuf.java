package com.carry.pr.base.bytes;


public class BytesBuf implements BytesRW {

    private static final int SIZE = 16;
    private static final boolean GROW = true;

    private byte[] data;
    private int limit;
    private int readOffset;
    private int writeOffset;

    public BytesBuf(int size) {
        this.data = new byte[size];
    }

    public BytesBuf() {
        this.data = new byte[SIZE];
    }

    public BytesBuf(byte[] bytes) {
        this.data = bytes;
    }

    public int getLimit() {
        return limit;
    }

    public void setReadOffset(int offset) {
        this.readOffset = offset;
    }

    public void setWriteOffset(int offset) {
        this.writeOffset = offset;
    }

    @Override
    public byte readByte() {
        byte b = data[readOffset];
        readOffset++;
        return b;
    }

    @Override
    public void writeByte(byte b) {
        data[writeOffset] = b;
        writeOffset++;
        limit++;
    }
}
