package br.infnet.com.cineenem.Atividade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import br.infnet.com.cineenem.CineEnemActivity;
import br.infnet.com.cineenem.Entidade.Item;
import br.infnet.com.cineenem.Formulario.FormCadastroVideoEnum;
import br.infnet.com.cineenem.Formulario.FormLoginEnum;
import br.infnet.com.cineenem.R;
import br.infnet.com.cineenem.Util.FirebaseConfiguration;
import br.infnet.com.cineenem.Util.FormUtils;

public class CadastroVideoActivity extends CineEnemActivity {

    private Button storageCadsatroBtn;
    private DatabaseReference refDb;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_video);

        refDb = FirebaseConfiguration.getDatabase().child("items");

        storageCadsatroBtn = findViewById(R.id.btnConfirm);
        mToolbar = findViewById(R.id.nav_action);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        setupListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do botão (gerado automaticamente pelo android)
                startActivity(new Intent(this, MainActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        finish(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }


    private void setupListeners() {
        storageCadsatroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String title = FormCadastroVideoEnum.edtTitle.getValor(CadastroVideoActivity.this);
                    String youtube  = FormCadastroVideoEnum.edtCode.getValor(CadastroVideoActivity.this);
                    String imdb  = FormCadastroVideoEnum.edtImdb.getValor(CadastroVideoActivity.this);
                    String description = FormCadastroVideoEnum.edtDescription.getValor(CadastroVideoActivity.this);
                    String categoria = FormCadastroVideoEnum.spnCategoria.getValor(CadastroVideoActivity.this);

                    Item i = new Item(title,description,youtube,imdb,categoria);


                    refDb.push().setValue(i).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FormUtils.limpaCampos(CadastroVideoActivity.this,FormCadastroVideoEnum.values());
                            Toast.makeText(CadastroVideoActivity.this,"Video Cadastrado com sucesso!",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(CadastroVideoActivity.this, R.string.error_unexpected,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
