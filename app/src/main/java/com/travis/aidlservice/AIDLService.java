package com.travis.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.travis.aidlclient.Info;
import com.travis.aidlclient.MessageCenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yutao on 2018/8/7.
 */

public class AIDLService extends Service {

    public static final String TAG = "AIDLService";

    private List<Info> messages = new ArrayList<Info>();


    private final MessageCenter.Stub messageCenter = new MessageCenter.Stub() {
        @Override
        public List<Info> getInfo() throws RemoteException {
            synchronized (this) {
                if (messages != null) {
                    return messages;
                }
                return new ArrayList<Info>();
            }
        }

        @Override
        public void addInfo(Info info) throws RemoteException {
            synchronized (this){
                if (messages == null){
                    messages = new ArrayList<Info>();
                }

                if (info == null){
                    info = new Info();
                }

                if (!messages.contains(info)){
                    messages.add(info);
                }
                Log.d(TAG, "Custom transact message: " +messages.toString());
            }
        }
    };

    @Override
    public void onCreate() {
        Info message = new Info();
        message.setContent("Message");
        messages.add(message);

        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, String.format("on bind, intent = %s ", intent.toString()));
        return messageCenter;
    }
}
