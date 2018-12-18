package br.infnet.com.cineenem.Util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseConfiguration {
    private static FirebaseAuth auth;
    private static DatabaseReference db;
    private static FirebaseMessaging message;

    public static FirebaseAuth getAuth() {
        if(auth == null) {
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }


    public static DatabaseReference getDatabase() {
        if(db == null) {
            db = FirebaseDatabase.getInstance().getReference();
        }

        return db;
    }

    public static FirebaseMessaging getMessageInstance() {
        if (message == null) {
            message = FirebaseMessaging.getInstance();
        }

        return message;
    }

}
