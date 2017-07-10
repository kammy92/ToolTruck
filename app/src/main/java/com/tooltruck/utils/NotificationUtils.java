package com.tooltruck.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

public class NotificationUtils {
// Notification Type => 1=> new property add, 2=>, 3=>
// Notification Style => 1=> simple_notification, 2=>inbox_style, 3=>big_text_style, 4=>big_picture_style, 5=> custom layout
// Notification Priority => -2=>PRIORITY_MIN, -1=>PRIORITY_LOW, 0=>PRIORITY_DEFAULT, 1=>PRIORITY_HIGH, 2=>PRIORITY_MAX
    
    private static String TAG = NotificationUtils.class.getSimpleName ();
    private Context mContext;
    
    public NotificationUtils (Context mContext) {
        this.mContext = mContext;
    }
    
    public void showNotificationMessage (com.tooltruck.model.Notification notification) {
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder (mContext);
        if (TextUtils.isEmpty (notification.getMessage ()))
            return;
        
        PendingIntent pendingIntent = null;
        Notification notification1;
        NotificationManager mNotifyManager = (NotificationManager) mContext.getSystemService (Context.NOTIFICATION_SERVICE);
        switch (notification.getNotification_type ()) {
            case 1:
                break;
        }
    }
}