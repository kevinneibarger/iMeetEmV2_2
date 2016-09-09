/**
 * 
 */
package com.android.imeetem.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.imeetem.IMEETEmSearchGridActivity;
import com.android.imeetem.R;
import com.android.imeetem.services.beans.IMEETEmMemberInfoBean;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

/**
 * @author kevinscomp
 * 
 */
public class GetFacebookMemberInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String TAG = "GetFacebookMemberInfo";
	private IMEETEmMemberInfoBean memberInfo;
	private static final Uri M_FACEBOOK_URL = Uri.parse("http://m.facebook.com");
	private IMEETEmSearchGridActivity activity;
	private SharedPreferences prefs;

	private GetFacebookMemberInfo(IMEETEmSearchGridActivity activity) {

		if (activity != null) {
			this.activity = activity;
			prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		}
	}

	public static GetFacebookMemberInfo getInstance(IMEETEmSearchGridActivity activity) {
		return new GetFacebookMemberInfo(activity);
	}

	public Request makeMeRequest(final Session session) {

		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {

			@Override
			public void onCompleted(GraphUser user, Response response) {

				if (session == Session.getActiveSession()) {

					String email = null;

					if (user != null) {

						String birthdate = user.getBirthday();

						if (birthdate == null) {
							birthdate = "01/01/2000";
						} else {
							birthdate = user.getBirthday().toString();
						}

						try {
							email = user.getInnerJSONObject().getString("email");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							email = "testAppEmail@email.com"; // Put something
																// here
																// and we'll fix
																// it
																// later in the
																// application
							e.printStackTrace();
						}

						memberInfo = buildMemberInfoBean(user.getId(),
								"https://graph.facebook.com/" + user.getId() + "/picture?type=large", email,
								birthdate,
								// user.asMap().get("birthday").toString(),
								user.getFirstName(), user.getLastName(), user.asMap().get("gender")
										.toString());

					}
				}
				if (response.getError() != null) {
					handleError(response.getError());
				}
			}
		});

		return request;
		// request.executeAndWait(request);
		// request.executeAsync();
	}

	public IMEETEmMemberInfoBean buildMemberInfoBean(String userId, String imageName, String userEmail,
			String birthdate, String firstName, String lastName, String gender) {

		IMEETEmMemberInfoBean bean = IMEETEmMemberInfoBean.getInstance();

		if (prefs != null) {
			if (userId != null) {
				bean.setMemFBId(userId);
				prefs.edit().putString("fbId", userId).commit();
				Log.d(TAG, "Got User Id: " + userId);
			}

			if (imageName != null) {
				bean.setMemImage(imageName);
				prefs.edit().putString("imageName", imageName).commit();
				Log.d(TAG, "Got User Image Name:  " + imageName);
			}

			if (userEmail != null) {
				bean.setEmailId(userEmail);
				prefs.edit().putString("userEmail", userEmail).commit();
				Log.d(TAG, "Got User Email: " + userEmail);
			}

			if (birthdate != null) {
				// Parse the Birth Day and Year from birthdate
				int year = 0;
				int month = 0;
				int day = 0;

				try {

					SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
					Calendar cal = Calendar.getInstance();
					Date date = formatter.parse(birthdate);
					cal.setTime(date);

					year = cal.get(Calendar.YEAR);
					month = cal.get(Calendar.MONTH);
					day = cal.get(Calendar.DATE);

					bean.setBirthDay(day);
					bean.setBirthMonth(month + 1);
					bean.setBirthYear(year);

					prefs.edit().putInt("day", day).commit();
					prefs.edit().putInt("month", month + 1).commit();
					prefs.edit().putInt("year", year).commit();

					Log.d(TAG, "Got User Birth Year: " + year);
					Log.d(TAG, "Got User Birth Month: " + month);
					Log.d(TAG, "Got User Birth Day: " + day);

				} catch (ParseException e) {
					e.printStackTrace();
				}

				prefs.edit().putString("birthdate", birthdate).commit();
			}

			if (firstName != null) {
				bean.setMemFName(firstName);
				prefs.edit().putString("firstName", firstName).commit();
				Log.d(TAG, "Got User First Name: " + firstName);
			}

			if (lastName != null) {
				bean.setMemLName(lastName);
				prefs.edit().putString("lastName", lastName).commit();
				Log.d(TAG, "Got User Last Name: " + lastName);
			}

			if (gender != null) {
				bean.setMemGender(gender);
				prefs.edit().putString("gender", gender).commit();
				Log.d(TAG, "Got User Gender: " + gender);
			}
		}

		return bean;
	}

	private void requestPublishPermissions(Session session) {
		if (session != null) {

			IMEETEmUtil util = IMEETEmUtil.getInstance();
			Session.NewPermissionsRequest newPermReq = new Session.NewPermissionsRequest(activity,
					util.getIMeetEmPermissions());
			session.requestNewPublishPermissions(newPermReq);
		}
	}

	public void handleError(FacebookRequestError error) {
		DialogInterface.OnClickListener listener = null;
		String dialogBody = null;

		if (error == null) {
			dialogBody = activity.getString(R.string.error_dialog_default_text);
		} else {
			switch (error.getCategory()) {
			case AUTHENTICATION_RETRY:
				// tell the user what happened by getting the message id, and
				// retry the operation later
				String userAction = (error.shouldNotifyUser()) ? "" : activity.getString(error
						.getUserActionMessageId());
				dialogBody = activity.getString(R.string.error_authentication_retry, userAction);
				listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent intent = new Intent(Intent.ACTION_VIEW, M_FACEBOOK_URL);
						activity.startActivity(intent);
					}
				};
				break;

			case AUTHENTICATION_REOPEN_SESSION:
				// close the session and reopen it.
				dialogBody = activity.getString(R.string.error_authentication_reopen);
				listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Session session = Session.getActiveSession();
						if (session != null && !session.isClosed()) {
							session.closeAndClearTokenInformation();
						}
					}
				};
				break;

			case PERMISSION:
				// request the publish permission
				dialogBody = activity.getString(R.string.error_permission);
				listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						// pendingAnnounce = true;
						requestPublishPermissions(Session.getActiveSession());
					}
				};
				break;

			case SERVER:
			case THROTTLING:
				// this is usually temporary, don't clear the fields, and
				// ask the user to try again
				dialogBody = activity.getString(R.string.error_server);
				break;

			case BAD_REQUEST:
				// this is likely a coding error, ask the user to file a bug
				dialogBody = activity.getString(R.string.error_bad_request, error.getErrorMessage());
				break;

			case OTHER:
			case CLIENT:
			default:
				// an unknown issue occurred, this could be a code error, or
				// a server side issue, log the issue, and either ask the
				// user to retry, or file a bug
				dialogBody = activity.getString(R.string.error_unknown, error.getErrorMessage());
				break;
			}
		}

		new AlertDialog.Builder(activity).setPositiveButton(R.string.error_dialog_button_text, listener)
				.setTitle(R.string.error_dialog_title).setMessage(dialogBody).show();
	}

	public IMEETEmMemberInfoBean getMemberInfo() {
		return memberInfo;
	}
}
