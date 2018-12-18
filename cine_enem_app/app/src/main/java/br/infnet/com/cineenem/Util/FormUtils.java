package br.infnet.com.cineenem.Util;

import android.content.Context;
import android.widget.EditText;

import br.infnet.com.cineenem.Formulario.FormInterface;


public abstract class FormUtils {

    public static void limpaCampos(Context ctx, FormInterface[] values) {
        for (FormInterface v : values ) {
            v.setValor("",ctx);
        }

        EditText first = values[0].getField(ctx);
        first.requestFocus();
    }
}
