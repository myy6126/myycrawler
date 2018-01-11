package com.crawler.wrapper;

import com.crawler.been.ResultStatus;
import com.crawler.been.WrapperParams;
import com.crawler.been.WrapperResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseWrapper {

    private static final Logger logger = LoggerFactory.getLogger(BaseWrapper.class);

    public abstract WrapperResult executeWrapper(WrapperParams wrapperParams);

    public WrapperResult executeProcess(WrapperParams requiredKeys) {
        try {
            return executeWrapper(requiredKeys);
        } catch (Exception e) {
            logger.error("executeProcess exception:{}", requiredKeys.toString(), e);
            return new WrapperResult(ResultStatus.PARSING_FAIL);
        }
    }

}
