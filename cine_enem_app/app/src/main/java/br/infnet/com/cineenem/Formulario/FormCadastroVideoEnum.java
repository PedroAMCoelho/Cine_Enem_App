package br.infnet.com.cineenem.Formulario;

import android.app.Activity;
import android.content.Context;
import android.nfc.FormatException;
import android.widget.EditText;
import android.widget.Spinner;

import br.infnet.com.cineenem.R;

public enum FormCadastroVideoEnum implements FormInterface {
    edtTitle {
        @Override
        public String getValor(Context ctx) throws FormatException {
            EditText f = getField(ctx);
            String valor = f.getText().toString();
            if(valor.trim().isEmpty()) {
                f.requestFocus();
                throw new FormatException("Campo Título Inválido");
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
            return (EditText) ( (Activity) ctx).findViewById(R.id.edtTitle);
        }
    },
    edtCode {
        @Override
        public String getValor(Context ctx) throws FormatException {
            EditText f = getField(ctx);
            String valor = f.getText().toString();
            if(valor.trim().isEmpty()) {
                f.requestFocus();
                throw new FormatException("Campo Código YouTube Inválido");
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
            return (EditText) ( (Activity) ctx).findViewById(R.id.edtCode);
        }
    },
    edtImdb {
        @Override
        public String getValor(Context ctx) throws FormatException {
            EditText f = getField(ctx);
            String valor = f.getText().toString();
            if(valor.trim().isEmpty()) {
                f.requestFocus();
                throw new FormatException("Campo Código Imdb Inválido");
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
            return (EditText) ( (Activity) ctx).findViewById(R.id.edtImdb);
        }
    },
    edtDescription {
        @Override
        public String getValor(Context ctx) throws FormatException {
            EditText f = getField(ctx);
            String valor = f.getText().toString();
            if(valor.trim().isEmpty()) {
                f.requestFocus();
                throw new FormatException("Campo Descrição Inválido");
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
            return (EditText) ( (Activity) ctx).findViewById(R.id.edtDescription);
        }
    },
    spnCategoria {
        @Override
        public String getValor(Context ctx) throws Exception {
            Spinner f = getField(ctx);
            return f.getSelectedItem().toString();
        }

        @Override
        public boolean setValor(Object valor, Context ctx) {

            int index = (String.valueOf(valor).isEmpty()) ?
                    0 : Integer.parseInt(String.valueOf(valor));
            getField(ctx).setSelection(index);

            return true;
        }

        @Override
        public Spinner getField(Context ctx) {
            return (Spinner) ( (Activity) ctx).findViewById(R.id.spnCategoria);
        }
    }
}
