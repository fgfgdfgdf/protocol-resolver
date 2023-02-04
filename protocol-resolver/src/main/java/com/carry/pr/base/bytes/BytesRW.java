package com.carry.pr.base.bytes;


import java.nio.ByteOrder;

public interface BytesRW {

    byte readByte();

    void writeByte(byte b);

    default boolean isBigEndian() {
        return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
    }

    default short readShort() {
        short val = (short) (readByte() | (readByte() << 8));
        return isBigEndian() ? val : Short.reverseBytes(val);
    }

    default int readInt() {
        int val = readByte() |
                readByte() << 8 |
                readByte() << 16 |
                readByte() << 24;
        return isBigEndian() ? val : Integer.reverseBytes(val);
    }

    default long readLong() {
        long val = (long) readByte() |
                (long) readByte() << 8 |
                (long) readByte() << 16 |
                (long) readByte() << 24 |
                (long) readByte() << 32 |
                (long) readByte() << 40 |
                (long) readByte() << 48 |
                (long) readByte() << 56;
        return isBigEndian() ? val : Long.reverseBytes(val);
    }

    default short readUnsignedByte() {
        return (short) (readByte() & 0xFF);
    }

    default int readUnsignedShort() {
        return readShort() & 0xFFFF;
    }

    default long readUnsignedInt() {
        return readInt() & 0xFFFFFFFFL;
    }

    default void writeShort(short b) {
        if (isBigEndian()) {
            writeByte((byte) (b & 0xFF));
            writeByte((byte) (b >>> 8));
        } else {
            writeByte((byte) (b >>> 8));
            writeByte((byte) (b & 0xFF));
        }
    }

    default void writeLong(long b) {
        if (isBigEndian()) {
            writeByte((byte) (b & 0xFF));
            writeByte((byte) (b >> 8 & 0xFF));
            writeByte((byte) (b >> 16 & 0xFF));
            writeByte((byte) (b >> 24));
        } else {
            writeByte((byte) (b >> 24));
            writeByte((byte) (b >> 16 & 0xFF));
            writeByte((byte) (b >> 8 & 0xFF));
            writeByte((byte) (b & 0xFF));
        }

    }

    default void writeInt(int b) {
        writeByte((byte) (b >> 24));
        writeByte((byte) (b >> 16 & 0xFF));
        writeByte((byte) (b >> 8 & 0xFF));
        writeByte((byte) (b & 0xFF));
    }


}
