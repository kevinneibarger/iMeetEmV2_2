package com.android.imeetem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class CurrentCreditsActivity extends FragmentActivity {

	ProgressDialog pDialog;
	String userId;
	String nextBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.current_credits);

		// enables the activity icon as a 'home' button. required if
		// "android:targetSdkVersion" > 14
		getActionBar().setHomeButtonEnabled(true);

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		userId = sharedPreferences.getString("userId", userId);
		nextBtn = sharedPreferences.getString("nextEmBtn", nextBtn);
		sharedPreferences.edit().remove("nextEmBtn").commit(); // Remove the
																// next button
																// so it won't
																// run the
																// dialog.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.action_menu_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.friends:
			Intent friendsList = new Intent(this,
					IMEETEmFriendsListActivity.class);
			startActivity(friendsList);
			return true;
		case R.id.email:
			Intent messages = new Intent(this,
					IMEETEmMessagesListActivity.class);
			startActivity(messages);
			return true;
		case R.id.search_update:
			Toast.makeText(this, "User's Search has been Updated",
					Toast.LENGTH_LONG).show();
			return true;
		case android.R.id.home:
			Intent home = new Intent(this, IMEETEmSearchGridActivity.class);
			startActivity(home);
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void click(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.profileUpdates: // R.id.textView1
			intent = new Intent(this, UpdateProfileActivity.class);
			break;
		case R.id.searchUpdate: // R.id.textView1
			intent = new Intent(this, SearchUpdateActivity.class);
			break;
		}
		startActivity(intent);
	}

}
