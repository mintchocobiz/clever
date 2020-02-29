package biz.mintchoco.clever.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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

  private List<String> excludePaths;

  @PostConstruct
  public void info() {
    log.debug("xss config -> exclude-path");
    for (String path : excludePaths) {
      log.debug(path);
    }
  }

  public boolean isExcludePath(HttpServletRequest request) {
    String requestPath = request.getRequestURI();
    for (String path : excludePaths) {
      if (requestPath.startsWith(path)) {
        return true;
      }
    }
    return false;
  }
}