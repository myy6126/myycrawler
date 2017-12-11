package com.crawler.wrapper;

import com.crawler.been.ResultStatus;
import com.crawler.been.WrapperParams;
import com.crawler.been.WrapperResult;
import com.crawler.utils.HttpExecuteUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class PllcPageListWrapper extends BaseWrapper {

    private static final Logger logger = LoggerFactory.getLogger(PllcPageListWrapper.class);

    private final String TARGET_URL = "http://new.pllc.cn/masterspeech/list/51.html?page=%s";

    private final String FILE_PATH = "src/com/crawler/out/";

    private final String DETAIL_URL_FILE = "pllcPgaeListUrl.properties";

    public WrapperResult executeWrapper(WrapperParams wrapperParams) {
        Map<String, String> paramMap = wrapperParams.getParam();
        if (MapUtils.isEmpty(paramMap)) {
            logger.error("wrapperParam's paramMap is null...");
        }
        String pageNo = paramMap.get("pageNo");
        List<String> pageList = getPageList(pageNo);
        if(CollectionUtils.isNotEmpty(pageList)){
            WrapperResult wrapperResult = new WrapperResult(ResultStatus.NORMAL);
            wrapperResult.setResult(pageList);
            return wrapperResult;
        }
        WrapperResult wrapperResult = new WrapperResult(ResultStatus.NO_RESULT);
        return wrapperResult;
    }


    //TODO 抓取页面逻辑
    public List getPageList(String pageNo) {
        String listIrl = String.format(TARGET_URL, pageNo);
        String htmlStr = HttpExecuteUtils.executeHttps(listIrl);
        Document document = Jsoup.parse(htmlStr, listIrl);
        return null;
    }

    public static void main(String[] args) {

    }

}
