package com.fiepi.moebooru.api;

import com.fiepi.moebooru.bean.PostBean;

import java.util.List;

/**
 * Created by fiepi on 11/12/17.
 */

public interface PostsAPIListener {
    public void postsResult(List<PostBean> postsResult);
}