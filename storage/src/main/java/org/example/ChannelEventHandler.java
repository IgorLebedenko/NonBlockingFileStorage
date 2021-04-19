package org.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Iterator;

public class ChannelEventHandler implements Runnable {
    private final ServerSocketChannel socketChannel;
    private final Selector selector;
    private static final int BUFFER_SIZE = 1024;

    public ChannelEventHandler(ServerSocketChannel socketChannel, Selector selector) {
        this.socketChannel = socketChannel;
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            while (socketChannel.isOpen()) {
                selector.selectNow();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    // тут как будто бы правильнее isAcceptable() (нужно разобраться)
                    if (key.isWritable())
                        this.writeToChannel(key);
                    if (key.isReadable())
                        this.readFromChannel(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Чтение из файла и запись в сокет канал
     */
    private void writeToChannel(SelectionKey key) throws IOException {
        Path path = Paths.get("D:/file-storage/test.txt");
        try (SocketChannel channel = (SocketChannel) key.channel();
             // AsynchronousFileChannel ???
             FileChannel fileChannel = FileChannel.open(path)) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            channel.configureBlocking(false);

            while (fileChannel.read(buffer) > 0) {
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
            }
        }
    }

    /**
     * Чтение из сокет канала и запись в файл на диске
     */
    private void readFromChannel(SelectionKey key) throws IOException {
        // TODO здесь стоит воспользоваться fileChannel, чтобы записывать файл по частям
        // развить эту идею
        // путь получать по id из storage, либо формировать по id
        Path path = Paths.get("D:/file-storage/test.txt");

        try (SocketChannel channel = (SocketChannel) key.channel();
             // AsynchronousFileChannel ???
             FileChannel fileChannel = FileChannel.open(path,
                     EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE))) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            channel.configureBlocking(false);

            while (channel.read(buffer) > 0) {
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }
        }
    }
}
