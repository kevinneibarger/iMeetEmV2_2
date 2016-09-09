/**
 * 
 */
package com.android.imeetem.services;

import com.android.imeetem.services.beans.IMEETEmMemberInfoBean;

/**
 * @author kevinscomp
 *
 */
public interface IMEETEmServicesUtil {
	
	/**
	 * This method will call mem_add.php to add the new user to the iMeetEm DB
	 *  
	 * @param userGraphJSON - A JSON Object representing the users information (might want to change to 
	 * individual session variables for first release)
	 * @return An integer representing the members ID
	 */
	//public int addNewMember(JSONObject userGraphJSON);
	
	/**
	 * This member will add a new member to the database
	 * 
	 * @param memberInfo - A JavaBean with the new users information
	 * @return An integer representing the users IMeetEm id
	 */
	public int addUser(IMEETEmMemberInfoBean memberInfo);
	
	//public int addNewMember(String fbId, String memEmail, String memImageName, String bYear, String bMonth, String bDay, String fName, String lName, String gender, String latitude, String longitude);
	
	/**
	 * This method will call mem_check.php to check the members status
	 * 
	 * 	0 - New User
	 *  1 - Existing User
	 *  2 - Blocked User
	 *  
	 * @param fbId - A String representing the users FB Id (check against ID in DB)
	 * @return An integer, 0,1 or 2
	 */
	public int checkMemberStatus(String fbId);
	
	
	// Add more DB Util methods as needed...
}
