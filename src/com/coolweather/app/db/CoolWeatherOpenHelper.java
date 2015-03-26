package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * ����������ݿ���ص���
 * @author Lenovo
 *
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper{
	/**
	 * Province�������
	 */
	public static final String CREATE_PROVINCE="create table Province("
			+ "id integer PRIMARY KEY AUTOINCREMENT  ,"
			+ "province_name text,"
			+ "province_code text)";
	/**
	 * City�������
	 */
	public static final String CREATE_CITY="create table City(id integer PRIMARY KEY AUTOINCREMENT  ,"
			+ "city_name text,"
			+ "city_code text,"
			+ "provinceId integer)";
	/**
	 * County�������
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
		db.execSQL(CREATE_PROVINCE);//����Province��
		db.execSQL(CREATE_CITY);//����City��
		db.execSQL(CREATE_COUNTY);//����County��
		System.out.println("������ɹ�!!!");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}

}
