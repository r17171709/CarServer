package com.renyu.carserver.commons;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by renyu on 15/10/14.
 */
public class OKHttpUtils {

    static OKHttpUtils instance=null;

    static OkHttpClient client=null;

    static CookieManager manager=null;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OKHttpUtils() {
        client=new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        client.setReadTimeout(5, TimeUnit.SECONDS);
        client.setWriteTimeout(5, TimeUnit.SECONDS);
        manager=new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(manager);
//        setCertificates();
    }

    public interface OnDownloadListener {
        void onSuccess(String filePath);
        void onError();
    }

    public interface OnSuccessListener {
        void onResponse(Response response);
    }

    public interface OnErrorListener {
        void onFailure();
    }

    public interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }

    public static OKHttpUtils getInstance() {
        if (instance==null) {
            synchronized (OKHttpUtils.class) {
                if (instance==null) {
                    instance=new OKHttpUtils();
                }
            }
        }
        return instance;
    }

    public void get(String url, HashMap<String, String> headers, final OnSuccessListener successListener, final OnErrorListener errorListener) {
        Request.Builder req_builder=new Request.Builder();
        if (headers!=null&&headers.size()>0) {
            Iterator<Map.Entry<String, String>> header_it=headers.entrySet().iterator();
            while (header_it.hasNext()) {
                Map.Entry<String, String> header_en=header_it.next();
                req_builder.addHeader(header_en.getKey(), header_en.getValue());
            }
        }
        Request request=req_builder.url(url).tag(url).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (errorListener != null) {
                    errorListener.onFailure();
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response!=null && response.isSuccessful()) {
                    if (successListener != null) {
                        successListener.onResponse(response);
                    }
                } else {
                    if (errorListener != null) {
                        errorListener.onFailure();
                    }
                }
            }
        });
    }

    public void get(String url, OnSuccessListener successListener, OnErrorListener errorListener) {
        get(url, null, successListener, errorListener);
    }

    public void get(String url, HashMap<String, String> headers, OnSuccessListener successListener) {
        get(url, headers, successListener, null);
    }

    public void get(String url, OnSuccessListener successListener) {
        get(url, null, successListener);
    }

    public void get(String url, HashMap<String, String> headers) {
        get(url, headers, null);
    }

    public void get(String url) {
        get(url, new HashMap<String, String>());
    }

    /**
     * post数据准备
     * @param url
     * @param params
     * @param headers
     * @return
     */
    private Call postPrepare(String url, HashMap<String, String> params, HashMap<String, String> headers) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Iterator<Map.Entry<String, String>> params_it = params.entrySet().iterator();
        while (params_it.hasNext()) {
            Map.Entry<String, String> params_en = params_it.next();
            builder.add(params_en.getKey(), params_en.getValue());
        }
        Request.Builder req_builder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            Iterator<Map.Entry<String, String>> header_it = headers.entrySet().iterator();
            while (header_it.hasNext()) {
                Map.Entry<String, String> header_en = header_it.next();
                req_builder.addHeader(header_en.getKey(), header_en.getValue());
            }
        }
        Request request = req_builder.url(url).tag(url).post(builder.build()).build();
        return client.newCall(request);
    }

    private Call postJsonPrepare(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        return client.newCall(request);
    }

    /**
     * post请求
     * @param url
     * @param params
     * @param headers
     * @param successListener
     * @param errorListener
     */
    public void asyncPost(String url, HashMap<String, String> params, HashMap<String, String> headers, final OnSuccessListener successListener, final OnErrorListener errorListener) {
        Call call=postPrepare(url, params, headers);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (errorListener != null) {
                    errorListener.onFailure();
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (successListener != null) {
                        successListener.onResponse(response);
                    }
                } else {
                    if (errorListener != null) {
                        errorListener.onFailure();
                    }
                }
            }
        });
    }

    public void asyncPost(String url, HashMap<String, String> params, OnSuccessListener successListener, OnErrorListener errorListener) {
        asyncPost(url, params, null, successListener, errorListener);
    }

    public void asyncPost(String url, HashMap<String, String> params, HashMap<String, String> headers, OnSuccessListener successListener) {
        asyncPost(url, params, headers, successListener, null);
    }

    public void asyncPost(String url, HashMap<String, String> params, OnSuccessListener successListener) {
        asyncPost(url, params, successListener, null);
    }

    public void asyncPost(String url, HashMap<String, String> params, HashMap<String, String> headers) {
        asyncPost(url, params, headers, null);
    }

    public void asyncPost(String url, HashMap<String, String> params) {
        asyncPost(url, params, new HashMap<String, String>());
    }

    public Response syncPost(String url, HashMap<String, String> params, HashMap<String, String> headers) {
        Call call=postPrepare(url, params, headers);
        try {
            Response response=call.execute();
            if (!response.isSuccessful()) {
                return null;
            }
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response syncPost(String url, HashMap<String, String> params) {
        return syncPost(url, params, null);
    }

    /**
     * 下载
     * @param url
     * @param dirPath
     * @param downloadListener
     * @param progressListener
     */
    public void download(final String url, final String dirPath, final OnDownloadListener downloadListener, final ProgressListener progressListener) {
        OkHttpClient client2=client.clone();
        client2.setConnectTimeout(10, TimeUnit.SECONDS);
        client2.setReadTimeout(10, TimeUnit.SECONDS);
        client2.setWriteTimeout(10, TimeUnit.SECONDS);
        Request request=new Request.Builder().tag(url).url(url).build();
        final ProgressListener listener=new ProgressListener() {
            @Override
            public void update(final long bytesRead, final long contentLength, final boolean done) {
                if (progressListener!=null) {
                    progressListener.update(bytesRead, contentLength, done);
                }
            }
        };
        client2.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), listener))
                        .build();
            }
        });
        Call call=client2.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                downloadListener.onError();
            }

            @Override
            public void onResponse(Response response) {
                try {
                    if (response!=null && response.isSuccessful()) {
                        InputStream is = response.body().byteStream();
                        File file = null;
                        if (url.indexOf("?") != -1) {
                            String url_ = url.substring(0, url.indexOf("?"));
                            file = new File(dirPath + File.separator + url_.substring(url_.lastIndexOf("/") + 1));
                        } else {
                            file = new File(dirPath + File.separator + url.substring(url.lastIndexOf("/") + 1));
                        }
                        if (file.exists()) {
                            file.delete();
                        } else {
                            file.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        int count = 0;
                        byte[] buffer = new byte[1024];
                        while ((count = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, count);
                        }
                        is.close();
                        fos.close();
                        downloadListener.onSuccess(file.getPath());
                    } else {
                        downloadListener.onError();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    downloadListener.onError();
                }

            }
        });
    }

    public static class ProgressResponseBody extends ResponseBody {
        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override public long contentLength() throws IOException {
            return responseBody.contentLength();
        }

        @Override public BufferedSource source() throws IOException {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;
                @Override public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

    /**
     * 上传参数准备
     * @param url
     * @param params
     * @param files
     * @return
     */
    private Call uploadPrepare(String url, HashMap<String, String> params, HashMap<String, File> files) {
        OkHttpClient client2=client.clone();
        client2.setConnectTimeout(10, TimeUnit.SECONDS);
        client2.setReadTimeout(10, TimeUnit.SECONDS);
        client2.setWriteTimeout(10, TimeUnit.SECONDS);
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM);
        Iterator<Map.Entry<String, String>> params_it=params.entrySet().iterator();
        while (params_it.hasNext()) {
            Map.Entry<String, String> params_en= params_it.next();
            multipartBuilder.addFormDataPart(params_en.getKey(), params_en.getValue());
        }
        /**
         * 遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
         for (String path : paths) {
         builder.addFormDataPart("upload", null, RequestBody.create(MEDIA_TYPE_PNG, new File(path)));
         */
        Iterator<Map.Entry<String, File>> file_it=files.entrySet().iterator();
        while (file_it.hasNext()) {
            Map.Entry<String, File> entry=file_it.next();
            multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue().getName(), RequestBody.create(MediaType.parse("application/octet-stream"), entry.getValue()));
        }
        RequestBody formBody = multipartBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .tag(url)
                .post(formBody)
                .build();
        return client2.newCall(request);
    }

    /**
     * 异步上传
     * @param url
     * @param params
     * @param files
     * @param successListener
     * @param errorListener
     */
    public void asyncUpload(String url, HashMap<String, String> params, HashMap<String, File> files, final OnSuccessListener successListener, final OnErrorListener errorListener) {
        Call call=uploadPrepare(url, params, files);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (errorListener != null) {
                    errorListener.onFailure();
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response!=null && response.isSuccessful()) {
                    if (successListener != null) {
                        successListener.onResponse(response);
                    }
                }
                else {
                    if (errorListener != null) {
                        errorListener.onFailure();
                    }
                }
            }
        });
    }

    /**
     * 同步上传
     * @param url
     * @param params
     * @param files
     * @return
     */
    public Response syncUpload(String url, HashMap<String, String> params, HashMap<String, File> files) {
        Call call=uploadPrepare(url, params, files);
        try {
            Response response=call.execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cancel(String tag) {
        client.cancel(tag);
    }

    /**
     * HTTPS相关
     * @param certificates
     *
     * 这里可以设置证书CER_12306
     * setCertificates(new InputStream[]{new Buffer().writeUtf8(CER_12306).inputStream()});
     */
    public void setCertificates(InputStream... certificates) {
        //默认信任全部证书
        client.setSslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null));
    }
}
