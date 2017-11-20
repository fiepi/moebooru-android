package com.fiepi.moebooru.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fiepi.moebooru.R;
import com.fiepi.moebooru.ui.adapter.TagViewAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MoebooruActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MoebooruActivity.class.getSimpleName();

    private Fragment mPostFragment = new PostFragment();
    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private TagViewAdapter mTagAdapter;
    private RecyclerView mTagRecyclerView;
    private RecyclerView.LayoutManager mTagLayoutManager;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        mTagAdapter = new TagViewAdapter(initData(), this);
        mTagRecyclerView.setAdapter(mTagAdapter);
    }

    private List<String> initData(){
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            data.add("TAG: " + i);
            Log.i(TAG, data.get(i));
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
