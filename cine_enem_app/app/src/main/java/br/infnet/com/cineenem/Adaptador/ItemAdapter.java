package br.infnet.com.cineenem.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.infnet.com.cineenem.Atividade.MainActivity;
import br.infnet.com.cineenem.Atividade.SharedPreference;
import br.infnet.com.cineenem.Entidade.Item;
import br.infnet.com.cineenem.R;

public class ItemAdapter extends ArrayAdapter<Item> {

    private Context ctx;
    SharedPreference sharedPreference;
    List<Item> items;

    public ItemAdapter(Context context, List<Item> items, SharedPreference sharedPreference) {
        super(context, 0, items);
        this.ctx = context;
        this.sharedPreference = sharedPreference;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.favorites_item, parent, false);
        }
        // Lookup view for data population
        TextView titulo = (TextView) convertView.findViewById(R.id.txt_titulo);

        // Populate the data into the template view using the data object
        titulo.setText(item.getTitulo());
        // Return the completed view to render on screen

        Button btnDelete = (Button) convertView.findViewById(R.id.delete_btn);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Item> items = sharedPreference.getFavorites(getContext());
                sharedPreference.removeFavorite(getContext(), items.get(position));
                remove(items.get(position));

            }
        });

        return convertView;
    }

    @Override
    public void add(Item product) {
        super.add(product);
        items.add(product);
        notifyDataSetChanged();
    }

    @Override
    public void remove(Item product) {
        super.remove(product);
        items.remove(product);
        if (items.size() == 0) {
            Intent intent = new Intent(ctx, MainActivity.class);
            ctx.startActivity(intent);
            ( (Activity) ctx).finish();
        } else {
            notifyDataSetChanged();
        }
    }

}