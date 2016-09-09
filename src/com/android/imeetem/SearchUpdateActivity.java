package com.android.imeetem;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.imeetem.asynctasks.IMeetEmSearchUpdate;
import com.android.imeetem.services.beans.IMeetEmSearchCriteriaInfoBean;
import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;
import com.android.imeetem.util.IMEETEmConstants;

public class SearchUpdateActivity extends FragmentActivity {
	private static final String TAG = "SearchUpdateActivity";
	ProgressDialog pDialog;
	Spinner genderValue;
	Spinner distance;
	Spinner fromAge;
	Spinner toAge, wantskids, haskids, haircolor, eyecolor, ethnicity_sp,
			education_sp;
	String userId, gend, dist, fromA, toA, wantsK, hasK, hair, eyes, ethn, edu;
	String nextBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.search_update);

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

		genderValue = (Spinner) findViewById(R.id.spinnerGender);
		distance = (Spinner) findViewById(R.id.spinnerDistance);
		fromAge = (Spinner) findViewById(R.id.spinnerFromAge);
		toAge = (Spinner) findViewById(R.id.spinnerToAge);
		wantskids = (Spinner) findViewById(R.id.wants_kids_spinner);
		haskids = (Spinner) findViewById(R.id.has_kids_spinner);
		haircolor = (Spinner) findViewById(R.id.hair_spinner);
		eyecolor = (Spinner) findViewById(R.id.eye_spinner);
		ethnicity_sp = (Spinner) findViewById(R.id.ethnicity_spinner);
		education_sp = (Spinner) findViewById(R.id.education_spinner);

		new IMeetEmGetSearchCriteriaInfo(this).execute();

		setButtonSearchUpdate((Button) findViewById(R.id.updatesearchbtn),
				IMEETEmConstants.SEARCH_UPDATE_BTN);

	}

	private void setButtonSearchUpdate(final Button btn, String buttonName) {

		if (buttonName != null) {

			if (buttonName.equalsIgnoreCase(IMEETEmConstants.SEARCH_UPDATE_BTN)) {
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(v.getContext(), "Update Search",
								Toast.LENGTH_LONG).show();

						Button imgBtn = (Button) findViewById(R.id.updatesearchbtn);
						imgBtn.setEnabled(false);
						gend = genderValue.getSelectedItem().toString();
						dist = distance.getSelectedItem().toString();
						fromA = fromAge.getSelectedItem().toString();
						toA = toAge.getSelectedItem().toString();
						eyes = eyecolor.getSelectedItem().toString();
						hair = haircolor.getSelectedItem().toString();
						ethn = ethnicity_sp.getSelectedItem().toString();
						edu = education_sp.getSelectedItem().toString();
						hasK = haskids.getSelectedItem().toString();
						wantsK = wantskids.getSelectedItem().toString();
						// Create Async Task
						new IMeetEmSearchUpdate(userId, gend, dist, fromA, toA,
								eyes, hair, ethn, edu, hasK, wantsK)
								.execute(" ");

						Intent i = new Intent(v.getContext(),
								IMEETEmMainActivity.class);
						v.getContext().startActivity(i);

					}
				});
			}
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
		case R.id.currentCredits: // R.id.textView1
			intent = new Intent(this, CurrentCreditsActivity.class);
			break;
		}
		startActivity(intent);
	}

	private class IMeetEmGetSearchCriteriaInfo extends
			AsyncTask<String, String, String> {

		private String TAG = "IMeetEmSearchCriteriaInfo";
		private IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl
				.getInstance();
		private String memId = userId;
		private List<IMeetEmSearchCriteriaInfoBean> resultList;
		private Context context;

		public IMeetEmGetSearchCriteriaInfo(SearchUpdateActivity activity) {
			this.context = activity;
		}

		@Override
		protected String doInBackground(String... arg0) {

			Log.d(TAG, "Changing the User's search  " + memId);

			boolean isSystemDown = util
					.checkSystemStatus(IMEETEmConstants.IMEETEM_SYS_ANDROID);

			System.out
					.println("^^^^^^^^^^^^^^^^^^^ IS DOWN FOR MAINTENANCE (IMeetEmGetSearchCriteriaInfo.doInBackground)? "
							+ (isSystemDown ? "YES" : "NO")
							+ " ^^^^^^^^^^^^^^^^^^^");

			if (!isSystemDown) {

				try {
					resultList = util.getSearchUpdateCritera(memId);

				} catch (Exception e) {
					e.printStackTrace();
				}

				return resultList.toString();
			} else {
				return "SystemDown";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null && !result.equals("SystemDown")) {
				setupSearchScreen(resultList);
				Log.d(TAG, "Got results " + result);
			} else {
				Intent i = new Intent(context, IMEETEmMainActivity.class);
				i.putExtra("SystemIsDown", "DOWN");
				context.startActivity(i);
			}

		}
	}

	private void setupSearchScreen(
			List<IMeetEmSearchCriteriaInfoBean> resultList2) {
		// TODO Auto-generated method stub
		List<String> searchFilter = new ArrayList<String>();

		for (IMeetEmSearchCriteriaInfoBean b : resultList2) {
			if (b != null) {
				searchFilter.add(String.valueOf(b.getDistance()));
				searchFilter.add(String.valueOf(b.getOld_age()));
				searchFilter.add(String.valueOf(b.getYoung_age()));
				searchFilter.add(b.getGender());
				searchFilter.add(b.getEducation());
				searchFilter.add(b.getEthnic());
				searchFilter.add(b.getEyecolor());
				searchFilter.add(b.getHaircolor());
				searchFilter.add(b.getHaskid());
				searchFilter.add(b.getWantkid());
			}
		}

		if (searchFilter != null && searchFilter.size() > 0) {

			Log.d(TAG, "setupMatches in Search Grid - Match Names > 0");

			String[] inst_young_age = new String[101];
			String[] gendr = new String[2];
			String[] distance1 = new String[7];
			String[] inst_old_age = new String[201];
			String[] has_kids = new String[2];
			String[] wants_kids = new String[2];
			String[] hair_color = new String[10];
			String[] eye_color = new String[6];
			String[] ethnicity = new String[9];
			String[] education = new String[5];

			// Change these hard coded values to dynamic later
			gendr[0] = "Male";
			gendr[1] = "Female";

			distance1[0] = "30";
			distance1[1] = "40";
			distance1[2] = "50";
			distance1[3] = "60";
			distance1[4] = "70";
			distance1[5] = "100";
			distance1[6] = "200";

			has_kids[0] = "Y";
			has_kids[1] = "N";

			wants_kids[0] = "Y";
			wants_kids[1] = "N";

			hair_color[0] = "Auburn/Red";
			hair_color[1] = "Black";
			hair_color[2] = "Light Brown";
			hair_color[3] = "Dark Brown";
			hair_color[4] = "Blonde";
			hair_color[5] = "Salt and Pepper";
			hair_color[6] = "Silver";
			hair_color[7] = "Grey";
			hair_color[8] = "Platnum";
			hair_color[9] = "Bald";

			eye_color[0] = "Black";
			eye_color[1] = "Blue";
			eye_color[2] = "Brown";
			eye_color[3] = "Grey";
			eye_color[4] = "Green";
			eye_color[5] = "Hazel";

			ethnicity[0] = "Asian";
			ethnicity[1] = "Black/African Descent";
			ethnicity[2] = "Black";
			ethnicity[3] = "East Indian";
			ethnicity[4] = "Latino/Hispanic";
			ethnicity[5] = "Middle Eastern";
			ethnicity[6] = "Native American";
			ethnicity[7] = "Pacific Islander";
			ethnicity[8] = "White/Caucasion";
			ethnicity[8] = "Other";

			education[0] = "High School";
			education[1] = "Some College";
			education[2] = "Associates Degree";
			education[3] = "Bachelors Degree";
			education[4] = "Graduate Degree";
			education[4] = "PHD/Post Doctoral";
			education[4] = "Other";

			for (int j = 0; j < 101; j++) {
				inst_young_age[j] = String.valueOf(j);
			}
			for (int i = 0; i < 201; i++) {
				inst_old_age[i] = String.valueOf(i);
			}
			try {

				ArrayAdapter adapter = new ArrayAdapter(this,
						android.R.layout.simple_spinner_item, inst_young_age);
				ArrayAdapter adapter2 = new ArrayAdapter(this,
						android.R.layout.simple_spinner_item, gendr);
				ArrayAdapter adapter3 = new ArrayAdapter(this,
						android.R.layout.simple_spinner_item, distance1);
				ArrayAdapter adapter4 = new ArrayAdapter(this,
						android.R.layout.simple_spinner_item, inst_old_age);
				ArrayAdapter adapter5 = new ArrayAdapter(this,
						android.R.layout.simple_spinner_item, wants_kids);
				ArrayAdapter adapter6 = new ArrayAdapter(this,
						android.R.layout.simple_spinner_item, has_kids);
				ArrayAdapter adapter7 = new ArrayAdapter(this,
						android.R.layout.simple_spinner_item, hair_color);
				ArrayAdapter adapter8 = new ArrayAdapter(this,
						android.R.layout.simple_spinner_item, eye_color);
				ArrayAdapter adapter9 = new ArrayAdapter(this,
						android.R.layout.simple_spinner_item, ethnicity);
				ArrayAdapter adapter10 = new ArrayAdapter(this,
						android.R.layout.simple_spinner_item, education);

				// put these into a method to make it dynamic
				distance.setAdapter(adapter3);
				ArrayAdapter distanceApp = (ArrayAdapter) distance.getAdapter();
				String dis = searchFilter.get(0);
				int disPos = distanceApp.getPosition(dis);
				distance.setSelection(disPos);

				toAge.setAdapter(adapter4);
				ArrayAdapter toAgeApp = (ArrayAdapter) toAge.getAdapter();
				String ageYoung = searchFilter.get(1);
				int agePos = toAgeApp.getPosition(ageYoung);
				toAge.setSelection(agePos);

				fromAge.setAdapter(adapter);
				ArrayAdapter fromAgeApp = (ArrayAdapter) fromAge.getAdapter();
				String ageOld = searchFilter.get(2);
				int oldPos = fromAgeApp.getPosition(ageOld);
				fromAge.setSelection(oldPos);

				genderValue.setAdapter(adapter2);
				ArrayAdapter genderApp = (ArrayAdapter) genderValue
						.getAdapter();
				String gen = searchFilter.get(3);
				int position = genderApp.getPosition(gen);
				genderValue.setSelection(position);

				wantskids.setAdapter(adapter5);
				ArrayAdapter wantsKidsApp = (ArrayAdapter) wantskids
						.getAdapter();
				String wk = searchFilter.get(9);
				int wkPos = wantsKidsApp.getPosition(wk);
				wantskids.setSelection(wkPos);

				haskids.setAdapter(adapter6);
				ArrayAdapter haskidsApp = (ArrayAdapter) haskids.getAdapter();
				String hk = searchFilter.get(8);
				int hkPos = haskidsApp.getPosition(hk);
				haskids.setSelection(hkPos);

				haircolor.setAdapter(adapter7);
				ArrayAdapter haircolorApp = (ArrayAdapter) haircolor
						.getAdapter();
				String hc = searchFilter.get(7);
				int hcPos = haircolorApp.getPosition(hc);
				haircolor.setSelection(hcPos);

				eyecolor.setAdapter(adapter8);
				ArrayAdapter eyecolorApp = (ArrayAdapter) eyecolor.getAdapter();
				String ec = searchFilter.get(6);
				int ecPos = eyecolorApp.getPosition(ec);
				eyecolor.setSelection(ecPos);

				ethnicity_sp.setAdapter(adapter9);
				ArrayAdapter ethnicityApp = (ArrayAdapter) ethnicity_sp
						.getAdapter();
				String eth = searchFilter.get(5);
				int ethPos = ethnicityApp.getPosition(eth);
				ethnicity_sp.setSelection(ethPos);

				education_sp.setAdapter(adapter10);
				ArrayAdapter educationApp = (ArrayAdapter) education_sp
						.getAdapter();
				String edu = searchFilter.get(4);
				int eduPos = educationApp.getPosition(edu);
				education_sp.setSelection(eduPos);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}