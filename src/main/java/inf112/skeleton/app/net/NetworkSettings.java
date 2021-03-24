package inf112.skeleton.app.net;

/**
 * Class that holds all the settings for network:
 * IP
 * Ports
 */
public class NetworkSettings {
    private final String ip;
    private final String state;
    private final int tcpPort;
    private final int udpPort;

    public NetworkSettings(String state, String ip, int tcpPort, int udpPort){
        this.state = state.toLowerCase();
        this.ip = ip;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
    }

    public String getIp() {
        return ip;
    }

    public String getState() {
        return state;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }
}
