package com.carry.pr.base.tcp;


import com.carry.pr.base.common.SelectorProvider;
import com.carry.pr.base.executor.WorkGroup;
import com.carry.pr.base.executor.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class TcpWorker extends Worker {

    private static final Logger log = LoggerFactory.getLogger(TcpWorker.class);

    private final AtomicBoolean ioBlock;
    protected Selector selector;

    public TcpWorker(WorkGroup workGroup) {
        super(workGroup);
        ioBlock = new AtomicBoolean();
        this.selector = SelectorProvider.open();
    }

    @Override
    public void start() throws IOException {
        super.start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            int select = select();

            doIoTask(select);

            doQuqueTask();
        }
    }

    private int select() {
        int select = 0;
        try {
            if (!hasTask()) {
                ioBlock.compareAndSet(false, true);
                select = selector.select();
            } else {
                select = selector.selectNow();
            }
        } catch (IOException t) {
            t.printStackTrace();
        }
        return select;
    }


    private void doIoTask(int select) {
        if (select <= 0) return;
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            iterator.remove();
            if (selectionKey.isValid() && selectionKey.isAcceptable()) {
                accept(selectionKey);
            } else if (selectionKey.isValid() && selectionKey.isReadable()) {
                read(selectionKey);
            } else if (selectionKey.isValid() && selectionKey.isWritable()) {
                write(selectionKey);
            } else if (selectionKey.isValid() && selectionKey.isConnectable()) {
                connect(selectionKey);
            }
        }
    }

    protected void doQuqueTask() {
        // queueTask
        try {
            doQueueTask(false);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void processSelectedKeys() {
    }

    @Override
    public void execute(Runnable command) {
        super.execute(command);
    }

    @Override
    public boolean addTask(Runnable task) {
        boolean added = super.addTask(task);
        if (ioBlock.get()) {
            ioBlock.compareAndSet(true, false);
            selector.wakeup();
        }
        return added;
    }

    protected void accept(SelectionKey selectionKey) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel accept = serverSocketChannel.accept();
            accept.configureBlocking(false);
            accept.register(selector, SelectionKey.OP_READ);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    protected void read(SelectionKey selectionKey) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            TcpChannel tcpChannel = (TcpChannel) selectionKey.attachment();
            execute(new TcpReadTask(tcpChannel, selectionKey));
        } catch (Throwable t) {
            log.error("read error");
            t.printStackTrace();
        }
    }


    protected void write(SelectionKey selectionKey) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            TcpChannel tcpChannel = (TcpChannel) selectionKey.attachment();
            TaskContent taskContent = tcpChannel.getContent();
            if (taskContent != null) {
                int remaining = taskContent.out.getByteBuffer().remaining();
                int write = socketChannel.write(taskContent.out.getByteBuffer());
                if (remaining < write) {
                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                    selector.wakeup();
                    return;
                }
            }
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    protected void connect(SelectionKey selectionKey) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel accept = serverSocketChannel.accept();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
