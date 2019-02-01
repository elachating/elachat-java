package com.eladapp.elachat.manager;

import android.util.Log;

import com.eladapp.elachat.listener.MessageListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MessageManager {
    private static final String TAG = "BMChatManager";
    private List<MessageListener> messageListeners = Collections.synchronizedList(new ArrayList());
    public void addMessageListener(MessageListener var1) {
        Log.d("messagemanager","addMessageListener");
        System.out.println("111111111111111111111111");
        System.out.println(var1);
        System.out.println("22222222222222222222222");
        if (var1 != null) {
            if (!this.messageListeners.contains(var1)) {
                this.messageListeners.add(var1);
            }

        }
    }
    public void removeMessageListener(MessageListener var1) {
        if (var1 != null) {
            this.messageListeners.remove(var1);
        }
    }

    public void onMessageReceived( List<String> var1){

        Log.d("messagemanager","onMessageReceived");
        System.out.println("asdfadfasd");
        System.out.println(this.messageListeners);
        System.out.println("asdfadsf");
        System.out.print(this.messageListeners);
        synchronized(MessageManager.this.messageListeners) {
            Iterator var2 = MessageManager.this.messageListeners.iterator();
            while(var2.hasNext()) {
                MessageListener var3 = (MessageListener)var2.next();
               // Log.d("messagemanager","11111onMessageReceived"+var1.toString());
                //Log.d("dssssssssssssssss",var1.toString());
                var3.onMessageReceived(var1);
            }

        }

    }
}
