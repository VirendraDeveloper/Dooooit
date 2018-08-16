package cl.activaresearch.android_app.Dooit.fcm;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 20 Jul,2018
 */
public class DooitFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (SharedPreferenceUtility.getInstance(this).getBoolean(Constants.NOTIFICATION)) {
            try {
                RemoteMessage.Notification notification = remoteMessage.getNotification();
                String body = notification.getBody();
                String title = notification.getTitle();
                sendMyNotification(title, body);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMyNotification(String title, String message) {
        try {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_app)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(soundUri);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

