package com.dpower4.mobac;

import org.json.JSONObject;

import com.dpower4.mobac.models.User;
import com.dpower4.mobac.services.MobacService;
import com.dpower4.mobac.utils.CheckInternetStatus;
import com.dpower4.mobac.utils.MobacAuthSdk;
import com.dpower4.mobac.utils.MobacSdk;
import com.dpower4.mobac.utils.SerializeDeserilizeObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class RegisterActivity extends Activity {
	private Button regButton = null;
	private EditText firstnameEdit = null;
	private EditText usernameEdit = null;
	private EditText passwordEdit = null;

	String username = "";
	String password = "";
	String firstName = "";
	private String email = "";
	private User user = null;
	private EditText emailEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			User user = (User) SerializeDeserilizeObject
					.loadSerializedObject(MainActivity.USER_FILE);

			Toast.makeText(this, "welcome " + user.getFirstName(),
					Toast.LENGTH_LONG).show();

			Intent i = new Intent(getApplicationContext(),
					WelcomeActivity.class);
			startActivity(i);
			finish();
		} catch (Exception e) {
			setContentView(R.layout.register);

			regButton = (Button) findViewById(R.id.btnRegister);
			firstnameEdit = (EditText) findViewById(R.id.reg_fullname);
			usernameEdit = (EditText) findViewById(R.id.reg_phone);
			passwordEdit = (EditText) findViewById(R.id.reg_password);
			emailEdit = (EditText) findViewById(R.id.reg_email);

			regButton.setOnClickListener(new View.OnClickListener() {
				private String authKey;

				public void onClick(View view) {
					username = usernameEdit.getText().toString();
					password = passwordEdit.getText().toString();
					firstName = firstnameEdit.getText().toString();
					email = emailEdit.getText().toString();

					if (username.isEmpty()
							|| password.isEmpty()
							|| firstName.isEmpty()
							|| !CheckInternetStatus
									.isNetworkAvailable(RegisterActivity.this)

					) {

						Toast.makeText(
								getBaseContext(),
								"Name or phone No. or password can not be empty ",
								Toast.LENGTH_LONG).show();
					} else {
						Log.v("EditText", username);

						user = new User(firstName, "", username, email,
								password);
						Log.d("firstName", user.getFirstName());

						MobacSdk mobac = new MobacSdk();

						MobacAuthSdk auth = new MobacAuthSdk();

						try {

							JSONObject response = mobac.registorUser(user
									.toJsonString());
							Log.v("Reg Resp: ", response.toString());
							if (response != null) {

								int i = 0;
								do {
									i++;
									authKey = auth.getAuthKey(user
											.toJsonString());
									Thread.sleep(2000);
								} while (authKey == null && i < 3);

								if (authKey != null || authKey != "") {

									user.setAuthKey(authKey);

									SerializeDeserilizeObject.saveObject(user,
											MainActivity.USER_FILE);
									Intent welcomeActivity = new Intent(
											getApplicationContext(),
											WelcomeActivity.class);
									startActivity(welcomeActivity);
									finish();
								} else {

									Intent welcomeActivity = new Intent(
											getApplicationContext(),
											MainActivity.class);
									startActivity(welcomeActivity);
									finish();

									Toast.makeText(
											getBaseContext(),
											"Auth failed, User registored successfully. Kindly login ",
											Toast.LENGTH_LONG).show();
								}
							}

						} catch (Exception e) {
							Toast.makeText(
									getBaseContext(),
									"Sorry! Something went wrong. Plz try again ",
									Toast.LENGTH_LONG).show();
							Log.e("Reg Exception", e.toString());
						}

					}
				}
			});

			TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

			loginScreen.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {

					Intent welcomeActivity = new Intent(
							getApplicationContext(), MainActivity.class);
					startActivity(welcomeActivity);
					finish();
				}
			});
		}
	}

}