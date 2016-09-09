package com.android.imeetem.asynctasks;

import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;

import android.os.AsyncTask;
import android.util.Log;

public class IMeetEmFinalizeProfileAsync extends AsyncTask<String, Integer, String> {

	private String TAG = "IMeetEmFinalizeProfileAsync";
	private IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl.getInstance();
	private int memId;
	private String gender;
	/*private String distance;
	private String young;
	private String old;*/
	private String eyes;
	private String hair;
	private String ethnicity;
	private String education;
	private String wants_kids;
	private String has_kids;
	private String bday,email;
	
	//Constructor. Not sure we need it, but lets define it anyway
	public IMeetEmFinalizeProfileAsync(String memId, String eyes, String hair, String ethnicity, String education, String wants_kids, String has_kids, String email, String bday){
		this.memId = Integer.valueOf(memId);
		/*this.distance = distance;
		this.young = young;
		this.old = old;*/
		this.eyes = eyes;
		this.hair = hair;
		this.ethnicity = ethnicity;
		this.education = education; 
		this.has_kids = has_kids;
		this.wants_kids = wants_kids;
		this.bday = bday;
		this.email = email;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Changing the User's search  "+memId);
		boolean result = util.updateFinalizeProfile(String.valueOf(memId), eyes,hair,education, ethnicity, wants_kids,has_kids, bday, email);
		if (result != false) {
			return "Update Successful";
		} else {
			return null;
		}
	}
	@Override
	protected void onPostExecute(String result) {
		if (result.equals("Update Successful")) {
			Log.d(TAG, "Successfully updated the search screen");
		} else {
			Log.d(TAG,"Result was null, something bad happened.");
		}
		System.out.println("DONE: Got result onPostExecute: "+result);
	}
	
}

