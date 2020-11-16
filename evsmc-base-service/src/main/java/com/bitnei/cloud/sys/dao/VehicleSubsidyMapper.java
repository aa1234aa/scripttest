package com.bitnei.cloud.sys.dao;


import com.bitnei.cloud.common.handler.IMapper;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link VehicleSubsidyMapper} extends {@link IMapper}
 * @author zxz
 */
@Mapper
public interface VehicleSubsidyMapper extends IMapper {

    /**
     * 游标查询, 必须在事务中使用
     * xml 配置中必须加上 resultOrdered="true"
     *
     * @param args 查询参数
     * @return 结果集
     */
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
    <TArgs, TEntity> Cursor<TEntity> findPager(@Nullable final TArgs args);

    /**
     * 流式查询
     * xml 配置中必须加上 resultOrdered="true"
     *
     * @param args    查询参数
     * @param handler 结果集
     */
    <TArgs, TEntity> void findPager(@Nullable final TArgs args, @NotNull final ResultHandler<TEntity> handler);

    /**
     * 流式查询
     * xml 配置中必须加上 resultOrdered="true"
     *
     * @param args    查询参数
     * @param rowBounds 分页参数
     */
    <TArgs, TEntity> Page<TEntity> findPager(@Nullable final TArgs args, @NotNull final PageRowBounds rowBounds);


    /**
     * 更新
     * @param obj
     * @return
     */
    int update(Vehicle obj);
}
