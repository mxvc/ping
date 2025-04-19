package io.github.mxvc.ping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RestController
public class HomeController {

    @RequestMapping(value = {"/", "/index.html"},produces = "text/plain")
    public String request(HttpServletRequest request) {
        List<String> info = new ArrayList<>();
        List<String> serverIps = getServerIps();
        info.add("SERVER IP:");
        info.addAll(serverIps);


        StringBuilder sb = new StringBuilder();
        for (String s : info) {
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

}
