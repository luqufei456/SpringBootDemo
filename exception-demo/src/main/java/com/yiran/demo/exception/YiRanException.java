package com.yiran.demo.exception;

/*
* 自定义异常
* message 为 Throwable 的属性
* */
public class YiRanException extends RuntimeException {
    private int code;

    public YiRanException() {
        super();
    }

    public YiRanException(int code, String message) {
        super(message);
        this.setCode(code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
