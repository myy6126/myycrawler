package com.crawler.wrapper;

import com.crawler.been.ResultStatus;
import com.crawler.been.WrapperParams;
import com.crawler.been.WrapperResult;
import com.crawler.classLoader.WrapperReference;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author caozhaorui
 */
public class WrapperCrawlerGenerator {

    private static final Logger logger = LoggerFactory.getLogger(WrapperCrawlerGenerator.class);

    private WrapperParams wrapperParams;

    public WrapperCrawlerGenerator(WrapperParams wrapperParams) {
        this.wrapperParams = wrapperParams;
    }

    public WrapperResult generator() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        if (checkRequiredKeys()) {
            BaseWrapper wrapperIns = WrapperReference.getBaseWrapperRef(wrapperParams.getWrapperId(), wrapperParams.getWrapperClass());
            if (null != wrapperIns) {
                WrapperResult wrapperResult = wrapperIns.executeProcess(wrapperParams);
                return wrapperResult;
            }
        }
        return new WrapperResult(ResultStatus.NO_RESULT);
    }


    private boolean checkRequiredKeys() {
        if (StringUtils.isBlank(wrapperParams.getWrapperClass())) {
            logger.warn("request error. wrapperId or className is null. requiredKeys:{}", wrapperParams);
            return false;
        }
        return true;
    }
}
