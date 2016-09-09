package com.android.imeetem.asynctasks;

import android.os.AsyncTask;

import com.android.imeetem.services.beans.IMEETEmMemberInfoBean;
import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;

public class IMeetEmAddNewMember extends AsyncTask<IMEETEmMemberInfoBean, Integer, String> {
	
	
	@Override
	protected String doInBackground(IMEETEmMemberInfoBean... params) {
		
		IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl.getInstance();
		return String.valueOf(util.addUser(params[0]));
		
	}

	@Override
	protected void onPostExecute(String result) {
		System.out.println("DONE: Add Member Got result onPostExecute: "+result);
	}
}

