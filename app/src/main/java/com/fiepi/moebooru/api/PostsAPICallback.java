package com.fiepi.moebooru.api;

import com.fiepi.moebooru.bean.PostBean;

import java.util.List;

/**
 * Created by fiepi on 11/12/17.
 */

public interface PostsAPICallback {
    public void getPostsResult(List<PostBean> postsResult);
}