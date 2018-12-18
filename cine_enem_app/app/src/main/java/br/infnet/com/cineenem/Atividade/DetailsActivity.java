package br.infnet.com.cineenem.Atividade;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;

import br.infnet.com.cineenem.Adaptador.RecyclerViewAdapter;
import br.infnet.com.cineenem.CineEnemActivity;
import br.infnet.com.cineenem.Entidade.Item;
import br.infnet.com.cineenem.R;
import br.infnet.com.cineenem.Util.YouTubeConfiguration;


public class DetailsActivity extends CineEnemActivity implements YouTubePlayer.OnInitializedListener {
    private YouTubePlayer playingInstance = null;
    private android.support.v7.widget.Toolbar mToolbar;
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerSupportFragment youTubeFragment;
    private Item item;
    private ArrayList<Item> listItems;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        listItems = (ArrayList<Item>) intent.getSerializableExtra("list");
        position =  intent.getIntExtra("pos",0);

        setTitle(item.getTitulo());

        TextView titulo = findViewById(R.id.titulo);
        TextView descricao = findViewById(R.id.descricao);

        titulo.setText(item.getTitulo());
        descricao.setText(item.getDescricao());


        mToolbar = findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        youTubeFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);

        youTubeFragment.initialize(YouTubeConfiguration.YOUTUBE_API_KEY,this);

        //Anuncio Banner

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Anuncio Intertitial

        mInterstitialAd = new InterstitialAd(DetailsActivity.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // VAI PARA O PROXIMO VIDEO

                position++;
                if( listItems.size() > position ) {
                    item = listItems.get(position);
                    Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
                    i.putExtra("item", item);
                    i.putExtra("list", listItems);
                    i.putExtra("pos", position);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(),"Você já terminou todos os vídeos ;-)", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                }
                finish();
            }
        });

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        // icone para o imdb

        ImageButton imdb = findViewById(R.id.imdbLink);
        imdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.imdb.com/title/" + item.getCodigo_imdb()));
                startActivity(browser);
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

//    @Override
//    public void onBackPressed(){ //Botão BACK padrão do android
//        startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
//        finish(); //Método para matar a activity e não deixa-lá indexada na pilhagem
//        return;
//    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, final boolean wasRestored) {

        if(!wasRestored) {
            player.cueVideo(item.getCodigo_youtube());
            playingInstance = player;
        }

        player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {
            }

            @Override
            public void onVideoEnded() {
                Log.d("-------wasRestored", Boolean.toString(wasRestored));
                Log.d("-----VideoEnded", "-----VideoEnded");
                if(listItems != null) {
                    MyDialogFragment dialog = new MyDialogFragment();
                    dialog.setAdd(mInterstitialAd);
                    dialog.show(getFragmentManager(), "test");
                } else {
                    Intent intent = new Intent(DetailsActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        });



    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if(errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this,RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error),errorReason.toString());
            Toast.makeText(DetailsActivity.this,error,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        if(playingInstance != null) {
            playingInstance.setFullscreen( ( newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ) );
        }
        super.onConfigurationChanged(newConfig);
    }

}
