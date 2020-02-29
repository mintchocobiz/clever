package biz.mintchoco.clever.biz.xss.entity;

import lombok.Data;

@Data
public class XssRequestParam {

    private String company;

    private String country;

    private String address;

    private String phoneNumber;
}
