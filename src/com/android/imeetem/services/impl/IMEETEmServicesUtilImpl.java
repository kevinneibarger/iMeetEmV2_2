/**
 * 
 */
package com.android.imeetem.services.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.imeetem.services.IMEETEmServicesUtil;
import com.android.imeetem.services.beans.IMEETEmChatMessagesBean;
import com.android.imeetem.services.beans.IMEETEmFriendsListBean;
import com.android.imeetem.services.beans.IMEETEmMemberInfoBean;
import com.android.imeetem.services.beans.IMEETEmMessagesListBean;
import com.android.imeetem.services.beans.IMEETEmSearchResultsBean;
import com.android.imeetem.services.beans.IMEETEmSystemCheckBean;
import com.android.imeetem.services.beans.IMeetEmSearchCriteriaInfoBean;
import com.android.imeetem.util.CustomHttpClient;
import com.android.imeetem.util.IMEETEmConstants;
import com.android.imeetem.util.IMEETEmUtil;
import com.android.imeetem.util.TimeGeneralizer;

/**
 * @author kevinscomp
 * 
 */
public class IMEETEmServicesUtilImpl implements IMEETEmServicesUtil {

	private SharedPreferences sharedPreferences;

	private IMEETEmServicesUtilImpl() {
	}

	private IMEETEmServicesUtilImpl(SharedPreferences sharedPreferences) {

		if (sharedPreferences != null) {
			this.sharedPreferences = sharedPreferences;
		}
	}

	public static IMEETEmServicesUtilImpl getInstance(
			SharedPreferences sharedPreferences) {
		return new IMEETEmServicesUtilImpl(sharedPreferences);
	}

	public static IMEETEmServicesUtilImpl getInstance() {
		return new IMEETEmServicesUtilImpl();
	}

	@Override
	public int checkMemberStatus(String fbId) {

		String response = null;
		int status = 0;

		/*** Un comment to Test ***/
		/*
		 * IMEETEmUtil util = IMEETEmUtil.getInstance(); String phpServiceURI =
		 * null; List<String> params = new ArrayList<String>();
		 */

		try {
			response = CustomHttpClient
					.executeHttpGet("http://imeetem.com/services/mem_check.php?fb_id="
							+ fbId);

			/*** Un comment to Test ***/
			/*
			 * // Add the parameters if there are any
			 * params.add(URLEncoder.encode(fbId, "UTF-8")); // Build the PHP
			 * Service URI phpServiceURI =
			 * util.getPHPServiceURI(IMEETEmConstants.PHP_SRVC_KEY_MEM_CHECK,
			 * params); // Call the Service response =
			 * CustomHttpClient.executeHttpGet(phpServiceURI);
			 * System.out.println
			 * ("\n -------- Calling Member Check Status ------------- \n");
			 * System.out.println(phpServiceURI);
			 * System.out.println("\n -------- DONE Calling ------------- \n");
			 */

			// store the result returned by PHP script that runs MySQL query
			String result = response.trim();

			status = Integer.parseInt(result);
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
		}

		return status;
	}

