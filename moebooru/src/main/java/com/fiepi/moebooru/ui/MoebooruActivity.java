package com.fiepi.moebooru.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.fiepi.moebooru.util.SharedPreferencesUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoebooruActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MoebooruActivity.class.getSimpleName();

    private static final String namePref = "booru_used";
    private static final String booruTypeKey = "booru_type";
    private static final String booruNameKey = "booru_name";
    private static final String booruDomainKey = "booru_domain";

    private static final String nameTagPref = "tag_search";

    private Fragment mPostFragment = new PostFragment();
    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private TagViewAdapter mTagAdapter;
    private RecyclerView mTagRecyclerView;
    private RecyclerView.LayoutManager mTagLayoutManager;
    private NavBooruFragment mNavBooruFragment = new NavBooruFragment();

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moebooru);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView leftNavigationView = (NavigationView) findViewById(R.id.nav_view);
        leftNavigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
            mFragmentManager.beginTransaction()
                    .replace(R.id.frag_moebooru, mPostFragment)
                    .commit();
        }

        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_right);
//        rightNavigationView.setNavigationItemSelectedListener(this);
        mTagRecyclerView = (RecyclerView) this.findViewById(R.id.rv_tags);
        mTagLayoutManager = new LinearLayoutManager(this);
        mTagRecyclerView.setLayoutManager(mTagLayoutManager);

        mTagAdapter = new TagViewAdapter(this);
        mTagRecyclerView.setAdapter(mTagAdapter);

        View headerView = leftNavigationView.getHeaderView(0);
        LinearLayout headerInfoLayout = (LinearLayout) headerView.findViewById(R.id.layout_head_info);
        headerInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "点击头部信息");
                if (mNavBooruFragment.isVisible()){
                    Log.i(TAG, "可见");
                    FrameLayout frameLayout = (FrameLayout) leftNavigationView.findViewById(R.id.frag_nav_booru_view);
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
        textViewName.setText(new SharedPreferencesUtils().getStringValus(namePref, booruNameKey));
        textViewUrl.setText(new SharedPreferencesUtils().getStringValus(namePref, booruTypeKey)
                + new SharedPreferencesUtils().getStringValus(namePref, booruDomainKey));

        ImageView imageViewLogo = headerView.findViewById(R.id.iv_logo);
        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBooruDialog();
                textViewName.setText(new SharedPreferencesUtils().getStringValus(namePref, booruNameKey));
                textViewUrl.setText(new SharedPreferencesUtils().getStringValus(namePref, booruTypeKey)
                        + new SharedPreferencesUtils().getStringValus(namePref, booruDomainKey));
            }
        });

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
        booruDialogBuilder.setPositiveButton("ADD",null);

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
                                new SharedPreferencesUtils().saveString(namePref, booruTypeKey, "http://");
                                break;
                            case R.id.booru_https:
                                textViewType.setText("https://");
                                new SharedPreferencesUtils().saveString(namePref, booruTypeKey, "https://");
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
                        String type = new SharedPreferencesUtils().getStringValus(namePref, booruTypeKey);
                        if (!name.isEmpty() || !domain.isEmpty()){
                            if (type == "null"){
                                SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils();
                                sharedPreferencesUtils.saveString(namePref, booruNameKey, name);
                                sharedPreferencesUtils.saveString(namePref, booruDomainKey, domain);
                                sharedPreferencesUtils.saveString(namePref, booruTypeKey, "http://");
                                Toast.makeText(MoebooruActivity.this, "Added successfully!", Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            }else {
                                SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils();
                                sharedPreferencesUtils.saveString(namePref, booruNameKey, name);
                                sharedPreferencesUtils.saveString(namePref, booruDomainKey, domain);
                                Toast.makeText(MoebooruActivity.this, "Added successfully!", Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            }
                        }else {
                            Toast.makeText(MoebooruActivity.this, "Input can not be empty.", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
        booruAlertDialog.show();
    }

    private Boolean booruIsExist(){
        if (new SharedPreferencesUtils().getStringValus(namePref, booruDomainKey) == "null"){
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
                                new SharedPreferencesUtils().saveBoolean(nameTagPref, inputString, false);
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
                getAnalytics("search", "tags search", "search");
                Intent intent = new Intent(MoebooruActivity.this, PostSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getAnalytics(String id, String name, String type){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void getSelectedTags(){
        SharedPreferences tagPref = MoebooruActivity.this.getSharedPreferences("tags", MoebooruActivity.MODE_PRIVATE);
        Map<String, ?> allTag = tagPref.getAll();
        String tags = "";
//        Log.i(TAG, tagPref.getAll().toString());
        for (Map.Entry<String, ?>  entry : allTag.entrySet()){
            if(entry.getValue().equals(true)){
                if (tags == ""){
                    tags = entry.getKey();
                }else {
                    tags = tags + "+" + entry.getKey();
                }
            }
        }
//        Log.i(TAG, "TAG: "+ tags);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
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

        } else if (id == R.id.nav_booru) {
            Fragment fragment = new BooruFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frag_moebooru, fragment)
                    .commit();

        } else if (id == R.id.nav_download) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
