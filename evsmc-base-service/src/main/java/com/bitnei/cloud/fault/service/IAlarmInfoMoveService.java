package com.bitnei.cloud.fault.service;

/**
 * 历史报警数据迁移业务接口
 */
public interface IAlarmInfoMoveService {

    /**
     * 迁移当前报警已结束数据
     */
    void move();

    /**
     * 迁移历史报警数据
     */
    void moveHistory();
}
