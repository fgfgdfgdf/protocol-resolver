package com.carry.pr.tcp;


import com.carry.pr.base.Task;
import com.carry.pr.base.Worker;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class TcpWorker extends Worker {

    private static ByteBuffer cacheBuf = ByteBuffer.allocate(512);

    private AtomicBoolean ioBlock;
    Selector selector;

    public TcpWorker() {
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ioBlock = new AtomicBoolean();
    }

    @Override
    protected void start() throws Exception {
        super.start();
    }

    @Override
    public void work() {
        try {
            int select = 0;
            if (!hasTask()) {
                ioBlock.compareAndSet(false, true);
                select = selector.select();
            } else {
                select = selector.selectNow();
            }
            if (select > 0) {
                doIoTask();
            }
            doQueueTask();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void doIoTask() {
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            iterator.remove();
            if (selectionKey.isAcceptable()) {
                accept(selectionKey);
            } else if (selectionKey.isReadable()) {
                read(selectionKey);
            } else if (selectionKey.isWritable()) {
                write(selectionKey);
            } else if (selectionKey.isConnectable()) {
                connect(selectionKey);
            }
        }
    }

    private void processSelectedKeys() {
    }

    @Override
    protected boolean addTask(Task task) {
        boolean b = super.addTask(task);
        if (ioBlock.get()) {
            ioBlock.compareAndSet(true, false);
            selector.wakeup();
        }
        return b;
    }

    public void accept(SelectionKey selectionKey) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel accept = serverSocketChannel.accept();
            accept.configureBlocking(false);
            accept.register(selector, SelectionKey.OP_READ);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void read(SelectionKey selectionKey) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            // cacheBuf
            cacheBuf.clear();
            int read = 0;
            ByteBuffer data = null;
            while (true) {
                read = socketChannel.read(cacheBuf);
                if (read <= 0) break;
                if (data == null) {
                    byte[] bytes = new byte[cacheBuf.position()];
                    System.arraycopy(cacheBuf.array(), 0, bytes, 0, cacheBuf.position());
                    data=ByteBuffer.wrap(bytes, 0, bytes.length);
                } else {
                    byte[] bytes = new byte[data.limit() + cacheBuf.position()];
                    System.arraycopy(data, 0, bytes, 0, data.position());
                    System.arraycopy(cacheBuf.array(), 0, bytes, data.position(), cacheBuf.position());
                    data=ByteBuffer.wrap(bytes, 0, bytes.length);
                }
            }
            //
            if (data != null) {
                execute(new TcpTask(data));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void write(SelectionKey selectionKey) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void connect(SelectionKey selectionKey) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel accept = serverSocketChannel.accept();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


}
