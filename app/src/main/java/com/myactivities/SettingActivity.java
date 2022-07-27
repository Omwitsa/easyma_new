package com.myactivities;

import java.util.Locale;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SettingActivity extends MyActivity {
	Spinner spLanguage;
	Button btnReturn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		myToolbar.setTitle("Settings");
		setSupportActionBar(myToolbar);

		myToolbar.setNavigationOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				finish();
			}
		});

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		spLanguage = (Spinner) findViewById(R.id.spLanguage);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.language, android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spLanguage.setAdapter(adapter);
		Locale locale = Locale.getDefault();
		if (locale.equals(Locale.KOREAN)) {
			spLanguage.setSelection(1);
		} else {
			spLanguage.setSelection(0);
		}
		spLanguage
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {
						Locale locale2;
						Configuration config = new Configuration();
						if (position == 0) {
							locale2 = Locale.ENGLISH;
							Locale.setDefault(locale2);
							config.locale = locale2;
						} else if (position == 1) {
							locale2 = Locale.KOREAN;
							Locale.setDefault(locale2);
							config.locale = locale2;

						}
						getBaseContext().getResources().updateConfiguration(
								config,
								getBaseContext().getResources()
										.getDisplayMetrics());
						SharedPreferences settings = getSharedPreferences(
								"locale", 0);
						settings.edit().putInt("Locale", position).commit();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		btnReturn = (Button) findViewById(R.id.btnReturn);
		btnReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//startActivity(new Intent(SettingActivity.this, MainActivity.class));
				finish();
			}
		});

	}

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
			return false;
		}
		return false;
	}

	protected void dialog() {
		AlertDialog.Builder build = new AlertDialog.Builder(SettingActivity.this);
		build.setTitle(R.string.message);
		build.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
		build.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		build.create().show();
	}*/
}