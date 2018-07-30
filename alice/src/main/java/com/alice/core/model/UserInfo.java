package com.alice.core.model;

import java.io.Serializable;

public class UserInfo implements Serializable{
	
	//用户ID
	private String  user_id;
	//用户姓名
	private String yhm;
	//用户头像
	private String tx;
	//用户角色
	private String jsm;
	//用户手机号
	private String sjh;
	//用户性别
	private String sex;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getYhm() {
		return yhm;
	}
	public void setYhm(String yhm) {
		this.yhm = yhm;
	}
	public String getTx() {
		return tx;
	}
	public void setTx(String tx) {
		this.tx = tx;
	}
	public String getJsm() {
		return jsm;
	}
	public void setJsm(String jsm) {
		this.jsm = jsm;
	}
	public String getSjh() {
		return sjh;
	}
	public void setSjh(String sjh) {
		this.sjh = sjh;
	}
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}
