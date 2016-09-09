/**
 * 
 */
package com.android.imeetem.asynctasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.GridView;

import com.android.imeetem.IMEETEmEmailAndBirthday;
import com.android.imeetem.IMEETEmMainActivity;
import com.android.imeetem.IMEETEmNoMatches;
import com.android.imeetem.IMEETEmSearchGridActivity;
import com.android.imeetem.R;
import com.android.imeetem.adapters.IMEETEmMatchesGridAdapter;
import com.android.imeetem.dialog.CustomizeDialog;
import com.android.imeetem.services.beans.IMEETEmMemberInfoBean;
import com.android.imeetem.services.beans.IMEETEmSearchResultsBean;
import com.android.imeetem.services.beans.IMeetEmSearchCriteriaInfoBean;
import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;
import com.android.imeetem.util.GetFacebookMemberInfo;
import com.android.imeetem.util.IMEETEmConstants;
import com.android.imeetem.util.IMEETEmUtil;
import com.android.imeetem.util.MatchInfoBean;
import com.facebook.Request;
import com.facebook.Session;

/**
 * @author U567262
 * 
 */
public class IMEETEmSearchGridAsyncTask extends
		AsyncTask<String[], String, String> {

	private String TAG = "IMEETEmSearchGridAsyncTask";
	private CustomizeDialog mCustomizeDialog;
	private final Context context;
	private int memId;
	private IMEETEmMatchesGridAdapter mAdapter;
	private List<IMEETEmSearchResultsBean> resultList;
	private ProgressDialog progressDialog;
	private String pictureURL;
	private List<IMeetEmSearchCriteriaInfoBean> memberProfileInfo;
	private GetFacebookMemberInfo fbMemInfo;
	private IMEETEmMemberInfoBean memInfo;
	private int member_id = 0;
	// private String next;
	private SharedPreferences prefs;
	final private IMEETEmSearchGridActivity activity;
	private Session session;
	private List<MatchInfoBean> matchInfo;
	IMEETEmServicesUtilImpl servicesUtil;
	IMEETEmUtil genericUtil = IMEETEmUtil.getInstance();
	private GridView gridview;

	public IMEETEmSearchGridAsyncTask(IMEETEmSearchGridActivity activity,
			Session session, IMEETEmMatchesGridAdapter mAdapter,
			GridView gridview) {
		this.context = activity;
		this.mAdapter = mAdapter;
		this.activity = activity;
		this.session = session;
		this.gridview = gridview;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		servicesUtil = IMEETEmServicesUtilImpl.getInstance(prefs);
		this.fbMemInfo = GetFacebookMemberInfo.getInstance(activity);
	}

	@Override
	protected void onPreExecute() {

		mCustomizeDialog = new CustomizeDialog(this.context);
		mCustomizeDialog.setTitle("");
		mCustomizeDialog.show();
		mCustomizeDialog.setCanceledOnTouchOutside(false);
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String[]... arg0) {

		boolean isSystemDown = servicesUtil
				.checkSystemStatus(IMEETEmConstants.IMEETEM_SYS_ANDROID);

		System.out
				.println("^^^^^^^^^^^^^^^^^^^ IS DOWN FOR MAINTENANCE (IMEETEmSearchGridAsyncTask.doInBackground)? "
						+ (isSystemDown ? "YES" : "NO")
						+ " ^^^^^^^^^^^^^^^^^^^");

		if (!isSystemDown) {

			// Check to see if FB information has been stored in Shared
			// preferences
			String fbId = null;
			if (prefs == null || prefs != null
					&& prefs.getString("fbId", fbId) == null) {
				System.out
						.println("<<<< FB INFO IS NOT IN SHARED PREFERENCES!! LOADING makeMeRequest >>>> ");
				Request req = fbMemInfo.makeMeRequest(session);
				req.executeAndWait();
				this.memInfo = fbMemInfo.getMemberInfo();
			} else {
				// System.out.println("<<<< FB INFO IS IN SHARED PREFERENCES!! LOADING FROM SHARED PREFEFENCES >>>> ");
				this.memInfo = genericUtil
						.buildFBMemberInfoFromSharedPrefences(prefs);
			}

			int memberStatus = -1;

			if (this.memInfo != null && this.memInfo.getMemFBId() != null) {

				Log.d(TAG, "Calling checkMemberStatus");
				// Call Get User Status
				memberStatus = servicesUtil.checkMemberStatus(this.memInfo
						.getMemFBId());

				// TEST DATA
				// memberStatus = 3;

				// Set memberStatus in the Shared Preferences
				prefs.edit().putInt("memberStatus", memberStatus).commit();

				System.out
						.println("\n\n\n =========== Member Status in SharedPrefs "
								+ prefs.getInt("memberStatus", memberStatus)
								+ " ============= \n\n\n\n");

				if (memberStatus == 0) {
					Log.d(TAG, "ADD NEW USER!");

					if (arg0[0] != null) {
						String[] latLong = arg0[0];
						this.memInfo.setMemLatitude(latLong[0]);
						this.memInfo.setMemLongitude(latLong[1]);
						Log.d(TAG,
								"Setting latitude: "
										+ this.memInfo.getMemLatitude()
										+ " and longitude: "
										+ this.memInfo.getMemLongitude());
					}

					member_id = servicesUtil.addUser(this.memInfo);
					// Set the member Id to the GetMemberInfo object so we can
					// use
					// it in the search
					this.memInfo.setMemId(member_id);
					// This service will update the search table with so the
					// user
					// has matches when you first log on or are a new user.
					servicesUtil.memLogin(this.memInfo); // Update the search
															// items
															// of this
															// particular
															// user after we add
															// them.

					resultList = servicesUtil
							.getResultsSearchFromJSON(this.memInfo.getMemFBId());

				} else if (memberStatus == 1) {

					Log.d(TAG, "EXISTING USER! Grab initial matches!");

					System.out.println("<<<<< Latitude/Longitude: " + arg0[0]
							+ " >>>>> ");

					if (arg0[0] != null) {
						String[] latLong = arg0[0];
						this.memInfo.setMemLatitude(latLong[0]);
						this.memInfo.setMemLongitude(latLong[1]);
						Log.d(TAG,
								"Setting latitude: "
										+ this.memInfo.getMemLatitude()
										+ " and longitude: "
										+ this.memInfo.getMemLongitude());
					}
					// Here call the new service that returns the mem_id
					this.memInfo.setMemId(servicesUtil.getMemId(this.memInfo
							.getMemFBId()));
					servicesUtil.memLogin(this.memInfo); // Update the search
															// items
															// for this user

					resultList = servicesUtil
							.getResultsSearchFromJSON(this.memInfo.getMemFBId());

					// Gotta get the actual photo from the DB if the user has
					// logged
					// in before.
					try {
						memberProfileInfo = servicesUtil
								.getMemberProfileInfo(String
										.valueOf(this.memInfo.getMemId()));
						// Now set the values ya dingus!
						pictureURL = memberProfileInfo.get(0).getMemberPhoto();

						// This is to get the userId stored so we can use it
						// places.
						prefs.edit()
								.putString("userId",
										String.valueOf(this.memInfo.getMemId()))
								.commit();
						if (pictureURL != null) {
							prefs.edit().putString("picLoc", pictureURL)
									.commit();
						} else {
							prefs.edit()
									.putString(
											"picLoc",
											"https://graph.facebook.com/"
													+ Integer
															.parseInt(fbMemInfo
																	.getMemberInfo()
																	.getMemFBId())
													+ "/picture?type=large")
									.commit();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			System.out
					.println("****************** RESULT LIST ********************* \n\n\n "
							+ resultList + " \n\n\n");

			if (resultList == null
					|| resultList != null
					&& resultList.size() == 1
					&& resultList
							.get(0)
							.getNoMatches()
							.equalsIgnoreCase(
									IMEETEmConstants.NO_MATCHES_FOR_YOU)) {
				return String.valueOf(memberStatus);
			} else {

				// Build a list of MatchInfo beans used in postExecute to update
				// ListView
				matchInfo = new ArrayList<MatchInfoBean>();
				String name = null;
				String age = null;

				for (int i = 0; i < resultList.size(); i++) {

					MatchInfoBean bean = MatchInfoBean.getInstance();
					IMEETEmSearchResultsBean result = resultList.get(i);
					name = result.getSearchResultMemName();
					age = result.getSearchResultMemAge();

					System.out.println("\n\n *********** RESULTS\n "
							+ result.toString() + "\n\n\n");
					bean.setMemberName(name);
					bean.setMemberAge(age);
					bean.setImageUrl(result.getSearchResultMemImgLoc());
					bean.setImageDrawable(createDrawableImg(context,
							result.getSearchResultMemImg(), "image"));
					bean.setSearchStatus(result.getSearchResultStatus());
					bean.setMemberId(result.getSearchResultMemId());

					// New Hartley fields - 7/6/2014
					bean.setMemberHairColor(result.getSearchResultMemHair());
					bean.setMemberEyeColor(result.getSearchResultMemEyes());
					bean.setMemberHasKids((result.getSearchResultMemHasKids() == "Yes" ? true
							: false));
					bean.setMemberWantsKids((result
							.getSearchResultMemWantsKids() == "Yes" ? true
							: false));
					bean.setMemberEducation(result
							.getSearchResultMemEducation());
					bean.setMemberEthnicity(result
							.getSearchResultMemEthnicity());

					// Need to get distance specifically from saved JSON Results
					// object
					String resultsJSON = null;
					IMEETEmUtil jsonUtil = IMEETEmUtil.getInstance();
					if (prefs != null
							&& prefs.getString(
									IMEETEmConstants.SIX_MATCHES_RESULTS_JSON,
									resultsJSON) != null) {

						System.out
								.println("\n === The JSON Search RESULTS Object EXISTS in the Shared Prefs! === \n\n");
						resultsJSON = prefs.getString(
								IMEETEmConstants.SIX_MATCHES_RESULTS_JSON,
								resultsJSON);
						String distance = jsonUtil
								.getFieldFrom6MatchesJSONData(
										IMEETEmConstants.SR_MATCH_DISTANCE,
										resultsJSON,
										result.getSearchResultMemId());
						bean.setMemberDistance(String.valueOf(Math.round(Double
								.parseDouble(distance))));
					}

					matchInfo.add(bean);
				}

				// TODO: At this point create JSON object of matchInfo Objects
				// and
				// store in shared prefs
				// convertMatchInfoObjectToJSONAndStoreSP();

				Log.d(TAG, "Got Matches, no matter what... " + matchInfo.size());

				return String.valueOf(memberStatus);
			}
		} else {
			return "SystemDown";
		}
	}

	@Override
	protected void onPostExecute(String result) {

		if (mCustomizeDialog != null) {
			if (mCustomizeDialog.isShowing()) {
				mCustomizeDialog.dismiss();
			}
		}

		// else if (progressDialog != null) {
		// if (progressDialog.isShowing()) {
		// progressDialog.dismiss();
		// }
		// }

		if (result != null && !result.equals("SystemDown")) {

			int memberStatus = Integer.parseInt(result);

			Log.d(TAG, "member status (onPostExecute) = " + memberStatus);
			if (memberStatus != -1) {

				if (memberStatus == 3) {
					Log.d(TAG, "BLOCKED USER!");
					Intent i = new Intent(context, IMEETEmMainActivity.class);
					context.startActivity(i);
				} else {

					if (memberStatus == 0) {
						// For first time users, we'll need the email and
						if (memInfo.getBirthDay() == 1
								&& memInfo.getBirthMonth() == 1
								&& memInfo.getBirthYear() == 2000
								&& memInfo.getEmailId().equals(
										"testAppEmail@email.com")) {
							Intent i = new Intent(context,
									IMEETEmEmailAndBirthday.class);
							context.startActivity(i);
						}
					}

					// Set the Adapter
					Log.d(TAG,
							"\n-------------------- Set the Grid Adapter ------------------- \n");
					if (matchInfo != null && matchInfo.size() > 0) {
						mAdapter = new IMEETEmMatchesGridAdapter(context,
								R.layout.matches_grid, matchInfo,
								this.memInfo.getMemId(), activity);
						gridview.setAdapter(mAdapter);
					} else { // No matches after nextEm.
						// Launch a new activity
						Intent i = new Intent(context, IMEETEmNoMatches.class);
						context.startActivity(i);
					}
					/*
					 * mAdapter = new IMEETEmMatchesGridAdapter(context,
					 * R.layout.matches_grid, matchInfo,
					 * this.memInfo.getMemId(), activity);
					 * gridview.setAdapter(mAdapter);
					 */

					Log.d(TAG, "DONE - Setting up the match information!");
				}
			}
		} else {
			Intent i = new Intent(context, IMEETEmMainActivity.class);
			i.putExtra("SystemIsDown", "DOWN");
			context.startActivity(i);
		}

		System.out
				.println("\n\n\n === Here at the end of onPostExecute === \n\n\n");
		super.onPostExecute(result);
	}

	private Drawable createDrawableImg(Context ctx, String url,
			String saveFilename) {
		try {

			InputStream is = (InputStream) fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");

			return d;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object fetch(String address) throws MalformedURLException,
			IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}
}
