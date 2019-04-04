package com.cjc.rxdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import org.reactivestreams.Publisher;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity {

    int i = 0;

    private io.reactivex.Single<String> getMyTask() {
        return io.reactivex.Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.d("ERSEN", "Task Started!");
                Random random = new Random(System.currentTimeMillis());

//                if(random.nextBoolean()){
//                    return "WORK COMPLETED";
//                }

                Log.e("test", "call " + i + " times");
                if (i++ == 0) {

                    Log.d("ERSEN", "Task Had An Error!");
                    throw new IllegalArgumentException();

                } else {
                    if (random.nextBoolean()) {
                        return "WORK COMPLETED";
                    }
                    throw new IllegalArgumentException();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getMyTask()
                .retryWhen(throwableFlowable -> throwableFlowable.flatMap(
                        new Function<Throwable, Publisher<?>>() {
                            @Override
                            public Publisher<?> apply(Throwable throwable) throws Exception {
                                if (throwable instanceof ClassCastException) {
                                    return Flowable.error(throwable);
                                }
                                return Flowable.just("ignored").delay(1, TimeUnit.SECONDS);
                            }
                        }
                )).subscribe(
                new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("test", s);
                    }
                }
        );
    }
}
