/**
 * 
 */
package com.android.imeetem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.imeetem.adapters.IMEETEmMatchesGridAdapter;
import com.android.imeetem.asynctasks.IMEETEmSearchGridAsyncTask;
import com.android.imeetem.asynctasks.IMeetEmNextEm;
import com.android.imeetem.util.GPSTracker;
import com.facebook.Session;

/**
 * @author U567262
 * 
 */
public class IMEETEmSearchGridActivity extends Activity {

	private static final String TAG = "IMEETEmSearchGridActivity";

	private GridView gridview;
	private IMEETEmMatchesGridAdapter gridviewAdapter;
	ImageButton nextEmBtn;
	String memberIdforNextEm, fbIdForNextEm;
	GPSTracker gpsTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// We need to set the label for the app on the phone but remove by
		// setting the title to empty spaces since it would appear twice in the
		// title bar
		setTitle("");

		setContentView(R.layout.matches_grid_main);

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String memId = null;
		String nextEm = null;
		String fbId = null;
		memId = sharedPreferences.getString("userId", memId);

		nextEmBtn = (ImageButton) findViewById(R.id.next_em_button);
		nextEm = sharedPreferences.getString("nextEmBtn", nextEm);
		fbId = sharedPreferences.getString("fbId", fbId);

		// Vars for the onclick of the NextEm Button
		memberIdforNextEm = sharedPreferences.getString("userId", memId);
		fbIdForNextEm = sharedPreferences.getString("fbId", fbId);

		/*** Get Latitude and Longitude ***/
		String[] latLong = new String[2];
		gpsTracker = new GPSTracker(this);
		latLong[0] = String.valueOf(gpsTracker.getLatitude());
		latLong[1] = String.valueOf(gpsTracker.getLongitude());

		Log.d(TAG,
				"\n-------------------- Loading search grid adapter------------------- \n");
		// initialize the grid
		gridview = (GridView) findViewById(R.id.grid_view);

		// NextEm button junk.
		nextEmBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Put in the logic to run the nextEm button PHP stuff that
				// hartely created.
				Toast.makeText(getBaseContext(),
						"The user: " + memberIdforNextEm + " next 6 matches",
						Toast.LENGTH_LONG).show();
				// Set up an async task to take the memId, pass it into the
				// mem_btn service and if it passes,
				// run the getSearches with that ID and reload the images.
				// Set a shared preference thing for a nextEm Click to determine
				// which progress dialog to run.
				// sharedPreferences.edit().putString("nextEmBtn","nextEm").commit();
				IMeetEmNextEm nextEmTask = new IMeetEmNextEm(Integer
						.parseInt(memberIdforNextEm), fbIdForNextEm, String
						.valueOf(gpsTracker.getLatitude()), String
						.valueOf(gpsTracker.getLongitude()), gridviewAdapter,
						gridview, getApplicationContext());
				nextEmTask.execute();
				Intent i = new Intent(v.getContext(),
						IMEETEmSearchGridActivity.class);
				v.getContext().startActivity(i);

			}
		});

		// TODO: Check if we are coming from search grid or nextEm, if nextEm,
		// run other AsyncTask
		if (sharedPreferences == null || sharedPreferences != null
				&& sharedPreferences.getString("nextEmBtn", nextEm) != null) {
			System.out
					.println("\n\n****************** Getting NextEm Matches ********************************\n\n");
			/*
			 * IMeetEmNextEm nextEmTask = new
			 * IMeetEmNextEm(Integer.parseInt(memId), fbId,
			 * String.valueOf(gpsTracker.getLatitude()),
			 * String.valueOf(gpsTracker.getLongitude()), gridviewAdapter,
			 * gridview, getApplicationContext(), this); nextEmTask.execute();
			 */
		} else {
			System.out
					.println("\n\n****************** Getting First 6 Matches ********************************\n\n");
			// Load the Data!
			Log.d(TAG,
					"\n-------------------- Loading search grid adapter for ID: "
							+ memId + " ------------------- \n");
			IMEETEmSearchGridAsyncTask getData = new IMEETEmSearchGridAsyncTask(
					this, Session.getActiveSession(), gridviewAdapter, gridview);
			getData.execute(latLong);
		}
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
			Intent i = new Intent(this, SearchUpdateActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
