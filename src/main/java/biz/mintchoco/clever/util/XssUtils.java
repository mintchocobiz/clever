package biz.mintchoco.clever.util;

public class XssUtils {

    public static String stripXss(String value) {
        String buf = "";
        buf = value.replaceAll("'", "&#39;");
        buf = buf.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        buf = buf.replaceAll("\\(", "& #40;").replaceAll("\\)", "&#41;");
        buf = buf.replaceAll("eval\\((.*)\\)", "");
        buf = buf.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        buf = buf.replaceAll("(?i)script", "");
        return buf;
    }
}
