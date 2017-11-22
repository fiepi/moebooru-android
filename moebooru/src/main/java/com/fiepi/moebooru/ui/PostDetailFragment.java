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

import com.bm.library.PhotoView;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fiepi.moebooru.GlideApp;
import com.fiepi.moebooru.R;
import com.fiepi.moebooru.bean.PostBean;
import com.fiepi.moebooru.ui.adapter.TagDetailViewAdapter;
import com.fiepi.moebooru.util.ImgDownloadUtils;
import com.fiepi.moebooru.util.ShareUtils;
import com.fiepi.moebooru.util.SharedPreferencesUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class PostDetailFragment extends Fragment {

    private static final String TAG = PostDetailFragment.class.getSimpleName();

    private static final String POST = "POST";

    private static final String namePref = "booru_used";
    private static final String booruTypeKey = "booru_type";
    private static final String booruNameKey = "booru_name";
    private static final String booruDomainKey = "booru_domain";

    private PhotoView mPhotoView;
//    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private PostBean mPostBean;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private RecyclerView.LayoutManager mTagDetailLayoutManager;
    private RecyclerView mRecyclerView;
    private TagDetailViewAdapter mTagAdapter;

    private TextView mTextViewInfoID;
    private TextView mTextViewInfoSize;
    private TextView mTextViewInfoAuthor;
    private TextView mTextViewInfoCreator;
    private TextView mTextViewInfoCreatedAt;
    private TextView mTextViewInfoSource;
    private TextView mTextViewInfoRating;
    private TextView mTextViewInfoScore;

    private ImageView mImageViewDL;

    public PostDetailFragment() {

    }

    public static PostDetailFragment newInstance(PostBean postBean) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(POST, postBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //开启菜单
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mPostBean = getArguments().getParcelable(POST);
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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_tags_detail);
        mTagDetailLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mTagDetailLayoutManager);
        mTagAdapter = new TagDetailViewAdapter(mPostBean.getTags());
        mRecyclerView.setAdapter(mTagAdapter);

        LinearLayout originUrlLayout = (LinearLayout) rootView.findViewById(R.id.origin_url_layout);
        LinearLayout largerUrlLayout = (LinearLayout) rootView.findViewById(R.id.larger_url_layout);

        originUrlLayout.setVisibility(LinearLayout.GONE);
        largerUrlLayout.setVisibility(LinearLayout.GONE);

        GlideApp.with(getContext())
                .load(mPostBean.getSample_url())
                .fitCenter()
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

        /**
        Picasso.with(getContext())
                .load(mPostBean.getSample_url())
                .into(mPhotoView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
         **/

        return rootView;
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
                String domain = new SharedPreferencesUtils().getStringValus(namePref, booruDomainKey);
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
            String domain = new SharedPreferencesUtils().getStringValus(namePref, booruDomainKey);
            new ImgDownloadUtils(mPostBean.getFile_url(), mPostBean.getTags(), mPostBean.getId(), domain, getActivity()).toDownload();
            return true;
        }else if (id == R.id.action_share_post){
            String site = new SharedPreferencesUtils().getStringValus(namePref, booruTypeKey) + new SharedPreferencesUtils().getStringValus(namePref, booruDomainKey);;
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
