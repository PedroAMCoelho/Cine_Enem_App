package br.infnet.com.cineenem.Atividade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.infnet.com.cineenem.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
     Thread splashThread = new Thread(){
         @Override
         public void run() {
             try {
                 sleep(3000);
                 Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                 startActivity(intent);
                 finish();
             } catch (InterruptedException e){
                 e.printStackTrace();
             }
         }
     };
        splashThread.start();
    }
}
