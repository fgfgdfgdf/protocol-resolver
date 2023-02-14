package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;

/**
 * CipherSuites
 * +----+----+----+----+----+----+
 * |    |    |    |    |    |    |
 * |    |    |    |    |    |    |
 * +----+----+----+----+----+----+
 * \-----\   \-----\    \----\
 * \         \          \
 * length    cipher Id  cipherId
 */
public class CipherSuites {
    int length;
    Type[] type;

    public static CipherSuites decode(ByteBufferPool.ByteBufferCache in) {
        CipherSuites cipherSuites = new CipherSuites();
        cipherSuites.length = in.readShort();
        cipherSuites.type = new Type[cipherSuites.length / 2];
        for (int i = 0; i < cipherSuites.length; i++) {
            cipherSuites.type[i] = Type.valueOf(in.readShort());
        }
        return cipherSuites;
    }

    public enum Type {

        AES128_SHA(0x002F),                       // SSLv3    Kx=RSA       Au=RSA    Enc=AES(128)                Mac=SHA1

        DHE_RSA_AES128_SHA(0x0033),                // SSLv3    Kx=DH        Au=RSA    Enc=AES(128)                Mac=SHA1

        AES256_SHA(0x0035),                         // SSLv3    Kx=RSA       Au=RSA    Enc=AES(256)                Mac=SHA1

        DHE_RSA_AES256_SHA(0x0039),                // SSLv3    Kx=DH        Au=RSA    Enc=AES(256)                Mac=SHA1

        PSK_AES128_CBC_SHA(0x008C),              // SSLv3    Kx=PSK       Au=PSK    Enc=AES(128)                Mac=SHA1

        PSK_AES256_CBC_SHA(0x008D),             // SSLv3    Kx=PSK       Au=PSK    Enc=AES(256)                Mac=SHA1

        DHE_PSK_AES128_CBC_SHA(0x0090),         // SSLv3    Kx=DHEPSK    Au=PSK    Enc=AES(128)                Mac=SHA1

        DHE_PSK_AES256_CBC_SHA(0x0091),         // SSLv3    Kx=DHEPSK    Au=PSK    Enc=AES(256)                Mac=SHA1

        RSA_PSK_AES128_CBC_SHA(0x0094),             // SSLv3    Kx=RSAPSK    Au=RSA    Enc=AES(128)                Mac=SHA1

        RSA_PSK_AES256_CBC_SHA(0x0095),             // SSLv3    Kx=RSAPSK    Au=RSA    Enc=AES(256)                Mac=SHA1

        SRP_AES_128_CBC_SHA(0xC01D),            // SSLv3    Kx=SRP       Au=SRP    Enc=AES(128)                Mac=SHA1

        SRP_RSA_AES_128_CBC_SHA(0xC01E),            // SSLv3    Kx=SRP       Au=RSA    Enc=AES(128)                Mac=SHA1

        SRP_AES_256_CBC_SHA(0xC020),            // SSLv3    Kx=SRP       Au=SRP    Enc=AES(256)                Mac=SHA1

        SRP_RSA_AES_256_CBC_SHA(0xC021),            // SSLv3    Kx=SRP       Au=RSA    Enc=AES(256)                Mac=SHA1

        PSK_AES128_CBC_SHA256(0x00AE),          // TLSv1    Kx=PSK       Au=PSK    Enc=AES(128)                Mac=SHA256

        PSK_AES256_CBC_SHA384(0x00AF),          // TLSv1    Kx=PSK       Au=PSK    Enc=AES(256)                Mac=SHA384

        DHE_PSK_AES128_CBC_SHA256(0x00B2),          // TLSv1    Kx=DHEPSK    Au=PSK    Enc=AES(128)                Mac=SHA256

        DHE_PSK_AES256_CBC_SHA384(0x00B3),          // TLSv1    Kx=DHEPSK    Au=PSK    Enc=AES(256)                Mac=SHA384

