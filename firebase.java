package brejapp.com.brejapp;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Rayza on 12/12/2017.
 */

public class firebase extends FirebaseInstanceIdService {
    String idClient="";
    @Override
    public void onCreate(){


    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);

    }



    private void sendRegistrationToServer(String refreshedToken) {




    }
}
