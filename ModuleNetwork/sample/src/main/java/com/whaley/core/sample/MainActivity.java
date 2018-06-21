package com.whaley.core.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mw.persistent.connect.info.UserInfo;
import com.whaley.core.appcontext.AppContextInit;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaleyvr.core.network.http.HttpManager;
import com.whaleyvr.core.network.longconnection.ConnectService;
import com.whaleyvr.core.network.longconnection.Connection;
import com.whaleyvr.core.network.longconnection.ConnectionListener;
import com.whaleyvr.core.network.longconnection.ConnectionStatusListener;
import com.whaleyvr.core.network.socketio.IScocketio;
import com.whaleyvr.core.network.socketio.SocketioManager;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.URISyntaxException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContextInit.appContextInit(getApplicationContext(),"");
        //socketio
        SocketioManager socketioManager = new SocketioManager("http://www.baidu.com", new IScocketio() {

            @Override
            public void onConnected(Object... args) {
                //
            }

            @Override
            public void onDisconnected(Object... args) {
                //
            }
        });
        try {
            socketioManager.initSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socketioManager.connectSocket();

        //longconnection
        getApplication().registerActivityLifecycleCallbacks(Connection.getInstance().build());
        Connection.getInstance().setConnectionStatusListener(new ConnectionStatusListener() {
            @Override
            public void onServiceConnected(ConnectService.ConnectBinder connectBinder) {
                Connection.getInstance().setupConnectAction(new ConnectionListener() {
                    @Override
                    public void onReceive(String s) {
                        //
                    }

                    @Override
                    public void onDeviceLogin(String s) {
                        //
                    }

                    @Override
                    public void onUserLogin(UserInfo userInfo) {
                        //
                    }

                    @Override
                    public void onUserLogout(UserInfo userInfo) {
                        //
                    }

                    @Override
                    public void unAvailable(int i, String s) {
                        //
                    }

                    @Override
                    public void available() {
                        //
                    }
                });
            }

            @Override
            public void onServiceDisconnected() {
                //
            }
        });
        Connection.getInstance().connect(AppContextProvider.getInstance().getContext(), "12345");

        //http
        TestApi testApi = HttpManager.getInstance().getRetrofit(TestApi.class).create(TestApi.class);
        Observable<ResponseBody> observable = testApi.requestTest();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
