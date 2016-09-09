/**
 * 
 */
package com.android.imeetem.services.junitest;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;

/**
 * @author kevinscomp
 *
 */
public class EventHorizonDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl.getInstance();
		JSONObject obj = new JSONObject();
		
		//util.saveNewUser(obj);
		
		util.checkMemberStatus("1151573558");
		//util.addNewMember(obj);
		//util.loginUser("1", "-45.909090", "21.99992332");

	}
	
	private static void createTempJSONObj() throws JSONException {
		
		JSONObject obj = new JSONObject();
		
		// JSON Object for sending to PHP
		obj.put("fb_id", "034342432445");
		obj.put("mem_email", "someone@email.com");
		obj.put("image_name", "/img/main/main_img.jpg");
		obj.put("b_year", "1985");
		obj.put("b_day", "25");
		obj.put("b_month", "11");
		obj.put("fname", "Joe");
		obj.put("lname", "Smith");
		obj.put("gender", "M");
		obj.put("latitude", "-34.34342432445");
		obj.put("longitude", "56.034342432445");
		
	}

}
