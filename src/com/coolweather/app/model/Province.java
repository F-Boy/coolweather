package com.coolweather.app.model;
/**
 * ʡ��ʵ����
 * @author Lenovo
 *
 */
public class Province {
	private int id;
	private String provinceName;//ʡ����
	private String provinceCode;//ʡ������
	
	public Province(int id, String provinceName, String provinceCode) {
		super();
		this.id = id;
		this.provinceName = provinceName;
		this.provinceCode = provinceCode;
	}
	
	
	public Province() {
		super();
		
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	
}
