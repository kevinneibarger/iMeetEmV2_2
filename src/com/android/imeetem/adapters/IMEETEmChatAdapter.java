/**
 * 
 */
package com.android.imeetem.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.imeetem.R;
import com.android.imeetem.services.beans.IMEETEmChatMessagesBean;
import com.android.imeetem.util.IMEETEmUtil;

/**
 * @author kevinscomp
 * 
 */
public class IMEETEmChatAdapter extends BaseAdapter {

	private static final String TAG = "IMEETEmChatAdapter";
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<IMEETEmChatMessagesBean> chatMessages = new ArrayList<IMEETEmChatMessagesBean>();
	private String myId;
	private IMEETEmUtil util = IMEETEmUtil.getInstance();

	public IMEETEmChatAdapter(Context context, String myId) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.myId = myId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return chatMessages.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return chatMessages.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		RelativeLayout itemView = null;
		ImageView imageView = null;
		TextView chatMsgText = null;
		TextView chatMsgSentTo = null;
		TextView chatMsgSentFrom = null;

		Log.d(TAG,
				"GOT VIEW IN CHAT MESSAGES LIST - IMEETEmChatAdapter There are: "
						+ chatMessages.size() + " MESSAGES!");

		// We want to list the message and any other chat messages below it
		if (chatMessages != null && chatMessages.size() > 0) {
			// Determine if the chat messages are sent or received

			System.out
					.println("\n ------------ IMAGE URL --------------- \n\n\n\n\n");
			System.out.println(chatMessages.get(position).getChatMsgImageURL());
			System.out
					.println("\n ------------ IMAGE URL --------------- \n\n\n\n\n");

			// If you are sending to a recipient, then align picture to right.
			if (chatMessages.get(position).getChatMsgFromId()
					.equalsIgnoreCase(this.myId)) {

				itemView = (RelativeLayout) mLayoutInflater.inflate(
						R.layout.chat_messages_me_list, parent, false);

				imageView = (ImageView) itemView
						.findViewById(R.id.chatMsgToImage);

				imageView.setImageDrawable(chatMessages.get(position)
						.getChatMsgImg());

				chatMsgText = (TextView) itemView
						.findViewById(R.id.chatMsgToText);

				chatMsgSentTo = (TextView) itemView
						.findViewById(R.id.chatMsgToSent);

				System.out.println("\n\n -- GOT CHAT TO MESSAGE: "
						+ chatMessages.get(position).getChatMsgText()
						+ " -- \n\n");

				chatMsgText
						.setText(chatMessages.get(position).getChatMsgText());

				chatMsgSentTo.setText(chatMessages.get(position)
						.getChatMsgDate());

			} else {
				itemView = (RelativeLayout) mLayoutInflater.inflate(
						R.layout.chat_messages_them_list, parent, false);

				imageView = (ImageView) itemView
						.findViewById(R.id.chatMsgFromImage);

				imageView.setImageDrawable(chatMessages.get(position)
						.getChatMsgImg());

				chatMsgText = (TextView) itemView
						.findViewById(R.id.chatMsgFromText);

				System.out.println("\n\n -- GOT CHAT FROM MESSAGE: "
						+ chatMessages.get(position).getChatMsgText()
						+ " -- \n\n");

				chatMsgText
						.setText(chatMessages.get(position).getChatMsgText());

				chatMsgSentFrom = (TextView) itemView
						.findViewById(R.id.chatMsgFromSent);

				chatMsgSentFrom.setText(chatMessages.get(position)
						.getChatMsgDate());

			}

		}
		return itemView;
	}

	public void upDateMessagesEntries(List<IMEETEmChatMessagesBean> chatMessages) {
		this.chatMessages = chatMessages;
		notifyDataSetChanged();
	}
}
