package br.infnet.com.cineenem.Formulario;


import android.content.Context;

public interface FormInterface {
    public abstract <T>T getField(Context ctx);
    public abstract boolean setValor(Object valor,Context ctx);
    public abstract <T>T getValor(Context ctx) throws Exception;
}
