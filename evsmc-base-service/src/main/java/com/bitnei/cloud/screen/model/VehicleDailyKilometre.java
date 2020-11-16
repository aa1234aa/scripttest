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
public class VehicleDailyKilometre {

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 日平均里程
     */
    private double avgDailyMileage;

    /**
     * 每日里程
     */
    private List<DailyKilometre> kilometreList;

    @Getter
    @Setter
    public static class DailyKilometre {
        /**
         * 车牌号
         */
        @JsonIgnore
        private String licensePlate;
        /**
         * 天
         */
        private Integer day;
        /**
         * 里程
         */
        private Double kilometre;

    }

}
