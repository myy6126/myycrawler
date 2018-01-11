package com.crawler.service.impl;

import com.crawler.been.ResultStatus;
import com.crawler.been.WrapperParams;
import com.crawler.been.WrapperResult;
import com.crawler.model.PllcPageDetail;
import com.crawler.model.PllcPageList;
import com.crawler.model.PllcPageListResult;
import com.crawler.service.PllcService;
import com.crawler.utils.Configs;
import com.crawler.utils.CsvFileUtils;
import com.crawler.utils.DownloadUtils;
import com.crawler.wrapper.WrapperCrawlerGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PllcServiceImpl implements PllcService {

    private final Logger logger = LoggerFactory.getLogger(PllcServiceImpl.class);

    private static final String LIST_URL_FILE = "pllc_list_url";
    private static final String MP3_URL_FILE = "pllc_mp3_url";
    private static final String MP4_URL_FILE = "pllc_mp4_url";

    private static final String WRAPPER_ID = "pllcWrapper";
    private static final String LIST_WRAPPER_CLASS = "PllcPageListWrapper";
    private static final String MP3_WRAPPER_CLASS = "PllcMp3Wrapper";
    private static final String MP4_WRAPPER_CLASS = "PllcMp4Wrapper";
    private static final String DOWNLOAD_WRAPPER_CLASS = "PllcDownloadWrapper";

    private static final int FAILED_MAX_COUNT = 5;

    private static final String PLLC_OUT_PATH = Configs.getValue("pllc.out.path");
    private static final String PLLC_MP3_OUT_PATH = PLLC_OUT_PATH + "mp3/";
    private static final String PLLC_MP4_OUT_PATH = PLLC_OUT_PATH + "mp4/";

    @Override
    public void getPageList() {
        String fileName = PLLC_OUT_PATH + LIST_URL_FILE;
        List<PllcPageList> list = crawlListUrl(LIST_WRAPPER_CLASS);
        saveInFile(fileName, list);
    }

    @Override
    public void getMp3Url() {
        String fileName = PLLC_OUT_PATH + MP3_URL_FILE;
        List<PllcPageList> pllcPageLists = crawlListUrl(MP3_WRAPPER_CLASS);
        saveInFile(fileName, pllcPageLists);
    }

    private List<PllcPageList> crawlListUrl(String wrapperClass) {
        List<PllcPageList> resultMap = Lists.newArrayList();
        int pageNo = 1;
        boolean isEnd = false;
        int failedCount = 0;
        while (!isEnd && failedCount < FAILED_MAX_COUNT) {
            Map<String, String> paramMap = Maps.newHashMap();
            paramMap.put("pageNo", String.valueOf(pageNo));
            WrapperParams wrapperParams = WrapperParams.create(WRAPPER_ID, wrapperClass);
            wrapperParams.setParam(paramMap);
            WrapperCrawlerGenerator wrapperCrawlerGenerator = new WrapperCrawlerGenerator(wrapperParams);
            WrapperResult wrapperResult = wrapperCrawlerGenerator.generator();
            ResultStatus resultStatus = wrapperResult.getResultStatus();
            if (resultStatus.getValue() != ResultStatus.NORMAL.getValue() || Objects.isNull(wrapperResult.getResult())) {
                ++failedCount;
                logger.info(" get wrapper result error. {} ", resultStatus.getMonitorSuffix());
            }
            PllcPageListResult pllcPageListResult = (PllcPageListResult) wrapperResult.getResult();
            if (CollectionUtils.isNotEmpty(pllcPageListResult.getPllcPageLists())) {
                resultMap.addAll(pllcPageListResult.getPllcPageLists());
                logger.info("pllcPageList getPage:{}", pageNo);
            }
            isEnd = pllcPageListResult.isCrawlEnd();
            ++pageNo;
        }
        return resultMap;
    }

    private void saveInFile(String fileName, List<PllcPageList> pllcPageLists) {
        List<List<String>> saveLists = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(pllcPageLists)) {
            pllcPageLists.forEach(pllcPageList -> {
                        List<String> list = Lists.newArrayList();
                        list.add(pllcPageList.getId());
                        list.add(pllcPageList.getUrl());
                        saveLists.add(list);
                    }
            );
        }
        CsvFileUtils.exportCsv(fileName, saveLists, null);
    }

    @Override
    public void getMp4Url() {
        String fileName = PLLC_OUT_PATH + MP4_URL_FILE;
        crawlAndGetMp4List();
        // write file
    }

    private List<PllcPageDetail> crawlAndGetMp4List() {
        // crawl
        return null;
    }

    @Override
    public void downloadMp3() {
        List<Map<String, String>> dataListMap = CsvFileUtils.importCsv(MP3_URL_FILE, null);
        logger.info("count {}",dataListMap.size());
        dataListMap.forEach( map ->
            map.forEach((name,url)->{
                logger.info("{} download mp3 Start...",name);
                DownloadUtils.downLoadFromUrl(url,name,PLLC_MP3_OUT_PATH);
            })
        );
    }

    @Override
    public void downloadMp4() {
        List<Map<String, String>> dataListMap = CsvFileUtils.importCsv(MP4_URL_FILE, null);
        logger.info("count {}",dataListMap.size());
        dataListMap.forEach( map ->
                map.forEach((name,url)->{
                    logger.info("{} download mp4 Start...",name);
                    DownloadUtils.downLoadFromUrl(url,name,PLLC_MP4_OUT_PATH);
                })
        );
    }

    public static void main(String[] args) {
        PllcServiceImpl pllcService = new PllcServiceImpl();
        pllcService.getMp3Url();
    }

}
