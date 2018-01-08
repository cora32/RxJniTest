package org.iskopasi.rxjnitest;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.iskopasi.rxjnitest.model.IRepo;
import org.iskopasi.rxjnitest.model.Repo;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Rx";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.sample_text);

        IRepo repo = Repo.getInstance();
        repo.getJniCachedData().subscribe(getConsumer());
//        repo.getFromJni().subscribe(getJniConsumer(tv));
//
//        repo.getObservableString().subscribe(getObserver());
//        repo.getObservableString().subscribe(getConsumer());
//
//        repo.getObservableList().subscribe(getObserver());
//        repo.getObservableList().subscribe(getConsumer());
//
//        repo.getObservableListWithError().subscribe(getObserver());
    }

    private Consumer<String> getJniConsumer(TextView tv) {
        return s -> {
            if (s != null)
                tv.setText(s);
        };
    }

    private Observer<List<String>> getObserverList() {
        return new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(List<String> s) {
                Toast.makeText(MainActivity.this, "We got: " + s, Toast.LENGTH_SHORT).show();
                Log.e(TAG, " -- onNext " + Thread.currentThread().getName() + " " + s);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                View rootView = ((ViewGroup) MainActivity.this.findViewById(android.R.id.content)).getChildAt(0);
                if (rootView != null)
                    Snackbar.make(rootView, "Error: " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete 2");
            }
        };
    }

    private Observer<String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(String s) {
                Toast.makeText(MainActivity.this, "We got: " + s, Toast.LENGTH_SHORT).show();
                Log.e(TAG, " -- onNext " + Thread.currentThread().getName() + " " + s);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                View rootView = ((ViewGroup) MainActivity.this.findViewById(android.R.id.content)).getChildAt(0);
                if (rootView != null)
                    Snackbar.make(rootView, "Error: " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete 2");
            }
        };
    }

    private Consumer<String> getConsumer() {
        return s -> {
            Log.e(TAG, " -- getConsumer " + Thread.currentThread().getName() + " " + s);
            Toast.makeText(MainActivity.this, "accept: " + s, Toast.LENGTH_SHORT).show();
            View rootView = ((ViewGroup) MainActivity.this.findViewById(android.R.id.content)).getChildAt(0);
            if (rootView != null)
                Snackbar.make(rootView, "accept: " + s, Snackbar.LENGTH_SHORT).show();
        };
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
