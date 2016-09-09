//package com.android.imeetem.asynctasks;
//
//import android.os.AsyncTask;
//
//import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;
//
//public class IMeetEmShowResultsSearch extends AsyncTask<String, Integer, String> {
//	@Override
//	protected String doInBackground(String... params) {
//		
//		IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl.getInstance();
//		return String.valueOf(util.getResultsSearch(params[0]));
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//		System.out.println("DONE: Got result onPostExecute: "+result);
//	}
//}
