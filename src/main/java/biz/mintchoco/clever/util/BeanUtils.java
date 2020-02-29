package biz.mintchoco.clever.util;

import biz.mintchoco.clever.config.XssConfig;
import biz.mintchoco.clever.util.provider.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
public class BeanUtils {

    public static ApplicationContext getApplicationContext() {
        return ApplicationContextProvider.getApplicationContext();
    }

    public static XssConfig getXssConfig() {
        return getApplicationContext().getBean("xssConfig", XssConfig.class);
    }
}
