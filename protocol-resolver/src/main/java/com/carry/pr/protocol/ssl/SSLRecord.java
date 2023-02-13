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
        SSL_3_0(0x0300),// 3,0
        TLS_1_0(0x0301),// 3,1
        TLS_1_1(0x0302),// 3,2
        TLS_1_2(0x0303);// 3,3
        public final int value;

        Version(int value) {
            this.value = value;
        }

        static Version valueOf(short value) {
            for (Version v : values()) {
                if (v.value == value) {
                    return v;
                }
            }
            return null;
        }

    }


}
