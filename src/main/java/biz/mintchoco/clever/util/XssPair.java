package biz.mintchoco.clever.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum XssPair {
    HARDCORE_01("'", "&#39;"),
    HARDCORE_02("<", "&lt;"),
    HARDCORE_03(">", "&gt;"),
    HARDCORE_04("(", "&#40;"),
    HARDCORE_05(")", "&#41;"),

    REGEX_01("eval\\((.*)\\)", ""),
    REGEX_02("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\""),
    REGEX_03("(?i)script", ""),
    REGEX_04("", ""),
    ;

    XssPair(String target, String replacement) {
        this.target = target;
        this.replacement = replacement;
    }

    private String target;

    private String replacement;

    public static String applyXssRegex() {
        String buf = "";
        return buf;
    }

    public static String xssHardcore(String str) {
        return "";
    }

    private static String replace(String str, XssPair xssPair) {
        return str.replace(xssPair.target, xssPair.replacement);
    }
}
