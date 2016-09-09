/**
 * 
 */
package com.android.imeetem.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;

/**
 * @author kevinscomp
 * 
 */
public class IMeetEmRemoveOneTimeMsg extends AsyncTask<String, Integer, String> {

	private String TAG = "IMeetEmRemoveOneTimeMsg";
	private IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl
			.getInstance();
	private String fromMemId;
	private String toMemId;

	public IMeetEmRemoveOneTimeMsg(String fromMemId, String toMemId) {
		Log.d(TAG, "Removing a 1 time Message!");
		this.fromMemId = fromMemId;
		this.toMemId = toMemId;
	}

	@Override
	protected String doInBackground(String... params) {
		return String.valueOf(util.removeOnetimeMessage(fromMemId, toMemId));
	}

	@Override
	protected void onPostExecute(String result) {
		if (!result.equals(null) || !result.equals(null)
				&& !result.equals("-1")) {
			Log.d(TAG,
					"Successfully removed the one time message from this match");
		} else {
			Log.d(TAG, "Result was null or -1, something bad happened.");
		}
		System.out.println("DONE: Got result onPostExecute: " + result);
	}
}
