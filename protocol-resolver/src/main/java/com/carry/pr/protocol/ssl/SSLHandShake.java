package com.carry.pr.protocol.ssl;


import com.carry.pr.protocol.ssl.impl.*;

public class SSLHandShake {

    HandShakeType handShakeType;

    int length;

    HandShakeContent content;

    enum HandShakeType {
        HELLO_REQUEST(0x00, HelloRequestContent.class),// 0
        CLIENT_HELLO(0x01, ClientHelloContent.class),// 1
        SERVER_HELLO(0x02, ServerHelloContent.class),// 2
        CERTIFICATE(0x0b, CertificateContent.class),// 11
        SERVER_KEY_EXCHANGE(0x0c, ServerKeyExchangeContent.class),// 12
        CERTIFICATE_REQUEST(0x0d, CertificateRequestContent.class),// 13
        SERVER_HELLO_DONE(0x0e, ServerHelloDoneContent.class),// 14
        CERTIFICATE_VERIFY(0x0f, ClientKeyExchangeContent.class),// 15
        CLIENT_KEY_EXCHANGE(0x10, ClientKeyExchangeContent.class),// 16
        FINISHED(0x14, FinishedContent.class),// 20
        ;
        final int value;

        final Class<? extends HandShakeContent> contentClass;

        HandShakeType(int value, Class<? extends HandShakeContent> contentClass) {
            this.value = value;
            this.contentClass = contentClass;
        }

        HandShakeContent createContent() {
            HandShakeContent handShakeContent = null;
            try {
                handShakeContent = contentClass.newInstance();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return handShakeContent;
        }

        static SSLHandShake.HandShakeType valueOf(byte value) {
            for (SSLHandShake.HandShakeType rt : values()) {
                if (rt.value == value) {
                    return rt;
                }
            }
            return null;
        }
    }

}
