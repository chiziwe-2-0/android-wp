package app.wordpress.test;

import android.app.Application;
import android.content.Intent;

import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //OneSignal Push
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new NotificationHandler())
                .init();
    }

    // This fires when a notification is opened by tapping on it or one is received while the app is running.
    class NotificationHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            try {

               if (!result.notification.isAppInFocus) {
                    Intent mainIntent;
                    mainIntent = new Intent(App.this, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainIntent);
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }


    }

   
}