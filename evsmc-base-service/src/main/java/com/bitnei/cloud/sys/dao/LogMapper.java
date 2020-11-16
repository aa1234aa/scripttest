package com.bitnei.cloud.sys.dao;

import com.bitnei.cloud.common.handler.IMapper;
import com.bitnei.cloud.sys.domain.Log;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by Lijiezhou on 2018/12/24.
 */
@Mapper
public interface LogMapper extends IMapper {

    /**
     * 分页查询
     * @param params 查询参数
     * @param rowBounds 分页参数
     * @return 分页结果
     */
    Page<Log> pagerModel(@NotNull final Map<String,Object> params, @NotNull final PageRowBounds rowBounds);

    /**
     * 通过ID查询
     * @param params
     * @return
     */
    Log findById(Map<String,Object> params);

    /**
     * 插入
     * @param log
     * @return
     */
    int insert(Log log);
}
