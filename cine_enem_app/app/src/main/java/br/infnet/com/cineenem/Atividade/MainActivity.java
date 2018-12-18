package br.infnet.com.cineenem.Atividade;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.facebook.login.LoginManager;
import com.google.android.gms.ads.MobileAds;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import br.infnet.com.cineenem.Adaptador.RecyclerViewAdapter;
import br.infnet.com.cineenem.CineEnemActivity;
import br.infnet.com.cineenem.Entidade.Item;
import br.infnet.com.cineenem.R;
import br.infnet.com.cineenem.Util.FirebaseConfiguration;


public class MainActivity extends CineEnemActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private android.support.v7.widget.Toolbar mToolbar;
    private DatabaseReference refDb;
    private NavigationView mNavigationView;

    private ArrayList<Item> mList;
    private SearchView mSearchIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.listar_itens);
        manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        populateList();
        mToolbar = findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        mNavigationView = (NavigationView) findViewById(R.id.nav_menu);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                if(item.getTitle().equals("Logout")) {
                    firebaseAuth.signOut();
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(item.getTitle().equals("Novos Vídeos")) {
                    Intent intent = new Intent(MainActivity.this, CadastroVideoActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (item.getTitle().equals("Favoritos")){
                    //switchContent(favoritos, "favorites");
                    Intent favoritos = new Intent(MainActivity.this, FavoritosActivity.class);
                    startActivity(favoritos);
                    finish();
                }
                else if(item.getTitle().equals("Todos os Vídeos")) {
                    recyclerView.setAdapter(new RecyclerViewAdapter(MainActivity.this,  mList) );

                } else  {
                    ArrayList<Item> filtered = new ArrayList<>(Collections2.filter(mList, new Predicate<Item>() {
                        @Override
                        public boolean apply(@Nullable Item candidate) {
                            return candidate.getCategoria().equals((String) item.getTitle());
                        }
                    }));

                    if(filtered.size() > 0) {
                        recyclerView.setAdapter(new RecyclerViewAdapter(MainActivity.this, filtered));
                    } else {
                        noResultsWarning("a categoria", item.getTitle().toString());
                    }
                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");
    }

    @Override
    protected void onResume() {
        super.onResume();

        firebaseUser = firebaseAuth.getCurrentUser();

        Menu menu = mNavigationView.getMenu();
        menu.findItem(R.id.admin_group).setVisible( ( firebaseUser != null && firebaseUser.getEmail().equals("marcos.ammon@gmail.com") ) );
    }

    private void populateList(){

        refDb = FirebaseConfiguration.getDatabase().child("items");

        refDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,HashMap<String,Object>> td = (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();
                mList = new ArrayList<>();

                if(td != null) {
                    for (Map<String,Object> o : td.values()) {
                        String titulo = o.get("titulo").toString();
                        String descricao = o.get("descricao").toString();
                        String codigo_youtube = o.get("codigo_youtube").toString();
                        String codigo_imdb = o.get("codigo_imdb").toString();
                        String categoria = o.get("categoria").toString();

                        mList.add(new Item(titulo,descricao,codigo_youtube,codigo_imdb,categoria));
                    }
                }


                recyclerView.setAdapter(new RecyclerViewAdapter(MainActivity.this, mList) );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        mSearchIcon = (SearchView) menu.findItem(R.id.search).getActionView();

        mSearchIcon.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchIcon.setMaxWidth(Integer.MAX_VALUE);
            }
        });

        mSearchIcon.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String s) {
                if (s.isEmpty()) {
                    recyclerView.setAdapter(new RecyclerViewAdapter(MainActivity.this,  mList) );
                } else {
                    ArrayList<Item> filtered = new ArrayList<>(Collections2.filter(mList, new Predicate<Item>() {
                        @Override
                        public boolean apply(@Nullable Item candidate) {
                            return candidate.getTitulo().toLowerCase().contains(s.toLowerCase());
                        }
                    }));

                    if(filtered.size() > 0) {
                        recyclerView.setAdapter(new RecyclerViewAdapter(MainActivity.this, filtered));
                    } else {
                        noResultsWarning("o texto", s);
                    }

                }
                mSearchIcon.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }
}

