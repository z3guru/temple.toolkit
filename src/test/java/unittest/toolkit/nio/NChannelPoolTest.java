package unittest.toolkit.nio;

import guru.z3.toolkit.nio.Transceiver;
import guru.z3.toolkit.nio.NChannelPool;
import guru.z3.toolkit.nio.NioContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class NChannelPoolTest {

    private NChannelPool pool;
    private AsynchronousServerSocketChannel server;
    private InetSocketAddress serverAddress;

    @BeforeEach
    void setUp() throws IOException {
        //
        NioContext.getInstance().setScheduler(new ScheduledThreadPoolExecutor(1));

        pool = new NChannelPool();
        // 테스트용 루프백 서버 생성 (포트 0은 사용 가능한 임의 포트 할당)
        server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress("127.0.0.1", 0));
        serverAddress = (InetSocketAddress) server.getLocalAddress();
        
        // 서버 수락 루프 (테스트 연결용)
        acceptNext();
    }

    private void acceptNext() {
        server.accept(null, new java.nio.channels.CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel result, Void attachment) {
                server.accept(null, this);
            }
            @Override
            public void failed(Throwable exc, Void attachment) {}
        });
    }

    @AfterEach
    void tearDown() throws IOException {
        if (server != null) server.close();
    }

    @Test
    @DisplayName("서버 연결 후 채널 대여 및 반납 테스트")
    void testLeaseAndRelease() throws Exception {
        // Given: 서버 주소 설정 및 연결 시작
        List<SocketAddress> addrs = List.of(serverAddress);
        pool.setup(addrs);

        // When: 채널 대여 시도 (연결될 때까지 잠시 대기)
        Transceiver.Peer peer = pool.leaseChannel(3, TimeUnit.SECONDS);

        // Then: 대여 성공 확인
        assertNotNull(peer, "Leased peer should not be null");
        assertEquals(serverAddress, peer.addr());
        assertTrue(peer.channel().isOpen());

        // When: 채널 반납
        pool.releaseChannel(peer);
        Transceiver.Peer reLeasedPeer = pool.leaseChannel(1, TimeUnit.SECONDS);

        // Then: 동일한 채널이 다시 대여되어야 함
        assertNotNull(reLeasedPeer);
        assertEquals(peer, reLeasedPeer);
    }

    @Test
    @DisplayName("비어있는 풀에서 대여 시도 시 타임아웃 발생 확인")
    void testLeaseTimeout() throws InterruptedException {
        // 연결을 설정하지 않은 상태에서 대여 시도
        Transceiver.Peer peer = pool.leaseChannel(500, TimeUnit.MILLISECONDS);
        
        assertNull(peer, "Peer should be null on timeout");
    }

    @Test
    @DisplayName("Trash 처리된 채널이 재연결 메커니즘을 타는지 확인")
    void testTrashChannel() throws Exception {
        pool.setup(List.of(serverAddress));
        Transceiver.Peer peer = pool.leaseChannel(3, TimeUnit.SECONDS);
        assertNotNull(peer);

        // When: 채널을 폐기(trash) 처리
        pool.trashChannel(peer);

        // Then: 풀은 일시적으로 비어있어야 함
        Transceiver.Peer emptyPeer = pool.leaseChannel(100, TimeUnit.MILLISECONDS);
        assertNull(emptyPeer);

        // Then: 잠시 후 재연결되어 다시 대여 가능해져야 함 (NChannelPool 내부 1000ms 재시도 로직 고려)
        Transceiver.Peer reconnectedPeer = pool.leaseChannel(3, TimeUnit.SECONDS);
        assertNotNull(reconnectedPeer, "Should reconnect and be available again");
        assertNotSame(peer, reconnectedPeer, "Should be a new peer instance or at least re-established");
    }
}
