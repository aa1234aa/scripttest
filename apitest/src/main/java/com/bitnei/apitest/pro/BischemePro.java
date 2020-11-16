package com.bitnei.apitest.pro;

import java.util.Date;

import com.bitnei.apitest.annotation.pro.TableName;

@TableName(name="bi_scheme")
public class BischemePro extends Po {
		
		private String id;
		private String scheme_name;
		private String template_type;
		private int status;
		private String description;
		private Date create_time;
		
		public BischemePro() {
			super();
			// TODO Auto-generated constructor stub
		}
		public BischemePro(String id, String scheme_name, String template_type, int status, String description,
				Date create_time) {
			super();
			this.id = id;
			this.scheme_name = scheme_name;
			this.template_type = template_type;
			this.status = status;
			this.description = description;
			this.create_time = create_time;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getScheme_name() {
			return scheme_name;
		}
		public void setScheme_name(String scheme_name) {
			this.scheme_name = scheme_name;
		}
		public String getTemplate_type() {
			return template_type;
		}
		public void setTemplate_type(String template_type) {
			this.template_type = template_type;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Date getCreate_time() {
			return create_time;
		}
		public void setCreate_time(Date create_time) {
			this.create_time = create_time;
		}
		
		
	}

