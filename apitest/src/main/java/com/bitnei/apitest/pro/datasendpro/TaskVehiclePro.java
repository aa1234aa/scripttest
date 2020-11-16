package com.bitnei.apitest.pro.datasendpro;

import com.bitnei.apitest.annotation.pro.TableName;

@TableName(name="forward_task_vehicle")
public class TaskVehiclePro {
	private int taskId;
	private String platformId;
	private String vehicleIds;
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getPlatformId() {
		return platformId;
	}
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	public String getVehicleIds() {
		return vehicleIds;
	}
	public void setVehicleIds(String vehicleIds) {
		this.vehicleIds = vehicleIds;
	}

}
