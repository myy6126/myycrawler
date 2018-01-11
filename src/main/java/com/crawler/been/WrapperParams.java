package com.crawler.been;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class WrapperParams {
    private String wrapperId;
    private String wrapperClass;
    private String source;
    private Map<String, String> param = Maps.newHashMap();

    public static WrapperParams create(String wrapperId,String className) {
        WrapperParams requiredKeys = new WrapperParams();
        requiredKeys.setWrapperId(wrapperId);
        requiredKeys.setWrapperClass(className);
        return requiredKeys;
    }
}
