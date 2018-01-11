package com.crawler.utils;

import com.google.common.collect.Lists;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class CsvFileUtils {

    private static final Logger logger = LoggerFactory.getLogger(CsvFileUtils.class);


    public static void main(String[] args) {
        List<String> arrayList = Lists.newArrayList();
        List<String> arrayList2 = Lists.newArrayList();
        arrayList2.add("2");
        arrayList2.add("2");
        arrayList2.add("2");
        arrayList2.add("2");
        arrayList2.add("2");
        List<List<String>> dataList = Lists.newArrayList();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        arrayList.add("1，2");
        arrayList.add("s,,d");
        dataList.add(arrayList);
        dataList.add(arrayList2);
        exportCsv("/Users/nestia/test/666.csv",dataList,null);
        // System.out.println(importCsv(file));
    }

    public static File exportCsv(String fileName, List<List<String>> dataLists, String[] header) {
        CSVFormat format;
        if (ArrayUtils.isEmpty(header)) {
            format = CSVFormat.DEFAULT.withIgnoreHeaderCase();
        } else {
            format = CSVFormat.DEFAULT.withHeader(header);
        }
        CSVPrinter printer = null;
        try {
            Writer out = new FileWriter(fileName);
            printer = new CSVPrinter(out, format);
            for (List<String> list : dataLists) {
                    printer.printRecord(list);
            }
        } catch (Exception e) {
            logger.error("exportCsv error {}", e);
        } finally {
            flushAndClose(printer);
        }
        return new File(fileName);
    }

    private static void flushAndClose(CSVPrinter printer) {
        if (printer != null) {
            try {
                printer.flush();
                printer.close();
            } catch (IOException e) {
                logger.error("exportCsv error {}", e);
            }
        }
    }

    public static List<Map<String, String>> importCsv(String fileName,String[] header) {
        List<Map<String, String>> dataList = Lists.newArrayList();
        CSVFormat format;
        if(ArrayUtils.isEmpty(header)){
            format = CSVFormat.DEFAULT.withIgnoreHeaderCase();
        }   else {
            format = CSVFormat.DEFAULT.withHeader(header);
        }
        Reader in = null;
        try {
            in = new FileReader(fileName);
            Iterable<CSVRecord> records = format.parse(in);
            for (CSVRecord record : records) {
                dataList.add(record.toMap());
            }
        } catch (Exception e) {
            logger.error("importCsv error {}", e);
        } finally {
            readerClose(in);
        }
        return dataList;
    }

    private static void readerClose(Reader in){
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            logger.error("importCsv error {}", e);
        }
    }

}
