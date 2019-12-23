package com.elastic.beans.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.io.Serializable;

/**
 * 用户实体对应的ES库实体
 *
 * @Date: 2019/12/19 15:25
 **/

public class BaseESEnity implements Serializable {

	@Id
	private String id;

	@Version
	private Long version;

	private String updateTime;

	private String createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
