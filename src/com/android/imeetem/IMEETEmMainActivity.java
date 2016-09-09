/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.imeetem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.android.imeetem.util.IMEETEmConstants;
import com.android.imeetem.util.IMEETEmUtil;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class IMEETEmMainActivity extends Activity {

	private static final String TAG = "IMEETEmMainActivity";
	private UiLifecycleHelper uiHelper;
	private LoginButton buttonLoginLogout;
	private IMEETEmUtil util = IMEETEmUtil.getInstance();
	private boolean isDownForMaintenance;

	private SharedPreferences sharedPreferences;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			try {
				PackageInfo info = getPackageManager().getPackageInfo(
						getPackageName(), PackageManager.GET_SIGNATURES);
				for (Signature signature : info.signatures) {
					MessageDigest md;

					md = MessageDigest.getInstance("SHA");
					md.update(signature.toByteArray());
					String something = new String(Base64.encode(md.digest(), 0));
					Log.d("Hash key", something);
				}
			} catch (NameNotFoundException e1) {
				Log.e("name not found", e1.toString());
			}

			catch (NoSuchAlgorithmException e) {
				Log.e("no such an algorithm", e.toString());
			} catch (Exception e) {
				Log.e("exception", e.toString());
			}

			updateView();
		}
	};

	private Callbacks mCallbacks;

	public interface Callbacks {
		public void onBackPressedCallback();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d("IMEETEmMainActivity", "user pressed the back button");
		if (mCallbacks != null)
			mCallbacks.onBackPressedCallback();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		// We need to set the label for the app on the phone but remove by
		// setting the title to empty spaces since it would appear twice in the
		// title bar
		setTitle("");

		// onCreate we should always be at the login screen.
		setContentView(R.layout.imeetem_login);

		// This will determine if the user has selected to logout, or if
		// it's
		// the users first time in the app
		determineLogoutLoginStatus();

		buttonLoginLogout = (LoginButton) findViewById(R.id.login_button);

		Session session = Session.getActiveSession();

		System.out.println("IN MAIN ACTIVITY onCreate METHOD....\n\n\n\n");

		if (session == null) {
			if (savedInstanceState != null) {
				System.out.println("RESTORING SESSION....\n\n\n\n");
				session = Session.restoreSession(this, null, callback,
						savedInstanceState);
			}
			if (session == null) {
				System.out.println("CREATING NEW SESSION....\n\n\n\n");
				session = new Session(this);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				System.out.println("OPEN FOR READING....\n\n\n\n");
				session.openForRead(new Session.OpenRequest(this)
						.setCallback(callback));
			}
		}

		updateView();

		// checkForUpdates();
	}

	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(callback);
	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(callback);
	}

	@Override
	public void onResume() {
		super.onResume();
		// checkForCrashes();
		// checkForUpdates();
		uiHelper.onResume();
		// isResumed = true;

		// Call the 'activateApp' method to log an app event for use in
		// analytics and advertising reporting. Do so in
		// the onResume methods of the primary Activities that an app may be
		// launched into.
		AppEventsLogger.activateApp(this);

	}

	private void checkForCrashes() {
		CrashManager.register(this, "feae8dd007842a4126ec0f12923d102c");
	}

	private void checkForUpdates() {
		// Remove this for store builds!
		UpdateManager.register(this, "feae8dd007842a4126ec0f12923d102c");
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		// isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	/**
	 * This method will determine if the user is logged in and if so, show the 6
	 * matches screen if any exist.
	 */
	private void updateView() {

		Session session = Session.getActiveSession();
		String logoutRequested = IMEETEmConstants.IMEETEM_LOGOUT_REQ_N;

		if (session.isOpened()) {

			logoutRequested = sharedPreferences.getString(
					IMEETEmConstants.IMEETEM_LOGOUT_REQ, logoutRequested);

			if (IMEETEmConstants.IMEETEM_LOGOUT_REQ_Y.equals(logoutRequested)) {

				System.out
						.println("\n\n\n A SESSION IS OPEN BUT LOGOUT WAS REQUESTED, GOTO LOGOUT PAGE....\n\n\n\n");

				onClickLogout();
				// Reset the shared pref, cause we don't want to log out
				// twice... makes no sense...
				sharedPreferences
						.edit()
						.putString(IMEETEmConstants.IMEETEM_LOGOUT_REQ,
								IMEETEmConstants.IMEETEM_LOGOUT_REQ_N).commit();

			} else {
				System.out
						.println("\n\n A SESSION IS OPEN AND LOGOUT HAS NOT BEEN REQUESTED, GOING TO MATCHES PAGE I HOPE....\n\n\n\n");

				generateSixMatchesScreen(session);
			}

		} else {

			buttonLoginLogout.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					onClickLogin();
				}
			});
		}
	}

	/**
	 * This method will start a new Facebook Session when the user clicks the
	 * "Login" button
	 */
	private void onClickLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this)
					.setCallback(callback));
		} else {
			Session.openActiveSession(this, true, callback);
		}
	}

	/**
	 * This method will clear the Facebook Session once a logout has been
	 * requested
	 */
	private void onClickLogout() {
		Session session = Session.getActiveSession();
		if (!session.isClosed()) {
			session.closeAndClearTokenInformation();
			setContentView(R.layout.imeetem_login);
		}
	}

	/**
	 * This method will start the activity that shows the 6 matches screen
	 * 
	 * @param session
	 *            A Session object with a known value
	 */
	private void generateSixMatchesScreen(Session session) {

		Log.d(TAG, "In generateSixMatchesScreen");
		Intent i = null;

		if (session != null && session.isOpened()) {
			Log.d(TAG, "In generateSixMatchesScreen - Session is opened");
			System.out
					.println("\n\n ==== GETTING 6 MATCHES SCREEN ==== \n\n\n\n");

			setContentView(R.layout.blank_screen);

			// Get the member status, if it exists
			int memberStatus = -1;
			if (sharedPreferences != null
					&& sharedPreferences.getInt("memberStatus", memberStatus) != -1) {

				memberStatus = sharedPreferences.getInt("memberStatus",
						memberStatus);
			}

			System.out
					.println("\n\n ========== Member Status in Main Activity: "
							+ memberStatus + " ============= \n\n\n");

			if (memberStatus != -1 && memberStatus == 3) {
				setContentView(R.layout.imeetem_login);
				showBlockedUserDialog();
			} else if (isSystemDown()) {
				setContentView(R.layout.imeetem_login);
				onClickLogout();
				util.showSystemDownDialog(this, true);
				getIntent().removeExtra("SystemIsDown"); // Remove this flag
			} else {
				System.out
						.println("^^^^^^^^^^^^^^^^^^^^^^^^ LOADING THE SEARCH GRID! ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				i = new Intent(this, IMEETEmSearchGridActivity.class);
				startActivity(i);
			}
		}

	}

	private boolean isSystemDown() {

		boolean systemDown = false;

		if (getIntent() != null && getIntent().getExtras() != null
				&& getIntent().getExtras().getString("SystemIsDown") != null) {

			systemDown = true;
			System.out
					.println(" TTTTTTTTTTTTTTTTTTTTTTTTTTT - SYSTEM IS DOWN - TTTTTTTTTTTTTTTTTTTTTTTTTTT");
		} else {
			System.out
					.println(" TTTTTTTTTTTTTTTTTTTTTTTTTTT - SYSTEM IS NOT DOWN - TTTTTTTTTTTTTTTTTTTTTTTTTTT");
		}

		return systemDown;
	}

	/**
	 * This method will determine from the SharedPreferences whether or not a
	 * user requested to be logged out. If the SharedPreference is null then we
	 * need to set the default as "N"
	 */
	private void determineLogoutLoginStatus() {

		// This shared preference will determine if the user has requested to be
		// logged out
		String logoutReq = IMEETEmConstants.IMEETEM_LOGOUT_REQ_N;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		if (sharedPreferences.getString(IMEETEmConstants.IMEETEM_LOGOUT_REQ,
				logoutReq) == null) {

			sharedPreferences
					.edit()
					.putString(IMEETEmConstants.IMEETEM_LOGOUT_REQ,
							IMEETEmConstants.IMEETEM_LOGOUT_REQ_N).commit();

		}
	}

	private void showBlockedUserDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.CustomAlertDialog);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.blocked_user_layout, null);

		Drawable bgDrawable = view.getBackground();
		bgDrawable.setAlpha(50);
		view.setBackground(bgDrawable);

		Button tryAgainBtn = (Button) view.findViewById(R.id.tryAgainBtn);

		builder.setCustomTitle(view);
		builder.setIcon(android.R.drawable.ic_dialog_alert);

		final AlertDialog alert = builder.create();
		this.runOnUiThread(new java.lang.Runnable() {
			public void run() {
				// show AlertDialog
				alert.show();
			}
		});

		tryAgainBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Cancel the AlertDialog
				alert.cancel();
			}
		});

		// Logout of the Facebook session, user is blocked!
		Session session = Session.getActiveSession();
		if (!session.isClosed()) {
			session.closeAndClearTokenInformation();
		}
	}
}
