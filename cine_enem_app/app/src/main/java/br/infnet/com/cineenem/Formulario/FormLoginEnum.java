package br.infnet.com.cineenem.Formulario;


import android.app.Activity;
import android.content.Context;
import android.nfc.FormatException;
import android.widget.EditText;

import br.infnet.com.cineenem.Formulario.FormInterface;
import br.infnet.com.cineenem.R;

public enum FormLoginEnum implements FormInterface {
    edtEmail {
        @Override
        public String getValor(Context ctx) throws FormatException {
            EditText f = getField(ctx);
            String valor = f.getText().toString();
            if(valor.trim().isEmpty()) {
                f.requestFocus();
                throw new FormatException("Campo Nome Inválido");
            }
            return valor;
        }

        @Override
        public boolean setValor(Object valor,Context ctx) {
            getField(ctx).setText(valor.toString());
            return true;
        }

        @Override
        public EditText getField(Context ctx) {
            return (EditText) ( (Activity) ctx).findViewById(R.id.edtEmail);
        }
    },
    edtSenha {
        @Override
        public String getValor(Context ctx) throws FormatException {
            EditText f = getField(ctx);
            String valor = f.getText().toString();
            if(valor.trim().isEmpty()) {
                f.requestFocus();
                throw new FormatException("Campo Senha Inválido");
            }
            return valor;
        }

        @Override
        public boolean setValor(Object valor,Context ctx) {
            getField(ctx).setText(valor.toString());
            return true;
        }

        @Override
        public EditText getField(Context ctx) {
            return (EditText) ( (Activity) ctx).findViewById(R.id.edtSenha);
        }
    };
}
