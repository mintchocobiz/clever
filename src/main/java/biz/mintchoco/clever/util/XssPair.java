package biz.mintchoco.clever.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
public enum XssPair {

    HARDCORE_01("'", "&#39;"),
    HARDCORE_02("<", "&lt;"),
    HARDCORE_03(">", "&gt;"),
    HARDCORE_04("\\(", "&#40;"),
    HARDCORE_05("\\)", "&#41;"),

    REGEX_01("(?i)eval", "x-eval"),
    REGEX_02("(?i)javascript", "x-javascript"),
    REGEX_03("(?i)vbscript", "x-vbscript"),
    REGEX_04("(?i)script", "x-script"),
    REGEX_05("(?i)iframe", "x-iframe"),
    REGEX_06("(?i)document", "x-document"),
    REGEX_07("(?i)applet", "x-applet"),
    REGEX_08("(?i)embed", "x-embed"),
    REGEX_09("(?i)object", "x-object"),
    REGEX_10("(?i)frame", "x-frame"),
    REGEX_11("(?i)grameset", "x-grameset"),
    REGEX_12("(?i)layer", "x-layer"),
    REGEX_13("(?i)bgsound", "x-bgsound"),
    REGEX_14("(?i)alert", "x-alert"),
    REGEX_15("(?i)onblur", "x-onblur"),
    REGEX_16("(?i)onchange", "x-onchange"),
    REGEX_17("(?i)onclick", "x-onclick"),
    REGEX_18("(?i)ondblclick", "x-ondblclick"),
    REGEX_19("(?i)enerror", "x-enerror"),
    REGEX_20("(?i)onfocus", "x-onfocus"),
    REGEX_21("(?i)onload", "x-onload"),
    REGEX_22("(?i)onmouse", "x-onmouse"),
    REGEX_23("(?i)onscroll", "x-onscroll"),
    REGEX_24("(?i)onsubmit", "x-onsubmit"),
    REGEX_25("(?i)onunload", "x-onunload"),
    REGEX_27("(?i)img src", "x-img x-src"),
    REGEX_28("(?i)img dynsrc", "x-img x-dynsrc"),
    REGEX_29("(?i)img lowsrc", "x-img x-lowsrc"),
    REGEX_30("(?i)style", "x-style"),
    REGEX_31("(?i)link", "x-link"),
    REGEX_32("(?i)href", "x-href"),
    REGEX_33("(?i)input", "x-input"),
    REGEX_34("(?i)body", "x-body"),
    REGEX_35("(?i)meta", "x-meta"),
    ;

    XssPair(String target, String replacement) {
        this.target = target;
        this.replacement = replacement;
    }

    private String target;

    private String replacement;

    public static List<XssPair> all() {
        return Arrays.asList(values());
    }

    public static List<XssPair> hardcore() {
        return Arrays.asList(values()).stream()
                .filter(item -> item.name().startsWith("HARDCORE"))
                .collect(Collectors.toList());
    }

    public static List<XssPair> regex() {
        return Arrays.asList(values()).stream()
                .filter(item -> item.name().startsWith("REGEX"))
                .collect(Collectors.toList());
    }
}
