package br.infnet.com.cineenem.Atividade;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.infnet.com.cineenem.CineEnemActivity;
import br.infnet.com.cineenem.Formulario.FormCadastroEnum;
import br.infnet.com.cineenem.Formulario.FormLoginEnum;
import br.infnet.com.cineenem.R;
import br.infnet.com.cineenem.Util.FirebaseConfiguration;

public class CadastroActivity extends CineEnemActivity {
    private ProgressDialog dialog;

    private Button firebaseCadastroBtn;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        setTitle("Cadastre-se");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseConfiguration.getAuth();

        firebaseCadastroBtn = (Button) findViewById(R.id.btnCadastrar);

        setupListeners();

    }

    @Override
    public boolean onSupportNavigateUp() {
        returnToLogInScreen();
        return true;
    }

    private void setupListeners() {
        firebaseCadastroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String email = FormLoginEnum.edtEmail.getValor(CadastroActivity.this);
                    String senha = FormLoginEnum.edtSenha.getValor(CadastroActivity.this);

                    String senha_confirm = FormCadastroEnum.edtConfirmSenha.getValor(CadastroActivity.this);

                    if(!senha.equals(senha_confirm)) {
                        throw new Exception("Senhas não conferem");
                    }
                    handleFirebaseSignUp(email,senha_confirm);
                } catch (Exception e) {
                    Toast.makeText(CadastroActivity.this, R.string.error_signup_unexpected,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void handleFirebaseSignUp(String email, String senha) {
        dialog =  ProgressDialog.show(CadastroActivity.this,getString(R.string.wait_label),getString(R.string.sign_wait),true,false);

        firebaseAuth
                .createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            returnToLogInScreen();


                        } else {
                            String error = "";

                            try {
                                throw task.getException();
                            } catch( FirebaseAuthWeakPasswordException e ) {
                                error = getString(R.string.error_signup_weakpassword);
                            } catch ( FirebaseAuthInvalidCredentialsException e ) {
                                error = getString(R.string.error_signup_invalid_email);
                            } catch( FirebaseAuthUserCollisionException e) {
                                error = getString(R.string.error_signup_collision);
                            } catch (Exception e) {
                                error = getString(R.string.error_signup_unexpected);
                            }

                            Toast.makeText(CadastroActivity.this,"Erro: " + error, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }

                    }
                });
    }

    private void returnToLogInScreen() {
        Intent intent = new Intent(CadastroActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
