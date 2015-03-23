package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 常用数据库操作封装类
 * @author Lenovo
 *
 */
public class CoolWeatherDB {
	/**
	 * 数据库名
	 */
	public static final String DB_NAME="cool_weather";
	/**
	 * 数据库版本
	 */
	public static final int VERSION=1;
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;
	/**
	 * 将构造方法私有化
	 * @param context
	 */
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper helper=new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db=helper.getWritableDatabase();
	}
	/**
	 * 获取CoolWeatherDB的实例
	 * @param context
	 * @return
	 */
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB==null){
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	/**
	 * 将Province实例存储到数据库
	 * @param province
	 */
	public void saveProvince(Province province){
		if(province!=null){
			ContentValues values=new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);//添加到数据库
		}
	}
	/**
	 * 从数据库读取全国所有省份信息
	 * @return list
	 */
	public List<Province> loadProvinces(){
		List<Province> list=new ArrayList<Province>();
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province=new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		return list;
	}
	
	
	/**
	 * 将City实例存储到数据库
	 * @param city
	 */
	public void saveCity(City city){
		if(city!=null){
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_Id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	/**
	 * 从数据库读取某省下的所有的城市信息
	 * @param provinceId
	 * @return list
	 */
	public List<City> loadCity(int provinceId){
		List<City> list=new ArrayList<City>();
		Cursor crusor=db.query("City", null, "province_Id=?", new String[] {String.valueOf(provinceId)}, null, null, null);
		if(crusor.moveToFirst()){
			do{
				City city=new City();
				city.setId(crusor.getInt(crusor.getInt(crusor.getColumnIndex("id"))));
				city.setCityName(crusor.getString(crusor.getColumnIndex("city_name")));
				city.setCityCode(crusor.getString(crusor.getColumnIndex("city_code")));
			
				city.setProvinceId(provinceId);
				list.add(city);
			}while(crusor.moveToNext());
		}
		return list;
	}
	
	/**
	 * 将County实例存储到数据库
	 * @param county
	 */
	public void saveCounty(County county){
		if(county!=null){
			ContentValues values=new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("count_code", county.getCountyCode());
			values.put("city_Id", county.getCityId());
			db.insert("County", null, values);
		}
	}
	
	public List<County> loadCounty(int cityId){
		List<County> list=new ArrayList<County>();
		Cursor cursor=db.query("County", null, "city_Id=?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				County city=new County();
				city.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				city.setCountyCode(cursor.getString(cursor.getColumnIndex("count_code")));
				city.setCityId(cityId);
				list.add(city);
			}while(cursor.moveToNext());
		}
		return list;
	}
}
