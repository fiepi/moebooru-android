package com.fiepi.moebooru.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fiepi.moebooru.AppConfig;
import com.fiepi.moebooru.glide.GetGlideUrl;
import com.fiepi.moebooru.glide.GlideApp;
import com.fiepi.moebooru.R;
import com.fiepi.moebooru.bean.PostBean;
import com.fiepi.moebooru.ui.adapter.TagDetailViewAdapter;
import com.fiepi.moebooru.util.ClipboardUtils;
import com.fiepi.moebooru.util.ImgDownloadUtils;
import com.fiepi.moebooru.util.ShareUtils;
import com.fiepi.moebooru.util.SharedPreferencesUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;

import static com.fiepi.moebooru.AppConfig.*;

public class PostDetailFragment extends Fragment {

    private static final String TAG = PostDetailFragment.class.getSimpleName();

    private static final String POS = "POS";
    private static final String TYPE = "TYPE";

    private PhotoView mPhotoView;
    private ProgressBar mProgressBar;
    private PostBean mPostBean;
    private RecyclerView.LayoutManager mTagDetailLayoutManager;
    private RecyclerView mRecyclerView;
    private TagDetailViewAdapter mTagAdapter;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;

    private TextView mTextViewInfoAuthor;
    private TextView mTextViewInfoCreator;
    private TextView mTextViewInfoCreatedAt;
    private TextView mTextViewInfoSource;
    private TextView mTextViewInfoRating;
    private TextView mTextViewInfoScore;

    private ImageView mImageViewDL;

    private TextView mTextViewSample;
    private ImageView mImageViewSampleShare;
    private ImageView mImageViewSampleCopy;
    private ImageView mImageViewSampleDownload;

    private TextView mTextViewLarger;
    private ImageView mImageViewLargerShare;
    private ImageView mImageViewLargerCopy;
    private ImageView mImageViewLargerDownload;

    private TextView mTextViewOrigin;
    private ImageView mImageViewOriginShare;
    private ImageView mImageViewOriginCopy;
    private ImageView mImageViewOriginDownload;

    public PostDetailFragment() {

    }

