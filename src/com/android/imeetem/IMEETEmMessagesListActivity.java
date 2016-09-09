/**
 * 
 */
package com.android.imeetem;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.imeetem.dialog.CustomizeDialog;
import com.android.imeetem.services.beans.IMEETEmMessagesListBean;
import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;
import com.android.imeetem.util.IMEETEmConstants;
import com.android.imeetem.util.IMEETEmUtil;
import com.android.imeetem.util.MatchInfoBean;

/**
 * @author kevinscomp
 * 
 */

interface OnTaskFinishedMessagesList {
	void onTaskFinished(List<IMEETEmMessagesListBean> messagesList);
}

public class IMEETEmMessagesListActivity extends Activity implements
		OnTaskFinishedMessagesList {

	private static final String TAG = "IMEETEmMessagesListActivity";
	private String memId;
	private List<IMEETEmMessagesListBean> mEntries = null;
	private LinearLayout messagesListLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages_list_main);

		// Build a List Programmatically
		messagesListLayout = (LinearLayout) findViewById(R.id.messagesListView);

		// "android:targetSdkVersion" > 14
		getActionBar().setHomeButtonEnabled(true);

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		memId = sharedPreferences.getString("userId", memId);

		new LoadMessagesData(this, memId, this).execute(" ");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.imeetem.OnTaskFinished#onTaskFinished(java.util.List)
	 */
	public void onTaskFinished(List<IMEETEmMessagesListBean> messagesList) {

		View messagesView = null;
		View errorMsgView = null;

		LayoutInflater inflater = getLayoutInflater();
		this.mEntries = messagesList;

		System.out
				.println("$$$$$$$$$$$$$$$$ GETTING TO ON TASK FINISHED! $$$$$$$$$$$$$$$$$$$$");

		if (mEntries != null && mEntries.size() > 0) {

			System.out
					.println("\n\n\n ------> Messages List Has Items!!! <------------ \n\n\n");

			Log.d(TAG, "GOT VIEW IN MESSAGES LIST - OnTaskFinished There are: "
					+ mEntries.size() + " MESSAGES!");

			final IMEETEmUtil util = IMEETEmUtil.getInstance();

			for (IMEETEmMessagesListBean b : this.mEntries) {

				messagesView = (RelativeLayout) inflater.inflate(
						R.layout.messages_list, null);

				ImageView imageView = (ImageView) messagesView
						.findViewById(R.id.listMessageImage);
				TextView ageNameText = (TextView) messagesView
						.findViewById(R.id.listMessageTitle);
				TextView message = (TextView) messagesView
						.findViewById(R.id.listMessageDesc);
				TextView newMessageInd = (TextView) messagesView
						.findViewById(R.id.newMessageInd);

				final Drawable image = b.getMemImgDrawable();
				imageView.setImageDrawable(image);
				final String imgURL = b.getMemImage();

				final String ageName = b.getAge() + " - " + b.getMemName();
				ageNameText.setText(ageName);

				message.setText(b.getMemMessage());

				if (IMEETEmConstants.EMAIL_READ.equals(b.getMemMessageStatus())) {
					newMessageInd.setText(" ");
				} else {
					newMessageInd.setTextColor(Color.RED);
					newMessageInd.setTextAppearance(this,
							R.style.IMeetEmNewText);
					newMessageInd.setText(b.getMemMessageStatus());
				}

				// Set the picture and text (age - name) as clickable and go to
				// MatchInfoActivity
				final MatchInfoBean matchInfo = setMessagesListBeanToMatchInfo(b);
				final String memMessage = b.getMemMessage();

				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						// When we click image and text for messages we go to
						// chat???
						Intent i = util.setClickableForImageAndText(matchInfo,
								memId, null, new Intent(v.getContext(),
										IMEETEmChatActivity.class), imgURL,
								memMessage);
						v.getContext().startActivity(i);
					}
				});

				ageNameText.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = util.setClickableForImageAndText(matchInfo,
								memId, null, new Intent(v.getContext(),
										IMEETEmChatActivity.class), imgURL,
								memMessage);
						v.getContext().startActivity(i);
					}
				});

				messagesListLayout.addView(messagesView);
			}
		} else {
			errorMsgView = inflater.inflate(R.layout.messages_list_error, null);
			messagesListLayout.addView(errorMsgView);
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

	private MatchInfoBean setMessagesListBeanToMatchInfo(
			IMEETEmMessagesListBean message) {

		MatchInfoBean bean = MatchInfoBean.getInstance();

		if (message != null) {

			bean.setImageDrawable(message.getMemImgDrawable());
			bean.setMemberName(message.getMemName());
			bean.setMemberDistance(message.getMemDistance());
			bean.setMemberId(message.getMemId());
			bean.setMemberAge(message.getAge());
			bean.setMemberHairColor(message.getMemberHairColor());
			bean.setMemberEyeColor(message.getMemberEyeColor());
			bean.setMemberHasKids(message.isMemberHasKids());
			bean.setMemberWantsKids(message.isMemberWantsKids());
			bean.setMemberEducation(message.getMemberEducation());
			bean.setMemberEthnicity(message.getMemberEthnicity());
			bean.setMemberDistance(String.valueOf(Math.round(Double
					.parseDouble(message.getMemDistance()))));
		}

		return bean;
	}

	@Override
	public void onBackPressed() {
		Intent searchGridActivity = new Intent(this,
				IMEETEmSearchGridActivity.class);
		startActivity(searchGridActivity);
	}

	private class LoadMessagesData extends AsyncTask<String, String, String> {

		private String TAG = "LoadMessagesData";
		private IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl
				.getInstance();
		private IMEETEmUtil generalUtil = IMEETEmUtil.getInstance();
		private CustomizeDialog mCustomizeDialog;

		private final Context context;
		private int memId;
		private final OnTaskFinishedMessagesList listener;
		private List<IMEETEmMessagesListBean> messagesList = new ArrayList<IMEETEmMessagesListBean>();

		public LoadMessagesData(IMEETEmMessagesListActivity activity,
				String memId, OnTaskFinishedMessagesList listener) {
			this.context = activity;
			this.memId = Integer.valueOf(memId);
			this.listener = listener;
		}

		@Override
		protected void onPreExecute() {

			mCustomizeDialog = new CustomizeDialog(this.context);
			mCustomizeDialog.setTitle("Loading Messages List");
			mCustomizeDialog.show();
			mCustomizeDialog.setCanceledOnTouchOutside(false);

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {

			Log.d(TAG, "Do In Background - getting Messages List Data");

			boolean isSystemDown = util
					.checkSystemStatus(IMEETEmConstants.IMEETEM_SYS_ANDROID);

			System.out
					.println("^^^^^^^^^^^^^^^^^^^ IS DOWN FOR MAINTENANCE (LoadFriendsListData.doInBackground)? "
							+ (isSystemDown ? "YES" : "NO")
							+ " ^^^^^^^^^^^^^^^^^^^");

			if (!isSystemDown) {

				try {
					messagesList = util.getMessagesFromJSON(String
							.valueOf(this.memId));

					Log.d(TAG, "Do In Background - GOT Friends List Data: "
							+ messagesList.size());

					// Create Drawable images
					for (IMEETEmMessagesListBean b : messagesList) {

						if (b.getMemImage() != null) {
							Log.d(TAG,
									"Do In Background - Setting Image Drawable from URL ALTERNATIVE: "
											+ b.getMemImage());
							b.setMemImgDrawable(generalUtil.createDrawableImg(
									b.getMemImage(), "image"));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				return "SystemDown";
			}

			// We never wanna get here, if so an error has occurred.
			return "Success";
		}

		protected void onPostExecute(String result) {

			if (mCustomizeDialog.isShowing()) {
				mCustomizeDialog.dismiss();
			}

			if (result != null && !result.equals("SystemDown")) {
				System.out
						.println("@@@@@@@@@@@ Setting Freinds List! ALTERNATIVE @@@@@@@@@@@@@@");
				listener.onTaskFinished(this.messagesList);

			} else {
				Intent i = new Intent(context, IMEETEmMainActivity.class);
				i.putExtra("SystemIsDown", "DOWN");
				context.startActivity(i);
			}
		}
	}
}
