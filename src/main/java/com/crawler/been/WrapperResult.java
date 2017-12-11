package com.crawler.been;

public class WrapperResult<T> {

    private ResultStatus resultStatus;

    private T result;

    public WrapperResult() {
    }

    public WrapperResult(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

}
