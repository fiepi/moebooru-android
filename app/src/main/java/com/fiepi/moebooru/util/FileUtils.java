package com.fiepi.moebooru.util;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.fiepi.moebooru.R;
import com.fiepi.moebooru.api.GetPost;
import com.fiepi.moebooru.api.RawPostBean;
import com.fiepi.moebooru.bean.PostBean;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiepi on 11/11/17.
 */

public class FileUtils {
    private static final String mFileName = "posts.json";

    public FileUtils(){
    }

    public List<PostBean> getPostBeanFromFile() throws IOException {
        List<PostBean> tmp = new GetPost().getPostBean(getRawPostBeanFromFile());
        if (tmp == null){
            return null;
        }
        return tmp;
    }

    public void saveJson(String json){
        try (OutputStream outputStream = new FileOutputStream(getFile())) {
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<RawPostBean> getRawPostBeanFromFile() throws IOException {
        JsonParser jsonParser = new JsonParser();
        String string = readFile(getFile());
        if (string == null){
            return null;
        }
        JsonArray jsonArray = jsonParser.parse(string).getAsJsonArray();
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
        if (file.exists()) {
            StringBuffer sb = new StringBuffer();
            FileUtils.readToBuffer(sb, file);
            return sb.toString();
        }
        return null;
    }
    public File getFile(){
        File file = new File(getContext().getCacheDir(), mFileName);
//        File path = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File file2 = new File(path, mFileName);
        return file;
    }

    private Context getContext(){
        try {
            Application application = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
            return application.getApplicationContext();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Log.i("getContext", "application.getApplicationContext() 大失败!");
        return null;
    }
}
