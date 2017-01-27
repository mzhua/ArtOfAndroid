package im.hua.artofandroid.chapter_234;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import static im.hua.artofandroid.chapter_234.Constans.MSG_FROM_CLIENT;

/**
 * Created by hua on 2017/1/27.
 */

public class MessengerService extends Service {

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_CLIENT:
                    Log.d("MessengerHandler", "receive msg from client: " + msg.getData().getString("msg"));
                    Messenger replyTo = msg.replyTo;
                    Message replyMsg = Message.obtain(null, Constans.MSG_FROM_SERVER);
                    Bundle data = new Bundle();
                    data.putString("reply","你的消息我收到啦！");
                    replyMsg.setData(data);
                    try {
                        replyTo.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
