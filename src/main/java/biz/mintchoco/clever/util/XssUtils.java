package biz.mintchoco.clever.util;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class XssUtils {

    public static String stripXss(String value) {
        String buf = value;
        List<XssPair> xssPairs = BeanUtils.getXssConfig().getXssPairs();
        for (XssPair xssPair : xssPairs) {
            buf = buf.replaceAll(xssPair.getTarget(), xssPair.getReplacement());
        }
        log.debug("xss before -> {}", value);
        log.debug("xss after -> {}", buf);
        return buf;
    }
}
