package com.crawler.wrapper;

import com.crawler.been.WrapperParams;
import com.crawler.been.WrapperResult;

public abstract class BaseWrapper {

    public abstract WrapperResult executeWrapper(WrapperParams wrapperParams);

}
