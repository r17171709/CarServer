package com.renyu.carserver.commons;

import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by RG on 2015/10/15.
 */
public class OKHttpHelper {

    public interface StartListener {
        void onStart();
    }

    public interface RequestListener {
        void onSuccess(String string);
        void onError();
    }

    public interface ProgressListener {
        void updateprogress(int progress);
    }

    /**
     * 一般使用
     * @param url
     * @param params
     * @param startListener
     * @param requestListener
     */
    public void commonPostRequest(String url, HashMap<String, String> params, final StartListener startListener, final RequestListener requestListener) {
        if (startListener!=null) {
            startListener.onStart();
        }
        OKHttpUtils.getInstance().asyncPost(url, params, new OKHttpUtils.OnSuccessListener() {
            @Override
            public void onResponse(Response response) {
                try {
                    String string = response.body().string();
                    Observable.just(string).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (requestListener != null) {
                                requestListener.onSuccess(s);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Observable.just("").subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (requestListener != null) {
                                requestListener.onError();
                            }
                        }
                    });
                }
            }
        }, new OKHttpUtils.OnErrorListener() {
            @Override
            public void onFailure() {
                Observable.just("").subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (requestListener != null) {
                            requestListener.onError();
                        }
                    }
                });
            }
        });
    }

    public Response syncPostRequest(String url, HashMap<String, String> params) {
        return OKHttpUtils.getInstance().syncPost(url, params);
    }

    /**
     * 一般调用
     * @param url
     * @param startListener
     * @param requestListener
     */
    public void commonGetRequest(String url, final StartListener startListener, final RequestListener requestListener) {
        if (startListener!=null) {
            startListener.onStart();
        }
        OKHttpUtils.getInstance().get(url, new OKHttpUtils.OnSuccessListener() {
            @Override
            public void onResponse(Response response) {
                try {
                    String string = response.body().string();
                    Observable.just(string).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (requestListener != null) {
                                requestListener.onSuccess(s);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Observable.just("").subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (requestListener != null) {
                                requestListener.onError();
                            }
                        }
                    });
                }
            }
        }, new OKHttpUtils.OnErrorListener() {
            @Override
            public void onFailure() {
                Observable.just("").subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (requestListener != null) {
                            requestListener.onError();
                        }
                    }
                });
            }
        });
    }

    public void asyncUpload(HashMap<String, File> files, String url, HashMap<String, String> params, final StartListener startListener, final RequestListener requestListener) {
        if (startListener!=null) {
            startListener.onStart();
        }
        OKHttpUtils.getInstance().asyncUpload(url, params, files, new OKHttpUtils.OnSuccessListener() {
            @Override
            public void onResponse(Response response) {
                try {
                    String string = response.body().string();
                    Observable.just(string).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (requestListener != null) {
                                requestListener.onSuccess(s);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Observable.just("").subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (requestListener != null) {
                                requestListener.onError();
                            }
                        }
                    });
                }
            }
        }, new OKHttpUtils.OnErrorListener() {
            @Override
            public void onFailure() {
                Observable.just("").subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (requestListener != null) {
                            requestListener.onError();
                        }
                    }
                });
            }
        });
    }

    public Response syncUpload(HashMap<String, File> files, String url, HashMap<String, String> params) {
        return OKHttpUtils.getInstance().syncUpload(url, params, files);
    }

    public void downloadFile(String url, String dirPath, final RequestListener requestListener, final ProgressListener progressListener) {
        OKHttpUtils.getInstance().download(url, dirPath, new OKHttpUtils.OnDownloadListener() {
            @Override
            public void onSuccess(String filePath) {
                Observable.just(filePath).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (requestListener != null) {
                            requestListener.onSuccess(s);
                        }
                    }
                });
            }

            @Override
            public void onError() {
                Observable.just("").subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (requestListener != null) {
                            requestListener.onError();
                        }
                    }
                });
            }
        }, new OKHttpUtils.ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
//                System.out.println(bytesRead);
//                System.out.println(contentLength);
//                System.out.println(done);
//                Log.d("OKHttpHelper", (100 * bytesRead) / contentLength + " " + done);
                if (progressListener!=null) {
                    progressListener.updateprogress((int) ((100 * bytesRead) / contentLength));
                }
            }
        });
    }

    public void cancel(String tag) {
        OKHttpUtils.getInstance().cancel(tag);
    }
}
