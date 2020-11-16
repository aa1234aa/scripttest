package com.bitnei.cloud.sys.dao;

import com.bitnei.cloud.common.handler.IMapper;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import com.bitnei.cloud.sys.model.VehicleInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehicleMapper接口<br>
* 描述： VehicleMapper接口，在xml中引用<br>
* 授权 : (C) Copyright (c) 2017 <br>
* 公司 : 北京理工新源信息科技有限公司<br>
* ----------------------------------------------------------------------------- <br>
* 修改历史 <br>
* <table width="432" border="1">
* <tr>
* <td>版本</td>
* <td>时间</td>
* <td>作者</td>
* <td>改变</td>
* </tr>
* <tr>
* <td>1.0</td>
* <td>2018-12-12 17:40:52</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Mapper
public interface VehicleMapper extends IMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    Vehicle findById(Map<String,Object> params);

    /**
     * 通过id查询
     * @param vids ids
     * @return
     */
    List<Vehicle> getByIds(List<String> vids);

 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(Vehicle obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(Vehicle obj);

	/**
     * 删除
     * @param params
     * @return
     */
    int delete(Map<String,Object> params);



    /**
     * 车辆销售列表分页条件查询
     * @param params
     * @return
     */
    List<Vehicle> findVehSellPager(Map<String,Object> params);

    /**
     * 车辆销售列表分页条件查询
     * @param params
     * @return
     */
    List<Vehicle> findVehOperPager(Map<String,Object> params);

    /**
     * 车辆列表分页条件查询：明确条件：
     * @param params
     * @return
     */
    List<Vehicle> findPagerModelByBaseInfo(Map<String,Object> params);

    /**
     * 通过vin和车牌号查询车辆uuid
     * @param params
     * @return
     */
    String findUuidByVinAndLicense(Map<String,Object> params);

    /**
     * 通过vin和车牌号和内部编码查询车辆
     * @param params
     * @return
     */
    Vehicle findByVinAndLicenseAndInterNo(Map<String,Object> params);

    /**
     * 通过uuid查询车辆
     * @param params
     * @return
     */
    Vehicle findByUuid(Map<String,Object> params);

    /**
     * 通过权限sql查找车辆id
     * @param authSQL
     * @return
     */
    List<String> findIdsAuthSql(String authSQL);

    /**
     * 通过vin查询车辆
     * @param params
     * @return
     */
    Vehicle findByVin(Map<String,Object> params);

    /**
     * 批量通过vin查询车辆
     * @param params
     * @return
     */
    List<Vehicle> findByVins(Map<String,Object> params);

    /**
     * 通过iccid查询车辆
     * @param params
     * @return
     */
    Vehicle findByIccId(Map<String,Object> params);

    /**
     * 车辆换件、解绑
     * @param params
     */
    void changeORUnBindParts(Map<String,Object> params);

    /**
     * 通过车牌号查找
     * @param params
     * @return
     */
    Vehicle findByLicensePlate(Map<String,Object> params);

    /**
     * 通过运营内部编号查找
     * @param params
     * @return
     */
    Vehicle findByOperInterNo(Map<String,Object> params);

    /**
     * 游标查询, 必须在事务中使用
     * xml 配置中必须加上 resultOrdered="true"
     *
     * @param args 查询参数
     * @return 结果集
     */
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
    <TArgs, TEntity> Cursor<TEntity> findPagerModel(@Nullable final TArgs args);

    /**
     * 流式查询
     * xml 配置中必须加上 resultOrdered="true"
     *
     * @param args    查询参数
     * @param handler 结果集
     */
    <TArgs, TEntity> void findPagerModel(@Nullable final TArgs args, @NotNull final ResultHandler<TEntity> handler);

    List<VehicleInfoModel> vehicles(@Param("vid") String vid, @Param("updateTime") String updateTime);

    /**
     * 流式查询
     * xml 配置中必须加上 resultOrdered="true"
     *
     * @param args      查询参数
     * @param rowBounds 分页参数
     */
    <TArgs, TEntity> Page<TEntity> findPagerModel(@Nullable final TArgs args, @NotNull final PageRowBounds rowBounds);

    /**
     * 查询车辆基础信息供外部关联使用
     * @param params 查询参数
     * @return
     */
    List<Vehicle> findForLk(Map<String,Object> params);

    /**
     * 根据id查询芯片型号id
     * @param id 车辆id
     * @return 芯片型号id
     */
    String getChipModelId(@Param("id") String id);

    /**
     *  根据车辆id查询芯片id
     * @param id 车辆id
     * @return 芯片型号id
     */
    String getChipId(@Param("id") String id);

    /**
     * 查询车辆销售信息
     * @param id
     * @return
     */
    Map<String, Object> getSellInfo(@Param("id") String id);

    int updateInspectStatusOutOfPeriodByVins(List<String> vins);

    Integer findByLicensePlateCount(@Param("licensePlate") String licensePlate, @Param("id") String id);
}
