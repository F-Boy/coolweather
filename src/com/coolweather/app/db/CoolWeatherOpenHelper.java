package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * 存放所有数据库相关的类
 * @author Lenovo
 *
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper{
	/**
	 * Province表建表语句
	 */
	public static final String CREATE_PROVINCE="create table Province("
			+ "id integer PRIMARY KEY AUTOINCREMENT  ,"
			+ "province_name text,"
			+ "province_code text)";
	/**
	 * City表建表语句
	 */
	public static final String CREATE_CITY="create table City(id integer PRIMARY KEY AUTOINCREMENT  ,"
			+ "city_name text,"
			+ "city_code text,"
			+ "provinceId integer)";
	/**
	 * County表建表语句
	 */
	public static final String CREATE_COUNTY="create table County(id integer PRIMARY KEY AUTOINCREMENT  ,"
			+ "county_name text,"
			+ "county_code text,"
			+ "cityId integer )";
	
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);//创建Province表
		db.execSQL(CREATE_CITY);//创建City表
		db.execSQL(CREATE_COUNTY);//创建County表
		System.out.println("创建表成功!!!");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}

}
