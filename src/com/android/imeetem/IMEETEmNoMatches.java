package com.android.imeetem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.imeetem.util.IMEETEmConstants;

public class IMEETEmNoMatches extends Activity {

	Button logoutBtn, updateSearchCritButton;
	public static final String PREFS_NAME = "LoginPrefs";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.no_matches);
		final SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		updateSearchCritButton = (Button) findViewById(R.id.upateProfileBtnNoMatches);
		updateSearchCritButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent i = null;
				Intent i = new Intent(getApplicationContext(),
						SearchUpdateActivity.class);
				v.getContext().startActivity(i);

				finish();

			}
		});

		// Necessary final variables for successfully logging the user out
		final SharedPreferences logoutPrefs = sharedPreferences;

		logoutBtn = (Button) findViewById(R.id.logoutBtnNoMatches);
		logoutBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Set SharedPreferences to successfully log the user out
				logoutPrefs
						.edit()
						.putString(IMEETEmConstants.IMEETEM_LOGOUT_REQ,
								IMEETEmConstants.IMEETEM_LOGOUT_REQ_Y).commit();

				Intent i = null;
				i = new Intent(v.getContext(), IMEETEmMainActivity.class);
				v.getContext().startActivity(i);

				finish();

			}
		});
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
			Toast.makeText(this, "EMAIL", Toast.LENGTH_LONG).show();
			return true;
		case R.id.search_update:
			Intent i = new Intent(this, SearchUpdateActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
}
