package com.android.imeetem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;

public class IMEETEmEmailForm extends Activity {
	Button send; 
	String fromEmail;
	EditText address, subject, emailtext;
	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.contact_us_layout);
	        send=(Button) findViewById(R.id.submitBtn);
	        //address=(EditText) findViewById(R.id.emailText);
	        emailtext=(EditText) findViewById(R.id.messageInfo);
	        final String aEmailList[] = { "rhartleyoh@gmail.com","keithneibarger14@gmail.com","kevinneibarger@hotmail.com"};
	        final String subjectString = "IMeetEm Help";
	        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	        fromEmail = sharedPreferences.getString("userEmail", fromEmail);
	       
	        send.setOnClickListener(new OnClickListener() {
	                       
	                        @Override
	                        public void onClick(View v) {
	                        	
	                                // TODO Auto-generated method stub
	                                 
	                                      final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	                               
	                                      emailIntent.setType("plain/text");
	                                 
	                                      emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
	                               
	                                      emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subjectString);
	                               
	                                      emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailtext.getText());
	                       
	                                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	                                    //sharedPreferences.edit().putString("emailSent","Yes").commit();
	                                    finish();
	 
	                        }

	                });
	    }
	    
	}
	
