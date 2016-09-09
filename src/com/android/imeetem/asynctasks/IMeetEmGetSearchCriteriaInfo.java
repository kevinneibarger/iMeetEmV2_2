package com.android.imeetem.asynctasks;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.android.imeetem.services.beans.IMEETEmSearchResultsBean;
import com.android.imeetem.services.beans.IMeetEmSearchCriteriaInfoBean;
import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;

public class IMeetEmGetSearchCriteriaInfo extends AsyncTask<String, String, String> {

	private String TAG = "IMeetEmSearchCriteriaInfo";
	private IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl.getInstance();
	private int memId;
	private List<IMeetEmSearchCriteriaInfoBean> resultList;
	private String results;
	
	//Constructor. Not sure we need it, but lets define it anyway
	public IMeetEmGetSearchCriteriaInfo(String memId){
		this.memId = Integer.valueOf(memId);
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Changing the User's search  "+memId);
		try {
			resultList = util.getSearchUpdateCritera(String.valueOf(memId));
			//results = util.getResultsSearch(String.valueOf(memId));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList.toString();
	}
	@Override
	protected void onPostExecute(String result) {
		if (!result.equals(null)) {
			Log.d(TAG, "Something went wrong");
		} else {
			Log.d(TAG,"Got results "+result);
		}
		System.out.println("DONE: Got result onPostExecute: "+result);
	}
	
}