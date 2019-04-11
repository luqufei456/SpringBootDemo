package com.yiran.demo.exception;

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
