package com.carry.pr.protocol.ssl;

public class SSLRecord {

    RecordType recordType;
    Version version;
    int length;

    public SSLRecord() {
    }

    enum RecordType {
        CHANGE_CIPHER_SPEC(0x14),// 20
        ALERT(0x15),// 21
        HANDSHAKE(0x16),// 22
        APPLICATION_DATA(0x17); // 23

        public final int value;

        RecordType(int value) {
            this.value = value;
        }

        static RecordType valueOf(byte value) {
            for (RecordType rt : values()) {
                if (rt.value == value) {
                    return rt;
                }
            }
            return null;
        }
    }

    public enum Version {
        SSL_3_0(0x03,0x00),// 3,0
        TLS_1_0(0x03,0x01),// 3,1
        TLS_1_1(0x03,0x02),// 3,2
        TLS_1_2(0x03,0x03);// 3,3
        public final int big;
        public final int small;

        Version(int big,int small) {
            this.big=big;
            this.small=small;
        }

        public static Version valueOf(int big,int small) {
            for (Version v : values()) {
                if (v.big == big && v.small==small) {
                    return v;
                }
            }
            return null;
        }

    }


}
