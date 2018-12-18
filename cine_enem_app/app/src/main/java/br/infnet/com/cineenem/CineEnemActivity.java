package br.infnet.com.cineenem;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.infnet.com.cineenem.Util.FirebaseConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CineEnemActivity extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser firebaseUser = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth = FirebaseConfiguration.getAuth();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void noResultsWarning(String type, String condition) {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Não existem vídeos com " + type + " " + condition)
                .setNeutralButton("Ok",null)
                .create();


        dialog.show();

    }
}
