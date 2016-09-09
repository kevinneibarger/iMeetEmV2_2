package com.android.imeetem;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.imeetem.asynctasks.IMeetEmDateEm;
import com.android.imeetem.asynctasks.IMeetEmPassEm;
import com.android.imeetem.asynctasks.IMeetEmWinkEm;
import com.android.imeetem.services.beans.IMeetEmSearchCriteriaInfoBean;
import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;
import com.android.imeetem.util.IMEETEmConstants;
import com.android.imeetem.util.IMEETEmUtil;
import com.android.imeetem.util.MatchInfoBean;

public class MatchInfoActivity extends Activity {

	private static final String TAG = "MatchInfoActivity";
	ImageView adPicture;
	TextView profileInformation;
	String adpicFromDB, adURL;
	String userId, impactId;
	boolean isFromFriendsScreen = false;
	boolean isFromChatScreen = false;
	private final IMEETEmUtil util = IMEETEmUtil.getInstance();
	private MatchInfoBean matchInfo = MatchInfoBean.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String fromScreen = getIntent().getExtras().getString("fromScreen");
		Log.d(TAG, "GOT FROM SCREEN: " + fromScreen);

		// We need to set the label for the app on the phone but remove by
		// setting the title to empty spaces since it would appear twice in the
		// title bar
		setTitle("");

		if (fromScreen != null
				&& fromScreen
						.equalsIgnoreCase(IMEETEmConstants.CALLED_FROM_FRIENDS_LIST)) {
			isFromFriendsScreen = true;
		} else if (fromScreen != null
				&& fromScreen
						.equalsIgnoreCase(IMEETEmConstants.CALLED_FROM_CHAT)) {
			isFromChatScreen = true;
		}

		if (isFromChatScreen) {
			System.out
					.println("\n\n********** Loading Match Info WITHOUT buttons *******\n\n");
			setContentView(R.layout.match_info_profile_no_buttons);
		} else {
			System.out
					.println("\n\n********** Loading Match Info with buttons *******\n\n");
			setContentView(R.layout.match_info_profile);
		}

		// enables the activity icon as a 'home' button. required if
		// "android:targetSdkVersion" > 14
		getActionBar().setHomeButtonEnabled(true);

		matchInfo = util.convertToMatchInfoListFromIntent(getIntent());

