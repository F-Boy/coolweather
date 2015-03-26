package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;
import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	/**
	 * 省列表
	 */
	private List<Province> provinceList;

	/**
	 * 市列表
	 */
	private List<City> cityList;

	/**
	 * 县列表
	 */
	private List<County> countyList;

	/**
	 * 选中的省份
	 */
	private Province selectedProvince;

	/**
	 * 选中的城市
	 */
	private City selectedCity;

	/**
	 * 当前选中的级别
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);// 获取ListView的实例
		titleText = (TextView) findViewById(R.id.title_text);// 获取TextView的实例
		// 初始化数组适配器
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		// 设置为listView的适配器
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);// 获得CoolWeatherDB的实例
		// 给listView设置点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int index, long id) {
				if (countyList == null) {
					if (cityList != null) {
						selectedCity = cityList.get(index);
						queryCounties();
						cityList = null;
					} else{
						selectedProvince = provinceList.get(index);
						queryCities();
					}
				}
			}
		});
		queryProvinces();// 加载省级数据
	}

	/**
	 * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryProvinces() {
		// 调用loadProvinces()方法从数据库中读取省级数据,
		// 如果读到了就直接讲数据显示到界面上,
		// 如果没有读取到见调用queryFormServer()方法来从服务器上获取数据
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {

				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
		} else {
			queryFormServer(null, "province");
		}
	}

	/**
	 * 查询选中省内所有的市，优先从数据库查询,如果没有查询到再去服务器上查询
	 */
	private void queryCities() {
		cityList = coolWeatherDB.loadCity(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
		} else {
			queryFormServer(selectedProvince.getProvinceCode(), "city");
		}
	}

	/**
	 * 查询选中市内所有的县,优先从数据库中查询,如果没有查询到再去服务器行查询
	 */
	private void queryCounties() {
		countyList = coolWeatherDB.loadCounty(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
		} else {
			queryFormServer(selectedCity.getCityCode(), "county");
		}
	}

	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据
	 */
	private void queryFormServer(final String code, final String type) {
		String address;
		// 根据传入的参数来拼装查询地址
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		// 调用HttpUtil的sendHttpRequest()方法来向服务器发送请求
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			// 响应的数据会回调到onFinish()中
			@Override
			public void onFinish(String response) {
				boolean result = false;
				// 调用Utilit的handleProvinceResponse()方法来解析和处理服务器返回的数据，并存储到数据库中
				if ("province".equals(type)) {
					result = Utility.handleProvinceResponse(coolWeatherDB,
							response);
					// 调用Utilit的handleCitiesResponse()方法来解析和处理服务器返回的数据,并存储到数据库中
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(coolWeatherDB,
							response, selectedProvince.getId());
					// 调用Utilit的handleCountiesResponse()方法来解析和处理服务器返回的数据,并存储到数据库中
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(coolWeatherDB,
							response, selectedCity.getId());
				}
				if (result) {
					// 通过runOnUiThread()方法回到主线程处理逻辑,runOnUiThread()方法来实现从子线程切换到主线程
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();// 解析和处理完数据之后，再次调用queryProvinces()方法来重新加载省级数据
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// 通过runOnUiThread()方法回到主线程处理逻辑
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", 0)
								.show();
					}
				});
			}
		});
	}

	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载....");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 捕捉back键,根据当前级别来判断，此时应该返回市列表，省列表，还是直接退出
	 */
	@Override
	// 覆盖默认Back键的行为
	public void onBackPressed() {
		if (cityList != null) {
			queryProvinces();
			cityList = null;
		} else if (countyList != null) {
			queryCities();
			countyList = null;
		} else {
			finish();
		}

	}

}
