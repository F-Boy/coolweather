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
	 * ʡ�б�
	 */
	private List<Province> provinceList;

	/**
	 * ���б�
	 */
	private List<City> cityList;

	/**
	 * ���б�
	 */
	private List<County> countyList;

	/**
	 * ѡ�е�ʡ��
	 */
	private Province selectedProvince;

	/**
	 * ѡ�еĳ���
	 */
	private City selectedCity;

	/**
	 * ��ǰѡ�еļ���
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);// ��ȡListView��ʵ��
		titleText = (TextView) findViewById(R.id.title_text);// ��ȡTextView��ʵ��
		// ��ʼ������������
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		// ����ΪlistView��������
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);// ���CoolWeatherDB��ʵ��
		// ��listView���õ���¼�
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
		queryProvinces();// ����ʡ������
	}

	/**
	 * ��ѯȫ�����е�ʡ�����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ
	 */
	private void queryProvinces() {
		// ����loadProvinces()���������ݿ��ж�ȡʡ������,
		// ��������˾�ֱ�ӽ�������ʾ��������,
		// ���û�ж�ȡ��������queryFormServer()�������ӷ������ϻ�ȡ����
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {

				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
		} else {
			queryFormServer(null, "province");
		}
	}

	/**
	 * ��ѯѡ��ʡ�����е��У����ȴ����ݿ��ѯ,���û�в�ѯ����ȥ�������ϲ�ѯ
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
	 * ��ѯѡ���������е���,���ȴ����ݿ��в�ѯ,���û�в�ѯ����ȥ�������в�ѯ
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
	 * ���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯʡ��������
	 */
	private void queryFormServer(final String code, final String type) {
		String address;
		// ���ݴ���Ĳ�����ƴװ��ѯ��ַ
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		// ����HttpUtil��sendHttpRequest()���������������������
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			// ��Ӧ�����ݻ�ص���onFinish()��
			@Override
			public void onFinish(String response) {
				boolean result = false;
				// ����Utilit��handleProvinceResponse()�����������ʹ������������ص����ݣ����洢�����ݿ���
				if ("province".equals(type)) {
					result = Utility.handleProvinceResponse(coolWeatherDB,
							response);
					// ����Utilit��handleCitiesResponse()�����������ʹ������������ص�����,���洢�����ݿ���
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(coolWeatherDB,
							response, selectedProvince.getId());
					// ����Utilit��handleCountiesResponse()�����������ʹ������������ص�����,���洢�����ݿ���
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(coolWeatherDB,
							response, selectedCity.getId());
				}
				if (result) {
					// ͨ��runOnUiThread()�����ص����̴߳����߼�,runOnUiThread()������ʵ�ִ����߳��л������߳�
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();// �����ʹ���������֮���ٴε���queryProvinces()���������¼���ʡ������
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
				// ͨ��runOnUiThread()�����ص����̴߳����߼�
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", 0)
								.show();
					}
				});
			}
		});
	}

	/**
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���....");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/**
	 * ��׽back��,���ݵ�ǰ�������жϣ���ʱӦ�÷������б���ʡ�б�������ֱ���˳�
	 */
	@Override
	// ����Ĭ��Back������Ϊ
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