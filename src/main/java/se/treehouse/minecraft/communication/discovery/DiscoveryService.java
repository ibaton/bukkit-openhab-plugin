package se.treehouse.minecraft.communication.discovery;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;

public class DiscoveryService {

    private JmDNS jmdns;

    /**
     * Creates a zero conf service that brodcasts to network.
     * @param port the port to broadcast.
     */
    public void registerService(int port) {
        ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "wc-minecraft", port, "");
        try {
            jmdns = JmDNS.create();
            jmdns.registerService(serviceInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop broadcasting service.
     */
    public void deregisterService() {
        if (jmdns != null) {
            try {
                jmdns.unregisterAllServices();
                jmdns.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