	/**
	 * Add a new user to the table with credentials from Facebook
	 * 
	 * @param memberInfo
	 */
	public int addUser(IMEETEmMemberInfoBean memberInfo) {

		String response = null;

		/*** Un comment to Test ***/
		/*
		 * IMEETEmUtil util = IMEETEmUtil.getInstance(); String phpServiceURI =
		 * null; List<String> params = new ArrayList<String>();
		 */

		if (memberInfo != null) {

			try {

				// Add the user to the table with info from Facebook.
				/*** Un comment to Test ***/
				// Add the parameters if there are any, in this case there
				// are...
				/*
				 * params.add(URLEncoder.encode(memberInfo.getMemFBId(),
				 * "UTF-8"));
				 * params.add(URLEncoder.encode(memberInfo.getEmailId(),
				 * "UTF-8"));
				 * params.add(URLEncoder.encode(memberInfo.getMemImage(),
				 * "UTF-8"));
				 * params.add(URLEncoder.encode(String.valueOf(memberInfo
				 * .getBirthYear()), "UTF-8"));
				 * params.add(URLEncoder.encode(String
				 * .valueOf(memberInfo.getBirthDay()), "UTF-8"));
				 * params.add(URLEncoder
				 * .encode(String.valueOf(memberInfo.getBirthMonth()),
				 * "UTF-8"));
				 * params.add(URLEncoder.encode(memberInfo.getMemFName(),
				 * "UTF-8"));
				 * params.add(URLEncoder.encode(memberInfo.getMemLName(),
				 * "UTF-8"));
				 * params.add(URLEncoder.encode(memberInfo.getMemGender(),
				 * "UTF-8"));
				 * params.add(URLEncoder.encode(memberInfo.getMemLatitude(),
				 * "UTF-8"));
				 * params.add(URLEncoder.encode(memberInfo.getMemLongitude(),
				 * "UTF-8"));
				 * 
				 * // Build the PHP Service URI phpServiceURI =
				 * util.getPHPServiceURI(IMEETEmConstants.PHP_SRVC_KEY_MEM_ADD,
				 * params); // Call the Service response =
				 * CustomHttpClient.executeHttpGet(phpServiceURI);
				 * System.out.println
				 * ("\n -------- Calling Add Member Service ------------- \n");
				 * System.out.println(phpServiceURI);
				 * System.out.println("\n -------- DONE Calling ------------- \n"
				 * );
				 */

				response = CustomHttpClient
						.executeHttpGet("http://imeetem.com/services/mem_add.php?fb_id="
								+ memberInfo.getMemFBId()
								+ "&mem_email="
								+ URLEncoder.encode(memberInfo.getEmailId(),
										"UTF-8")
								+ "&image_name="
								+ URLEncoder.encode(memberInfo.getMemImage(),
										"UTF-8")
								+ "&b_year="
								+ URLEncoder.encode(String.valueOf(memberInfo
										.getBirthYear()), "UTF-8")
								+ "&b_day="
								+ URLEncoder.encode(String.valueOf(memberInfo
										.getBirthDay()), "UTF-8")
								+ "&b_month="
								+ URLEncoder.encode(String.valueOf(memberInfo
										.getBirthMonth()), "UTF-8")
								+ "&fname="
								+ URLEncoder.encode(memberInfo.getMemFName(),
										"UTF-8")
								+ "&lname="
								+ URLEncoder.encode(memberInfo.getMemLName(),
										"UTF-8")
								+ "&gender="
								+ URLEncoder.encode(memberInfo.getMemGender(),
										"UTF-8")
								+ "&latitude="
								+ URLEncoder.encode(
										memberInfo.getMemLatitude(), "UTF-8")
								+ "&longitude="
								+ URLEncoder.encode(
										memberInfo.getMemLongitude(), "UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("log_tag", "Error!!" + e.toString());
			}
		}

		if (response != null) {
			System.out.println("\n\n -- Returning Response: " + response
					+ " as an INT! -- \n\n");
			return Integer.parseInt(response);
		} else {
			return -1; // An error has occurred, and/or the user has not been
						// added!
		}
	}

	/**
	 * After new user add or the person has to log in, run this service
	 * 
	 * @param memInfo
	 * @param lat
	 * @param lon
	 */
	public void memLogin(IMEETEmMemberInfoBean memInfo) {
		String response = null;

		/*** Un comment to Test ***/
		/*
		 * IMEETEmUtil util = IMEETEmUtil.getInstance(); String phpServiceURI =
		 * null; List<String> params = new ArrayList<String>();
		 */

		if (memInfo != null) {
			// Add the user to the table with info from Facebook.
			try {

				/*** Un comment to Test ***/
				/*
				 * // Add the parameters if there are any, in this case there
				 * are... params.add(URLEncoder.encode(memInfo.getMemFBId(),
				 * "UTF-8"));
				 * params.add(URLEncoder.encode(memInfo.getMemLatitude(),
				 * "UTF-8"));
				 * params.add(URLEncoder.encode(memInfo.getMemLongitude(),
				 * "UTF-8")); // Build the PHP Service URI phpServiceURI =
				 * util.getPHPServiceURI
				 * (IMEETEmConstants.PHP_SRVC_KEY_MEM_LOGIN, params); // Call
				 * the Service response =
				 * CustomHttpClient.executeHttpGet(phpServiceURI);
				 * System.out.println("\n -------- Calling ------------- \n");
				 * System.out.println(phpServiceURI);
				 * System.out.println("\n -------- DONE Calling ------------- \n"
				 * );
				 */

				response = CustomHttpClient
						.executeHttpGet("http://imeetem.com/services/mem_login.php?face_id="
								+ memInfo.getMemFBId()
								+ "&latitude="
								+ URLEncoder.encode(memInfo.getMemLatitude(),
										"UTF-8")
								+ "&longitude="
								+ URLEncoder.encode(memInfo.getMemLongitude(),
										"UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("log_tag", "Error!!" + e.toString());
			}
		}
		if (response != null) {
			System.out
					.println("\n\n -- The memLogin service finished successfully! \n\n");
		} else {
			System.out.println("\n\n -- The memLogin service failed! \n\n");
		}

	}

	/**
	 * This method will get the 6 matches from the DB for the current user. This
	 * is the first screen seen by the user after the login and is loaded as
	 * long as the users status is 1 and matches are available. If the response
	 * is null from the service an error message is set.
	 * 
	 * @param fbId
	 *            A String representing the Facebook ID used to store the user
	 * @return A List of IMEETEmSearchResultsBean Objects with matches if
	 *         applicable
	 */
	public List<IMEETEmSearchResultsBean> getResultsSearchFromJSON(String fbId) {

		String response = null;
		String result = null;
		List<IMEETEmSearchResultsBean> results = new ArrayList<IMEETEmSearchResultsBean>();

		/*** Un comment to Test ***/
		IMEETEmUtil util = IMEETEmUtil.getInstance();
		String phpServiceURI = null;
		List<String> params = new ArrayList<String>();

		try {

			/*** Un comment to Test ***/
			// Add the parameters if there are any, in this case there are...
			params.add(URLEncoder.encode(fbId, "UTF-8"));
			// Build the PHP Service URI
			phpServiceURI = util.getPHPServiceURI(
					IMEETEmConstants.PHP_SRVC_KEY_MEM_SEARCH, params);
			// Call the Service
			response = CustomHttpClient.executeHttpGet(phpServiceURI);
			System.out
					.println("\n -------- Calling Results PHP to get 6 Matches ------------- \n");
			System.out.println(phpServiceURI);
			System.out.println("\n -------- DONE Calling ------------- \n");

			// response =
			// CustomHttpClient.executeHttpGet("http://imeetem.com/services/results.php?ptype=mem_search&data1="+fbId);

			if (response.equals("null")) {

				System.out.println("************** RESPONSE NULL: " + result
						+ " *****************************************");

				IMEETEmSearchResultsBean bean = IMEETEmSearchResultsBean
						.getInstance();
				bean.setNoMatches(IMEETEmConstants.NO_MATCHES_FOR_YOU);
				bean.setSearchResultMemId(fbId);
				results.add(0, bean);
			} else {
				// store the result returned by PHP script that runs MySQL query
				result = response.toString();

				if (!result.trim().equalsIgnoreCase(
						IMEETEmConstants.NO_MATCHES_FOR_YOU)) {

					// Store the result String from the PHP script into
					// SharedPreferences for later use
					if (sharedPreferences != null) {
						sharedPreferences
								.edit()
								.putString(
										IMEETEmConstants.SIX_MATCHES_RESULTS_JSON,
										result).commit();
					} else {
						System.out
								.println("\n\n\n $$$$$$$$$$$$ - SharedPrefs was passed in as NULL!!! - $$$$$$$$$$$$$$$$$ \n\n\n");
					}

					JSONArray jArray = new JSONArray(result);

					for (int i = 0; i < jArray.length(); i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						IMEETEmSearchResultsBean bean = IMEETEmSearchResultsBean
								.getInstance();
						bean.setSearchResultStatus(json_data
								.getString(IMEETEmConstants.SR_MATCH_SEARCH_STATUS));
						bean.setSearchResultMemId(json_data
								.getString(IMEETEmConstants.SR_MATCH_MEM_ID));
						bean.setSearchResultMemFbId(json_data
								.getString(IMEETEmConstants.SR_MATCH_MEM_FB_ID));
						bean.setSearchResultMemAcctStatus(json_data
								.getString(IMEETEmConstants.SR_MATCH_MEM_ACCT_STATUS));
						bean.setSearchResultLastActivity(json_data
								.getString(IMEETEmConstants.SR_MATCH_LAST_ACTIVITY));
						bean.setSearchResultMemImg(json_data
								.getString(IMEETEmConstants.SR_MATCH_MEM_IMAGE));
						bean.setSearchResultMemImgLoc(json_data
								.getString(IMEETEmConstants.SR_MATCH_MEM_IMAGE_LOC));
						bean.setSearchResultMemImgLocFld(json_data
								.getString(IMEETEmConstants.SR_MATCH_MEM_IMAGE_FLD));
						bean.setSearchResultMemName(json_data
								.getString(IMEETEmConstants.SR_MATCH_MEM_NAME));
						bean.setSearchResultMemAge(json_data
								.getString(IMEETEmConstants.SR_MATCH_AGE));
						bean.setSearchResultMemHair(json_data
								.getString(IMEETEmConstants.SR_MATCH_HAIR));
						bean.setSearchResultMemEyes(json_data
								.getString(IMEETEmConstants.SR_MATCH_EYES));
						bean.setSearchResultMemHasKids(json_data
								.getString(IMEETEmConstants.SR_MATCH_HAS_KIDS));
						bean.setSearchResultMemWantsKids(json_data
								.getString(IMEETEmConstants.SR_MATCH_WANTS_KIDS));
						bean.setSearchResultMemEducation(json_data
								.getString(IMEETEmConstants.SR_MATCH_EDUCATION));
						bean.setSearchResultMemEthnicity(json_data
								.getString(IMEETEmConstants.SR_MATCH_ETHNICITY));
						bean.setSearchResultMemDistance(json_data
								.getString(IMEETEmConstants.SR_MATCH_DISTANCE));

						results.add(bean);

					}
				}
			}

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
			Log.e("log_tag", "Error parsing data " + e.toString());

		}

		return results;
	}

	/**
	 * This method will get the requests/friends list (called when user clicks
	 * on "friends" in top nav menu") as a JSON Object from the DB via the PHP
	 * script. This JSON Object is moved into a Java Bean for easier access. If
	 * the JSON response returns null then the return List is setup with 1
	 * record that shows a status of "No Requests" or something to that affect.
	 * 
	 * @param memId
	 *            A String representing the users id, this is used to get all
	 *            requests
	 * @return A List of IMEETEmFriendsListBean Objects in a known state, null
	 *         if an Exception has occurred
	 * @throws Exception
	 *             is thrown if the PHP script returns null.
	 */
	public List<IMEETEmFriendsListBean> getFriendsListFromJSON(String memId)
			throws Exception {

		String response = null;
		String result = null;
		List<IMEETEmFriendsListBean> results = new ArrayList<IMEETEmFriendsListBean>();
		List<IMEETEmFriendsListBean> listWithSections = new ArrayList<IMEETEmFriendsListBean>();

		IMEETEmUtil util = IMEETEmUtil.getInstance();
		String phpServiceURI = null;
		List<String> params = new ArrayList<String>();

		try {

			// Add the parameters if there are any
			params.add(URLEncoder.encode(memId, "UTF-8"));
			// Build the PHP Service URI
			phpServiceURI = util.getPHPServiceURI(
					IMEETEmConstants.PHP_SRVC_KEY_REQ_MEM_VALUES, params);
			// Call the Service
			response = CustomHttpClient.executeHttpGet(phpServiceURI);
			System.out
					.println("\n -------- Calling Friends/Requests List ------------- \n");
			System.out.println(phpServiceURI);
			System.out.println("\n -------- DONE Calling ------------- \n");

			// response =
			// CustomHttpClient.executeHttpGet("http://imeetem.com/services/req_mem_values.php?mem_id="+memId);

			// if (response.equals("null")) {
			// IMEETEmFriendsListBean bean = IMEETEmFriendsListBean
			// .getInstance();
			// bean.setMemMsgStatus(IMEETEmConstants.NO_DATE_WINK_ONETIME_REQUESTS);
			// bean.setSectionHeader(false);
			// results.add(0, bean);
			// }

			if (!response.equals("null")) {
				// store the result returned by PHP script that runs MySQL query
				result = response.toString();

				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);

					IMEETEmFriendsListBean bean = IMEETEmFriendsListBean
							.getInstance();
					bean.setGroupType(json_data
							.getString(IMEETEmConstants.FL_GROUP_TYPE_REC));
					bean.setWinkEmAvail(json_data
							.getString(IMEETEmConstants.FL_WINKEM_AVL_REC));
					bean.setDateRequested(json_data
							.getString(IMEETEmConstants.FL_DATE_REQ_REC));
					bean.setMemId(json_data
							.getString(IMEETEmConstants.FL_MEM_ID_REC));
					bean.setMemImageUrl(json_data
							.getString(IMEETEmConstants.FL_MEM_IMG_REC));
					bean.setMemImageLoc(json_data
							.getString(IMEETEmConstants.FL_MEM_IMG_LOC_REC));
					bean.setMemImageLocFolder(json_data
							.getString(IMEETEmConstants.FL_MEM_IMG_FLD_REC));
					bean.setMemFname(json_data
							.getString(IMEETEmConstants.FL_MEM_NAME_REC));
					bean.setMemMsgDetail(json_data
							.getString(IMEETEmConstants.FL_MESS_DETAIL_REC));
					bean.setMemMsgStatus(json_data
							.getString(IMEETEmConstants.FL_MESS_STATUS_REC));
					bean.setMemAge(json_data
							.getString(IMEETEmConstants.FL_AGE_REC));

					bean.setMemberHairColor(json_data
							.getString(IMEETEmConstants.SR_MATCH_HAIR));
					bean.setMemberEyeColor(json_data
							.getString(IMEETEmConstants.SR_MATCH_HAIR));
					bean.setMemberHasKids(("Yes".equals(json_data
							.getString(IMEETEmConstants.SR_MATCH_HAS_KIDS)) ? true
							: false));
					bean.setMemberWantsKids(("Yes".equals(json_data
							.getString(IMEETEmConstants.SR_MATCH_WANTS_KIDS)) ? true
							: false));
					bean.setMemberEducation(json_data
							.getString(IMEETEmConstants.SR_MATCH_EDUCATION));
					bean.setMemberEthnicity(json_data
							.getString(IMEETEmConstants.SR_MATCH_ETHNICITY));
					bean.setMemDistance(json_data
							.getString(IMEETEmConstants.SR_MATCH_DISTANCE));

					// System.out
					// .println("\n NEW FIEILDS For Friend Requests! \n\n");
					// System.out.println("Eyes Color: "
					// + json_data.getString("MEM_EYES"));
					// System.out.println("Hair Color: "
					// + json_data.getString("MEM_HAIR"));
					// System.out.println("Kids Have: "
					// + json_data.getString("MEM_KIDS_HAVE"));
					// System.out.println("Kids Want: "
					// + json_data.getString("MEM_KIDS_WANT"));
					// System.out.println("Ethnicity: "
					// + json_data.getString("MEM_ETHNICITY"));
					// System.out.println("Education: "
					// + json_data.getString("MEM_EDUCATION"));
					// System.out
					// .println("Distance: "
					// + json_data
					// .getString(IMEETEmConstants.SR_MATCH_DISTANCE));
					// System.out.println("\n END NEW FIELDS \n\n");

					bean.setSectionHeader(false);
					results.add(bean);

				}
			}

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
			Log.e("log_tag", "Error parsing data " + e.toString());
			throw new Exception(
					"An Error Has OCCURRED Parsing JSON Object or running PHP");
		}

		if (results != null && results.size() > 0) {

			if (results.size() == 1) {

				if (results.get(0).getMemMsgStatus()
						.equals(IMEETEmConstants.NO_DATE_WINK_ONETIME_REQUESTS)) {
					return results;
				} else {
					listWithSections = addSectionBreaks(results);
				}
			} else {
				listWithSections = addSectionBreaks(results);
			}

		} else {

			// We need to return an empty list if no friend requests occur.
			return new ArrayList<IMEETEmFriendsListBean>();

			// throw new Exception(
			// "**** An Exception Has OCCURRED getting friends/requests list, DB returned NULL ******");
		}

		return listWithSections;

	}

	/**
	 * This method will add the section breaks to the requests/friends list,
	 * separating the WinkEm, DateEm and 1 time message requests sections. This
	 * makes things much easier when displaying a List separated by headers.
	 * 
	 * @param friendsList
	 *            A List of IMEETEmFriendsListBean Objects in a known state.
	 * @return A List of IMEETEmFriendsListBean Objects in a known state,
	 *         complete with section breaks.
	 */
	private List<IMEETEmFriendsListBean> addSectionBreaks(
			List<IMEETEmFriendsListBean> friendsList) {

		List<IMEETEmFriendsListBean> listWithSections = new ArrayList<IMEETEmFriendsListBean>();
		List<IMEETEmFriendsListBean> winks = new ArrayList<IMEETEmFriendsListBean>();
		List<IMEETEmFriendsListBean> dateReqs = new ArrayList<IMEETEmFriendsListBean>();
		List<IMEETEmFriendsListBean> oneTimeMsg = new ArrayList<IMEETEmFriendsListBean>();

		for (IMEETEmFriendsListBean friend : friendsList) {

			if (friend.getGroupType() != null
					&& friend.getGroupType().equalsIgnoreCase(
							IMEETEmConstants.FL_GT_WINKEM)) {
				winks.add(friend);
			} else if (friend.getGroupType() != null
					&& friend.getGroupType().equalsIgnoreCase(
							IMEETEmConstants.FL_GT_DATEEM)) {
				dateReqs.add(friend);
			} else if (friend.getGroupType() != null
					&& friend.getGroupType().equalsIgnoreCase(
							IMEETEmConstants.FL_GT_ONETIMEMSG)) {
				oneTimeMsg.add(friend);
			}
		}

		listWithSections
				.add(buildSectionBean(IMEETEmConstants.FL_SEC_WINK_HEADER));
		listWithSections.addAll(winks);
		listWithSections
				.add(buildSectionBean(IMEETEmConstants.FL_SEC_DATE_HEADER));
		listWithSections.addAll(dateReqs);
		listWithSections
				.add(buildSectionBean(IMEETEmConstants.FL_SEC_ONETIMEMSG_HEADER));
		listWithSections.addAll(oneTimeMsg);
		listWithSections
				.add(buildSectionBean(IMEETEmConstants.FL_SEC_END_LIST));

		return listWithSections;
	}

	/**
	 * This method simply sets an individual IMEETEmFriendsListBean's memFName
	 * to the appropriate section header title.
	 * 
	 * @param type
	 *            A String representing the type of section header to build
	 *            (move hard-coded stuff to IMEETEmConstants)
	 * @return
	 */
	private IMEETEmFriendsListBean buildSectionBean(String type) {
		IMEETEmFriendsListBean sectionBean = new IMEETEmFriendsListBean();
		sectionBean.setSectionHeader(true);

		if (type != null
				&& type.equalsIgnoreCase(IMEETEmConstants.FL_SEC_WINK_HEADER)) {
			sectionBean.setMemFname(IMEETEmConstants.FL_SEC_WINK_HEADER);
		} else if (type != null
				&& type.equalsIgnoreCase(IMEETEmConstants.FL_SEC_DATE_HEADER)) {
			sectionBean.setMemFname(IMEETEmConstants.FL_SEC_DATE_HEADER);
		} else if (type != null
				&& type.equalsIgnoreCase(IMEETEmConstants.FL_SEC_ONETIMEMSG_HEADER)) {
			sectionBean.setMemFname(IMEETEmConstants.FL_SEC_ONETIMEMSG_HEADER);
		} else {
			sectionBean.setMemFname(IMEETEmConstants.FL_SEC_END_LIST);
		}

		return sectionBean;
	}

	/**
	 * This is a simple service to get the member id for the search.
	 * 
	 * @param memFBId
	 * @return
	 */
	public int getMemId(String memFBId) {
		String response = null;
		int mem_id = 0;

		/*** Un comment to Test ***/
		/*
		 * IMEETEmUtil util = IMEETEmUtil.getInstance(); String phpServiceURI =
		 * null; List<String> params = new ArrayList<String>();
		 */

		try {

			/*** Un comment to Test ***/
			// Add the parameters if there are any, in this case there are...
			/*
			 * params.add(URLEncoder.encode(memFBId, "UTF-8")); // Build the PHP
			 * Service URI phpServiceURI =
			 * util.getPHPServiceURI(IMEETEmConstants.PHP_SRVC_KEY_REQ_MEM_BTN,
			 * params); // Call the Service response =
			 * CustomHttpClient.executeHttpGet(phpServiceURI);
			 * System.out.println("\n -------- Calling ------------- \n");
			 * System.out.println(phpServiceURI);
			 * System.out.println("\n -------- DONE Calling ------------- \n");
			 */

			response = CustomHttpClient
					.executeHttpGet("http://imeetem.com/services/getmem_id.php?face_id="
							+ memFBId);

			// store the result returned by PHP script that runs MySQL query
			String result = response.toString();

			// This code is in here now, but we may need to check it to see if
			// we even need a JSON object, when this
			// method returns a string.
			try {
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = jArray.getJSONObject(0);
				Log.i("log_tag", ", name: " + json_data.getInt("MEM_ID"));
				mem_id += json_data.getInt("MEM_ID");
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
			}

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
		}

		if (mem_id != 0) {
			Log.e("log_tag", "\n\n -- Returning Response: " + mem_id
					+ " as an INT! -- \n\n");
			return mem_id;
		} else {
			Log.e("log_tag", "Error or User not found! " + mem_id);
			return -1; // An error has occurred, and/or the user has not been
						// found!
		}
	}

	/**
	 * The PassEm Button Logic. We call the mem_btn.php with PassEm as the
	 * btname and member id OR we'll call the req_mem_btn if isFromMatchInfo is
	 * false (clicked a button from the requests/friends list)
	 * 
	 * @param memID
	 *            A String representing the current users ID
	 * @param faceId
	 *            A String representing the member id of the requestor (from
	 *            Match Info page)/requested (from Friends List Page)
	 * @param isFromMatchInfo
	 *            A boolean representing true if WinkEm/PassEm/DateEm button is
	 *            clicked from Match Info Page, false if clicked from Friends
	 *            Lists/Requests Page.
	 * @return A String representing "Success" of no errors occurred, "-1" if an
	 *         error occurred.
	 */
	public String passEmWinkEmDateEmBtn(String memID, String impactId,
			String btnName, boolean isFromMatchInfo) {

		String response = null;
		IMEETEmUtil util = IMEETEmUtil.getInstance();
		String phpServiceURI = null;
		List<String> params = new ArrayList<String>();

		try {

			// Add the parameters if there are any, in this case there are...
			params.add(0, URLEncoder.encode(btnName, "UTF-8"));
			params.add(1, URLEncoder.encode(memID, "UTF-8"));
			params.add(2, URLEncoder.encode(impactId, "UTF-8"));

			if (!isFromMatchInfo) {
				phpServiceURI = util.getPHPServiceURI(
						IMEETEmConstants.PHP_SRVC_KEY_REQ_MEM_BTN, params);
			} else {
				phpServiceURI = util.getPHPServiceURI(
						IMEETEmConstants.PHP_SRVC_KEY_MEM_BTN, params);
			}

			response = CustomHttpClient.executeHttpGet(phpServiceURI);
			System.out
					.println("\n -------- Calling Button Request Service for "
							+ (isFromMatchInfo ? "Match Info Page"
									: "Friends Request List")
							+ "------------- \n");
			System.out.println(phpServiceURI);
			System.out.println("\n -------- DONE Calling ------------- \n");

			if (!response.equals(null)) {
				System.out.println(response);
				return response;
			} else {
				System.out.println("An Error has Occurred!!");
				return "-1";
			}
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
		}
		return null;
	}

	/**
	 * The NextEm Button Logic. We call the nextem button and then get our new
	 * searches.
	 * 
	 * @param memID
	 * @param faceId
	 * @param log
	 * @param lati
	 * @return
	 */
	public List<IMEETEmSearchResultsBean> nextEmBtn(String memID,
			String faceId, String lati, String log) throws Exception {

		String response = null;
		String response2 = null;
		// String results = null;
		List<IMEETEmSearchResultsBean> results = new ArrayList<IMEETEmSearchResultsBean>();
		IMEETEmSearchResultsBean bean = IMEETEmSearchResultsBean.getInstance();

		// NEW Stuff, uncomment to test
		/*
		 * IMEETEmUtil util = IMEETEmUtil.getInstance(); String phpServiceURI =
		 * null; List<String> params = new ArrayList<String>();
		 */

		try {

			// NEW Stuff, uncomment to test
			// Call the NextEm Service first
			/*
			 * params.add(URLEncoder.encode("Nextem", "UTF-8"));
			 * params.add(URLEncoder.encode(memID, "UTF-8")); phpServiceURI =
			 * util.getPHPServiceURI(IMEETEmConstants.PHP_SRVC_KEY_MEM_BTN,
			 * params); response =
			 * CustomHttpClient.executeHttpGet(phpServiceURI);
			 * System.out.println
			 * ("\n -------- Calling Next Em Service------------- \n");
			 * System.out.println(phpServiceURI); System.out.println(
			 * "\n -------- DONE Calling Next Em Service ------------- \n");
			 * 
			 * params = new ArrayList<String>(); phpServiceURI = null;
			 * 
			 * // Update the DB by calling the User Update Service
			 * params.add(URLEncoder.encode(faceId, "UTF-8"));
			 * params.add(URLEncoder.encode(lati, "UTF-8"));
			 * params.add(URLEncoder.encode(log, "UTF-8")); phpServiceURI =
			 * util.
			 * getPHPServiceURI(IMEETEmConstants.PHP_SRVC_KEY_GET_USER_UPDATE,
			 * params); response2 =
			 * CustomHttpClient.executeHttpGet(phpServiceURI);
			 * System.out.println
			 * ("\n -------- Calling Next Em Service------------- \n");
			 * System.out.println(phpServiceURI); System.out.println(
			 * "\n -------- DONE Calling Next Em Service ------------- \n");
			 */

			response = CustomHttpClient
					.executeHttpGet("http://imeetem.com/services/mem_btn.php?btname=Nextem&MEM_ID="
							+ memID);
			response2 = CustomHttpClient
					.executeHttpGet("http://imeetem.com/services/get_user_update.php?face_id="
							+ faceId
							+ "&latitude="
							+ lati
							+ "&longitude="
							+ log);

			if (response2
					.equals("Sorry, Current Search Critieria Can Not Find Anyone")) {
				bean.setNoMatches(response2);
				results.add(bean);
			} else {
				// Response 2 should have the matches in it, so we should just
				// be able to call this and save the JSON Object

				JSONArray jArray = new JSONArray(response2);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					bean.setSearchResultStatus(json_data
							.getString(IMEETEmConstants.SR_MATCH_SEARCH_STATUS));
					bean.setSearchResultMemId(json_data
							.getString(IMEETEmConstants.SR_MATCH_MEM_ID));
					bean.setSearchResultMemFbId(json_data
							.getString(IMEETEmConstants.SR_MATCH_MEM_FB_ID));
					bean.setSearchResultMemAcctStatus(json_data
							.getString(IMEETEmConstants.SR_MATCH_MEM_ACCT_STATUS));
					bean.setSearchResultLastActivity(json_data
							.getString(IMEETEmConstants.SR_MATCH_LAST_ACTIVITY));
					bean.setSearchResultMemImg(json_data
							.getString(IMEETEmConstants.SR_MATCH_MEM_IMAGE));
					bean.setSearchResultMemImgLoc(json_data
							.getString(IMEETEmConstants.SR_MATCH_MEM_IMAGE_LOC));
					bean.setSearchResultMemImgLocFld(json_data
							.getString(IMEETEmConstants.SR_MATCH_MEM_IMAGE_FLD));
					bean.setSearchResultMemName(json_data
							.getString(IMEETEmConstants.SR_MATCH_MEM_NAME));
					bean.setSearchResultMemAge(json_data
							.getString(IMEETEmConstants.SR_MATCH_AGE));
					results.add(bean);
				}
			}

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
		}

		if (!results.equals(null)) {
			Log.e("log_tag", "\n\n -- Returning Response: " + results
					+ "  -- \n\n");
			return results;
		} else {
			throw new Exception(
					"An Error Has OCCURRED getting member match information!!");
		}

	}

	/**
	 * Update the users Search criteria
	 * 
	 * @param memID
	 * @param gender
	 * @param distance
	 * @param young
	 * @param old
	 * @return
	 */
	public boolean updateSearch(String memID, String gender, String distance,
			String young, String old, String eyes, String hair,
			String education, String ethnicity, String wants_kids,
			String has_kids) {
		String response = null;
		String result = null;

		/*** Un comment to Test ***/
		/*
		 * IMEETEmUtil util = IMEETEmUtil.getInstance(); String phpServiceURI =
		 * null; List<String> params = new ArrayList<String>();
		 */

		try {

			/*** Un comment to Test ***/
			/*
			 * // Add the parameters if there are any, in this case there are...
			 * params.add(URLEncoder.encode(memID, "UTF-8"));
			 * params.add(URLEncoder.encode(gender, "UTF-8"));
			 * params.add(URLEncoder.encode(distance, "UTF-8"));
			 * params.add(URLEncoder.encode(young, "UTF-8"));
			 * params.add(URLEncoder.encode(old, "UTF-8"));
			 * 
			 * // Build the PHP Service URI phpServiceURI =
			 * util.getPHPServiceURI
			 * (IMEETEmConstants.PHP_SRVC_KEY_UPDATE_SAVE_SEARCH, params); //
			 * Call the Service response =
			 * CustomHttpClient.executeHttpGet(phpServiceURI);
			 * System.out.println
			 * ("\n -------- Calling Update Save Search ------------- \n");
			 * System.out.println(phpServiceURI);
			 * System.out.println("\n -------- DONE Calling ------------- \n");
			 */

			response = CustomHttpClient
					.executeHttpGet("http://imeetem.com/services/update_save_search.php?mem_id="
							+ memID
							+ "&GENDER1="
							+ gender
							+ ""
							+ "&DISTANCE1="
							+ distance
							+ "&YOUNG1="
							+ young
							+ "&OLD1="
							+ old
							+ "&EYES1="
							+ eyes
							+ "&HAIR1="
							+ URLEncoder.encode(hair, "UTF-8")
							+ "&EDUCATION1="
							+ URLEncoder.encode(education, "UTF-8")
							+ "&ETHNICITY1="
							+ URLEncoder.encode(ethnicity, "UTF-8")
							+ "&HAS_KIDS1="
							+ URLEncoder.encode(has_kids, "UTF-8")
							+ "&WANTS_KIDS1="
							+ URLEncoder.encode(wants_kids, "UTF-8"));

			// store the result returned by PHP script that runs MySQL query
			result = response.toString();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
		}

		if (!result.equals(null)) {
			Log.e("log_tag", "Update for user " + memID + " was successful!");
			return true;
		} else {
			Log.e("log_tag", "Something went wrong in the update with user "
					+ memID);
			return false; // An error has occurred, and/or the user has not been
							// found!
		}
	}

	public List<IMeetEmSearchCriteriaInfoBean> getSearchUpdateCritera(
			String memID) throws Exception {

		String response = null;
		List<IMeetEmSearchCriteriaInfoBean> results = new ArrayList<IMeetEmSearchCriteriaInfoBean>();

		/*** Un comment to Test ***/
		/*
		 * IMEETEmUtil util = IMEETEmUtil.getInstance(); String phpServiceURI =
		 * null; List<String> params = new ArrayList<String>();
		 */

		try {

			/*** Un comment to Test ***/
			/*
			 * // Add the parameters if there are any, in this case there are...
			 * params.add(URLEncoder.encode(memID, "UTF-8")); // Build the PHP
			 * Service URI phpServiceURI =
			 * util.getPHPServiceURI(IMEETEmConstants
			 * .PHP_SRVC_KEY_LOADSEARCHCRITERIA, params); // Call the Service
			 * response = CustomHttpClient.executeHttpGet(phpServiceURI);
			 * System.out.println(
			 * "\n -------- Calling Load Search Criteria ------------- \n");
			 * System.out.println(phpServiceURI);
			 * System.out.println("\n -------- DONE Calling ------------- \n");
			 */

			response = CustomHttpClient
					.executeHttpGet("http://imeetem.com/services/loadSearchCriteria.php?memID="
							+ memID);

			// store the result returned by PHP script that runs MySQL query
			// String result = response.toString();

			JSONArray jArray = new JSONArray(response);

			JSONObject json_data = jArray.getJSONObject(0);
			IMeetEmSearchCriteriaInfoBean searchInfoBean = new IMeetEmSearchCriteriaInfoBean();
			searchInfoBean.setOld_age(json_data.getInt("INST_OLD_AGE"));
			searchInfoBean.setYoung_age(json_data.getInt("INST_YNG_AGE"));
			searchInfoBean.setDistance(json_data.getInt("INST_DISTANCE"));
			searchInfoBean.setGender(json_data.getString("INST_GENDER"));
			searchInfoBean.setEyecolor(json_data.getString("INST_EYES"));
			searchInfoBean.setHaircolor(json_data.getString("INST_HAIR"));
			searchInfoBean.setEthnic(json_data.getString("INST_ETHNICITY"));
			searchInfoBean.setEducation(json_data.getString("INST_EDUCATION"));
			searchInfoBean.setWantkid(json_data.getString("INST_KIDS_WANT"));
			searchInfoBean.setHaskid(json_data.getString("INST_KIDS_HAVE"));
			results.add(searchInfoBean);

		} catch (JSONException e) {
			e.printStackTrace();
			throw new Exception("An Error Has OCCURRED Parsing JSON Object");
		}

		if (results != null && results.size() > 0) {
			return results;
		} else {
			throw new Exception(
					"An Error Has OCCURRED getting member match information!!");
		}
	}

	/**
	 * This updates user email and birthdate for first time users
	 * 
	 * @param email
	 * @param birthmonth
	 * @param birthday
	 * @param birthyear
	 * @param memId
	 */
	public void updateEmailAndBirthdate(String email, String birthmonth,
			String birthday, String birthyear, String memId) {
		String response = null;

		/*** Un comment to Test ***/
		/*
		 * IMEETEmUtil util = IMEETEmUtil.getInstance(); String phpServiceURI =
		 * null; List<String> params = new ArrayList<String>();
		 */

		try {

			/*** Un comment to Test ***/
			/*
			 * // Add the parameters if there are any, in this case there are...
			 * params.add(URLEncoder.encode(memId, "UTF-8"));
			 * params.add(URLEncoder.encode(email, "UTF-8"));
			 * params.add(URLEncoder.encode(birthmonth, "UTF-8"));
			 * params.add(URLEncoder.encode(birthday, "UTF-8"));
			 * params.add(URLEncoder.encode(birthyear, "UTF-8"));
			 * 
			 * // Build the PHP Service URI phpServiceURI =
			 * util.getPHPServiceURI
			 * (IMEETEmConstants.PHP_SRVC_KEY_UPDATE_EMAIL_BDATE, params); //
			 * Call the Service response =
			 * CustomHttpClient.executeHttpGet(phpServiceURI);
			 * System.out.println("\n -------- Calling ------------- \n");
			 * System.out.println(phpServiceURI);
			 * System.out.println("\n -------- DONE Calling ------------- \n");
			 */

			response = CustomHttpClient
					.executeHttpGet("http://imeetem.com/services/update_email_birthdate.php?memId="
							+ memId
							+ "&email="
							+ email
							+ "&birthMon="
							+ birthmonth
							+ "&birthDay="
							+ birthday
							+ "&birthYear=" + birthyear);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (response.equals("Update Success")) {
			Log.e("log_tag",
					"Successfully updated the users email and birthdate ");
		} else {
			Log.e("log_tag", "Error updating the users email and birthdate ");
		}
	}

	/**
	 * Method that saves the photo that the user chooses.
	 * 
	 * @param memId
	 * @param url
	 * @param has_kids
	 * @param wants_kids
	 * @param ethnicity
	 * @param education
	 * @param hair
	 * @param eyes
	 */
	public boolean updateProfileInfo(String memId, String url, String profInfo,
			String eyes, String hair, String education, String ethnicity,
			String wants_kids, String has_kids) {

		String response = null;
		String response2 = null;
		String replaceSpaces = profInfo.replace(" ", "%20");
		System.out.println(replaceSpaces);

		/*** Un comment to Test ***/
		/*
		 * IMEETEmUtil util = IMEETEmUtil.getInstance(); String phpServiceURI =
		 * null; List<String> params = new ArrayList<String>();
		 */

		try {

			/*** Un comment to Test ***/
			/*
			 * // Add the parameters if there are any, in this case there are...
			 * params.add(URLEncoder.encode(memId, "UTF-8"));
			 * params.add(URLEncoder.encode(url, "UTF-8"));
			 * 
			 * // Build the PHP Service URI phpServiceURI =
			 * util.getPHPServiceURI
			 * (IMEETEmConstants.PHP_SRVC_KEY_UPDATE_SAVE_PHOTO_2, params); //
			 * Call the Service response =
			 * CustomHttpClient.executeHttpGet(phpServiceURI);
			 * System.out.println
			 * ("\n -------- Calling Update Save Photo 2------------- \n");
			 * System.out.println(phpServiceURI);
			 * System.out.println("\n -------- DONE Calling ------------- \n");
			 * 
			 * params = new ArrayList<String>(); phpServiceURI = null;
			 * 
			 * // Add the parameters if there are any, in this case there are...
			 * params.add(URLEncoder.encode(memId, "UTF-8"));
			 * params.add(URLEncoder.encode(replaceSpaces, "UTF-8"));
			 * 
			 * // Build the PHP Service URI phpServiceURI =
			 * util.getPHPServiceURI
			 * (IMEETEmConstants.PHP_SRVC_KEY_UPDATE_SAVE_INFO, params); // Call
			 * the Service response =
			 * CustomHttpClient.executeHttpGet(phpServiceURI);
			 * System.out.println
			 * ("\n -------- Calling Update Save Info ------------- \n");
			 * System.out.println(phpServiceURI);
			 * System.out.println("\n -------- DONE Calling ------------- \n");
			 */

			response = CustomHttpClient
					.executeHttpGet("http://imeetem.com/services/update_save_photo_2.php?mem_id="
							+ memId + "&pic_url=" + url);
			response2 = CustomHttpClient
					.executeHttpGet("http://imeetem.com/services/update_save_info.php?mem_id="
							+ memId
							+ "&INFO1="
							+ replaceSpaces
							+ "&EYES1="
							+ eyes
							+ "&HAIR1="
							+ URLEncoder.encode(hair, "UTF-8")
							+ "&EDUCATION1="
							+ URLEncoder.encode(education, "UTF-8")
							+ "&ETHNICITY1="
							+ URLEncoder.encode(ethnicity, "UTF-8")
							+ "&HAS_KIDS1="
							+ URLEncoder.encode(has_kids, "UTF-8")
							+ "&WANTS_KIDS1="
							+ URLEncoder.encode(wants_kids, "UTF-8"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (response.trim().equals("Update Successful")
				&& response2.trim().equals("Update Successful")) {
			Log.e("log_tag", "Successfully updated the users information ");
			return true;
		} else {
			Log.e("log_tag", "Error updating the users information");
			return false;
		}
	}

	/**
	 * Method that brings back the profile info for the person
	 * 
	 * @param memID
	 * @return
	 * @throws Exception
	 */
	public List<IMeetEmSearchCriteriaInfoBean> getMemberProfileInfo(String memID)
			throws Exception {
		String response = null;
		List<IMeetEmSearchCriteriaInfoBean> results = new ArrayList<IMeetEmSearchCriteriaInfoBean>();

		/*** Un comment to Test ***/
		IMEETEmUtil util = IMEETEmUtil.getInstance();
		String phpServiceURI = null;
		List<String> params = new ArrayList<String>();

		try {

			/*** Un comment to Test ***/
			// Add the parameters if there are any, in this case there are...
			params.add(URLEncoder.encode(memID, "UTF-8"));
			// Build the PHP Service URI
			phpServiceURI = util.getPHPServiceURI(
					IMEETEmConstants.PHP_SRVC_KEY_LOAD_MEM_INFO, params);
			// Call the Service
			response = CustomHttpClient.executeHttpGet(phpServiceURI);
			System.out
					.println("\n -------- Calling Load Member Info ------------- \n");
			System.out.println(phpServiceURI);
			System.out.println("\n -------- DONE Calling ------------- \n");

			JSONArray jArray = new JSONArray(response);

			JSONObject json_data = jArray.getJSONObject(0);
			IMeetEmSearchCriteriaInfoBean searchInfoBean = new IMeetEmSearchCriteriaInfoBean();
			searchInfoBean.setMemberInfo(json_data.getString("MEM_INFO"));
			searchInfoBean.setMemberPhoto(json_data.getString("MEM_IMAGE"));
			searchInfoBean.setHaskid(json_data.getString("MEM_KIDS_HAVE"));
			searchInfoBean.setWantkid(json_data.getString("MEM_KIDS_WANT"));
			searchInfoBean.setHaircolor(json_data.getString("MEM_HAIR"));
			searchInfoBean.setEyecolor(json_data.getString("MEM_EYES"));
			searchInfoBean.setEthnic(json_data.getString("MEM_ETHNICITY"));
			searchInfoBean.setEducation(json_data.getString("MEM_EDUCATION"));
			results.add(searchInfoBean);

		} catch (JSONException e) {
			e.printStackTrace();
			throw new Exception("An Error Has OCCURRED Parsing JSON Object");
		}

		if (results != null && results.size() > 0) {
			return results;
		} else {
			throw new Exception(
					"An Error Has OCCURRED getting member match information!!");
		}
	}

	/**
	 * This method returns the Ad information to populate the Match info screen
	 * with an ad
	 * 
	 * @param memID
	 * @param instID
	 * @return
	 * @throws Exception
	 */
	public List<String> getAdsInformaton(String memID, String instID)
			throws Exception {
		String response = null;
		List<String> results = new ArrayList<String>();

		/*** Un comment to Test ***/
		IMEETEmUtil util = IMEETEmUtil.getInstance();
		String phpServiceURI = null;
		List<String> params = new ArrayList<String>();

		try {

			/*** Un comment to Test ***/
			// Add the parameters if there are any, in this case there are...
			params.add(URLEncoder.encode(memID, "UTF-8"));
			params.add(URLEncoder.encode(instID, "UTF-8"));

			// Build the PHP Service URI
			phpServiceURI = util.getPHPServiceURI(
					IMEETEmConstants.PHP_SRVC_KEY_RESULTSMEM, params);
			// Call the Service
			response = CustomHttpClient.executeHttpGet(phpServiceURI);
			System.out
					.println("\n -------- Calling Results Mem ------------- \n");
			System.out.println(phpServiceURI);
			System.out.println("\n -------- DONE Calling ------------- \n");

			// response =
			// CustomHttpClient.executeHttpGet("http://imeetem.com/services/resultsmem.php?mem_id="+memID+"&inst_memid="+instID);
			int start = response.toString().indexOf("]");
			String adString = response.toString().substring(start + 1);

			JSONArray jArray = new JSONArray(adString);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				results.add(json_data.getString("AD_URL")); // add the AD URL
				results.add(json_data.getString("AD_IMAGE_LOCATION")); // add
																		// the
																		// AD
																		// Image
			}

		} catch (JSONException e) {
			e.printStackTrace();
			throw new Exception("An Error Has OCCURRED Parsing JSON Object");
		}

		return results;
	}

	/**
	 * This method will call the date_mem_values.php and get the list of
	 * messages from different members.
	 * 
	 * @param memId
	 *            A String representing the current member id
	 * @return A List of IMEETEmMessagesListBean in a known state
	 * @throws Exception
	 */
	public List<IMEETEmMessagesListBean> getMessagesFromJSON(String memId)
			throws Exception {

		String response = null;
		// String result = null;
		List<IMEETEmMessagesListBean> results = new ArrayList<IMEETEmMessagesListBean>();

		try {
			/*** Un comment to Test ***/
			IMEETEmUtil util = IMEETEmUtil.getInstance();
			String phpServiceURI = null;
			List<String> params = new ArrayList<String>();

			// Add the parameters if there are any, in this case there are...
			params.add(URLEncoder.encode(memId, "UTF-8"));
			// params.add(URLEncoder.encode(instID, "UTF-8"));

			// Build the PHP Service URI
			phpServiceURI = util.getPHPServiceURI(
					IMEETEmConstants.PHP_SRVC_KEY_DATE_MEM_VALUES, params);
			// Call the Service
			response = CustomHttpClient.executeHttpGet(phpServiceURI);
			System.out
					.println("\n -------- Calling Results Mem ------------- \n");
			System.out.println(phpServiceURI);
			System.out.println("\n -------- DONE Calling ------------- \n");

			// result = response.toString();

			// if (response.equals("null")) {
			// IMEETEmMessagesListBean bean = IMEETEmMessagesListBean
			// .getInstance();
			// bean.setMemMessageStatus(IMEETEmConstants.NO_EMAIL_MESSAGES);
			// results.add(0, bean);
			// } else

			if (!response.equals("null")) {

				IMEETEmMessagesListBean bean = IMEETEmMessagesListBean
						.getInstance();

				JSONArray jArray = new JSONArray(response);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);

					bean = IMEETEmMessagesListBean.getInstance();
					bean.setMemId(json_data.getString("MEMBER_ID"));
					bean.setAge(json_data.getString("age"));
					bean.setMemImage(json_data.getString("MEM_IMAGE"));
					bean.setMemMessage(json_data.getString("MEM_MESS"));
					bean.setMemMessageStatus(json_data.getString("NewMessage"));
					bean.setMemMessLastActivity(json_data
							.getString("MEM_MESS_LAST_ACTIVITY"));
					bean.setMemName(json_data.getString("MEM_FNAME"));

					bean.setMemberHairColor(json_data
							.getString(IMEETEmConstants.SR_MATCH_HAIR));
					bean.setMemberEyeColor(json_data
							.getString(IMEETEmConstants.SR_MATCH_HAIR));
					bean.setMemberHasKids(("Yes".equals(json_data
							.getString(IMEETEmConstants.SR_MATCH_HAS_KIDS)) ? true
							: false));
					bean.setMemberWantsKids(("Yes".equals(json_data
							.getString(IMEETEmConstants.SR_MATCH_WANTS_KIDS)) ? true
							: false));
					bean.setMemberEducation(json_data
							.getString(IMEETEmConstants.SR_MATCH_EDUCATION));
					bean.setMemberEthnicity(json_data
							.getString(IMEETEmConstants.SR_MATCH_ETHNICITY));
					bean.setMemDistance(json_data
							.getString(IMEETEmConstants.SR_MATCH_DISTANCE));

					results.add(bean);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new Exception("An Error Has OCCURRED Parsing JSON Object");
		}

		return results;

	}

	/**
	 * Method will update the Member table with the finalize profile
	 * information.
	 * 
	 * @param valueOf
	 * @param gender
	 * @param eyes
	 * @param hair
	 * @param education
	 * @param ethnicity
	 * @param wants_kids
	 * @param has_kids
	 * @param bday
	 * @param email
	 * @return
	 */
	public boolean updateFinalizeProfile(String memID, String eyes,
			String hair, String education, String ethnicity, String wants_kids,
			String has_kids, String bday, String email) {
		// TODO Auto-generated method stub
		String response = null;
		String result = null;

		// Split out the bday here

		try {

			response = CustomHttpClient
					.executeHttpGet("http://imeetem.com/services/update_save_memdetail.php?mem_id="
							+ memID
							+ "&EMAIL="
							+ URLEncoder.encode(email, "UTF-8")
							+ "&EYES1="
							+ URLEncoder.encode(eyes, "UTF-8")
							+ "&HAIR1="
							+ URLEncoder.encode(hair, "UTF-8")
							+ "&EDUCATION1="
							+ URLEncoder.encode(education, "UTF-8")
							+ "&ETHNICITY1="
							+ URLEncoder.encode(ethnicity, "UTF-8")
							+ "&HAS_KIDS1="
							+ URLEncoder.encode(has_kids, "UTF-8")
							+ "&WANTS_KIDS1="
							+ URLEncoder.encode(wants_kids, "UTF-8")
							+ "&YEAR1="
							+ URLEncoder.encode(wants_kids, "UTF-8")
							+ "&MONTH="
							+ URLEncoder.encode(wants_kids, "UTF-8")
							+ "&DAY="
							+ URLEncoder.encode(wants_kids, "UTF-8"));
			// The Birth Month, Day and Year);

			// store the result returned by PHP script that runs MySQL query
			result = response.toString();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
		}

		if (!result.equals(null)) {
			Log.e("log_tag", "Update for user " + memID + " was successful!");
			return true;
		} else {
			Log.e("log_tag", "Something went wrong in the update with user "
					+ memID);
			return false; // An error has occurred, and/or the user has not been
							// found!
		}
	}

	/**
	 * This method will call the date_mem_chat_details.php and get the list of
	 * chat messages between two members
	 * 
	 * @param msgFromId
	 *            - A String representing the member sending the message
	 * @param msgToId
	 *            - A String representing the member receiving the message.
	 * @return A List of IMEETEmMessagesListBean in a known state
	 * @throws Exception
	 */
	public List<IMEETEmChatMessagesBean> getChatMessageDetails(String theirId,
			String myId) throws Exception {

		String response = null;
		List<IMEETEmChatMessagesBean> results = new ArrayList<IMEETEmChatMessagesBean>();
		TimeGeneralizer timeGen = TimeGeneralizer.getInstance();

		try {

			IMEETEmUtil util = IMEETEmUtil.getInstance();
			String phpServiceURI = null;
			List<String> params = new ArrayList<String>();

			// Add the parameters if there are any, in this case there are...
			params.add(URLEncoder.encode(theirId, "UTF-8"));
			params.add(URLEncoder.encode(myId, "UTF-8"));

			// Build the PHP Service URI
			phpServiceURI = util.getPHPServiceURI(
					IMEETEmConstants.PHP_SRVC_KEY_DATE_CHAT_DETAILS, params);
			// Call the Service
			response = CustomHttpClient.executeHttpGet(phpServiceURI);
			System.out
					.println("\n -------- Calling Results Mem ------------- \n");
			System.out.println(phpServiceURI);
			System.out.println("\n -------- DONE Calling ------------- \n");

			if (response.equals("null")) {
				return null;
			} else {

				IMEETEmChatMessagesBean bean = null;

				JSONArray jArray = new JSONArray(response);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);

					bean = IMEETEmChatMessagesBean.getInstance();
					bean.setChatMsgId(json_data
							.getString(IMEETEmConstants.IMEETEM_CHAT_MESS_ID));
					bean.setChatMsgDate(timeGen.getGeneralTime(json_data
							.getString(IMEETEmConstants.IMEETEM_CHAT_MESS_DATE)));
					bean.setChatMsgFromId(json_data
							.getString(IMEETEmConstants.IMEETEM_CHAT_FROM_ID));
					bean.setChatMsgReadStatus(json_data
							.getString(IMEETEmConstants.IMEETEM_CHAT_MESS_STATUS_RECIEVE));
					bean.setChatMsgText(json_data
							.getString(IMEETEmConstants.IMEETEM_CHAT_MESS_MESSAGE));
					bean.setChatMsgToId(json_data
							.getString(IMEETEmConstants.IMEETEM_CHAT_TO_ID));
					bean.setChatMsgType(json_data
							.getString(IMEETEmConstants.IMEETEM_CHAT_MESS_TYPE));
					bean.setChatMsgImageURL(json_data
							.getString(IMEETEmConstants.IMEETEM_CHAT_MESS_IMG_URL));

					// TODO: Try This along with chat messages ordering by id!
					bean.setChatMsgImg(util.getImageDrawable(null,
							bean.getChatMsgImageURL()));

					results.add(bean);

				}

				if (results != null && results.size() > 0) {
					Collections.sort(results);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new Exception("An Error Has OCCURRED Parsing JSON Object");
		}

		return results;

	}

	/**
	 * This method will update the chat session with new message when "Send" is
	 * clicked!
	 * 
	 * @param msgFromId
	 *            - A String representing the user sending the message
	 * @param msgToId
	 *            - A String representing the user receiving the message
	 * @param chatMsg
	 *            - A String representing the chat message
	 * @return A boolean, true if the Chat Messages DB was updated, false if
	 *         otherwise.
	 */
	public boolean updateChatMsgData(String msgFromId, String msgToId,
			String chatMsg) {

		String response = null;
		String result = null;

		try {

			IMEETEmUtil util = IMEETEmUtil.getInstance();
			String phpServiceURI = null;
			List<String> params = new ArrayList<String>();

			// Check for ' as they don't work in SQL, we'll need to replace with
			// '' so the string can be inserted
			String sqlChatMsg = chatMsg.replace("'", "''");

			// Add the parameters if there are any, in this case there are...
			params.add(URLEncoder.encode(msgFromId, "UTF-8"));
			params.add(URLEncoder.encode(msgToId, "UTF-8"));
			params.add(URLEncoder.encode(sqlChatMsg, "UTF-8"));
			// params.add(chatMsg);

			// Build the PHP Service URI
			phpServiceURI = util.getPHPServiceURI(
					IMEETEmConstants.PHP_SRVC_KEY_DATE_CHAT_SEND_MSG, params);

			// Call the Service
			response = CustomHttpClient.executeHttpGet(phpServiceURI);
			System.out
					.println("\n -------- Calling chat_msg_send ------------- \n");
			System.out.println(phpServiceURI);
			System.out
					.println("\n -------- DONE Calling: chat_msg_send ------------- \n");

			result = response.toString();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
		}

		if (!result.equals(null)) {
			Log.e("log_tag", "Update chat message for user sending: "
					+ msgFromId + " and user receiving: " + msgToId
					+ " was successful!");
			return true;
		} else {
			Log.e("log_tag", "Update chat message for user sending: "
					+ msgFromId + " and user receiving: " + msgToId
					+ " was NOT successful!");
			return false; // An error has occurred, and/or the user has not been
							// found!
		}
	}

	/**
	 * This method will check the system status of the current system, either
	 * iOS or Android indicated by the input parameter
	 * 
	 * @param osType
	 *            A String representing the Operating System Type (iOS or
	 *            Android)
	 * @return A boolean, true if the system is down, false if not.
	 */
	public boolean checkSystemStatus(String osType) {

		String response = null;
		boolean isSystemDown = false;
		List<IMEETEmSystemCheckBean> sysCheckOsList = new ArrayList<IMEETEmSystemCheckBean>();

		try {

			IMEETEmUtil util = IMEETEmUtil.getInstance();
			String phpServiceURI = null;
			List<String> params = new ArrayList<String>();

			// Build the PHP Service URI
			phpServiceURI = util.getPHPServiceURI(
					IMEETEmConstants.PHP_SRVC_KEY_MEM_SYS_CHECK, params);

			// Call the Service
			response = CustomHttpClient.executeHttpGet(phpServiceURI);
			System.out
					.println("\n -------- Calling mem_system_check ------------- \n");
			System.out.println(phpServiceURI);
			System.out
					.println("\n -------- DONE Calling: mem_system_check ------------- \n");

			// result = response.toString();

			if (response.equals("null")) {
				return true;
			} else {

				JSONArray jArray = new JSONArray(response);
				IMEETEmSystemCheckBean sysCheckBean = null;

				for (int i = 0; i < jArray.length(); i++) {

					JSONObject json_data = jArray.getJSONObject(i);
					sysCheckBean = IMEETEmSystemCheckBean.getInstance();

					sysCheckBean
							.setSysCheckSystemDate(json_data
									.getString(IMEETEmConstants.IMEETEM_SYS_CHK_SYS_DATE));
					sysCheckBean
							.setSysCheckSystemMessage(json_data
									.getString(IMEETEmConstants.IMEETEM_SYS_CHK_SYS_MESS));
					sysCheckBean
							.setSysCheckSystemStatus(json_data
									.getString(IMEETEmConstants.IMEETEM_SYS_CHK_SYS_STATUS));
					sysCheckBean
							.setSysCheckSystemTitleMessage(json_data
									.getString(IMEETEmConstants.IMEETEM_SYS_CHK_SYS_TITLE_MESS));
					sysCheckBean
							.setSysCheckSystemType(json_data
									.getString(IMEETEmConstants.IMEETEM_SYS_CHK_SYS_TYPE));
					sysCheckBean
							.setSysCheckSystemVersion(json_data
									.getString(IMEETEmConstants.IMEETEM_SYS_CHK_SYS_VERSION));

					sysCheckOsList.add(sysCheckBean);

				}

				if (sysCheckOsList != null && sysCheckOsList.size() > 0) {

					// There will only be two beans in this list initially, one
					// for iOS and one for Android
					System.out
							.println("ZZZZZZZZZZZZZZZ Checking System Maintenance ZZZZZZZZZZZZZZZZZZZZZZZZZZ");
					for (IMEETEmSystemCheckBean b : sysCheckOsList) {

						if (b.getSysCheckSystemType().equals(osType)
								&& !b.getSysCheckSystemStatus().equals("On")) {
							isSystemDown = true;
							// break;
						}

						System.out.println(b.toString());
					}

					System.out
							.println("ZZZZZZZZZZZZZZZ DONE Checking System Maintenance ZZZZZZZZZZZZZZZZZZZZZZZZZZ");
				}
			}

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
			isSystemDown = true;
		}

		return isSystemDown;
	}

	/**
	 * This method will remove the one time message from the friend requests
	 * list.
	 * 
	 * @param fromMemId
	 *            A String representing the senders member id
	 * @param toMemId
	 *            A String representing the receivers member id
	 * @return An int representing success or failure, 0 if successfully
	 *         removed, -1 if not or exception occurred.
	 */
	public int removeOnetimeMessage(String fromMemId, String toMemId) {

		String response = null;

		try {

			IMEETEmUtil util = IMEETEmUtil.getInstance();
			String phpServiceURI = null;
			List<String> params = new ArrayList<String>();

			params.add(URLEncoder.encode(toMemId, "UTF-8"));
			params.add(URLEncoder.encode(fromMemId, "UTF-8"));

			// Build the PHP Service URI
			phpServiceURI = util.getPHPServiceURI(
					IMEETEmConstants.PHP_SRVC_KEY_REMOVE_ONETIME_MSG, params);

			// Call the Service
			response = CustomHttpClient.executeHttpGet(phpServiceURI);
			System.out
					.println("\n -------- Calling resultsmem_remove_onemess.php ------------- \n");
			System.out.println(phpServiceURI);
			System.out
					.println("\n -------- DONE Calling: resultsmem_remove_onemess.php ------------- \n");

			if (response.equals("null")) {
				return -1;
			} else {
				return 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int sendOnetimeMessage(String fromMemId, String toMemId,
			String message) {

		System.out
				.println("WWWWWWWWWWWWWWWWW -- SENDING 1 TIME MESSAGE --- WWWWWWWWWWWWWWWWWWWWWWWWW");
		String response = null;

		try {

			IMEETEmUtil util = IMEETEmUtil.getInstance();
			String phpServiceURI = null;
			List<String> params = new ArrayList<String>();

			params.add(URLEncoder.encode(toMemId, "UTF-8"));
			params.add(URLEncoder.encode(fromMemId, "UTF-8"));
			params.add(URLEncoder.encode(message, "UTF-8"));

			// Build the PHP Service URI
			phpServiceURI = util.getPHPServiceURI(
					IMEETEmConstants.PHP_SRVC_KEY_SEND_ONETIME_MSG, params);

			// Call the Service
			response = CustomHttpClient.executeHttpGet(phpServiceURI);
			System.out
					.println("\n -------- Calling req_send_onemess.php ------------- \n");
			System.out.println(phpServiceURI);
			System.out
					.println("\n -------- DONE Calling: req_send_onemess.php ------------- \n");

			if (response.equals("null")) {
				return -1;
			} else {
				return 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
