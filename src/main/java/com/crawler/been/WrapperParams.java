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

    private Map<String, String> param = Maps.newHashMap();
}