		Log.d(TAG, "In MatchInfoActivity - Getting passed in text");
		String name = getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_NAME);
		String age = getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_AGE);
		String distance = getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_DISTANCE);
		String hair = getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_HAIR);
		String eyes = getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_EYES);
		String hasKids = getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_HAS_KIDS);
		String wantsKids = getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_WANTS_KIDS);
		String ethnicity = getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_ETHNICITY);
		String education = getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_EDUCATION);

		impactId = getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_IMPACT_ID);
		userId = getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_MEMBER_ID);

		Log.d(TAG, "GOT IMPACT ID AND USER ID: " + impactId + " " + userId);

		// Name
		TextView matchName = (TextView) findViewById(R.id.matchName);
		matchName.setText(name); // Set the text underneath

		// Age
		TextView matchAge = (TextView) findViewById(R.id.matchAge);
		matchAge.setText(age);

		// Distance
		TextView matchDistance = (TextView) findViewById(R.id.matchDistance);
		matchDistance.setText(distance);

		// Hair
		TextView matchHair = (TextView) findViewById(R.id.matchHair);
		matchHair.setText(hair);

		// Eyes
		TextView matchEyes = (TextView) findViewById(R.id.matchEyes);
		matchEyes.setText(eyes);

		// Has Kids
		TextView matchHasKids = (TextView) findViewById(R.id.matchHasKids);
		matchHasKids.setText(hasKids);

		// Wants Kids
		TextView matchWantsKids = (TextView) findViewById(R.id.matchWantsKids);
		matchWantsKids.setText(wantsKids);

		// Ethnicity
		TextView matchEthnicity = (TextView) findViewById(R.id.matchEthnicity);
		matchEthnicity.setText(ethnicity);

		// Education
		TextView matchEducation = (TextView) findViewById(R.id.matchEducation);
		matchEducation.setText(education);

		// profileInformation =
		// (TextView)findViewById(R.id.profileInfoReadOnly);
		profileInformation = (TextView) findViewById(R.id.match250Info);
		new IMeetEmGetMemberInfo().execute(); // Get the profile info 250 chars
												// thing from the table.

		// Set the image from the byte array
		byte[] b = getIntent().getExtras().getByteArray("imageDrawable");
		Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
		ImageView imgView = (ImageView) findViewById(R.id.matchImage);
		imgView.setImageBitmap(bmp);

		if (!isFromChatScreen) {
			Log.d(TAG,
					"In MatchInfoActivity - Implementing the DateEm, WinkEm and PassEm buttons");
			// Implement the actions for each button.
			setButtonOnMatchInfoScreen(
					(ImageButton) findViewById(R.id.dateembtn),
					IMEETEmConstants.DATE_EM_BTN, impactId, userId, fromScreen);
			setButtonOnMatchInfoScreen(
					(ImageButton) findViewById(R.id.winkembtn),
					IMEETEmConstants.WINK_EM_BTN, impactId, userId, fromScreen);
			setButtonOnMatchInfoScreen(
					(ImageButton) findViewById(R.id.passembtn),
					IMEETEmConstants.PASS_EM_BTN, impactId, userId, fromScreen);

			// Hook up the "Send 1 Time Message Link"
			TextView sendOneTimeMsgLink = (TextView) findViewById(R.id.oneTimeMessage);
			sendOneTimeMsgLink.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					util.displayOneTimeMsgPopup(v, getLayoutInflater(),
							IMEETEmConstants.CALLED_FROM_MATCH_INFO, impactId,
							userId);
				}
			});

			// Hook up the "Flag as Inappropriate Link"
			TextView flagAsInAppropriateLink = (TextView) findViewById(R.id.flagAsInappropriate);
			flagAsInAppropriateLink.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(v.getContext(), "Flagging as Inappropriate",
							Toast.LENGTH_SHORT).show();
					// TODO: Call script that does the "Flag as Inappropriate"
					// stuff...
				}
			});

			Log.d(TAG,
					"DONE - In MatchInfoActivity - Implementing the DateEm, WinkEm and PassEm buttons");
		} else {

			// Set the Back To Messages Screen link
			// backToMessages
			TextView backToMsgsLink = (TextView) findViewById(R.id.backToMessages);
			backToMsgsLink.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent messages = new Intent(v.getContext(),
							IMEETEmMessagesListActivity.class);
					v.getContext().startActivity(messages);
				}
			});

		}
		adPicture = (ImageView) findViewById(R.id.ads);
		// The async process to get the list
		new getAdInfoFromTable().execute(); // This will set the ad information

	}

	private void setButtonOnMatchInfoScreen(final ImageButton btn,
			String buttonName, final String impactId, final String userId,
			final String fromScreen) {

		if (buttonName != null) {

			if (buttonName.equalsIgnoreCase(IMEETEmConstants.WINK_EM_BTN)) {
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(v.getContext(), "WINKING AT EM",
								Toast.LENGTH_LONG).show();

						ImageButton imgBtn = (ImageButton) findViewById(R.id.winkembtn);
						imgBtn.setEnabled(false);

						new IMeetEmWinkEm(userId, impactId, true).execute(" ");

						// Intent i = new Intent(v.getContext(),
						// IMEETEmMainActivity.class);
						Intent i = null;

						if (fromScreen != null
								&& fromScreen.equalsIgnoreCase("BASIC_SEARCH")) {
							Log.d(TAG,
									"Selected WINKEm At Match Info Activity from BASIC SEARCH Screen, go back to BASIC SEARCH");
							i = new Intent(v.getContext(),
									IMEETEmMainActivity.class);
						} else {
							Log.d(TAG,
									"Selected WINKEm At Match Info Activity from FRIENDS LIST Screen, go back to FRIENDS LIST");
							i = new Intent(v.getContext(),
									IMEETEmFriendsListActivity.class);
						}
						v.getContext().startActivity(i);

					}
				});
			} else if (buttonName
					.equalsIgnoreCase(IMEETEmConstants.PASS_EM_BTN)) {

				Log.d(TAG, "Setting up the Pass Em Button!");

				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						new IMeetEmPassEm(userId, impactId, true).execute(" ");

						// Intent i = new Intent(v.getContext(),
						// IMEETEmMainActivity.class);
						Intent i = null;

						if (fromScreen != null
								&& fromScreen.equalsIgnoreCase("BASIC_SEARCH")) {
							Log.d(TAG,
									"Selected PASSEm At Match Info Activity from BASIC SEARCH Screen, go back to BASIC SEARCH");
							i = new Intent(v.getContext(),
									IMEETEmMainActivity.class);
						} else {
							Log.d(TAG,
									"Selected PASSEm At Match Info Activity from FRIENDS LIST Screen, go back to FRIENDS LIST");
							i = new Intent(v.getContext(),
									IMEETEmFriendsListActivity.class);
						}
						v.getContext().startActivity(i);

					}
				});
			} else if (buttonName
					.equalsIgnoreCase(IMEETEmConstants.DATE_EM_BTN)) {
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						new IMeetEmDateEm(userId, impactId, true).execute(" ");

						// Intent i = new Intent(v.getContext(),
						// IMEETEmMainActivity.class);
						Intent i = null;

						if (fromScreen != null
								&& fromScreen.equalsIgnoreCase("BASIC_SEARCH")) {
							Log.d(TAG,
									"Selected DATEEm At Match Info Activity from BASIC SEARCH Screen, go back to BASIC SEARCH");
							i = new Intent(v.getContext(),
									IMEETEmMainActivity.class);
						} else {
							Log.d(TAG,
									"Selected DATEEm At Match Info Activity from FRIENDS LIST Screen, go back to FRIENDS LIST");
							i = new Intent(v.getContext(),
									IMEETEmFriendsListActivity.class);
						}
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
			Intent i = new Intent(this, SearchUpdateActivity.class);
			startActivity(i);
			return true;
		case android.R.id.home:
			Intent home = new Intent(this, IMEETEmSearchGridActivity.class);
			startActivity(home);
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public class getAdInfoFromTable extends AsyncTask<String, String, String> {

		private List<String> resultList;
		IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl.getInstance();

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				resultList = util.getAdsInformaton(userId, impactId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			adURL = resultList.get(0);
			adpicFromDB = resultList.get(1);
			return resultList.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			adURL = resultList.get(0);
			adpicFromDB = resultList.get(1);

			if (adpicFromDB != null) {
				new getPictFromURLServer(adpicFromDB).execute();
			}
			adPicture.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Uri uri = Uri.parse(adURL); // Set the URL from the JSON
												// object here
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}
			});

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
			adPicture.setImageDrawable(drawable);
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
		private String memId = impactId;
		private List<IMeetEmSearchCriteriaInfoBean> resultList;

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				resultList = util.getMemberProfileInfo(memId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resultList.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			if (!result.equals(null)) {
				profileInformation.setText(resultList.get(0).getMemberInfo());
				Log.d(TAG, "Got results " + result);
			} else {

				Log.d(TAG, "Something went wrong");
			}

		}
	}

	@Override
	public void onBackPressed() {

		if (isFromFriendsScreen) {
			System.out
					.println("\n\n ******** Reload Friends Requests Screen!!! *******\n\n");
			Intent friendsList = new Intent(this,
					IMEETEmFriendsListActivity.class);
			startActivity(friendsList);

		} else if (isFromChatScreen) {
			System.out
					.println("\n\n ******** Reload Chat Messages Screen!!! *******\n\n");
			Intent chatList = util.setClickableForImageAndText(matchInfo,
					userId, IMEETEmConstants.CALLED_FROM_CHAT, new Intent(this,
							IMEETEmChatActivity.class), null, null);
			startActivity(chatList);
		} else {
			System.out
					.println("\n\n ******** Reload Search Grid Screen!!! *******\n\n");
			Intent searchGridActivity = new Intent(this,
					IMEETEmSearchGridActivity.class);
			startActivity(searchGridActivity);
		}
	}
}
