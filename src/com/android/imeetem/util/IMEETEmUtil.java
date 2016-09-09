/**
 * 
 */
package com.android.imeetem.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.imeetem.R;
import com.android.imeetem.asynctasks.IMeetEmSendOneTimeMsg;
import com.android.imeetem.services.beans.IMEETEmChatMessagesBean;
import com.android.imeetem.services.beans.IMEETEmMemberInfoBean;

/**
 * @author kevinscomp
 * 
 */
public class IMEETEmUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	private ResourceBundle imeetem_permissions_props;
	private ResourceBundle imeetem_services_props;

	// private boolean isSystemDown;

	private IMEETEmUtil() {

		// Load the properties files for permissions and services
		this.imeetem_permissions_props = ResourceBundle
				.getBundle("com.android.imeetem.util.properties.iMeetEmPermissions");
		this.imeetem_services_props = ResourceBundle
				.getBundle("com.android.imeetem.util.properties.iMeetEmServices");
	}

	public static IMEETEmUtil getInstance() {
		return new IMEETEmUtil();
	}

	public List<String> getIMeetEmPermissions() {

		List<String> permissionsList = new ArrayList<String>();

		// ResourceBundle rb =
		// ResourceBundle.getBundle("com.android.imeetem.util.properties.iMeetEmPermissions");
		String permissionsString = (String) this.imeetem_permissions_props
				.getString("permissions");
		String[] permissionTokens = permissionsString
				.split("\\s*(=>|,|\\s)\\s*");

		for (String perms : permissionTokens) {
			permissionsList.add(perms);
		}

		return permissionsList;
	}

	public Bitmap toGrayscale(Drawable d) {

		Bitmap bmpOriginal = ((BitmapDrawable) d).getBitmap();
		int width = bmpOriginal.getWidth();
		int height = bmpOriginal.getHeight();

		Bitmap bmpGrayScale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayScale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayScale;
	}

	/**
	 * This method will get the PHP service URI from the properties file and
	 * then add parameters if applicable
	 * 
	 * @param key
	 *            A String representing the key referencing the appropriate URI
	 *            in the properties file.
	 * @param params
	 *            A List of Strings representing the
	 * @return
	 */
	public String getPHPServiceURI(String key, List<String> params) {

		StringBuilder phpServiceURI = new StringBuilder();
		String phpServiceBaseURI = null;

		if (key != null) {
			// Get the base UI
			phpServiceBaseURI = (String) this.imeetem_services_props
					.getString(key);

			// Build Params into the Base URI
			if (params != null && params.size() > 0) {
				phpServiceURI.append(buildPHPServiceURIWithParams(params,
						phpServiceBaseURI));
			} else {
				phpServiceURI.append(phpServiceBaseURI);
			}
		}

		return phpServiceURI.toString();
	}

	/**
	 * This method will take the Base URI of the PHP service and add the
	 * parameters if applicable.
	 * 
	 * @param params
	 *            A List of parameters for the URI if applicable
	 * @param baseURI
	 *            A String representing the base URI of the PHP service
	 * @return A String representing the service PHP URI with parameters if
	 *         applicable.
	 */
	private String buildPHPServiceURIWithParams(List<String> params,
			String baseURI) {

		int count = 1;

		for (String parm : params) {
			String temp = baseURI.replace("[PARAM" + count + "]", parm);
			baseURI = temp;
			temp = null;
			count++;
		}

		return baseURI;
	}

	/**
	 * This method will build the Member Info Bean with Facebook information
	 * from the SharedPreferences.
	 * 
	 * @return An IMEETEmMemberInfoBean with a known value
	 */
	public IMEETEmMemberInfoBean buildFBMemberInfoFromSharedPrefences(
			SharedPreferences prefs) {

		String memberId = null;
		String imageName = null;
		String memberEmail = null;
		String birthdate = null;
		String firstName = null;
		String lastName = null;
		String gender = null;

		System.out
				.println("<<<< BUILDING FB INFO FROM SHARED PREFERENCES >>>> ");

		IMEETEmMemberInfoBean bean = IMEETEmMemberInfoBean.getInstance();

		if (prefs.getString("fbId", memberId) != null) {
			bean.setMemFBId(prefs.getString("fbId", memberId));
			System.out.println("Got User Id: " + bean.getMemFBId());
		}

		if (prefs.getString("imageName", imageName) != null) {
			bean.setMemImageLoc(prefs.getString("imageName", imageName));
			System.out.println("Got Image Loc: " + bean.getMemImageLoc());
		}

		if (prefs.getString("memberEmail", memberEmail) != null) {
			bean.setEmailId(prefs.getString("memberEmail", memberEmail));
			System.out.println("Got User Email: " + bean.getEmailId());
		}

		if (prefs.getString("birthdate", birthdate) != null) {
			// Parse the Birth Day and Year from birthdate
			int year = 0;
			int month = 0;
			int day = 0;

			try {

				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				Calendar cal = Calendar.getInstance();
				Date date = formatter.parse(prefs.getString("birthdate",
						birthdate));
				cal.setTime(date);

				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH);
				day = cal.get(Calendar.DATE);

				bean.setBirthDay(day);
				bean.setBirthMonth(month + 1);
				bean.setBirthYear(year);

				bean.setBirthDay(prefs.getInt("day", day));
				bean.setBirthMonth(prefs.getInt("month", month + 1));
				bean.setBirthYear(prefs.getInt("year", year));

				System.out.println("Got User Birth Year: "
						+ bean.getBirthYear());
				System.out.println("Got User Birth Month: "
						+ bean.getBirthMonth());
				System.out.println("Got User Birth Day: " + bean.getBirthDay());

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (prefs.getString("firstName", firstName) != null) {
			bean.setMemFName(prefs.getString("firstName", firstName));
			System.out.println("Got User First Name: " + bean.getMemFName());
		}

		if (prefs.getString("lastName", lastName) != null) {
			bean.setMemLName(prefs.getString("lastName", lastName));
			System.out.println("Got User Last Name: " + bean.getMemLName());
		}

		if (prefs.getString("gender", gender) != null) {
			bean.setMemGender(prefs.getString("gender", gender));
			System.out.println("Got User Gender: " + bean.getMemGender());
		}
		return bean;
	}

	/**
	 * This method will create a Drawable Object from a URL String
	 * 
	 * @param ctx
	 *            A Context Object with a known value
	 * @param url
	 *            A String representing the URL of the facebook or stored
	 *            picture
	 * @return A Drawable Object representing the picture
	 */
	public Drawable createDrawableImg(Context ctx, String url) {
		try {

			InputStream is = (InputStream) fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");

			return d;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This gets a URL and puts it into an InputStream
	 * 
	 * @param address
	 *            A String representing the URL
	 * @return An Object representing a URL
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private Object fetch(String address) throws MalformedURLException,
			IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	/**
	 * This method will set the image and text as clickable for the friends list
	 * and messages list pages. This is put here to avoid code redundancy.
	 * 
	 * @param v
	 *            - A View Object with a known value
	 * @param memName
	 *            - A String representing the members name
	 * @param memAge
	 *            - A String representing the members age
	 * @param distance
	 *            - A String representing the members distance
	 * @param impactId
	 *            - A String representing the member id of the match
	 * @param userId
	 *            - A String representing the member id of the user
	 * @param image
	 *            - A Drawable Object representing the match photo
	 * @param fromScreen
	 *            - A String representing the screen from which
	 *            MatchInfoActivity is being called.
	 * @return An Intent Object with the appropriate data to run the
	 *         MatchInfoActivity.
	 */
	public Intent setClickableForImageAndText(MatchInfoBean matchInfo,
			String userId, String fromScreen, Intent i, String imgUrl,
			String message) {

		String memName = matchInfo.getMemberName();
		String memAge = matchInfo.getMemberAge();
		String distance = matchInfo.getMemberDistance();
		String impactId = matchInfo.getMemberId();
		Drawable image = matchInfo.getImageDrawable();
		String hairColor = matchInfo.getMemberHairColor();
		String eyeColor = matchInfo.getMemberEyeColor();
		boolean hasKids = matchInfo.isMemberHasKids();
		boolean wantsKids = matchInfo.isMemberWantsKids();
		String education = matchInfo.getMemberEducation();
		String ethnicity = matchInfo.getMemberEthnicity();

		// Intent i = new Intent(v.getContext(), MatchInfoActivity.class);

		i.putExtra(IMEETEmConstants.MATCH_NAME, memName);
		i.putExtra(IMEETEmConstants.MATCH_AGE, memAge);

		// Load from MatchInfoBean when data is available.
		i.putExtra(IMEETEmConstants.MATCH_DISTANCE, distance);
		i.putExtra(IMEETEmConstants.MATCH_HAIR, hairColor);
		i.putExtra(IMEETEmConstants.MATCH_EYES, eyeColor);
		i.putExtra(IMEETEmConstants.MATCH_HAS_KIDS, (hasKids ? "Yes" : "No"));
		i.putExtra(IMEETEmConstants.MATCH_WANTS_KIDS,
				(wantsKids ? "Yes" : "No"));
		i.putExtra(IMEETEmConstants.MATCH_ETHNICITY, ethnicity);
		i.putExtra(IMEETEmConstants.MATCH_EDUCATION, education);
		i.putExtra(IMEETEmConstants.MATCH_250_CHAR_INFO,
				"I love NCAA basketball and fried shrimp, lets get together at the Sizzler.. ");

		i.putExtra(IMEETEmConstants.MATCH_FROM_SCREEN, fromScreen);
		i.putExtra(IMEETEmConstants.MATCH_MEMBER_ID, userId);
		i.putExtra(IMEETEmConstants.MATCH_IMPACT_ID, impactId);

		if (message != null) {
			i.putExtra(IMEETEmConstants.IMEETEM_CHAT_MESS_MESSAGE, message);
		}
		if (imgUrl != null) {
			i.putExtra("ImageURL", imgUrl);
		}

		Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bitMapData = stream.toByteArray();
		i.putExtra(IMEETEmConstants.MATCH_MEMBER_PHOTO, bitMapData);

		return i;
	}

	public String getFieldFrom6MatchesJSONData(String fieldName,
			String resultsJSON, String memberId) {

		String returnField = null;

		System.out.println("\n\n ==== Getting field: " + fieldName
				+ " from Results JSON: " + resultsJSON + " for member: "
				+ memberId + " ======\n\n\n");

		try {
			JSONArray jArray = new JSONArray(resultsJSON);

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);

				if (json_data.getString(fieldName) != null
						&& json_data
								.getString(IMEETEmConstants.SR_MATCH_MEM_ID) != null
						&& json_data
								.getString(IMEETEmConstants.SR_MATCH_MEM_ID)
								.equalsIgnoreCase(memberId)) {

					returnField = json_data.getString(fieldName);
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return returnField;
	}

	public MatchInfoBean convertToMatchInfoListFromIntent(Intent i) {

		MatchInfoBean matchInfoBean = MatchInfoBean.getInstance();

		// Get Member Name and Age
		matchInfoBean.setMemberName(i.getExtras().getString(
				IMEETEmConstants.MATCH_NAME));
		matchInfoBean.setMemberAge(i.getExtras().getString(
				IMEETEmConstants.MATCH_AGE));
		matchInfoBean.setMemberDistance(i.getExtras().getString(
				IMEETEmConstants.MATCH_DISTANCE));
		matchInfoBean.setMemberHairColor(i.getExtras().getString(
				IMEETEmConstants.MATCH_HAIR));
		matchInfoBean.setMemberEyeColor(i.getExtras().getString(
				IMEETEmConstants.MATCH_EYES));
		matchInfoBean.setMemberHasKids((i.getExtras()
				.getString(IMEETEmConstants.MATCH_HAS_KIDS)
				.equalsIgnoreCase("Yes") ? true : false));
		matchInfoBean.setMemberWantsKids((i.getExtras()
				.getString(IMEETEmConstants.MATCH_WANTS_KIDS)
				.equalsIgnoreCase("Yes") ? true : false));
		matchInfoBean.setMemberEthnicity(i.getExtras().getString(
				IMEETEmConstants.MATCH_ETHNICITY));
		matchInfoBean.setMemberEducation(i.getExtras().getString(
				IMEETEmConstants.MATCH_EDUCATION));
		matchInfoBean.setMember250CharText(i.getExtras().getString(
				IMEETEmConstants.MATCH_250_CHAR_INFO));
		matchInfoBean.setMemberId(i.getExtras().getString(
				IMEETEmConstants.MATCH_IMPACT_ID));

		byte[] b = i.getExtras().getByteArray("imageDrawable");
		Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
		Drawable memberImg = new BitmapDrawable(null, bmp);
		matchInfoBean.setImageDrawable(memberImg);

		return matchInfoBean;
	}

	public Drawable getImageDrawable(Drawable imageDrawable, String imageURL) {

		if (imageDrawable == null && imageURL != null) {
			System.out
					.println("\n\n >>>>> IMAGE DRAWABLE IS NULL!! <<<<<<< \n\n");
			return createDrawableImg(imageURL, "image");
		} else {
			System.out
					.println("\n\n >>>>> IMAGE DRAWABLE IS NOT NULL!! <<<<<<< \n\n");
			return imageDrawable;
		}

	}

	public Drawable createDrawableImg(String url, String saveFilename) {
		try {

			InputStream is = (InputStream) fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");

			return d;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public IMEETEmChatMessagesBean convertTextMessageToChatMsgBean(
			String textMessage) throws JSONException {

		JSONObject json_data = new JSONObject(textMessage);
		IMEETEmChatMessagesBean bean = IMEETEmChatMessagesBean.getInstance();

		if (textMessage != null) {

			System.out
					.println("\n\n ###### Got the JSON Text Message ##### \n\n");

			String chatMsgTxt = json_data
					.getString(IMEETEmConstants.IMEETEM_CHAT_MESS_MESSAGE);
			String chatMsgImgUrl = json_data
					.getString(IMEETEmConstants.IMEETEM_CHAT_MESS_IMG_URL);
			String chatMsgFromId = json_data
					.getString(IMEETEmConstants.IMEETEM_CHAT_FROM_ID);
			String chatMsgToId = json_data
					.getString(IMEETEmConstants.IMEETEM_CHAT_TO_ID);

			bean.setChatMsgText(chatMsgTxt);
			bean.setChatMsgImageURL(chatMsgImgUrl);
			bean.setChatMsgFromId(chatMsgFromId);
			bean.setChatMsgToId(chatMsgToId);

			System.out.println("\n\n ###### Got the CHAT MessageBean\n"
					+ bean.toString() + " ##### \n\n");
		} else {
			System.out
					.println("\n\n\n $$$$$$$   JSON DATA IS NULL!!!! $$$$$$$$$$$$$$$$$$ \n\n\n");
		}
		return bean;
	}

	public void showSystemDownDialog(Context context,
			final boolean fromLoginScreen) {

		System.out
				.println("SYSTEMDOWN-SYSTEMDOWN-SYSTEMDOWN-SYSTEMDOWN-SYSTEMDOWN ^^ -SYSTEMDOWN-SYSTEMDOWN-SYSTEMDOWN-SYSTEMDOWN");
		LayoutInflater inflater = null;
		AlertDialog.Builder builder = null;

		if (context != null) {

			builder = new AlertDialog.Builder(context,
					R.style.CustomAlertDialog);
			inflater = LayoutInflater.from(context);

			View view = inflater.inflate(R.layout.system_down_for_maintenance,
					null);

			Drawable bgDrawable = view.getBackground();
			bgDrawable.setAlpha(50);
			view.setBackground(bgDrawable);

			Button tryAgainBtn = (Button) view.findViewById(R.id.tryAgainBtn);

			builder.setCustomTitle(view);
			builder.setIcon(android.R.drawable.ic_dialog_alert);

			final AlertDialog alert = builder.create();

			Handler h = new Handler(Looper.getMainLooper());
			h.post(new Runnable() {

				@Override
				public void run() {
					System.out
							.println("\n\n\n ******* Showing System Maintenance Break Popup! ************ \n\n\n");
					alert.show();
				}
			});

			tryAgainBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					alert.cancel();
				}
			});

		}
	}

	public void displayOneTimeMsgPopup(View oneTimeMsgRemoveMsgView,
			final LayoutInflater inflater, String fromScreen,
			final String fromMemId, final String toMemId) {

		ImageButton onetimeMsgBtn = null;
		TextView oneTimeMsgLink = null;
		View onetimeMsg = null;

		if (fromScreen.equals(IMEETEmConstants.CALLED_FROM_FRIENDS_LIST)) {
			onetimeMsgBtn = (ImageButton) oneTimeMsgRemoveMsgView
					.findViewById(R.id.onetimemsgbackbtn_friends);
			onetimeMsg = onetimeMsgBtn;
		} else if (fromScreen.equals(IMEETEmConstants.CALLED_FROM_MATCH_INFO)) {
			oneTimeMsgLink = (TextView) oneTimeMsgRemoveMsgView
					.findViewById(R.id.oneTimeMessage);
			onetimeMsg = oneTimeMsgLink;
		}

		onetimeMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				View promptView = inflater.inflate(
						R.layout.send_onetime_message, null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						v.getContext());

				alertDialogBuilder.setView(promptView);

				final EditText input = (EditText) promptView
						.findViewById(R.id.onetimemessageInfo);

				Button sendBtn = (Button) promptView
						.findViewById(R.id.submitBtn);

				Button cancelBtn = (Button) promptView
						.findViewById(R.id.cancelBtn);

				final AlertDialog alertD = alertDialogBuilder.create();

				cancelBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertD.cancel();
					}
				});

				sendBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						System.out
								.println("%%%%%%%%%%%%%%%%%%%% IN UTIL METHOD, SENDING 1 TIME MESSAGE "
										+ input.getText().toString()
										+ " %%%%%%%%%%%%%%%%%%%%%");
						new IMeetEmSendOneTimeMsg(fromMemId, toMemId, input
								.getText().toString()).execute(" ");
						alertD.cancel();
					}
				});

				alertD.show();

			}
		});

	}
}
