/**
 * 
 */
package com.android.imeetem.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.imeetem.R;

/**
 * @author kneibarger
 * 
 */
public class CustomizeDialog extends Dialog implements OnClickListener {
	Button okButton;
	Context mContext;
	TextView mTitle = null;
	TextView mMessage = null;
	View v = null;
	Button cancelButton;
	ProgressBar progressBar;

	public CustomizeDialog(Context context) {
		super(context);
		mContext = context;
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.custom_dialog);

		v = getWindow().getDecorView();
		ImageView img = (ImageView) findViewById(R.id.imageView1);
		img.setBackgroundResource(R.drawable.imeetem_searching_animation);

		// Get the background, which has been compiled to an AnimationDrawable
		// object.
		AnimationDrawable frameAnimation = (AnimationDrawable) img
				.getBackground();

		// Start the animation (looped playback by default).
		frameAnimation.start();

	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
	}

	@Override
	public void setTitle(int titleId) {
		super.setTitle(titleId);
	}

	/**
	 * Set the message text for this dialog's window.
	 * 
	 * @param message
	 *            - The new message to display in the title.
	 */
	public void setMessage(CharSequence message) {
		mMessage.setText(message);
		mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	/**
	 * Set the message ID
	 * 
	 * @param messageId
	 */
	public void setMessage(int messageId) {
		mMessage.setText(mContext.getResources().getString(messageId));
		mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	}
}