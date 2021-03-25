package inf112.skeleton.app.networktests;

import inf112.skeleton.app.net.NetworkSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NetworkSettingsTest {

    private NetworkSettings settings;
    private String state;
    private String ip;
    private int tcpPort;
    private int udpPort;


    @BeforeEach
    public void setUp(){
        state = "server";
        ip = "localhost";
        tcpPort = 8080;
        udpPort = 8080;
        settings = new NetworkSettings(state, ip, tcpPort, udpPort);
    }

    @Test
    public void networkSettingsStateTest(){
        assertEquals(state, settings.getState());
    }

    @Test
    public void networkSettingsIpTest(){
        assertEquals(ip, settings.getIp());
    }

    @Test
    public void networkSettingsTcpTest(){
        assertEquals(tcpPort, settings.getTcpPort());
    }
    @Test
    public void networkSettingsUdpTest(){
        assertEquals(udpPort, settings.getUdpPort());
    }
}
