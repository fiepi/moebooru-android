package com.fiepi.moebooru.api;

import java.util.List;
import java.util.Map;

/**
 * Created by fiepi on 11/12/17.
 */

public class RawPostListBean {
    public List<RawPostBean> posts;
    public Map<String, String> tags;
    public Map<Long, Integer> votes;
}
