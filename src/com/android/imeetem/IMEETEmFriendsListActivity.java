/**
 * 
 */
package com.android.imeetem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.imeetem.asynctasks.IMeetEmDateEm;
import com.android.imeetem.asynctasks.IMeetEmPassEm;
import com.android.imeetem.asynctasks.IMeetEmRemoveOneTimeMsg;
import com.android.imeetem.asynctasks.IMeetEmWinkEm;
import com.android.imeetem.dialog.CustomizeDialog;
import com.android.imeetem.services.beans.IMEETEmFriendsListBean;
import com.android.imeetem.services.beans.IMEETEmMessagesListBean;
import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;
import com.android.imeetem.util.IMEETEmConstants;
import com.android.imeetem.util.IMEETEmUtil;
import com.android.imeetem.util.MatchInfoBean;

/**
 * @author kevinscomp
 * 
 */

interface OnTaskFinishedFriendsList {
	void onTaskFinished(List<IMEETEmFriendsListBean> friendsList);
}

public class IMEETEmFriendsListActivity extends Activity implements
		OnTaskFinishedFriendsList {

	private static final String TAG = "FriendsListAltActivity";
	private String memId;
	List<IMEETEmFriendsListBean> mEntries = null;

	private LinearLayout friendsListLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_list_main);

		// enables the activity icon as a 'home' button. required if
		// "android:targetSdkVersion" > 14
		getActionBar().setHomeButtonEnabled(true);

		// Build a List Programmatically
		friendsListLayout = (LinearLayout) findViewById(R.id.friendsListView);

		// System.out.println("*************** IS FRIENDS LIST LAYOUT NULL?? "
		// + (friendsListLayout == null ? "YES" : "NO")
		// + "*************************");

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		memId = sharedPreferences.getString("userId", memId);

		new LoadFriendsListData(this, memId, this).execute(" ");
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

	@Override
	public void onBackPressed() {
		Intent searchGridActivity = new Intent(this,
				IMEETEmSearchGridActivity.class);
		startActivity(searchGridActivity);
	}

	@Override
	public void onTaskFinished(List<IMEETEmFriendsListBean> friendsList) {

		this.mEntries = friendsList;
		View friendReqView = null;
		View winkEmHeaderRowView = null;
		View dateEmHeaderRowView = null;
		View oneTimeMsgHeaderRowView = null;
		View oneTimeMsgRemoveMsgView = null;
		View errorMsgView = null;

		// LayoutInflater mLayoutInflater = (LayoutInflater)
		// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LayoutInflater inflater = getLayoutInflater();

		System.out
				.println("$$$$$$$$$$$$$$$$ GETTING TO ON TASK FINISHED! $$$$$$$$$$$$$$$$$$$$");

		if (mEntries != null && mEntries.size() > 0) {

			System.out
					.println("\n\n\n ------> Friends List Has Items!!! <------------ \n\n\n");

			Log.d(TAG,
					"GOT VIEW IN FRIENDS LIST - FriendsListItemAdapter There are: "
							+ mEntries.size() + " FRIENDS!");

			// if (mEntries.size() == 1
			// && mEntries
			// .get(0)
			// .getMemMsgStatus()
			// .equalsIgnoreCase(
			// IMEETEmConstants.NO_DATE_WINK_ONETIME_REQUESTS)) {
			//
			// errorMsgView = inflater.inflate(R.layout.no_reqs, null);
			// friendsListLayout.addView(errorMsgView);
			//
			// } else {

			int index = 0;

			for (IMEETEmFriendsListBean b : this.mEntries) {
				IMEETEmFriendsListBean item = b;

				if (item.isSectionHeader()) {

					// System.out
					// .println("^^^^^^^^^^^^^^^^ WE HAVE SECTION HEADER!! ^^^^^^^^^^^^^^^^^^^^^^^^^^");

					// Get winks, date reqs and 1 time messages
					long winks = getRequestCounts().get(0);
					long dateReqs = getRequestCounts().get(1);
					long oneTimeMsgs = getRequestCounts().get(2);

					// System.out.println("\n\n --- ONE Time Msgs: "+oneTimeMsgs+" \n\n");

					if (b.getMemFname() != null
							&& b.getMemFname().equalsIgnoreCase("WINK_HEADER")
							&& winks > 0) {

						winkEmHeaderRowView = inflater.inflate(
								R.layout.winks_section_header_row, null);

						TextView sectionHeaderText = (TextView) winkEmHeaderRowView
								.findViewById(R.id.sectionHeaderTxt2);
						sectionHeaderText.setText(" (" + winks + " New )");

						friendsListLayout.addView(winkEmHeaderRowView);

					} else if (b.getMemFname() != null
							&& b.getMemFname().equalsIgnoreCase("DATE_HEADER")
							&& dateReqs > 0) {

						dateEmHeaderRowView = inflater.inflate(
								R.layout.date_section_header_row, null);

						TextView sectionHeaderText = (TextView) dateEmHeaderRowView
								.findViewById(R.id.sectionHeaderTxt2);
						sectionHeaderText.setText(" (" + dateReqs + " New )");

						friendsListLayout.addView(dateEmHeaderRowView);

					} else if (b.getMemFname() != null
							&& b.getMemFname().equalsIgnoreCase(
									"ONE_TIME_HEADER") && oneTimeMsgs > 0) {

						oneTimeMsgHeaderRowView = inflater.inflate(
								R.layout.onetime_section_header_row, null);

						TextView sectionHeaderText = (TextView) oneTimeMsgHeaderRowView
								.findViewById(R.id.sectionHeaderTxt2);
						sectionHeaderText
								.setText(" (" + oneTimeMsgs + " New )");

						friendsListLayout.addView(oneTimeMsgHeaderRowView);

					}

				} else {

					// System.out
					// .println("^^^^^^^^^^^^^^^^ Adding FriendsList View at position: "
					// + index + " ^^^^^^^^^^^^^^^^^^^^^^^^^^");
					friendReqView = inflater.inflate(R.layout.friends_list,
							null);

					// System.out
					// .println("*************** IS FRIENDS LIST LAYOUT NULL?? "
					// + (friendsListLayout == null ? "YES"
					// : "NO")
					// + "*************************");

					ImageView imageView = (ImageView) friendReqView
							.findViewById(R.id.listImage);
					TextView ageNameText = (TextView) friendReqView
							.findViewById(R.id.listTitle);
					TextView timeRequestedText = (TextView) friendReqView
							.findViewById(R.id.listDescription);

					imageView.setImageDrawable(b.getMemImgDrawable());
					ageNameText
							.setText(b.getMemFname() + " - " + b.getMemAge());

					if (b.getGroupType() != null
							&& !b.getGroupType()
									.equalsIgnoreCase("One_Message")) {
						final String timeRequested = getWinkDateMsgRequestTime(index);
						timeRequestedText.setText(timeRequested);
					} else if (b.getGroupType() != null
							&& b.getGroupType().equalsIgnoreCase("One_Message")) {
						final String oneTimeMesg = "Message: "
								+ b.getMemMsgDetail();
						timeRequestedText.setText(oneTimeMesg);

						oneTimeMsgRemoveMsgView = inflater.inflate(
								R.layout.friends_list_onetimemsg_removemsg,
								null);

						setRemoveOneTimeMessageButton(
								(ImageButton) oneTimeMsgRemoveMsgView
										.findViewById(R.id.removemsgbtn_friends),
								index, oneTimeMsgRemoveMsgView);
					}

					final IMEETEmUtil util = IMEETEmUtil.getInstance();
					final MatchInfoBean matchInfo = setFriendsListBeanToMatchInfo(b);

					// Set the picture and text (age - name) as clickable
					// and go
					// to
					// MatchInfoActivity
					imageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent i = util.setClickableForImageAndText(
									matchInfo, memId,
									IMEETEmConstants.CALLED_FROM_FRIENDS_LIST,
									new Intent(v.getContext(),
											MatchInfoActivity.class), null,
									null);
							v.getContext().startActivity(i);
						}
					});

					ageNameText.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent i = util.setClickableForImageAndText(
									matchInfo, memId,
									IMEETEmConstants.CALLED_FROM_FRIENDS_LIST,
									new Intent(v.getContext(),
											MatchInfoActivity.class), null,
									null);
							v.getContext().startActivity(i);
						}
					});

					// Get Small dateem, winkem and passem buttons
					boolean winkAvail = false;
					if (b.getWinkEmAvail() != null
							&& b.getWinkEmAvail().equalsIgnoreCase("Y")) {
						winkAvail = true;
					}
					setWinkDatePassEmButtons(
							(ImageButton) friendReqView
									.findViewById(R.id.dateembtn_friends),
							IMEETEmConstants.DATE_EM_BTN, index, winkAvail,
							friendReqView);
					setWinkDatePassEmButtons(
							(ImageButton) friendReqView
									.findViewById(R.id.winkembtn_friends),
							IMEETEmConstants.WINK_EM_BTN, index, winkAvail,
							friendReqView);
					setWinkDatePassEmButtons(
							(ImageButton) friendReqView
									.findViewById(R.id.passembtn_friends),
							IMEETEmConstants.PASS_EM_BTN, index, winkAvail,
							friendReqView);

					friendsListLayout.addView(friendReqView);

					if (oneTimeMsgRemoveMsgView != null) {
						System.out
								.println("!!!!!!!!!!!!!!! SETTING ONE TIME MESSAGE BUTTON AND REMOVE MSG BUTTON !!!!!!!!!!!!!!!!!!!!!");

						friendsListLayout.addView(oneTimeMsgRemoveMsgView);
						util.displayOneTimeMsgPopup(oneTimeMsgRemoveMsgView,
								inflater,
								IMEETEmConstants.CALLED_FROM_FRIENDS_LIST,
								b.getMemId(), memId);

					}

				}

				index++;

			}
			// }

		} else {

			errorMsgView = inflater.inflate(R.layout.no_reqs, null);
			friendsListLayout.addView(errorMsgView);

			System.out
					.println("\n\n\n ------> Friends List DOES NOT Have Items!!! <------------ \n\n\n");
		}
	}

	private void setWinkDatePassEmButtons(final ImageButton btn,
			String buttonName, int position, boolean winkAvail,
			final View itemView) {

		// get impact id
		final String impactId = mEntries.get(position).getMemId();

		if (buttonName != null) {

			if (buttonName.equalsIgnoreCase(IMEETEmConstants.WINK_EM_BTN)
					&& winkAvail) {
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(v.getContext(), "WINKING AT EM",
								Toast.LENGTH_LONG).show();

						ImageButton imgBtn = (ImageButton) itemView
								.findViewById(R.id.winkembtn_friends);
						imgBtn.setEnabled(false);

						// TODO: Gray out button somehow

						new IMeetEmWinkEm(memId, impactId, false).execute(" ");

						Intent i = new Intent(v.getContext(),
								IMEETEmFriendsListActivity.class);
						v.getContext().startActivity(i);

					}
				});
			} else if (buttonName
					.equalsIgnoreCase(IMEETEmConstants.WINK_EM_BTN)
					&& !winkAvail) {

				final float scale = this.getResources().getDisplayMetrics().density;
				int wpixels = (int) (70 * scale + 0.5f);
				int hpixels = (int) (35 * scale + 0.5f);
				int marginTop = (int) (12 * scale + 0.5f);

				btn.setBackgroundResource(R.drawable.results_winkem_grey);
				LayoutParams params = new LayoutParams(wpixels, hpixels);
				params.addRule(RelativeLayout.BELOW, R.id.listDescription);
				params.addRule(RelativeLayout.RIGHT_OF, R.id.dateembtn_friends);
				params.setMargins(0, marginTop, 0, 0);
				btn.setLayoutParams(params);
				btn.setClickable(false);
				btn.setEnabled(false);

			} else if (buttonName
					.equalsIgnoreCase(IMEETEmConstants.PASS_EM_BTN)) {

				Log.d(TAG, "Setting up the Pass Em Button!");

				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						new IMeetEmPassEm(memId, impactId, false).execute(" ");

						Intent i = new Intent(v.getContext(),
								IMEETEmFriendsListActivity.class);
						v.getContext().startActivity(i);

					}
				});
			} else if (buttonName
					.equalsIgnoreCase(IMEETEmConstants.DATE_EM_BTN)) {
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						// TODO: Do we gray this button out too??
						new IMeetEmDateEm(memId, impactId, false).execute(" ");

						Intent i = new Intent(v.getContext(),
								IMEETEmFriendsListActivity.class);
						v.getContext().startActivity(i);
					}
				});
			}
		}
	}

	private void setRemoveOneTimeMessageButton(final ImageButton btn,
			int position, final View itemView) {

		System.out
				.println("VVVVVVVVVVVVVVVVV - Setting Remove Message Button!! - VVVVVVVVVVVVVVVVVVVV");
		// get impact id
		final String fromMemId = mEntries.get(position).getMemId();

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "REMOVING MESSAGE!",
						Toast.LENGTH_LONG).show();

				ImageButton imgBtn = (ImageButton) itemView
						.findViewById(R.id.removemsgbtn_friends);
				imgBtn.setEnabled(false);

				new IMeetEmRemoveOneTimeMsg(fromMemId, String.valueOf(memId))
						.execute(" ");

				Intent i = new Intent(v.getContext(),
						IMEETEmFriendsListActivity.class);
				v.getContext().startActivity(i);

			}
		});
	}

	private MatchInfoBean setFriendsListBeanToMatchInfo(
			IMEETEmFriendsListBean friend) {

		MatchInfoBean bean = MatchInfoBean.getInstance();

		if (friend != null) {

			bean.setImageDrawable(friend.getMemImgDrawable());
			bean.setMemberName(friend.getMemFname());
			bean.setMemberDistance(friend.getMemDistance());
			bean.setMemberId(friend.getMemId());
			bean.setMemberAge(friend.getMemAge());

			bean.setMemberHairColor(friend.getMemberHairColor());
			bean.setMemberEyeColor(friend.getMemberEyeColor());
			bean.setMemberHasKids(friend.isMemberHasKids());
			bean.setMemberWantsKids(friend.isMemberWantsKids());
			bean.setMemberEducation(friend.getMemberEducation());
			bean.setMemberEthnicity(friend.getMemberEthnicity());
			bean.setMemberDistance(String.valueOf(Math.round(Double
					.parseDouble(friend.getMemDistance()))));
		}

		return bean;
	}

	private String getWinkDateMsgRequestTime(int position) {

		String dateStart = mEntries.get(position).getDateRequested();
		String dateFormat = "yy-MM-dd";

		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date d1 = null;
		Date d2 = new Date(System.currentTimeMillis());

		try {
			d1 = format.parse(dateStart);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long diff = d2.getTime() - d1.getTime();
		// long diffSeconds = diff / 1000 % 60;
		// long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		if (diffDays == 0) {
			return String.valueOf(diffHours + " hours ago");
		} else {
			if (diffDays == 1) {
				return String.valueOf(diffDays + " day ago");
			} else {
				return String.valueOf(diffDays + " days ago");
			}
		}
	}

	private List<Long> getRequestCounts() {

		List<Long> counts = new ArrayList<Long>();
		long winkCount = 0;
		long dateReqCount = 0;
		long oneTimeMsgCount = 0;

		for (IMEETEmFriendsListBean friend : mEntries) {

			if (friend.getGroupType() != null
					&& friend.getGroupType().equalsIgnoreCase(
							IMEETEmConstants.FL_GT_WINKEM)) {
				winkCount++;
			} else if (friend.getGroupType() != null
					&& friend.getGroupType().equalsIgnoreCase(
							IMEETEmConstants.FL_GT_DATEEM)) {
				dateReqCount++;
			} else if (friend.getGroupType() != null
					&& friend.getGroupType().equalsIgnoreCase(
							IMEETEmConstants.FL_GT_ONETIMEMSG)) {
				oneTimeMsgCount++;
			}
		}

		counts.add(0, winkCount);
		counts.add(1, dateReqCount);
		counts.add(2, oneTimeMsgCount);

		return counts;
	}

	/**
	 * This inner class is an AsyncTask to get the Friends Requests Data from
	 * the database. This replaces the old implementation of the ListViewAdapter
	 * 
	 * @author kevinscomp
	 * 
	 */
	private class LoadFriendsListData extends AsyncTask<String, String, String> {

		private String TAG = "LoadFriendsListDataAlt";
		private IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl
				.getInstance();
		private IMEETEmUtil generalUtil = IMEETEmUtil.getInstance();

		private CustomizeDialog mCustomizeDialog;
		private final Context context;
		private int memId;
		private final OnTaskFinishedFriendsList listener;

		private List<IMEETEmFriendsListBean> friendsList = new ArrayList<IMEETEmFriendsListBean>();

		public LoadFriendsListData(IMEETEmFriendsListActivity activity,
				String memId, OnTaskFinishedFriendsList listener) {
			this.context = activity;
			this.memId = Integer.valueOf(memId);
			this.listener = listener;
		}

		@Override
		protected void onPreExecute() {

			mCustomizeDialog = new CustomizeDialog(this.context);
			mCustomizeDialog.show();
			mCustomizeDialog.setCanceledOnTouchOutside(false);

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			Log.d(TAG,
					"Do In Background - getting Friends List Data ALTERNATIVE");

			boolean isSystemDown = util
					.checkSystemStatus(IMEETEmConstants.IMEETEM_SYS_ANDROID);

			// System.out
			// .println("^^^^^^^^^^^^^^^^^^^ IS DOWN FOR MAINTENANCE (LoadFriendsListData.doInBackground)? "
			// + (isSystemDown ? "YES" : "NO")
			// + " ^^^^^^^^^^^^^^^^^^^");

			if (!isSystemDown) {

				try {
					friendsList = util.getFriendsListFromJSON(String
							.valueOf(this.memId));

					Log.d(TAG,
							"Do In Background - GOT Friends List Data ALTERNATIVE: "
									+ friendsList.size());
					// System.out
					// .println("<<<<< Do In Background - GOT Friends List Data ALTERNATIVE: "
					// + friendsList.size() + " >>>>>>>");
					// Create Drawable images
					for (IMEETEmFriendsListBean b : friendsList) {

						if (b.getMemImageUrl() != null) {
							Log.d(TAG,
									"Do In Background - Setting Image Drawable from URL ALTERNATIVE: "
											+ b.getMemImageUrl());
							b.setMemImgDrawable(generalUtil.createDrawableImg(
									b.getMemImageUrl(), "image"));
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

				// System.out
				// .println("@@@@@@@@@@@ Setting Freinds List! ALTERNATIVE @@@@@@@@@@@@@@");
				listener.onTaskFinished(this.friendsList);
			} else {
				Intent i = new Intent(context, IMEETEmMainActivity.class);
				i.putExtra("SystemIsDown", "DOWN");
				context.startActivity(i);
			}
		}
	}

}
