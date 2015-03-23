package com.coolweather.app.model;
/**
 * 县级实体类
 * @author Lenovo
 *
 */
public class County {
	private int id;
	private String countyName;//县名
	private String countyCode;//县级代号
	private int cityId;//County表关联City表的外键
	
	public County(int id, String countyName, String countyCode, int cityId) {
		super();
		this.id = id;
		this.countyName = countyName;
		this.countyCode = countyCode;
		this.cityId = cityId;
	}
	
	
	public County() {
		super();
	
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
}
