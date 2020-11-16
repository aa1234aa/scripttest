package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.OfflineExport;
import com.bitnei.cloud.sys.domain.OfflineExportStateMachine;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author xzp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "下载任务")
public class DownloadTaskModel extends BaseModel {

    @ApiModelProperty(value = "任务标识")
    private String id;

    @ApiModelProperty(value = "导出文件名称")
    private String exportFileName;

    @ApiModelProperty(value = "开始时间")
    private String createTime;

    @ApiModelProperty(value = "任务状态")
    private String taskState;

    @Pattern(regexp = "[14]", message = "任务状态码无效")
    @NotBlank(message = "任务状态码无效")
    @ApiModelProperty(value = "任务状态编码")
    private String taskStateCode;

    @ApiModelProperty(value = "任务进度")
    private String taskProgress;

    @NotNull
    public static DownloadTaskModel fromEntry(@NotNull final OfflineExport entity, @NotNull final String progress){

        final String taskState;
        final String taskStateCode;
        final String taskProgress;

        switch (entity.getStateMachine()) {
            case OfflineExportStateMachine.CREATED:
            case OfflineExportStateMachine.EXPORTING: {
                if (StringUtils.isBlank(progress)) {
                    taskState = "已创建";
                    taskStateCode = "1";
                } else {
                    taskState = "导出中";
                    taskStateCode = "2";
                }
                break;
            }
            case OfflineExportStateMachine.FINISH: {
                taskState = "已完成";
                taskStateCode = "3";
                break;
            }
            case OfflineExportStateMachine.CANCELED: {
                taskState = "已取消";
                taskStateCode = "4";
                break;
            }
            case OfflineExportStateMachine.EXCEPTED: {
                taskState = "有异常";
                taskStateCode = "5";
                break;
            }
            default: {
                taskState = "";
                taskStateCode = "";
                break;
            }
        }

        switch (entity.getStateMachine()) {
            case OfflineExportStateMachine.CREATED:
            case OfflineExportStateMachine.EXPORTING: {
                taskProgress = progress;
                break;
            }
            case OfflineExportStateMachine.FINISH: {
                taskProgress = "100";
                break;
            }
            case OfflineExportStateMachine.CANCELED:
            case OfflineExportStateMachine.EXCEPTED: {
                taskProgress = "0";
                break;
            }
            default: {
                taskProgress = "";
                break;
            }
        }

        return new DownloadTaskModel(
            entity.getId(),
            entity.getExportFileName(),
            entity.getCreateTime(),
            taskState,
            taskStateCode,
            taskProgress
        );
    }
}
