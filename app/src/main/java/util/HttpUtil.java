package util;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by 风雨诺 on 2019/2/17.
 */

public class HttpUtil {

    private static Gson gson = new Gson();
    private static OkHttpClient okHttpClient = new OkHttpClient();


    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static Gson getGson() {
        return gson;
    }

    /**
     * 通过post方法获得json数据，并且解析成V类型数据
     * @param url 服务器地址
     * @param map 请求体键值对
     * @return 返回数据
     */
    public static String  postJson(String url, Map<String, String> map) throws IOException  {
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建表单请求体
        FormBody.Builder formbody = new FormBody.Builder();
        if (map != null) {
            for (Map.Entry<String, String> item : map.entrySet()) {
                formbody.add(item.getKey(), item.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(formbody.build())
                .build();

        //发送请求，并得到响应
        Response response = okHttpClient.newCall(request).execute();
        ResponseBody body = response.body();
        String json = body.string();
        //关闭responseBody
        body.close();
        return json;
    }

    public static String postJson(String url, @NonNull String key, @NonNull String value) throws IOException {

        FormBody.Builder builder = new FormBody.Builder();
        builder.add(key, value);
        Request request = new Request.Builder()
                .post(builder.build())
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String json = response.body().string();
        response.body().close();
        return json;
    }
}
