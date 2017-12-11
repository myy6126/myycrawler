package com.crawler.been;

public enum ResultStatus {
    UNKNOWN(0, "UNKNOWN_", "未知状态"), UN_EXCEPTED_ERROR(10, "UN_EXCEPTED_ERROR_", "不该出现的异常"), NORMAL(20, "NORMAL_",
            "正常"), NO_RESULT(30, "NO_RESULT_", "没有结果"), PARSING_FAIL(40, "PARSING_FAIL_", "解析失败"), WEB_RESULT_NULL(50,
            "WEB_RESULT_NULL_",
            "网络返回为空"), WEB_ERROR(60, "WEB_ERROR_", "网络错误"), TIMEOUT(70, "TIMEOUT_", "网络请求超时");

    private final int value;
    private final String monitorSuffix;
    private final String monitorDesc;

    ResultStatus(int value, String monitorSuffix, String monitorDesc) {
        this.value = value;
        this.monitorSuffix = monitorSuffix;
        this.monitorDesc = monitorDesc;
    }

    public int getValue() {
        return value;
    }

    public String getMonitorSuffix() {
        return monitorSuffix;
    }

    public String getMonitorDesc() {
        return monitorDesc;
    }
}
