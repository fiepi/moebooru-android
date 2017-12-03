package com.fiepi.moebooru.ui;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fiepi.moebooru.R;
import com.fiepi.moebooru.ui.adapter.TagViewAdapter;
import com.fiepi.moebooru.ui.settings.SettingsFragment;
import com.fiepi.moebooru.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import static com.fiepi.moebooru.AppConfig.*;

public class MoebooruActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MoebooruActivity.class.getSimpleName();

    private Fragment mPostFragment = new PostFragment();
    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private TagViewAdapter mTagAdapter;
    private RecyclerView mTagRecyclerView;
    private RecyclerView.LayoutManager mTagLayoutManager;
    private NavBooruFragment mNavBooruFragment = new NavBooruFragment();

    private Toolbar mToolbar;

    private NavigationView mLeftNavigationView;
    private NavigationView mRightNavigationView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moebooru);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Post");
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mLeftNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mLeftNavigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
            mFragmentManager.beginTransaction()
                    .replace(R.id.frag_moebooru, mPostFragment)
                    .commit();
        }

        mRightNavigationView = (NavigationView) findViewById(R.id.nav_right);
//        rightNavigationView.setNavigationItemSelectedListener(this);
        mTagRecyclerView = (RecyclerView) this.findViewById(R.id.rv_tags);
        mTagLayoutManager = new LinearLayoutManager(this);
        mTagRecyclerView.setLayoutManager(mTagLayoutManager);

        mTagAdapter = new TagViewAdapter(this);
        mTagRecyclerView.setAdapter(mTagAdapter);

        View headerView = mLeftNavigationView.getHeaderView(0);
        LinearLayout headerInfoLayout = (LinearLayout) headerView.findViewById(R.id.layout_head_info);
        headerInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "点击头部信息");
                if (mNavBooruFragment.isVisible()){
                    Log.i(TAG, "可见");
                    FrameLayout frameLayout = (FrameLayout) mLeftNavigationView.findViewById(R.id.frag_nav_booru_view);
                    frameLayout.setVisibility(FrameLayout.GONE);
                }else {
                    mNavBooruFragment = new NavBooruFragment();
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frag_nav_booru, mNavBooruFragment)
                            .commit();
                }
            }
        });

        initSearch();

        if (!booruIsExist()){
            initBooruDialog();
        }

        //init nav header
        TextView textViewName = headerView.findViewById(R.id.tv_nav_header_name);
        TextView textViewUrl = headerView.findViewById(R.id.tv_nav_header_url);
        textViewName.setText(new SharedPreferencesUtils().getStringValue(booruUsedPref, booruNameKey));
        textViewUrl.setText(new SharedPreferencesUtils().getStringValue(booruUsedPref, booruTypeKey)
                + new SharedPreferencesUtils().getStringValue(booruUsedPref, booruDomainKey));

        ImageView imageViewLogo = headerView.findViewById(R.id.iv_logo);
        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBooruDialog();
                textViewName.setText(new SharedPreferencesUtils().getStringValue(booruUsedPref, booruNameKey));
                textViewUrl.setText(new SharedPreferencesUtils().getStringValue(booruUsedPref, booruTypeKey)
                        + new SharedPreferencesUtils().getStringValue(booruUsedPref, booruDomainKey));
            }
        });

    }

    private List<String> initData(){
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            data.add("TAG: " + i);
//            Log.i(TAG, data.get(i));
        }
        return data;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else if (mDrawerLayout.isDrawerOpen(GravityCompat.END)){
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }else {
            super.onBackPressed();
        }
    }

    private void initBooruDialog(){

        final LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.dialog_add_booru_view, null)
                .findViewById(R.id.layout_dialog_add_booru);

        final EditText editTextName = (EditText) linearLayout.findViewById(R.id.et_booru_name);
        final EditText editTextDomain = (EditText) linearLayout.findViewById(R.id.et_booru_input);
        final TextView textViewType = (TextView) linearLayout.findViewById(R.id.tv_booru_type);

        AlertDialog.Builder booruDialogBuilder = new AlertDialog.Builder(MoebooruActivity.this);
        booruDialogBuilder.setTitle("Add booru");
        booruDialogBuilder.setView(linearLayout);
        booruDialogBuilder.setCancelable(false);
        booruDialogBuilder.setPositiveButton("OK",null);
        booruDialogBuilder.setNegativeButton("Cancel", null);

        textViewType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(linearLayout.getContext(), textViewType);
                popupMenu.inflate(R.menu.booru_type);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.booru_http:
                                textViewType.setText("http://");
                                new SharedPreferencesUtils().saveString(booruUsedPref, booruTypeKey, "http://");
                                break;
                            case R.id.booru_https:
                                textViewType.setText("https://");
                                new SharedPreferencesUtils().saveString(booruUsedPref, booruTypeKey, "https://");
                                break;
                        }
                        return false;
                    }
                });
            }
        });


        final AlertDialog booruAlertDialog = booruDialogBuilder.create();

        booruAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button addButton = booruAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = editTextName.getText().toString();
                        name = name.replaceAll("\\s*", "");
                        String domain = editTextDomain.getText().toString();
                        domain = domain.replaceAll("\\s*", "");
                        String type = new SharedPreferencesUtils().getStringValue(booruUsedPref, booruTypeKey);
                        if (!name.isEmpty() || !domain.isEmpty()){
                            if (type == "null"){
                                SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils();
                                sharedPreferencesUtils.saveString(booruUsedPref, booruNameKey, name);
                                sharedPreferencesUtils.saveString(booruUsedPref, booruDomainKey, domain);
                                sharedPreferencesUtils.saveString(booruUsedPref, booruTypeKey, "http://");
                                Toast.makeText(MoebooruActivity.this, "Added successfully!", Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            }else {
                                SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils();
                                sharedPreferencesUtils.saveString(booruUsedPref, booruNameKey, name);
                                sharedPreferencesUtils.saveString(booruUsedPref, booruDomainKey, domain);
                                Toast.makeText(MoebooruActivity.this, "Added successfully!", Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            }
                        }else {
                            Toast.makeText(MoebooruActivity.this, "Input can not be empty.", Toast.LENGTH_LONG).show();

                        }
                    }
                });

                Button cancelButton = booruAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        booruAlertDialog.show();
    }

    private Boolean booruIsExist(){
        if (new SharedPreferencesUtils().getStringValue(booruUsedPref, booruDomainKey) == "null"){
            return false;
        }
        return true;
    }

    private void initSearch(){
        ImageView mImageViewAddTag = (ImageView) findViewById(R.id.iv_add_tag);
        ImageView mImageViewSearch = (ImageView) findViewById(R.id.iv_search);

        mImageViewAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(MoebooruActivity.this);
                AlertDialog.Builder inputTagDialog = new AlertDialog.Builder(MoebooruActivity.this);

                inputTagDialog.setTitle("Add Tag");
                inputTagDialog.setView(editText);
                inputTagDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                inputTagDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputString = editText.getText().toString();
                        if (inputString.length() != 0){
                            inputString = inputString.replaceAll("\\s*", "");
                            if (inputString.length() != 0){
                                new SharedPreferencesUtils().saveBoolean(tagPref, inputString, false);
                                mTagAdapter.TagChanged();
                            }
                        }
                        Log.i(TAG, "输入内容："+inputString);
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });

        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoebooruActivity.this, PostSearchActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.END);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_open_search_nav) {
            mDrawerLayout.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_post) {
            Fragment fragment = new PostFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frag_moebooru, fragment)
                    .commit();
            mToolbar.setTitle("Post");

        } else if (id == R.id.nav_booru) {
            Fragment fragment = new BooruFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frag_moebooru, fragment)
                    .commit();
            mToolbar.setTitle("Boorus");
        } else if (id == R.id.nav_settings) {
            SettingsFragment fragment = new SettingsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frag_moebooru, fragment)
                    .commit();
            mToolbar.setTitle("Settings");

        } else if (id == R.id.nav_about) {
            Uri uri = Uri.parse("https://github.com/fiepi/moebooru-android");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
