package brejapp.com.brejapp;

import java.io.Serializable;
import java.util.ArrayList;

public class listanotf implements Serializable {

    public ArrayList<notifySimples> lista;

    public listanotf(){

    }

    public listanotf(ArrayList<notifySimples> lista){

        this.lista  =  lista;
    }
}
