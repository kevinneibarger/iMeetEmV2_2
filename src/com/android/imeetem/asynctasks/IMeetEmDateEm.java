package com.android.imeetem.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;

public class IMeetEmDateEm extends AsyncTask<String, Integer, String> {
		
		private String TAG = "IMeetEmDateEm";
		private IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl.getInstance();
		private int memId;
		private String impactId;
		private boolean fromMatchInfoPage;
		
		public IMeetEmDateEm(String memId, String impactId, boolean fromMatchInfoPage) {
			Log.d(TAG, "Wanna Date EM!!");
			this.impactId = impactId;
			this.memId = Integer.valueOf(memId);
			this.fromMatchInfoPage = fromMatchInfoPage;
		}
		
		@Override
		protected String doInBackground(String... params) {
			Log.d(TAG, "Dating Em with memId: "+memId+" Impact Id: "+impactId);
			return util.passEmWinkEmDateEmBtn(String.valueOf(memId), impactId, "Dateem", this.fromMatchInfoPage);
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (!result.equals(null)) {
				Log.d(TAG, "Successfully updated and Date this match");
			} else {
				Log.d(TAG,"Result was null, something bad happened.");
			}
			System.out.println("DONE: Got result onPostExecute: "+result);
		}
	}
