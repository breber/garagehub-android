package com.worthwhilegames.carhubmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * @author jamiekujawa
 *
 */
public class MainMenuActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		ImageButton getGasPrices = (ImageButton) findViewById(R.id.gasPrices);
		getGasPrices.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent gasPriceButtonClick = new Intent(MainMenuActivity.this,
						FindGasPricesActivity.class);
				startActivity(gasPriceButtonClick);
			}
		});

		ImageButton about = (ImageButton) findViewById(R.id.about);
		about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent aboutButtonClick = new Intent(MainMenuActivity.this,
						AboutActivity.class);
				startActivity(aboutButtonClick);
			}
		});
	}
}