        RSA_PSK_AES128_CBC_SHA256(0x00B6),          // TLSv1    Kx=RSAPSK    Au=RSA    Enc=AES(128)                Mac=SHA256

        RSA_PSK_AES256_CBC_SHA384(0x00B7),          // TLSv1    Kx=RSAPSK    Au=RSA    Enc=AES(256)                Mac=SHA384

        ECDHE_ECDSA_AES128_SHA(0xC009),             // TLSv1    Kx=ECDH      Au=ECDSA  Enc=AES(128)                Mac=SHA1

        ECDHE_ECDSA_AES256_SHA(0xC00A),         // TLSv1    Kx=ECDH      Au=ECDSA  Enc=AES(256)                Mac=SHA1

        ECDHE_RSA_AES128_SHA(0xC013),           // TLSv1    Kx=ECDH      Au=RSA    Enc=AES(128)                Mac=SHA1

        ECDHE_RSA_AES256_SHA(0xC014),           // TLSv1    Kx=ECDH      Au=RSA    Enc=AES(256)                Mac=SHA1

        ECDHE_PSK_AES128_CBC_SHA(0xC035),           // TLSv1    Kx=ECDHEPSK  Au=PSK    Enc=AES(128)                Mac=SHA1

        ECDHE_PSK_AES256_CBC_SHA(0xC036),           // TLSv1    Kx=ECDHEPSK  Au=PSK    Enc=AES(256)                Mac=SHA1

        ECDHE_PSK_AES128_CBC_SHA256(0xC037),        // TLSv1    Kx=ECDHEPSK  Au=PSK    Enc=AES(128)                Mac=SHA256

        ECDHE_PSK_AES256_CBC_SHA384(0xC038),            // TLSv1    Kx=ECDHEPSK  Au=PSK    Enc=AES(256)                Mac=SHA384

        AES128_SHA256(0x003C),              // TLSv1.2  Kx=RSA       Au=RSA    Enc=AES(128)                Mac=SHA256

        AES256_SHA256(0x003D),              // TLSv1.2  Kx=RSA       Au=RSA    Enc=AES(256)                Mac=SHA256

        DHE_RSA_AES128_SHA256(0x0067),          // TLSv1.2  Kx=DH        Au=RSA    Enc=AES(128)                Mac=SHA256

        DHE_RSA_AES256_SHA256(0x006B),          // TLSv1.2  Kx=DH        Au=RSA    Enc=AES(256)                Mac=SHA256

        AES128_GCM_SHA256(0x009C),          // TLSv1.2  Kx=RSA       Au=RSA    Enc=AESGCM(128)             Mac=AEAD

        AES256_GCM_SHA384(0x009D),// TLSv1.2  Kx=RSA       Au=RSA    Enc=AESGCM(256)             Mac=AEAD

        DHE_RSA_AES128_GCM_SHA256(0x009E),// TLSv1.2  Kx=DH        Au=RSA    Enc=AESGCM(128)             Mac=AEAD

        DHE_RSA_AES256_GCM_SHA384(0x009F),// TLSv1.2  Kx=DH        Au=RSA    Enc=AESGCM(256)             Mac=AEAD

        PSK_AES128_GCM_SHA256(0x00A8),// TLSv1.2  Kx=PSK       Au=PSK    Enc=AESGCM(128)             Mac=AEAD

        PSK_AES256_GCM_SHA384(0x00A9),// TLSv1.2  Kx=PSK       Au=PSK    Enc=AESGCM(256)             Mac=AEAD

        DHE_PSK_AES128_GCM_SHA256(0x00AA),// TLSv1.2  Kx=DHEPSK    Au=PSK    Enc=AESGCM(128)             Mac=AEAD

        DHE_PSK_AES256_GCM_SHA384(0x00AB),// TLSv1.2  Kx=DHEPSK    Au=PSK    Enc=AESGCM(256)             Mac=AEAD

        RSA_PSK_AES128_GCM_SHA256(0x00AC),// TLSv1.2  Kx=RSAPSK    Au=RSA    Enc=AESGCM(128)             Mac=AEAD

