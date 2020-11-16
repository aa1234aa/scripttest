package com.bitnei.cloud.screen.protocol;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * 数据处理工具
 *
 * @author xuzhijie
 */
public final class DataTool {

    /**
     * 将返回结果转成非空对象
     *
     * @param src
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static final <T> T nullConvert(T src, T defaultValue) {
        return src == null ? defaultValue : src;
    }

    /**
     * 截取从0 ~ size之间的数据
     *
     * @param data
     * @param size
     * @return
     */
    public static final <T> List<T> subList(List<T> data, int size) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        if (data.size() > size) {
            return data.subList(0, size);
        }
        return data;
    }
}
