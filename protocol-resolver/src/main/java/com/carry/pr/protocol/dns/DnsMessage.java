package com.carry.pr.protocol.dns;


import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.base.resolve.MsgReqObj;
import com.carry.pr.base.resolve.MsgRespObj;

/**
 * 1--head 固定
 * 2--question  @see{DnsMessage.Question}
 * 3--answer  @see{DnsMessage.ResRecord}
 * 4--Authority  @see{DnsMessage.ResRecord}
 * 5--Additional  @see{DnsMessage.ResRecord}
 */
public class DnsMessage implements MsgReqObj, MsgRespObj {
    private static final int END = 0x00;

    //--------------head-------------
    long transactionId;
    int qr;
    int opcode;
    int aa;
    int tc;
    int rd;
    int ra;
    int z;
    int rcode;
    long questionCount;
    long answerRRCount;
    long authorityRRsCount;
    long additionalRRsCount;

    //--------------Question--------------
    Question[] question;
    ResRecord[] answer;
    ResRecord[] authority;
    ResRecord[] additional;


    public boolean decode(ByteBufferPool.ByteBufferCache in) {
        decodeHead(in);
        question = new Question[(int) questionCount];
        answer = new ResRecord[(int) answerRRCount];
        authority = new ResRecord[(int) authorityRRsCount];
        additional = new ResRecord[(int) additionalRRsCount];
        decodeQuestion(question, in);
        decodeRR(answer, in);
        decodeRR(authority, in);
        decodeRR(additional, in);
        return true;
    }

    public byte[] encode() {
        return new byte[0];
    }

    private void decodeHead(ByteBufferPool.ByteBufferCache in) {
        this.transactionId = in.readLong();
        this.qr = in.readByte();
        this.opcode = in.readShort();
        this.aa = in.readByte();
        this.tc = in.readByte();
        this.rd = in.readByte();
        this.ra = in.readByte();
        // unused*3bit
        this.z |= in.readByte();
        this.z |= in.readByte();
        this.z |= in.readByte();
        this.z &= 0xfff;
        this.rcode = in.readInt();
        this.questionCount = in.readUnsignedInt();
        this.answerRRCount = in.readUnsignedInt();
        this.authorityRRsCount = in.readUnsignedInt();
        this.additionalRRsCount = in.readUnsignedInt();
    }

    private void decodeQuestion(Question[] questions, ByteBufferPool.ByteBufferCache in) {
        for (Question q : questions) {
            q = new Question();
            q.decode(in);
        }
    }

    private void decodeRR(ResRecord[] records, ByteBufferPool.ByteBufferCache in) {
        for (ResRecord r : records) {
            r = new ResRecord();
            r.decode(in);
        }
    }

    public static class Question {
        String name;
        int qType;
        int qClass;

        protected void decode(ByteBufferPool.ByteBufferCache in) {
            StringBuilder sb = new StringBuilder();
            int length;
            do {
                length = in.readUnsignedByte();
                for (int i = 0; i < length; i++) {
                    sb.append((char) in.readByte());
                }
                length = in.readUnsignedByte();
                if (length != END) {
                    sb.append(".");
                }
            } while (length != END);

            qType = in.readUnsignedShort();
            qClass = in.readUnsignedShort();
        }
    }

    public static class ResRecord {
        byte[] name = new byte[32];
        int type;
        int clazz;
        int ttl;
        int rdlength;
        byte[] rdata;

        protected void decode(ByteBufferPool.ByteBufferCache in) {
            for (int i = 0; i < name.length; i++) {
                this.name[i] = in.readByte();
            }
            this.type = in.readShort();
            this.clazz = in.readShort();
            this.ttl = in.readInt();
            this.rdlength = in.readShort();
            this.rdata = new byte[rdlength];
            for (int i = 0; i < rdlength; i++) {
                this.rdata[i] = in.readByte();
            }
        }
    }

    public enum RRType {
        A(1),
        NS(2),
        MD(3),
        MF(4),
        CNAME(5),
        SOA(6),
        MB(7),
        MG(8),
        MR(9),
        NULL(10),
        WKS(11),
        PTR(12),
        HINFO(13),
        MINFO(14),
        MX(15),
        TXT(16),
        ;

        int value;

        RRType(int value) {
            this.value = value;
        }

        static RRType valueOf(byte value) {
            for (RRType rt : values()) {
                if (rt.value == value) {
                    return rt;
                }
            }
            return null;
        }

    }

}
