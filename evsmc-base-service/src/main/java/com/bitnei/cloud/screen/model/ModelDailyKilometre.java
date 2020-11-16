package com.bitnei.cloud.screen.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xuzhijie
 */
@Getter
@Setter
public class ModelDailyKilometre {

    /**
     * 车型名称
     */
    private String vehModelName;

    private List<DailyKilometre> kilometreList;

    @Getter
    @Setter
    public static class DailyKilometre {
        /**
         * 车型名称
         */
        @JsonIgnore
        private String vehModelName;
        /**
         * 天
         */
        private Integer day;
        /**
         * 平均里程
         */
        private Double avgKilometre;

    }

}