    public static PostDetailFragment newInstance(int pos, String type) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putInt(POS, pos);
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //开启菜单
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            String type = getArguments().getString(TYPE);
            Log.i(TAG, "Type: " + type);
            if (type.equals("post")){
                mPostBean = AppConfig.mPostBeanPostItems.get(getArguments().getInt(POS));
                Log.i(TAG, "post");
            }
            if (type.equals("search"))
                {
                mPostBean = AppConfig.mPostBeanSearchItems.get(getArguments().getInt(POS));
                Log.i(TAG, "search");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_detail, container, false);
        mPhotoView = (PhotoView) rootView.findViewById(R.id.pv_post);
        mPhotoView.enable();
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_detail);
        mProgressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        mSlidingUpPanelLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_panel);

        TextView textViewID = (TextView) rootView.findViewById(R.id.tv_id_panel);
        textViewID.setText("#" + mPostBean.getId());

        initInfoView(rootView);
        initUrlView(rootView);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_tags_detail);
        mTagDetailLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mTagDetailLayoutManager);
        mTagAdapter = new TagDetailViewAdapter(mPostBean.getTags());
        mRecyclerView.setAdapter(mTagAdapter);

        GlideApp.with(getContext())
                .load(new GetGlideUrl().makeGlideUrl(mPostBean.getSample_url()))
                .fitCenter()
                .transition(new DrawableTransitionOptions().crossFade(400))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mPhotoView);

        return rootView;
    }

    private void initUrlView(View view){
        mTextViewSample = (TextView) view.findViewById(R.id.tv_url_sample);
        mImageViewSampleShare = (ImageView) view.findViewById(R.id.iv_url_sample_share);
        mImageViewSampleCopy = (ImageView) view.findViewById(R.id.iv_url_sample_copy);
        mImageViewSampleDownload = (ImageView) view.findViewById(R.id.iv_url_sample_download);
        mTextViewSample.setText(mPostBean.getSample_width()
                + "x" + mPostBean.getSample_height());
        mImageViewSampleShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareUtils().shareText(mPostBean.getSample_url(), getActivity());
            }
        });
        mImageViewSampleCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ClipboardUtils().copy(mPostBean.getSample_url(), getContext());
                Toast.makeText(getContext(), mPostBean.getSample_url(), Toast.LENGTH_SHORT).show();
            }
        });
        mImageViewSampleDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImgDownloadUtils(mPostBean.getSample_url(), mPostBean.getTags(), mPostBean.getId(),
                        new SharedPreferencesUtils().getStringValue(booruUsedPref, booruDomainKey),
                        getActivity()).toDownload();
            }
        });

        mTextViewLarger = (TextView) view.findViewById(R.id.tv_url_larger);
        mImageViewLargerShare = (ImageView) view.findViewById(R.id.iv_url_larger_share);
        mImageViewLargerCopy = (ImageView) view.findViewById(R.id.iv_url_larger_copy);
        mImageViewLargerDownload = (ImageView) view.findViewById(R.id.iv_url_larger_download);
        mTextViewLarger.setText(mPostBean.getJpeg_width()
                + "x" + mPostBean.getJpeg_height());
        mImageViewLargerShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareUtils().shareText(mPostBean.getJpeg_url(), getActivity());
            }
        });
        mImageViewLargerCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ClipboardUtils().copy(mPostBean.getJpeg_url(), getContext());
                Toast.makeText(getContext(), mPostBean.getJpeg_url(), Toast.LENGTH_SHORT).show();
            }
        });
        mImageViewLargerDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImgDownloadUtils(mPostBean.getJpeg_url(), mPostBean.getTags(), mPostBean.getId(),
                        new SharedPreferencesUtils().getStringValue(booruUsedPref, booruDomainKey),
                        getActivity()).toDownload();
            }
        });

        mTextViewOrigin = (TextView) view.findViewById(R.id.tv_url_origin);
        mImageViewOriginShare = (ImageView) view.findViewById(R.id.iv_url_origin_share);
        mImageViewOriginCopy = (ImageView) view.findViewById(R.id.iv_url_origin_copy);
        mImageViewOriginDownload = (ImageView) view.findViewById(R.id.iv_url_origin_download);
        mTextViewOrigin.setText(mPostBean.getWidth()
                + "x" + mPostBean.getHeight());
        mImageViewOriginShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareUtils().shareText(mPostBean.getFile_url(), getActivity());
            }
        });
        mImageViewOriginCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ClipboardUtils().copy(mPostBean.getFile_url(), getContext());
                Toast.makeText(getContext(), mPostBean.getFile_url(), Toast.LENGTH_SHORT).show();
            }
        });
        mImageViewOriginDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImgDownloadUtils(mPostBean.getFile_url(), mPostBean.getTags(), mPostBean.getId(),
                        new SharedPreferencesUtils().getStringValue(booruUsedPref, booruDomainKey),
                        getActivity()).toDownload();
            }
        });
    }

    private String getSize(int size){
        int m = 0;
        size /= 1024;
        if (size < 1024){
            return size + " KiB";
        }
        float sf = size;
        while (sf >= 1024){
            m++;
            sf /= 1024;
        }
        sf = (float)(Math.round(sf*100))/100;
        float s = m + sf;
        return s + " MiB";
    }

    private void initInfoView(View view){

        mImageViewDL = (ImageView) view.findViewById(R.id.iv_panel_download);

//        mTextViewInfoID = (TextView) view.findViewById(R.id.tv_info_id);
//        mTextViewInfoSize = (TextView) view.findViewById(R.id.tv_info_size);
        mTextViewInfoAuthor = (TextView) view.findViewById(R.id.tv_info_author);
        mTextViewInfoCreator = (TextView) view.findViewById(R.id.tv_info_creator);
        mTextViewInfoCreatedAt = (TextView) view.findViewById(R.id.tv_info_created_at);
        mTextViewInfoSource = (TextView) view.findViewById(R.id.tv_info_source);
        mTextViewInfoRating = (TextView) view.findViewById(R.id.tv_info_rating);
        mTextViewInfoScore = (TextView) view.findViewById(R.id.tv_info_score);

//        mTextViewInfoID.setText(String.valueOf(mPostBean.getId()));
//        mTextViewInfoSize.setText(String.valueOf(mPostBean.getWidth()) + " x " + String.valueOf(mPostBean.getHeight()));
        mTextViewInfoAuthor.setText(mPostBean.getAuthor() == null ? " " : mPostBean.getAuthor() );
        mTextViewInfoCreator.setText(mPostBean.getCreator_id() + "");
        mTextViewInfoCreatedAt.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mPostBean.getCreated_at()));
        mTextViewInfoSource.setText(mPostBean.getSource() == null ? "" : mPostBean.getSource());
        mTextViewInfoRating.setText(mPostBean.getRating());
        mTextViewInfoScore.setText(mPostBean.getScore() + "");
    }

    private void actionListener(){
        mImageViewDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String domain = new SharedPreferencesUtils().getStringValue(booruUsedPref, booruDomainKey);
                if (domain != "null"){
                    new ImgDownloadUtils(mPostBean.getFile_url(), mPostBean.getTags(), mPostBean.getId(), domain, getActivity()).toDownload();
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actionListener();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_download_post) {
            String domain = new SharedPreferencesUtils().getStringValue(booruUsedPref, booruDomainKey);
            new ImgDownloadUtils(mPostBean.getFile_url(), mPostBean.getTags(), mPostBean.getId(), domain, getActivity()).toDownload();
            return true;
        }else if (id == R.id.action_share_post){
            String site = new SharedPreferencesUtils().getStringValue(booruUsedPref, booruTypeKey)
                    + new SharedPreferencesUtils().getStringValue(booruUsedPref, booruDomainKey);
            String url = site + "/post/show/" + mPostBean.getId();
            new ShareUtils().shareText(url, getActivity());
        }

        return super.onOptionsItemSelected(item);
    }

    private void onClickPhotoViewListener(){
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getView().getSystemUiVisibility() == 0){
                    hideSystemUI();
//                    mSlidingUpPanelLayout.setPanelHeight(R.dimen.zero);
//                    mSlidingUpPanelLayout.setEnabled(false);

                }else {
                    showSystemUI();
//                    mSlidingUpPanelLayout.setPanelHeight(R.dimen.sliding_panel_height);
//                    mSlidingUpPanelLayout.setEnabled(true);
                }
            }
        });
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        getView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
