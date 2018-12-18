package br.infnet.com.cineenem.Atividade;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import br.infnet.com.cineenem.CineEnemActivity;
import br.infnet.com.cineenem.Formulario.FormLoginEnum;
import br.infnet.com.cineenem.R;
import br.infnet.com.cineenem.Util.FirebaseConfiguration;

public class LoginActivity extends CineEnemActivity {
    private ProgressDialog dialog;

    private LoginButton fbLoginBtn;
    private Button firebaseLoginBtn;
    private Button firebaseCadastroBtn;

    private CallbackManager callbackManager;


    private TextView firebaseResetLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();



        fbLoginBtn = (LoginButton) findViewById(R.id.fb_login_btn);
        fbLoginBtn.setReadPermissions("email","public_profile","user_videos");


        firebaseLoginBtn = (Button) findViewById(R.id.btnLogin);

        firebaseCadastroBtn = (Button) findViewById(R.id.btnCadastrar);

        firebaseResetLink = (TextView) findViewById(R.id.txtResetLink);

        setupListeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null) {
            handleFirebaseUser();
        } else {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if( accessToken != null) openMainScreen();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }



    private void setupListeners() {

        fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookLogin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.e("onCancel: ", "cancelado" );
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("onerror: ", error.toString() );
            }
        });

        firebaseLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String email = FormLoginEnum.edtEmail.getValor(LoginActivity.this);
                    String senha  = FormLoginEnum.edtSenha.getValor(LoginActivity.this);
                    handleFirebaseLogin(email,senha);


                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
        });

        firebaseCadastroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,CadastroActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        firebaseResetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String email = FormLoginEnum.edtEmail.getValor(LoginActivity.this);
                    handleFirebaseReset(email);


                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, R.string.reset_error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void handleFirebaseReset(String email) {
        dialog =  ProgressDialog.show(LoginActivity.this,getString(R.string.wait_label),getString(R.string.sending_wait),true,false);

        firebaseAuth
                .sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.reset_successful,Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.error_unexpected,Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();

                    }
                });
    }

    private void handleFacebookLogin(AccessToken token) {
        dialog =  ProgressDialog.show(LoginActivity.this,getString(R.string.wait_label),getString(R.string.logging_wait),true,false);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        firebaseAuth
                .signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();

                            openMainScreen();
                        }
                        dialog.dismiss();


                    }
                });
    }

    private void handleFirebaseLogin(String email, String senha) {
        dialog =  ProgressDialog.show(LoginActivity.this,getString(R.string.wait_label),getString(R.string.logging_wait),true,false);

        firebaseAuth
                .signInWithEmailAndPassword(email,senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            handleFirebaseUser();

                        }

                        dialog.dismiss();

                    }
                });

    }

    private void handleFirebaseUser() {
        if( firebaseUser.isEmailVerified() ) {
            openMainScreen();
        }  else {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, R.string.verify_activation,Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                    }
                }
            });
        }
    }

    private void openMainScreen() {
        FirebaseMessaging m = FirebaseConfiguration.getMessageInstance();
        m.subscribeToTopic("news");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
