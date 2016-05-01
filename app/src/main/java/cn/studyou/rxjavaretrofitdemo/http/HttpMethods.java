package cn.studyou.rxjavaretrofitdemo.http;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.studyou.rxjavaretrofitdemo.entity.Health;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HttpMethods {

    //shareSDK接口，健康信息
    public static final String BASE_URL = "http://apicloud.mob.com/appstore/health/";

    private static final int DEFAULT_TIMEOUT = 15;

    private Retrofit retrofit;
    private HealthService movieService;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        movieService = retrofit.create(HealthService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * @param subscriber 由调用者传过来的观察者对象
     */
    public void getHealthInfo(Subscriber<Health> subscriber, String key, String name, int page, int size) {

        Observable observable = movieService.getHealthInfo(key, name, page, size)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 将Health.ResultEntity.ListEntity返回给subscriber
     */

    private class HttpResultFunc implements Func1<Health, List<Health.ResultEntity.ListEntity>> {
        @Override
        public List<Health.ResultEntity.ListEntity> call(Health health) {
            if (health == null) {
                throw new ApiException(100);
            } else if (!"200".equals(health.getRetCode())) {
                throw new ApiException(100);
            }
            return health.getResult().getList();
        }
    }


}