        RSA_PSK_AES256_GCM_SHA384(0x00AD),// TLSv1.2  Kx=RSAPSK    Au=RSA    Enc=AESGCM(256)             Mac=AEAD

        ECDHE_ECDSA_AES128_SHA256(0xC023),// TLSv1.2  Kx=ECDH      Au=ECDSA  Enc=AES(128)                Mac=SHA256

        ECDHE_ECDSA_AES256_SHA384(0xC024),// TLSv1.2  Kx=ECDH      Au=ECDSA  Enc=AES(256)                Mac=SHA384

        ECDHE_RSA_AES128_SHA256(0xC027),// TLSv1.2  Kx=ECDH      Au=RSA    Enc=AES(128)                Mac=SHA256

        ECDHE_RSA_AES256_SHA384(0xC028),// TLSv1.2  Kx=ECDH      Au=RSA    Enc=AES(256)                Mac=SHA384

        ECDHE_ECDSA_AES128_GCM_SHA256(0xC02B),// TLSv1.2  Kx=ECDH      Au=ECDSA  Enc=AESGCM(128)             Mac=AEAD

        ECDHE_ECDSA_AES256_GCM_SHA384(0xC02C),// TLSv1.2  Kx=ECDH      Au=ECDSA  Enc=AESGCM(256)             Mac=AEAD

        ECDHE_RSA_AES128_GCM_SHA256(0xC02F),// TLSv1.2  Kx=ECDH      Au=RSA    Enc=AESGCM(128)             Mac=AEAD

        ECDHE_RSA_AES256_GCM_SHA384(0xC030),// TLSv1.2  Kx=ECDH      Au=RSA    Enc=AESGCM(256)             Mac=AEAD

        ECDHE_RSA_CHACHA20_POLY1305(0xCCA8),// TLSv1.2  Kx=ECDH      Au=RSA    Enc=CHACHA20/POLY1305(256)  Mac=AEAD

        ECDHE_ECDSA_CHACHA20_POLY1305(0xCCA9),// TLSv1.2  Kx=ECDH      Au=ECDSA  Enc=CHACHA20/POLY1305(256)  Mac=AEAD

        DHE_RSA_CHACHA20_POLY1305(0xCCAA),// TLSv1.2  Kx=DH        Au=RSA    Enc=CHACHA20/POLY1305(256)  Mac=AEAD

        PSK_CHACHA20_POLY1305(0xCCAB),// TLSv1.2  Kx=PSK       Au=PSK    Enc=CHACHA20/POLY1305(256)  Mac=AEAD

        ECDHE_PSK_CHACHA20_POLY1305(0xCCAC),// TLSv1.2  Kx=ECDHEPSK  Au=PSK    Enc=CHACHA20/POLY1305(256)  Mac=AEAD

        DHE_PSK_CHACHA20_POLY1305(0xCCAD),// TLSv1.2  Kx=DHEPSK    Au=PSK    Enc=CHACHA20/POLY1305(256)  Mac=AEAD

        RSA_PSK_CHACHA20_POLY1305(0xCCAE),// TLSv1.2  Kx=RSAPSK    Au=RSA    Enc=CHACHA20/POLY1305(256)  Mac=AEAD

        TLS_AES_128_GCM_SHA256(0x1301),// TLSv1.3  Kx=any       Au=any    Enc=AESGCM(128)             Mac=AEAD

        TLS_AES_256_GCM_SHA384(0x1302),// TLSv1.3  Kx=any       Au=any    Enc=AESGCM(256)             Mac=AEAD

        TLS_CHACHA20_POLY1305_SHA256(0x1303),// TLSv1.3  Kx=any       Au=any    Enc=CHACHA20/POLY1305(256)  Mac=AEAD

        ;

        int value;

        Type(int value) {
            this.value = value;
        }

        static Type valueOf(int value) {
            for (Type t : values()) {
                if (t.value == value) {
                    return t;
                }
            }
            return null;
        }
    }

}
