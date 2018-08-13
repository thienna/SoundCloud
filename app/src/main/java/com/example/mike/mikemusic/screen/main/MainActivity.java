package com.example.mike.mikemusic.screen.main;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private static final int PERMISSION_CODE_WRITE_EXTERNAL = 1;
    private SearchView mSearchView;
    private MenuItem mSearchMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_CODE_WRITE_EXTERNAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(true);
        mSearchMenu = menu.findItem(R.id.action_search);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchMenu.setOnActionExpandListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
                Toast.makeText(this, "This app need " + permission + " to working properly.",
                        Toast
                                .LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            showIfPermissionSuccess();
//            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:
                    showIfPermissionSuccess();
                    break;
            }
//            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            showDialogPermissionDenied(permissions[0]);
        }
    }

    private void showIfPermissionSuccess() {
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout
                .activity_main);
        MainViewModel mainViewModel = new MainViewModel(this);
        initView();
        activityMainBinding.setViewModel(mainViewModel);
    }

    private void showDialogPermissionDenied(String permission){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("App need permissions")
                .setMessage("This app need " + permission + " to work properly!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue
                        goToSettingActivity();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void goToSettingActivity() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
