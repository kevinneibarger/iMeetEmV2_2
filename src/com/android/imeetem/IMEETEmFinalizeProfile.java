package com.android.imeetem;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class IMEETEmFinalizeProfile extends FragmentActivity {

	Spinner wantskids, haskids, haircolor, eyecolor, ethnicity_sp,
			education_sp;
	String wantsK, hasK, hair, eyes, ethn, edu, email, bday, userId, gender;
	private EditText emailAdd;
	private EditText birthdate;
	Button finalizeButton;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.finalize_profile);
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		userId = sharedPreferences.getString("userId", userId);
		// The Gender needs in the shared preferences
		gender = "M"; // Change this
		wantskids = (Spinner) findViewById(R.id.wants_kids_spinner);
		haskids = (Spinner) findViewById(R.id.has_kids_spinner);
		haircolor = (Spinner) findViewById(R.id.hair_spinner);
		eyecolor = (Spinner) findViewById(R.id.eye_spinner);
		ethnicity_sp = (Spinner) findViewById(R.id.ethnicity_spinner);
		education_sp = (Spinner) findViewById(R.id.education_spinner);
		emailAdd = (EditText) findViewById(R.id.emailAdd);
		birthdate = (EditText) findViewById(R.id.birthdate);
		finalizeButton = (Button) findViewById(R.id.finalizeprofilebtn);

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
			ArrayAdapter wantsKidsApp = (ArrayAdapter) wantskids.getAdapter();
			/*
			 * String wk = searchFilter.get(9); int wkPos =
			 * wantsKidsApp.getPosition(wk);
			 */
			wantskids.setSelection(0);

			haskids.setAdapter(adapter6);
			ArrayAdapter haskidsApp = (ArrayAdapter) haskids.getAdapter();
			haskids.setSelection(0);

			haircolor.setAdapter(adapter7);
			ArrayAdapter haircolorApp = (ArrayAdapter) haircolor.getAdapter();
			haircolor.setSelection(0);

			eyecolor.setAdapter(adapter8);
			ArrayAdapter eyecolorApp = (ArrayAdapter) eyecolor.getAdapter();
			eyecolor.setSelection(0);

			ethnicity_sp.setAdapter(adapter9);
			ArrayAdapter ethnicityApp = (ArrayAdapter) ethnicity_sp
					.getAdapter();
			ethnicity_sp.setSelection(0);

			education_sp.setAdapter(adapter10);
			ArrayAdapter educationApp = (ArrayAdapter) education_sp
					.getAdapter();
			education_sp.setSelection(0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		finalizeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "Finalizing Profile",
						Toast.LENGTH_LONG).show();

				Button imgBtn = (Button) findViewById(R.id.finalizeprofilebtn);
				imgBtn.setEnabled(false);
				eyes = eyecolor.getSelectedItem().toString();
				hair = haircolor.getSelectedItem().toString();
				ethn = ethnicity_sp.getSelectedItem().toString();
				edu = education_sp.getSelectedItem().toString();
				hasK = haskids.getSelectedItem().toString();
				wantsK = wantskids.getSelectedItem().toString();
				email = emailAdd.getText().toString();
				bday = birthdate.getText().toString();
				// Create Async Task
				// new IMeetEmFinalizeProfileAsync(userId, gender, eyes, hair,
				// ethn, edu, hasK, wantsK,email,bday).execute(" ");

				Intent i = new Intent(v.getContext(), IMEETEmMainActivity.class);
				v.getContext().startActivity(i);

			}
		});
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
		default:
			return super.onOptionsItemSelected(item);
		}

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

}
