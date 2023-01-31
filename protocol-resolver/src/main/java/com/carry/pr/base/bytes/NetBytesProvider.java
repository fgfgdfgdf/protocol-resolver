package com.carry.pr.base.bytes;

import com.carry.pr.base.bytes.BytesProvider;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NetBytesProvider implements BytesProvider {

    SocketChannel socketChannel;

    ByteBuffer out;

    public NetBytesProvider(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public ByteBuffer getBytes() {
        receiv();
        out.flip();
        return out;
    }

    public void receiv(){
        ByteBuffer receiver = ByteBuffer.allocate(1024);
        try {
            int readsize = 0;
            do {
                receiver.clear();
                readsize = socketChannel.read(receiver);
                if (readsize <= 0)
                    break;
                group(receiver);
            } while (true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void group(ByteBuffer t) {
        int oldSize = out == null ? 0 : out.limit();
        if (oldSize == 0) {
            out = ByteBuffer.allocate(t.position());
            out.put(t.array(), 0, t.position());
            return;
        }
        int size = t.position();
        byte[] all = new byte[size + oldSize];
        System.arraycopy(out.array(), 0, all, 0, out.position());
        System.arraycopy(t.array(), 0, all, oldSize, t.position());
        out = ByteBuffer.wrap(all);
    }
}
