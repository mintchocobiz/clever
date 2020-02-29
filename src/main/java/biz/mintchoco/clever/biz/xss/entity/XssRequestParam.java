package biz.mintchoco.clever.biz.xss.entity;

import lombok.Data;

import java.util.List;

@Data
public class XssRequestParam {

    private List<String> animals;
}
