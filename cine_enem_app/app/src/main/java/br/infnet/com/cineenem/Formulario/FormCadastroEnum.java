package br.infnet.com.cineenem.Formulario;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import br.infnet.com.cineenem.Formulario.FormInterface;
import br.infnet.com.cineenem.R;
import br.infnet.com.cineenem.Util.FormUtils;


public enum FormCadastroEnum  implements FormInterface {

    edtConfirmSenha {
        @Override
        public String getValor(Context ctx) throws Exception {
            EditText f = getField(ctx);
            String valor = f.getText().toString();
            if( valor.trim().isEmpty() ) {
                f.requestFocus();
                throw new Exception("Campo confirmar senha inválido");
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
            return (EditText) ( (Activity) ctx).findViewById(R.id.edtConfirmSenha);
        }
    };

}
