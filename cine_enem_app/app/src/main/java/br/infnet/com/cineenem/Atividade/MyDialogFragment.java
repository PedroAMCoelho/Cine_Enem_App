package br.infnet.com.cineenem.Atividade;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.ads.InterstitialAd;


/**
 * Created by suiamcosta on 28/11/17.
 */

public class MyDialogFragment extends DialogFragment {

    private InterstitialAd mInterstitialAd;

    public void setAdd(InterstitialAd ad) {
        mInterstitialAd = ad;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Deseja ver o próximo video?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d("-------isLoaded()", Boolean.toString(mInterstitialAd.isLoaded()));
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });


        builder.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent mainActivity = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
            }
        });

        return builder.create();

    }
}
