package com.android.imeetem.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;

public class IMeetEmPassEm extends AsyncTask<String, Integer, String> {
	
	private String TAG = "IMeetEmPassEm";
	private IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl.getInstance();
	private int memId;
	private String impactId;
	private boolean fromMatchInfoPage;
	
	public IMeetEmPassEm(String memId, String impactId, boolean fromMatchInfoPage) {
		Log.d(TAG, "Passing on EM!!");
		this.impactId = impactId;
		this.memId = Integer.valueOf(memId);
		this.fromMatchInfoPage = fromMatchInfoPage;
	}
	
	@Override
	protected String doInBackground(String... params) {
		Log.d(TAG, "Passing Em with memId: "+memId+" Impact Id: "+impactId);
		return util.passEmWinkEmDateEmBtn(String.valueOf(memId), impactId, "Passem", this.fromMatchInfoPage);
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (!result.equals(null)) {
			Log.d(TAG, "Successfully updated and Passed on this match");
		} else {
			Log.d(TAG,"Result was null, something bad happened.");
		}
		System.out.println("DONE: Got result onPostExecute: "+result);
	}
}
