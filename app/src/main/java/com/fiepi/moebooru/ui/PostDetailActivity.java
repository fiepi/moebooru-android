package com.fiepi.moebooru.ui;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fiepi.moebooru.R;
import com.fiepi.moebooru.bean.PostBean;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = PostDetailActivity.class.getSimpleName();
    private static final String ARG_POST_ITEM_POS = "ARG_POST_ITEM_POS";
    private static final String ARG_POST_ITEMS = "ARG_POST_ITEMS";

    private List<PostBean> mPostBeanItems;
    private int mPOS;
    private PostPagerAdapter mAdapter;

    private ViewPager mViewPager;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        mViewPager = (ViewPager) findViewById(R.id.vp_post);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        mToolbar.setTitle("Detail");
        mToolbar.setBackgroundColor(getColor(R.color.transparent));
        mToolbar.setTitleTextColor(getColor(R.color.white));
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPOS = bundle.getInt(ARG_POST_ITEM_POS);
        mPostBeanItems = bundle.getParcelableArrayList(ARG_POST_ITEMS);
        mAdapter = new PostPagerAdapter(getSupportFragmentManager(), mPostBeanItems);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPOS);
        /**
        try {
            Field field = mViewPager.getClass().getField("mCurItem");
            field.setAccessible(true);
            field.setInt(mViewPager, mPOS);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
         **/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_download_post) {
            downloadPost();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void downloadPost(){
        int pos = mViewPager.getCurrentItem();

        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };

        long id = mPostBeanItems.get(pos).getId();
        String str = mPostBeanItems.get(pos).getFile_url();
        String ext = str.substring(str.length()-4, str.length());
        String site = "Konachan.com";
        String tags = null;
        for(int i = 0; i < mPostBeanItems.get(pos).getTags().size(); i++){
            if (tags == null){
                tags = mPostBeanItems.get(pos).getTags().get(i).getName();
            }else {
                tags = tags + " " + mPostBeanItems.get(pos).getTags().get(i).getName();
            }
        }
        String fileName = site +"_" + id + " " + tags + ext;
        fileName = fileName.replaceAll("/", "_");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/Moebooru/" + fileName);

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }else {
                DownloadManager downloadManager = (DownloadManager) this.getSystemService(this.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
                request.setDestinationUri(Uri.fromFile(file));
                downloadManager.enqueue(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
