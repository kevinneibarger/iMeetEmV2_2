/**
 * 
 */
package com.android.imeetem.services.beans;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;

import com.android.imeetem.util.IMEETEmConstants;

/**
 * @author kevinscomp
 * 
 */
public class IMEETEmChatMessagesBean implements Serializable,
		Comparable<IMEETEmChatMessagesBean> {

	private static final long serialVersionUID = 1L;

	private IMEETEmChatMessagesBean() {
	}

	public static IMEETEmChatMessagesBean getInstance() {
		return new IMEETEmChatMessagesBean();
	}

	private String chatMsgId;
	private String chatMsgDate;
	private String chatMsgFromId;
	private String chatMsgToId;
	private String chatMsgType;
	private String chatMsgText;
	private String chatMsgImageURL;
	private String chatMsgReadStatus;
	private Drawable chatMsgImg;

	public String getChatMsgText() {
		return chatMsgText;
	}

	public void setChatMsgText(String chatMsgText) {
		this.chatMsgText = chatMsgText;
	}

	@Override
	public String toString() {

		StringBuilder bld = new StringBuilder();

		if (chatMsgText != null) {
			bld.append("CHAT_MESSAGE: " + this.chatMsgText + "\n");
		}

		if (chatMsgFromId != null) {
			bld.append("CHAT_MESSAGE_FROM: " + this.chatMsgFromId + "\n");
		}

		if (chatMsgToId != null) {
			bld.append("CHAT_MESSAGE_TO: " + this.chatMsgToId + "\n");
		}

		if (chatMsgDate != null) {
			bld.append("CHAT_MESSAGE_SENT: " + this.chatMsgDate + "\n");
		}

		if (chatMsgImageURL != null) {
			bld.append("CHAT_MESSAGE_IMAGE_URL:" + this.chatMsgImageURL + "\n");
		}

		if (chatMsgType != null) {
			bld.append("CHAT_MESSAGE TYPE:" + this.chatMsgType + "\n");
		}

		if (chatMsgReadStatus != null) {
			bld.append("CHAT MESSAGE READ STATUS:" + this.chatMsgReadStatus
					+ "\n");
		}

		return bld.toString();
	}

	public JSONObject getJSONObject() {
		JSONObject obj = new JSONObject();

		try {

			if (chatMsgText != null) {
				obj.put(IMEETEmConstants.IMEETEM_CHAT_MESS_MESSAGE, chatMsgText);
			}

			if (chatMsgFromId != null) {
				obj.put(IMEETEmConstants.IMEETEM_CHAT_FROM_ID, chatMsgFromId);
			}

			if (chatMsgToId != null) {
				obj.put(IMEETEmConstants.IMEETEM_CHAT_TO_ID, chatMsgToId);
			}

			if (chatMsgDate != null) {
				obj.put(IMEETEmConstants.IMEETEM_CHAT_MESS_DATE, chatMsgDate);
			}

			if (chatMsgId != null) {
				obj.put(IMEETEmConstants.IMEETEM_CHAT_MESS_ID, chatMsgId);
			}

			if (chatMsgType != null) {
				obj.put(IMEETEmConstants.IMEETEM_CHAT_MESS_TYPE, chatMsgType);
			}

			if (chatMsgReadStatus != null) {
				obj.put(IMEETEmConstants.IMEETEM_CHAT_MESS_STATUS_RECIEVE,
						chatMsgReadStatus);
			}

			if (chatMsgImageURL != null) {
				obj.put(IMEETEmConstants.SR_MATCH_MEM_IMAGE, chatMsgImageURL);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(this.chatMsgId) * 100;
	}

	public String getChatMsgId() {
		return chatMsgId;
	}

	public void setChatMsgId(String chatMsgId) {
		this.chatMsgId = chatMsgId;
	}

	public String getChatMsgDate() {
		return chatMsgDate;
	}

	public void setChatMsgDate(String chatMsgDate) {
		this.chatMsgDate = chatMsgDate;
	}

	public String getChatMsgFromId() {
		return chatMsgFromId;
	}

	public void setChatMsgFromId(String chatMsgFromId) {
		this.chatMsgFromId = chatMsgFromId;
	}

	public String getChatMsgToId() {
		return chatMsgToId;
	}

	public void setChatMsgToId(String chatMsgToId) {
		this.chatMsgToId = chatMsgToId;
	}

	public String getChatMsgType() {
		return chatMsgType;
	}

	public void setChatMsgType(String chatMsgType) {
		this.chatMsgType = chatMsgType;
	}

	public String getChatMsgReadStatus() {
		return chatMsgReadStatus;
	}

	public void setChatMsgReadStatus(String chatMsgReadStatus) {
		this.chatMsgReadStatus = chatMsgReadStatus;
	}

	public String getChatMsgImageURL() {
		return chatMsgImageURL;
	}

	public void setChatMsgImageURL(String chatMsgImageURL) {
		this.chatMsgImageURL = chatMsgImageURL;
	}

	public Drawable getChatMsgImg() {
		return chatMsgImg;
	}

	public void setChatMsgImg(Drawable chatMsgImg) {
		this.chatMsgImg = chatMsgImg;
	}

	@Override
	public int compareTo(IMEETEmChatMessagesBean another) {
		return this.chatMsgId.compareTo(another.chatMsgId);
	}
}
