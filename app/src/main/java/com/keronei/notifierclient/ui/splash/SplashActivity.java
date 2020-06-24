package com.keronei.notifierclient.ui.splash;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.keronei.notifierclient.data.Sdk;
import com.keronei.notifierclient.data.service.ActivityStarter;
import com.keronei.notifierclient.ui.login.LoginActivity;
import com.keronei.notifierclient.ui.main.MainActivity;
import com.facebook.stetho.Stetho;
import com.keronei.notifierclient.R;

import org.hisp.dhis.android.core.D2Manager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    private final static boolean DEBUG = true;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        disposable = D2Manager.instantiateD2(Sdk.getD2Configuration(this))
                .flatMap(d2 -> d2.userModule().isLogged())
                .doOnSuccess(isLogged -> {
                    if (isLogged) {
                        ActivityStarter.startActivity(this, MainActivity.getMainActivityIntent(this),true);
                    } else {
                        ActivityStarter.startActivity(this, LoginActivity.getLoginActivityIntent(this),true);
                    }
                }).doOnError(throwable -> {
                    throwable.printStackTrace();
                    ActivityStarter.startActivity(this, LoginActivity.getLoginActivityIntent(this),true);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}