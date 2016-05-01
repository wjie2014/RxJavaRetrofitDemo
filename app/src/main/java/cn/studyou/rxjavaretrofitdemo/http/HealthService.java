package cn.studyou.rxjavaretrofitdemo.http;

import cn.studyou.rxjavaretrofitdemo.entity.Health;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface HealthService {

 @GET("search")
 Observable<Health> getHealthInfo(@Query("key") String key, @Query("name") String name, @Query("page") int page, @Query("size") int size);

}
