package brejapp.com.brejapp;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Home on 22/04/2018.
 */

public class Itemloja_cartoes implements Serializable {

    public String Nome;
    public String Descricao;
    public String Cor;
    public int Quantidade=0;
    public Object coordenadas = new Object();
    public ImageView ic;
    public String urlic;
    public Object inscritos;
    public String topicontf;
    public String cat;
    public int cod ;
    public Object Lojas;
    public String call;
    public String urlBanner;
    public String whats;
    public String End;
    public boolean Debito;
    public boolean Cred;
    public boolean delivery;
    public boolean credito;
    public boolean status;


    public Itemloja_cartoes() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Itemloja_cartoes(String Nome, String Descricao, String cor, int qnt, Object Coord, String urlic, String cat, int cod, Object Lojas, Object inscritos, String topicontf, String call, String whats, String urlBanner, String End, boolean delivery, boolean credito, boolean status, boolean Cred,boolean Debito) {

        this.Nome = Nome;
        this.Descricao = Descricao;
        this.Cor = cor;
        this.Quantidade = qnt;
        this.coordenadas = Coord;
        this.urlic = urlic;
        this.cat = cat;
        this.cod =  cod;
        this.Lojas = Lojas;
        this.inscritos =  inscritos;
        this.topicontf =  topicontf;
        this.call =  call;
        this.urlBanner =  urlBanner;
        this.whats =  whats;
        this.End =  End;
        this.delivery =  delivery;
        this.credito =  credito;
        this.status =  status;
        this.Debito =  Debito;
        this.Cred =  Cred;

    }


}
