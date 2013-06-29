package com.worthwhilegames.carhubmobile;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by breber on 6/29/13.
 */
public class AddUserFuelRecordActivity extends AdActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfuel);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_record, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This will cover the Android menu button press
        switch (item.getItemId()) {
            case R.id.menu_save:
                // TODO: save
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
