package com.dpower4.mobac;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dpower4.mobac.models.User;
import com.dpower4.mobac.services.MobacService;
import com.dpower4.mobac.utils.CheckInternetStatus;
import com.dpower4.mobac.utils.MobacAuthSdk;
import com.dpower4.mobac.utils.SerializeDeserilizeObject;

/**
 * 
 * @author Rahul Lahoria
 * 
 */

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	public static final String USER_FILE = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/user.ser";

	Button loginButton = null;
	EditText usernameEdit = null;
	EditText passwordEdit = null;
	TextView registerScreen = null;

	String username = "";
	String password = "";

	User user = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			User user = (User) SerializeDeserilizeObject
					.loadSerializedObject(USER_FILE);

			Toast.makeText(this, "welcome " + user.getFirstName(),
					Toast.LENGTH_LONG).show();

			Intent i = new Intent(getApplicationContext(),
					WelcomeActivity.class);
			startActivity(i);
			finish();
		} catch (Exception e) {
			setContentView(R.layout.login);
			// btnLogin
			//Toast.makeText(this, "Login to continu ", Toast.LENGTH_LONG).show();

			loginButton = (Button) findViewById(R.id.btnLogin);
			usernameEdit = (EditText) findViewById(R.id.phoneET);
			passwordEdit = (EditText) findViewById(R.id.passwordET);
			registerScreen = (TextView) findViewById(R.id.link_to_register);

			loginButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					username = usernameEdit.getText().toString();
					password = passwordEdit.getText().toString();
					Log.v("EditText", usernameEdit.getText().toString());
					if (
							username.isEmpty() || 
							password.isEmpty() || 
							!CheckInternetStatus.isNetworkAvailable(MainActivity.this)
						){
						Toast.makeText(getBaseContext(),
								"Phone No. or password can not be empty ",
								Toast.LENGTH_LONG).show();
					}
					else {

						user = new User("", "", username, "", password);
						MobacAuthSdk auth = new MobacAuthSdk();

						try {
							String authKey = null;
							int i = 0 ;

							do {
								i++;
								authKey = auth.getAuthKey(user.toJsonString());
								Log.d("auth",authKey);
								Thread.sleep(2000);
							} while ((authKey == null || authKey.equalsIgnoreCase("null")) && i < 3);

							if (!authKey.isEmpty() && authKey != null && !authKey.equalsIgnoreCase("null")) {
								user.setAuthKey(authKey);

								Log.d("firstName", user.getFirstName());

								Toast.makeText(getBaseContext(),
										"welcome " + user.getFirstName(),
										Toast.LENGTH_LONG).show();

								SerializeDeserilizeObject.saveObject(user,
										USER_FILE);

								Intent welcomeActivity = new Intent(
										getApplicationContext(),
										WelcomeActivity.class);
								startActivity(welcomeActivity);
								finish();
							}

						} catch (IOException e1) {
							Toast.makeText(
									getBaseContext(),
									"Sorry! Something went wrong. Plz try again ",
									Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							Toast.makeText(getBaseContext(),
									"Error: " + e.toString(),
									Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
							Toast.makeText(getBaseContext(),
									"Error: " + e.toString(),
									Toast.LENGTH_SHORT).show();
						}

					}
				}
			});

			registerScreen.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					Intent i = new Intent(getApplicationContext(),
							RegisterActivity.class);
					startActivity(i);
					finish();
				}
			});

		}
	}

}