package com.android.imeetem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.imeetem.services.impl.IMEETEmServicesUtilImpl;
import com.android.imeetem.util.IMEETEmConstants;

//import com.android.imeetem.asynctasks.IMeetEmMainAsyncTask.IMeetEmNextEm;

public class IMEETEmEmailAndBirthday extends FragmentActivity {

	private ImageButton nextEmButton;
	private EditText emailAdd;
	private EditText birthdate;
	private String userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.email_bday_enter_screen);
		emailAdd = (EditText) findViewById(R.id.emailAdd);
		final SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		userId = sharedPreferences.getString("userId", userId);
		birthdate = (EditText) findViewById(R.id.birthdate);
		ImageButton imgBtn = (ImageButton) findViewById(R.id.updatebdayandemail);

		final IMEETEmEmailAndBirthday emailBdayActivity = this;

		imgBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IMeetEmUpdateBirthdayEmail emailBd = new IMeetEmUpdateBirthdayEmail(
						emailBdayActivity);
				emailBd.execute();
				Intent i = new Intent(v.getContext(), IMEETEmMainActivity.class);
				v.getContext().startActivity(i);
				// sharedPreferences.edit().remove("getEmailAndBirthday").commit();//
				// reset it so we only call it once
			}
		});
	}

	public void selectDate(View view) {
		DialogFragment newFragment = new SelectDateFragment();
		newFragment.show(getFragmentManager(), "DatePicker");
	}

	public void populateSetDate(int year, int month, int day) {
		birthdate = (EditText) findViewById(R.id.birthdate);
		birthdate.setText(month + "/" + day + "/" + year);
	}

	public class SelectDateFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			populateSetDate(yy, mm + 1, dd);
		}
	}

	public class IMeetEmUpdateBirthdayEmail extends
			AsyncTask<String, Integer, String> {
		IMEETEmServicesUtilImpl util = IMEETEmServicesUtilImpl.getInstance();
		String email = emailAdd.getText().toString();
		int birthMon = 1;
		int birthDay = 16;
		int birthYear = 1978;
		String memId = userId;
		String bd = birthdate.getText().toString();

		private final Context context;

		public IMeetEmUpdateBirthdayEmail(IMEETEmEmailAndBirthday activity) {
			this.context = activity;
		}

		@Override
		protected String doInBackground(String... params) {

			boolean isSystemDown = util
					.checkSystemStatus(IMEETEmConstants.IMEETEM_SYS_ANDROID);

			System.out
					.println("^^^^^^^^^^^^^^^^^^^ IS DOWN FOR MAINTENANCE (IMeetEmUpdateBirthdayEmail.doInBackground)? "
							+ (isSystemDown ? "YES" : "NO")
							+ " ^^^^^^^^^^^^^^^^^^^");

			if (!isSystemDown) {

				try {

					SimpleDateFormat formatter = new SimpleDateFormat(
							"mm/dd/yyyy");
					Calendar cal = Calendar.getInstance();
					Date date = formatter.parse(bd);
					cal.setTime(date);

					birthYear = cal.get(Calendar.YEAR);
					birthMon = cal.get(Calendar.MONTH);
					birthDay = cal.get(Calendar.DATE);

					birthMon = birthMon + 1;

				} catch (ParseException e) {
					e.printStackTrace();
				}

				// Call to the util class to update the birthdate and email
				// address
				util.updateEmailAndBirthdate(email, String.valueOf(birthMon),
						String.valueOf(birthDay), String.valueOf(birthYear),
						memId);
				return "";
			} else {
				return "SystemDown";
			}
		}

		@Override
		protected void onPostExecute(String result) {

			if (result != null && result.equals("SystemDown")) {
				Intent i = new Intent(context, IMEETEmMainActivity.class);
				i.putExtra("SystemIsDown", "DOWN");
				context.startActivity(i);
			} else {
				System.out.println("DONE: Got result onPostExecute: " + result);
			}
		}

	}

}
