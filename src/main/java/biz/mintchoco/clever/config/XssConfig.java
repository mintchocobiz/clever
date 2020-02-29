package biz.mintchoco.clever.config;

import biz.mintchoco.clever.util.wrapper.XssType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "xss")
public class XssConfig {

    private XssType type;

    private List<String> excludePaths;

    @PostConstruct
    public void info() {
        log.debug("xss config -> type");
        log.debug(type.name());
        log.debug("xss config -> exclude-path");
        excludePaths.forEach(item -> log.debug(item));
    }

    public boolean isExcludePath(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        if (StringUtils.isNotBlank(requestPath)) {
            for (String path : excludePaths) {
                if (requestPath.startsWith(path)) {
                    return true;
                }
            }
        }
        return false;
    }
}
