
package com.flowolf86.androidswipetodelete.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by github.com/flowolf86/ on 20.07.15
 *
 * A base activity handling basic android specific tasks. E.g. Setting up a toolbar or
 * handling Android M permissions
 */
public class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // TODO Marshmallow permissions
        // PermissionManager.getInstance().doFullPermissionCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        // TODO Marshmallow permissions
        /*switch (requestCode) {
            case PermissionManager.REQUEST_CODE_ASK_CALENDAR_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PermissionManager.HAS_CALENDAR_PERMISSIONS = true;
                } else {
                    PermissionManager.HAS_CALENDAR_PERMISSIONS = false;
                }
                break;
            case PermissionManager.REQUEST_CODE_ASK_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PermissionManager.HAS_STORAGE_PERMISSIONS = true;
                } else {
                    PermissionManager.HAS_STORAGE_PERMISSIONS = false;
                }
                break;
            case PermissionManager.REQUEST_CODE_ASK_CONTACTS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PermissionManager.HAS_CONTACTS_PERMISSIONS = true;
                } else {
                    PermissionManager.HAS_CONTACTS_PERMISSIONS = false;
                }
                break;
            case PermissionManager.REQUEST_CODE_ASK_LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PermissionManager.HAS_LOCATION_PERMISSIONS = true;
                } else {
                    PermissionManager.HAS_LOCATION_PERMISSIONS = false;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    /**
     * In case we define the toolbar directly in our layout
     *
     * @param toolbarId
     * @param setDisplayHomeAsEnabled
     */
    public void setupToolbar(int toolbarId, boolean setDisplayHomeAsEnabled){

        mToolbar = (Toolbar) findViewById(toolbarId);

        if(mToolbar != null){
            setSupportActionBar(mToolbar);
            if(getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(setDisplayHomeAsEnabled);
            }
        }
    }

    /**
     * In case we <include /> the toolbar to our layout
     *
     * @param parentViewId
     * @param toolbarId
     * @param setDisplayHomeAsEnabled
     */
    public void setupToolbar(int parentViewId, int toolbarId, boolean setDisplayHomeAsEnabled){

        final View view = findViewById(parentViewId);
        mToolbar = (Toolbar) view.findViewById(toolbarId);

        if(mToolbar != null){
            setSupportActionBar(mToolbar);
            if(getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(setDisplayHomeAsEnabled);
            }
        }
    }

    /**
     * Only use when NOT setting up the toolbar to act as supportActionBar
     * If you did, please call #getSupportActionBar() instead
     *
     * @return
     */
    public @Nullable
    Toolbar getToolbar(){
        return mToolbar;
    }

    /**
     * Sets the toolbar title if a toolbar or supportactionbar is available
     *
     * @param title
     * @return
     */
    public boolean setToolbarTitle(@Nullable final String title){

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
            return true;
        }else if(getToolbar() != null){
            getToolbar().setTitle(title);
            return true;
        }

        return false;
    }
}
