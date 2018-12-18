package br.infnet.com.cineenem.Atividade;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.infnet.com.cineenem.Adaptador.ItemAdapter;
import br.infnet.com.cineenem.CineEnemActivity;
import br.infnet.com.cineenem.Entidade.Item;
import br.infnet.com.cineenem.R;

public class FavoritosActivity extends CineEnemActivity {
    public static final String ARG_ITEM_ID = "favorite_list";

    ListView favoriteList;
    SharedPreference sharedPreference;
    List<Item> favorites;
    private android.support.v7.widget.Toolbar mToolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);

        mToolbar = findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      //Ativar o botão

        sharedPreference = new SharedPreference();
        favorites = sharedPreference.getFavorites(this);
        if (favorites == null)
            favorites = new ArrayList<>();

        favoriteList = (ListView) findViewById(R.id.list_favorites);
        ItemAdapter adapter = new ItemAdapter(this, favorites, sharedPreference);
        favoriteList.setAdapter(adapter);
        favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FavoritosActivity.this, DetailsActivity.class);
                Item item = (Item) favoriteList.getItemAtPosition(position);
                intent.putExtra("item", item);
                startActivity(intent);
            }
        });
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
}

