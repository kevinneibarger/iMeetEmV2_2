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
public class IMeetEmSendOneTimeMsg extends AsyncTask<String, Integer, String> {

	private String TAG = "IMeetEmSendOneTimeMsg";
	private IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl
			.getInstance();
	private String fromMemId;
	private String toMemId;
	private String message;

	public IMeetEmSendOneTimeMsg(String fromMemId, String toMemId,
			String message) {
		Log.d(TAG, "Sending a ONE TIME Message!");
		this.fromMemId = fromMemId;
		this.toMemId = toMemId;
		this.message = message;
	}

	@Override
	protected String doInBackground(String... params) {
		return String.valueOf(util.sendOnetimeMessage(fromMemId, toMemId,
				message));
	}

	@Override
	protected void onPostExecute(String result) {
		if (!result.equals(null) || !result.equals(null)
				&& !result.equals("-1")) {
			Log.d(TAG, "Successfully Sent a 1 time message to this match");
		} else {
			Log.d(TAG, "Result was null or -1, something bad happened.");
		}
		System.out.println("DONE: Got result onPostExecute: " + result);
	}
}
