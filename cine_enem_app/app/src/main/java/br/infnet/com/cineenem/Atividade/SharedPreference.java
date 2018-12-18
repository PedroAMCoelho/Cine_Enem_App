package br.infnet.com.cineenem.Atividade;

/**
 * Created by suiamcosta on 04/12/17.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.infnet.com.cineenem.Entidade.Item;

public class SharedPreference {

    public static final String PREFS_NAME = "Movie_APP";
    public static final String FAVORITES = "Movie_Favorite";

    public SharedPreference() {
        super();
    }


    public void saveFavorites(Context context, List<Item> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, Item product) {
        List<Item> favorites = getFavorites(context);

        if (favorites == null) {
            favorites = new ArrayList<Item>();
            favorites.add(product);
            saveFavorites(context, favorites);
        }
        else {
            boolean found = false;
            for (Item f : favorites) {
                if (f.getTitulo().equals(product.getTitulo()))
                    found = true;
            }
            if (!found) {
                favorites.add(product);
                saveFavorites(context, favorites);
            }
        }
    }

    public void removeFavorite(Context context, Item item) {
        ArrayList<Item> favorites = getFavorites(context);
        if (favorites != null) {
            if (favorites.remove(item)) {
                saveFavorites(context, favorites);
            }
        }
    }

    public ArrayList<Item> getFavorites(Context context) {
        SharedPreferences settings;
        List<Item> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Item[] favoriteItems = gson.fromJson(jsonFavorites,
                    Item[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Item>(favorites);
        } else
            return null;

        return (ArrayList<Item>) favorites;
    }
}

