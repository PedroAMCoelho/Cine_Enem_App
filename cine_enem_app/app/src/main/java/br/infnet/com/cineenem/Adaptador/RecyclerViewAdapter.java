package br.infnet.com.cineenem.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.infnet.com.cineenem.Atividade.DetailsActivity;
import br.infnet.com.cineenem.Atividade.SharedPreference;
import br.infnet.com.cineenem.Entidade.Item;
import br.infnet.com.cineenem.R;


public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<Item> listItems;
    SharedPreference sharedPreference;

    public RecyclerViewAdapter(Context context, ArrayList<Item> listItems){
        this.context = context;
        this.listItems = listItems;
        sharedPreference = new SharedPreference();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contetView = LayoutInflater.from(context).inflate(R.layout.layout_item_lista, null);
        return new Holder(contetView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = listItems.get(position);
        Holder h = (Holder) holder;
        h.listItems = listItems;
        h.item = item;
        //h.foto.setImageResource(item.getFoto());
        h.foto.setImageResource(R.drawable.logo_cine_enem);
        h.titulo.setText(item.getTitulo());
        //h.tipo.setText(item.getTipo());
        h.tipo.setText(item.getCategoria());
        h.setCurrentPosition(position);
        h.overflow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopupMenu(v, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements Serializable {
        public View view;
        public Item item;
        public ArrayList<Item> listItems;
        ImageView foto;
        ImageView overflow;
        TextView titulo;
        TextView tipo;
        Integer current;


        public Holder(View itemView) {
            super(itemView);
            view = itemView;
            foto = (ImageView) view.findViewById(R.id.foto);
            titulo = (TextView) view.findViewById(R.id.titulo);
            tipo = (TextView) view.findViewById(R.id.tipo);
            overflow = (ImageView) view.findViewById(R.id.over_flow);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent i = new Intent(context, DetailsActivity.class);
                    i.putExtra("item", item);
                    i.putExtra("list", listItems);
                    i.putExtra("pos",current);
                    context.startActivity(i);
                }
            });
        }

        public void setCurrentPosition(Integer position) {
            this.current = position;
        }

        public Integer getCurrentPosition() {
            return this.current;
        }
    }

    private void showPopupMenu (View view, Item item){
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_movie, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(view, item));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{
        Item item;
        View view;

        public MyMenuItemClickListener(View view, Item item) {
            this.view = view;
            this.item = item;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.action_add_favourite:
                    Toast.makeText(context, "Adicionado aos favoritos", Toast.LENGTH_LONG).show();
                    sharedPreference.addFavorite(view.getContext(), item);
                    return true;
                default:
            }
            return false;
        }
    }

}
