package cn.studyou.rxjavaretrofitdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.studyou.rxjavaretrofitdemo.R;
import cn.studyou.rxjavaretrofitdemo.adapter.RecyclerAdapter;
import cn.studyou.rxjavaretrofitdemo.entity.Health;
import cn.studyou.rxjavaretrofitdemo.http.HttpMethods;
import cn.studyou.rxjavaretrofitdemo.subscribers.ProgressSubscriber;
import cn.studyou.rxjavaretrofitdemo.subscribers.SubscriberOnNextListener;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.result_rec)
    RecyclerView resultRec;
    private SubscriberOnNextListener getTopMovieOnNext;
    private RecyclerAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData(){
        getTopMovieOnNext = new SubscriberOnNextListener<List<Health.ResultEntity.ListEntity>>() {
            @Override
            public void onNext(List<Health.ResultEntity.ListEntity> listEntities) {
                if (listEntities != null) {

                }
                resultRec.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(),listEntities);
                resultRec.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }
        };
        getHealthInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    //进行网络请求
    private void getHealthInfo() {
        HttpMethods.getInstance().getHealthInfo(new ProgressSubscriber(getTopMovieOnNext, MainActivity.this), "10407b4d6da4c", "苹果", 1, 10);
    }
}
