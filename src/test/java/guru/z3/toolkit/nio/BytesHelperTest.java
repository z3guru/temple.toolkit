package guru.z3.toolkit.nio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BytesHelperTest {

    @Test
    void testBytes2IntLE() {
        byte[] data = {0x12, 0x34, 0x56, 0x78};
        int expected = 0x12345678;
        assertEquals(expected, BytesHelper.bytes2IntLE(data, 0, 4));
        assertEquals(0x12, BytesHelper.bytes2IntLE(data, 0, 1));
        assertEquals(0x1234, BytesHelper.bytes2IntLE(data, 0, 2));
    }

    @Test
    void testBytes2IntBE() {
        byte[] data = {0x78, 0x56, 0x34, 0x12};
        int expected = 0x12345678;
        assertEquals(expected, BytesHelper.bytes2IntBE(data, 0, 4));
        assertEquals(0x78, BytesHelper.bytes2IntBE(data, 0, 1));
        assertEquals(0x5678, BytesHelper.bytes2IntBE(data, 0, 2));
    }

    @Test
    void testBytes2LongLE() {
        byte[] data = {0x12, 0x34, 0x56, 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0};
        long expected = 0x123456789ABCDEF0L;
        assertEquals(expected, BytesHelper.bytes2LongLE(data, 0, 8));
    }

    @Test
    void testBytes2LongBE() {
        byte[] data = {(byte) 0xF0, (byte) 0xDE, (byte) 0xBC, (byte) 0x9A, 0x78, 0x56, 0x34, 0x12};
        long expected = 0x123456789ABCDEF0L;
        assertEquals(expected, BytesHelper.bytes2LongBE(data, 0, 8));
    }

    @Test
    void testInt2BytesLE() {
        int value = 0x12345678;
        byte[] expected = {0x12, 0x34, 0x56, 0x78};
        byte[] actual = new byte[4];
        BytesHelper.int2BytesLE(value, actual, 0, 4);
        assertArrayEquals(expected, actual);
    }

    @Test
    void testInt2BytesBE() {
        int value = 0x12345678;
        byte[] expected = {0x78, 0x56, 0x34, 0x12};
        byte[] actual = new byte[4];
        BytesHelper.int2BytesBE(value, actual, 0, 4);
        assertArrayEquals(expected, actual);
    }

    @Test
    void testLong2BytesLE() {
        long value = 0x123456789ABCDEF0L;
        byte[] expected = {0x12, 0x34, 0x56, 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0};
        byte[] actual = new byte[8];
        BytesHelper.long2BytesLE(value, actual, 0, 8);
        assertArrayEquals(expected, actual);
    }

    @Test
    void testLong2BytesBE() {
        long value = 0x123456789ABCDEF0L;
        byte[] expected = {(byte) 0xF0, (byte) 0xDE, (byte) 0xBC, (byte) 0x9A, 0x78, 0x56, 0x34, 0x12};
        byte[] actual = new byte[8];
        BytesHelper.long2BytesBE(value, actual, 0, 8);
        assertArrayEquals(expected, actual);
    }
}
