package guru.z3.toolkit.nio;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class ReadToolTest {

    private ReadTool readTool;
    private MockAsynchronousByteChannel channel;

    @BeforeEach
    void setUp() {
        // Use a larger buffer in the mock channel to simulate reading into the ReadTool's buffer
        channel = new MockAsynchronousByteChannel(ByteBuffer.allocate(2048));
        readTool = ReadTool.attach(channel, ByteBuffer.allocate(1024));
        readTool.setTimeout(100); // Set a short timeout for testing
    }

    @AfterEach
    void tearDown() throws IOException {
        readTool.close();
    }

    @Test
    void testAttach() {
        assertNotNull(readTool);
        // Test attaching with a null channel
        assertThrows(IllegalArgumentException.class, () -> ReadTool.attach(null));
    }

    @Test
    void testGetByte() throws IOException, TimeoutException {
        channel.getBuffer().put((byte) 0x1A);
        channel.getBuffer().flip();

        byte result = readTool.get();
        assertEquals((byte) 0x1A, result);
    }

    @Test
    void testGetByteArray() throws IOException, TimeoutException {
        byte[] data = "Hello World".getBytes();
        channel.getBuffer().put(data);
        channel.getBuffer().flip();

        byte[] result = new byte[data.length];
        readTool.get(result);
        assertArrayEquals(data, result);
    }

    @Test
    void testGetShort() throws IOException, TimeoutException {
        channel.getBuffer().putShort((short) 12345);
        channel.getBuffer().flip();

        short result = readTool.getShort();
        assertEquals((short) 12345, result);
    }

    @Test
    void testGetInt() throws IOException, TimeoutException {
        channel.getBuffer().putInt(123456789);
        channel.getBuffer().flip();

        int result = readTool.getInt();
        assertEquals(123456789, result);
    }

    @Test
    void testGetLong() throws IOException, TimeoutException {
        channel.getBuffer().putLong(1234567890123456789L);
        channel.getBuffer().flip();

        long result = readTool.getLong();
        assertEquals(1234567890123456789L, result);
    }

    @Test
    void testGetFloat() throws IOException, TimeoutException {
        channel.getBuffer().putFloat(123.456f);
        channel.getBuffer().flip();

        float result = readTool.getFloat();
        assertEquals(123.456f, result, 0.001);
    }

    @Test
    void testGetDouble() throws IOException, TimeoutException {
        channel.getBuffer().putDouble(123456.789012);
        channel.getBuffer().flip();

        double result = readTool.getDouble();
        assertEquals(123456.789012, result, 0.000001);
    }

    @Test
    void testTimeoutException() {
        // No data in the channel, so it should time out
        assertThrows(TimeoutException.class, () -> readTool.get());
    }
    
    @Test
    void testClose() throws IOException {
        readTool.close();
        assertFalse(channel.isOpen());
    }

    /**
     * A mock implementation of AsynchronousByteChannel for testing purposes.
     * It uses a ByteBuffer as an in-memory source of data.
     */
    private static class MockAsynchronousByteChannel implements AsynchronousByteChannel {
        private final ByteBuffer buffer;
        private boolean open = true;

        public MockAsynchronousByteChannel(ByteBuffer buffer) {
            this.buffer = buffer;
        }

        public ByteBuffer getBuffer() {
            return buffer;
        }

        @Override
        public <A> void read(ByteBuffer dst, A attachment, CompletionHandler<Integer, ? super A> handler) {
            if (!open) {
                handler.failed(new java.nio.channels.ClosedChannelException(), attachment);
                return;
            }
            try {
                int bytesToRead = Math.min(dst.remaining(), buffer.remaining());
                if (bytesToRead > 0) {
                    byte[] data = new byte[bytesToRead];
                    buffer.get(data);
                    dst.put(data);
                }
                handler.completed(bytesToRead, attachment);
            } catch (Exception e) {
                handler.failed(e, attachment);
            }
        }

        @Override
        public Future<Integer> read(ByteBuffer dst) {
            CompletableFuture<Integer> future = new CompletableFuture<>();
            read(dst, null, new CompletionHandler<Integer, Object>() {
                @Override
                public void completed(Integer result, Object attachment) {
                    future.complete(result);
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    future.completeExceptionally(exc);
                }
            });
            return future;
        }

        @Override
        public <A> void write(ByteBuffer src, A attachment, CompletionHandler<Integer, ? super A> handler) {
            // Not implemented for this mock
            handler.failed(new UnsupportedOperationException(), attachment);
        }

        @Override
        public Future<Integer> write(ByteBuffer src) {
            // Not implemented for this mock
            return CompletableFuture.failedFuture(new UnsupportedOperationException());
        }

        @Override
        public boolean isOpen() {
            return open;
        }

        @Override
        public void close() throws IOException {
            open = false;
        }
    }
}
