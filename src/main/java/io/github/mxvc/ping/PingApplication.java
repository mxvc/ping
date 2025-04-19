package io.github.mxvc.ping;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RestController
@SpringBootApplication
public class PingApplication  implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PingApplication.class, args);
    }

    @RequestMapping(value = {"/", "/index.html"},produces = "text/plain")
    public String info() {
        List<String> result = new ArrayList<>();

        result.add("SERVER Name:");
        result.add(getServerName());
        result.add("");

        List<String> serverIps = getServerIps();
        result.add("SERVER IP:");
        result.addAll(serverIps);


        StringBuilder sb = new StringBuilder();
        for (String s : result) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }


    private List<String> getServerIps() {
        List<String> ips = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {
                        System.out.println("本机IP地址为: " + address.getHostAddress());
                        ips.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ips;
    }

    private String getServerName() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String hostname = inetAddress.getHostName();
            return hostname;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(info());
    }
}
