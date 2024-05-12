package com.dullfan.generate.utils.extremely;

public class ServiceException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected String errorCode = "500";
    /**
     * 错误信息
     */
    protected String errorMsg;

    public ServiceException() {
        super();
    }

    public ServiceException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ServiceException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public ServiceException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ServiceException(Exception e) {
        super(e);
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}