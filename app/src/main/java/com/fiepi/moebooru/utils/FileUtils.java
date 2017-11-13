package com.fiepi.moebooru.utils;

import android.content.Context;

import com.fiepi.moebooru.api.RawPostBean;
import com.fiepi.moebooru.bean.PostBean;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiepi on 11/11/17.
 */

public class FileUtils {
    public Context context;

    public FileUtils(Context context){
        this.context = context;
    }


    public List<RawPostBean> getJson(String name) throws IOException {
        String string = new String();
        string = name;
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(readFile(getFile(string))).getAsJsonArray();
        Gson gson = new Gson();
        List<RawPostBean> posts = new ArrayList<RawPostBean>();
        for (JsonElement post : jsonArray){
            RawPostBean rawPostBean = gson.fromJson(post, RawPostBean.class);
            posts.add(rawPostBean);
        }
        return posts;
    }

    private static void readToBuffer(StringBuffer buffer, File file) throws IOException {
        InputStream is = new FileInputStream(file);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }
    private static String readFile(File file) throws IOException {
        StringBuffer sb = new StringBuffer();
        FileUtils.readToBuffer(sb, file);
        return sb.toString();
    }
    private File getFile(String name){
        String fileName = name+".json";
        File file = new File(this.context.getFilesDir(), fileName);
        return file;
    }
}
