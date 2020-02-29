package biz.mintchoco.clever.biz.system.mvc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/system")
public class SystemController {

    @GetMapping("info")
    public ResponseEntity<Map<String, String>> info() {
        return new ResponseEntity(getSystemInfo(), HttpStatus.OK);
    }

    private Map<String, String> getSystemInfo() {
        try {
            Map<String, String> item = new HashMap<>();
            item.put("host-name", getHostName());
            item.put("host-addr", getHostAddress());
            item.put("datetime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return item;

        } catch(Exception ex) {
            return null;
        }
    }

    private String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    private String getHostAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
