package com.android.imeetem;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.imeetem.fbphotopicker.FBPhotoPickerActivity;
import com.android.imeetem.services.beans.IMeetEmSearchCriteriaInfoBean;
import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;
import com.android.imeetem.util.IMEETEmConstants;
import com.facebook.Session;

public class UpdateProfileActivity extends Activity {
	private static final String TAG = "UpdateProfileActivity";
	String image, fbImage, userId, profInfo;
	final private IMEETEmMainActivity activity = new IMEETEmMainActivity();
	ImageView imgView;
	TextView messageText;
	EditText profileInformation;
	EditText textFile;
	Button updateProfileBtn, deleteAccountBtn;
	Button contactUsBtn, logoutBtn;
	Button b;
	final Context context = this;
	Button browse;
	int serverResponseCode = 0;
	String memberProfileInfo = null;
	Spinner wantskids, haskids, haircolor, eyecolor, ethnicity_sp,
			education_sp;
	String wantsK, hasK, hair, eyes, ethn, edu;
	String emailSent;

	ProgressDialog dialog = null;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.update_profile2);

		// enables the activity icon as a 'home' button. required if
		// "android:targetSdkVersion" > 14
		getActionBar().setHomeButtonEnabled(true);
		imgView = (ImageView) findViewById(R.id.profileImage);
		profileInformation = (EditText) findViewById(R.id.profileInfo);

		final SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		image = sharedPreferences.getString("picLoc", image);
		userId = sharedPreferences.getString("userId", userId);
		fbImage = sharedPreferences.getString("photoUrl", fbImage);
		// emailSent = sharedPreferences.getString("emailSent", emailSent);

		/*
		 * if (emailSent == null) { emailSent = ""; }
		 */

		if (fbImage != null) {
			new getPictFromURLServer(fbImage).execute();
		} else {
			new getPictFromURLServer(image).execute();
		}
		// Get the extra info
		wantskids = (Spinner) findViewById(R.id.wants_kids_spinner);
		haskids = (Spinner) findViewById(R.id.has_kids_spinner);
		haircolor = (Spinner) findViewById(R.id.hair_spinner);
		eyecolor = (Spinner) findViewById(R.id.eye_spinner);
		ethnicity_sp = (Spinner) findViewById(R.id.ethnicity_spinner);
		education_sp = (Spinner) findViewById(R.id.education_spinner);

		new IMeetEmGetMemberInfo().execute();

		// uploadButton = (Button)findViewById(R.id.uploadButton);
		updateProfileBtn = (Button) findViewById(R.id.upateProfileBtn);
		deleteAccountBtn = (Button) findViewById(R.id.deleteAccountBtn);
		contactUsBtn = (Button) findViewById(R.id.contactUsBtn);
		logoutBtn = (Button) findViewById(R.id.logoutBtn);

		// Click on this button to contact US
		contactUsBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setContentView(R.layout.update_profile2);
				contactUsUserDialog();
				/*
				 * Intent i = null; i = new Intent(v.getContext(),
				 * IMEETEmEmailForm.class); v.getContext().startActivity(i);
				 */
				/*
				 * if (emailSent != null) { if (emailSent.equals("Yes")) {
				 * Intent k = null; k = new Intent(v.getContext(),
				 * UpdateProfileActivity.class);
				 * v.getContext().startActivity(k);
				 * sharedPreferences.edit().remove("emailSent").commit(); } else
				 * {
				 * 
				 * } }
				 */
			}

		});

		// Necessary final variables for successfully logging the user out
		final SharedPreferences logoutPrefs = sharedPreferences;

		// Click this button to logout
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

		profileInformation.setText(profInfo);
		// Click the photo to get the facebook images
		imgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),
						FBPhotoPickerActivity.class);
				v.getContext().startActivity(i);

			}
		});
		// Click the update button to update the user photo url
		updateProfileBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				eyes = eyecolor.getSelectedItem().toString();
				hair = haircolor.getSelectedItem().toString();
				ethn = ethnicity_sp.getSelectedItem().toString();
				edu = education_sp.getSelectedItem().toString();
				hasK = haskids.getSelectedItem().toString();
				wantsK = wantskids.getSelectedItem().toString();

				if (fbImage != null) {
					new IMeetEmProfileUpdate(fbImage, eyes, hair, ethn, edu,
							hasK, wantsK).execute();
				} else {
					new IMeetEmProfileUpdate(image, eyes, hair, ethn, edu,
							hasK, wantsK).execute();
				}

				Intent i = new Intent(v.getContext(), IMEETEmMainActivity.class);
				v.getContext().startActivity(i);
			}
		});
		// Click this button to delete the account
		deleteAccountBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(UpdateProfileActivity.this)
						.setTitle("Delete Account")
						.setMessage(
								"Are you sure you want to delete this Account?")
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// continue with delete
										// Async Process to delete the account
										// and
										// logout.
									}
								})
						.setNegativeButton(android.R.string.no,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// do nothing
									}
								}).setIcon(R.drawable.app_96_by_96).show();

			}

		});

	}

	private void contactUsUserDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.CustomAlertDialog);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.contact_us_layout, null);

		Drawable bgDrawable = view.getBackground();
		bgDrawable.setAlpha(50);
		view.setBackground(bgDrawable);

		Button send = (Button) view.findViewById(R.id.submitBtn);
		final EditText emailtext = (EditText) view
				.findViewById(R.id.messageInfo);
		final String aEmailList[] = { "rhartleyoh@gmail.com",
				"keithneibarger14@gmail.com", "kevinneibarger@hotmail.com" };
		final String subjectString = "IMeetEm Help";

		builder.setCustomTitle(view);
		builder.setIcon(android.R.drawable.ic_dialog_alert);

		final AlertDialog alert = builder.create();
		alert.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		this.runOnUiThread(new java.lang.Runnable() {
			public void run() {
				// show AlertDialog
				alert.show();
				alert.getWindow()
						.clearFlags(
								WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
										| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			}
		});

		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Cancel the AlertDialog
				final Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);

				emailIntent.setType("plain/text");

				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						aEmailList);

				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						subjectString);

				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						emailtext.getText());
				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
				finish();
			}
		});

		// Logout of the Facebook session, user is blocked!
		Session session = Session.getActiveSession();
		if (!session.isClosed()) {
			session.closeAndClearTokenInformation();
		}
	}

	public void click(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.textView: // R.id.textView1
			intent = new Intent(this, SearchUpdateActivity.class);
			break;
		case R.id.currentCredits: // R.id.textView1
			intent = new Intent(this, CurrentCreditsActivity.class);
			break;
		}
		startActivity(intent);
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
			Toast.makeText(this, "SEARCH_UPDATE", Toast.LENGTH_LONG).show();
			return true;
		case android.R.id.home:
			Intent home = new Intent(this, IMEETEmSearchGridActivity.class);
			startActivity(home);
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public class IMeetEmProfileUpdate extends AsyncTask<String, String, String> {

		private IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl
				.getInstance();
		private String memId = userId;
		private String profInfo = profileInformation.getText().toString();
		String url;
		String eyes;
		String hair;
		String ethnicity;
		String education;
		String wants_kids;
		String has_kids;

		protected IMeetEmProfileUpdate(String url, String eyes, String hair,
				String ethnicity, String education, String wants_kids,
				String has_kids) {
			this.url = url;
			this.eyes = eyes;
			this.hair = hair;
			this.ethnicity = ethnicity;
			this.education = education;
			this.has_kids = has_kids;
			this.wants_kids = wants_kids;
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			boolean result = util.updateProfileInfo(memId, url, profInfo, eyes,
					hair, education, ethnicity, wants_kids, has_kids);
			// util.updateProfileInfo(memId, profInfo);
			if (result != false) {
				return "Update Successful";
			} else {
				return null;
			}
		}

	}

	public class getPictFromURLServer extends
			AsyncTask<Integer, String, Integer> {

		String url;
		Drawable drawable;
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		HttpConnection conn;
		HttpURLConnection urlConnection;

		protected getPictFromURLServer(String url) {
			this.url = url;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// show a progress bar
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			drawable = loadImageFromWeb(url);
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			imgView.setImageDrawable(drawable);
		}

		private Drawable loadImageFromWeb(String url) {
			// TODO Auto-generated method stub
			try {

				InputStream is = (InputStream) new URL(url).getContent();
				Drawable d = Drawable.createFromStream(is, "src name");
				return d;
			} catch (Exception e) {
				System.out.println("Exc=" + e);
				return null;
			}
		}
	}

	public class IMeetEmGetMemberInfo extends AsyncTask<String, String, String> {

		private String TAG = "IMeetEmGetMemberInfo";
		private IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl
				.getInstance();
		private String memId = userId;
		private List<IMeetEmSearchCriteriaInfoBean> resultList;

		@Override
		protected String doInBackground(String... arg0) {

			boolean isSystemDown = util
					.checkSystemStatus(IMEETEmConstants.IMEETEM_SYS_ANDROID);

			System.out
					.println("^^^^^^^^^^^^^^^^^^^ IS DOWN FOR MAINTENANCE (LoadFriendsListData.doInBackground)? "
							+ (isSystemDown ? "YES" : "NO")
							+ " ^^^^^^^^^^^^^^^^^^^");

			if (!isSystemDown) {

				// TODO Auto-generated method stub
				try {
					resultList = util.getMemberProfileInfo(memId);
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
				profileInformation.setText(resultList.get(0).getMemberInfo());
				setupProfileScreen(resultList);
				Log.d(TAG, "Got results " + result);
			} else {
				Intent i = new Intent(context, IMEETEmMainActivity.class);
				i.putExtra("SystemIsDown", "DOWN");
				context.startActivity(i);
				Log.d(TAG, "Something went wrong");
			}

		}
	}

	private void setupProfileScreen(
			List<IMeetEmSearchCriteriaInfoBean> resultList2) {
		// TODO Auto-generated method stub
		List<String> searchFilter = new ArrayList<String>();

		for (IMeetEmSearchCriteriaInfoBean b : resultList2) {
			if (b != null) {

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

			String[] has_kids = new String[2];
			String[] wants_kids = new String[2];
			String[] hair_color = new String[10];
			String[] eye_color = new String[6];
			String[] ethnicity = new String[9];
			String[] education = new String[5];

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

			try {

				/*
				 * ArrayAdapter adapter = new ArrayAdapter(this,
				 * android.R.layout.simple_spinner_item, inst_young_age);
				 * ArrayAdapter adapter2 = new ArrayAdapter(this,
				 * android.R.layout.simple_spinner_item, gendr); ArrayAdapter
				 * adapter3 = new ArrayAdapter(this,
				 * android.R.layout.simple_spinner_item, distance1);
				 * ArrayAdapter adapter4 = new ArrayAdapter(this,
				 * android.R.layout.simple_spinner_item, inst_old_age);
				 */
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

				wantskids.setAdapter(adapter5);
				ArrayAdapter wantsKidsApp = (ArrayAdapter) wantskids
						.getAdapter();
				String wk = searchFilter.get(5);
				int wkPos = wantsKidsApp.getPosition(wk);
				wantskids.setSelection(wkPos);

				haskids.setAdapter(adapter6);
				ArrayAdapter haskidsApp = (ArrayAdapter) haskids.getAdapter();
				String hk = searchFilter.get(4);
				int hkPos = haskidsApp.getPosition(hk);
				haskids.setSelection(hkPos);

				haircolor.setAdapter(adapter7);
				ArrayAdapter haircolorApp = (ArrayAdapter) haircolor
						.getAdapter();
				String hc = searchFilter.get(3);
				int hcPos = haircolorApp.getPosition(hc);
				haircolor.setSelection(hcPos);

				eyecolor.setAdapter(adapter8);
				ArrayAdapter eyecolorApp = (ArrayAdapter) eyecolor.getAdapter();
				String ec = searchFilter.get(2);
				int ecPos = eyecolorApp.getPosition(ec);
				eyecolor.setSelection(ecPos);

				ethnicity_sp.setAdapter(adapter9);
				ArrayAdapter ethnicityApp = (ArrayAdapter) ethnicity_sp
						.getAdapter();
				String eth = searchFilter.get(1);
				int ethPos = ethnicityApp.getPosition(eth);
				ethnicity_sp.setSelection(ethPos);

				education_sp.setAdapter(adapter10);
				ArrayAdapter educationApp = (ArrayAdapter) education_sp
						.getAdapter();
				String edu = searchFilter.get(0);
				int eduPos = educationApp.getPosition(edu);
				education_sp.setSelection(eduPos);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
