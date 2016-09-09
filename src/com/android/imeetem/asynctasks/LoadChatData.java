/**
 * 
 */
package com.android.imeetem.asynctasks;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.imeetem.IMEETEmChatActivity;
import com.android.imeetem.IMEETEmMainActivity;
import com.android.imeetem.adapters.IMEETEmChatAdapter;
import com.android.imeetem.dialog.CustomizeDialog;
import com.android.imeetem.services.beans.IMEETEmChatMessagesBean;
import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;
import com.android.imeetem.util.IMEETEmConstants;
import com.android.imeetem.util.IMEETEmUtil;

/**
 * @author kevinscomp
 * 
 */
public class LoadChatData extends AsyncTask<String, String, String> {

	private String TAG = "LoadChatData";
	private IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl
			.getInstance();
	private CustomizeDialog mCustomizeDialog;
	private ProgressDialog dialog;
	private final Context context;
	private String memId;
	private String impactId;
	private IMEETEmChatAdapter chatMessageAdapter;
	private List<IMEETEmChatMessagesBean> chatMessages;
	private IMEETEmChatMessagesBean singleChatMessage;
	private SharedPreferences prefs;
	private IMEETEmUtil generalUtil = IMEETEmUtil.getInstance();
	private Intent i;

	public LoadChatData(IMEETEmChatActivity activity, String memId,
			IMEETEmChatAdapter mAdapter,
			IMEETEmChatMessagesBean singleChatMessage, String impactId, Intent i) {
		this.context = activity;
		this.memId = memId;
		this.impactId = impactId;
		this.chatMessageAdapter = mAdapter;
		this.i = i;

		if (singleChatMessage != null) {
			this.singleChatMessage = singleChatMessage;
		}

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	@Override
	protected void onPreExecute() {

		mCustomizeDialog = new CustomizeDialog(this.context);
		mCustomizeDialog.setTitle("");
		mCustomizeDialog.show();
		mCustomizeDialog.setCanceledOnTouchOutside(false);

		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... arg0) {
		Log.d(TAG, "Do In Background - getting Chat Messages Data");

		boolean isSystemDown = util
				.checkSystemStatus(IMEETEmConstants.IMEETEM_SYS_ANDROID);

		System.out
				.println("^^^^^^^^^^^^^^^^^^^ IS DOWN FOR MAINTENANCE (LoadChatData.doInBackground)? "
						+ (isSystemDown ? "YES" : "NO")
						+ " ^^^^^^^^^^^^^^^^^^^");

		if (!isSystemDown) {

			try {

				// TODO: Check if message from server is coming through, if
				// singleChatMessage != null
				if (singleChatMessage != null) {

					System.out
							.println("\n\n LOADCHATDATA - Got a single message from the Queue -- \n\n");
					// TODO: If calling LoadChatData from MessageListener then
					// update the message first!
					if (util.updateChatMsgData(this.impactId, this.memId,
							singleChatMessage.getChatMsgText())) {
						System.out
								.println("\n\n ---> We've successfully updated database with record\n"
										+ singleChatMessage.toString()
										+ " <---\n\n");
					}

					// We'll call this after updating the table since it will be
					// in
					// the DB now
					chatMessages = util.getChatMessageDetails(impactId, memId);

				} else {

					System.out
							.println("\n\n LOADCHATDATA - Get existing messages from DB, SHOULD ONLY HAPPEN WHEN PAGE IS LOADED! -- \n\n");
					// TODO: Else we simply want to pull from DB, do that :)
					chatMessages = util.getChatMessageDetails(impactId, memId);

					// TODO: If chatMessages are null, add the message from the
					// messages
					// list
					if (chatMessages == null) {
						System.out
								.println("\n\n\n ------- We have no chat messages from the DB, please add the one we clicked on ----- \n\n\n");
						addMessageFromMessagesListPage();
					}
				}

				System.out
						.println("\n\n **** CHAT MESSAGES IN DO IN BACKGROUND: "
								+ chatMessages.size() + " **** \n\n");

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
					.println("\n ----------------- BEGIN: Updating chat Messages ----------------------- \n");

			chatMessageAdapter.upDateMessagesEntries(chatMessages);

			System.out.println("\n\n -- GOT: " + chatMessages.size()
					+ " Chat Messages!! \n\n\n\n");

			System.out
					.println("\n ----------------- DONE: Updating chat Messages -------------------------\n");
		} else {
			Intent i = new Intent(context, IMEETEmMainActivity.class);
			i.putExtra("SystemIsDown", "DOWN");
			context.startActivity(i);
		}

	}

	/**
	 * This is a tricky case because a relationship could exist but an initial
	 * chat message could NOT exist. In this case we just grab the stuff from
	 * the clicked message from the messages screen.
	 */
	private void addMessageFromMessagesListPage() {

		chatMessages = new ArrayList<IMEETEmChatMessagesBean>();

		IMEETEmChatMessagesBean b = IMEETEmChatMessagesBean.getInstance();
		b.setChatMsgFromId(impactId);
		b.setChatMsgToId(memId);

		b.setChatMsgImg(generalUtil.createDrawableImg(
				i.getExtras().getString("ImageURL"), "image"));

		b.setChatMsgText(i.getExtras().getString(
				IMEETEmConstants.IMEETEM_CHAT_MESS_MESSAGE));
		b.setChatMsgImageURL(i.getExtras().getString("ImageURL"));

		chatMessages.add(0, b); // Always add to the first element of the list

		// Update this Msg in the Chat DB
		if (util.updateChatMsgData(this.impactId, this.memId,
				b.getChatMsgText())) {
			// System.out
			// .println("\n\n ---> addMessageFromMessagesListPage: We've successfully updated database with record\n"
			// + chatMessages.get(0).toString() + " <---\n\n");
		}

		// System.out
		// .println("\n\n ---> BEGIN Adding Message to Chat List From Messages Page <---\n\n");
		// System.out.println("\n\n " + b.toString() + "\n\n");
		// System.out
		// .println("\n\n ---> DONE Adding Message to Chat List From Messages Page <---\n\n");

	}

	public List<IMEETEmChatMessagesBean> getChatMessages() {
		return chatMessages;
	}

	public void setChatMessages(List<IMEETEmChatMessagesBean> chatMessages) {
		this.chatMessages = chatMessages;
	}

}
