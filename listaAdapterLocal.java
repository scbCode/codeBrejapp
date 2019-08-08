package brejapp.com.brejapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import brejapp.com.brejapp.R;


public class listaAdapterLocal extends ArrayAdapter<localloja> {


    int totalHeight;
    public ListView listaLojas;

    public listaAdapterLocal(ArrayList<localloja> nome, Context act,ListView listaLojas) {
        super(act, 0, nome);
        this.listaLojas = listaLojas;
  }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemloja, parent, false);
        }

        final String n = getItem(position).name;
        Log.i("local","local "+n);
        TextView text = (TextView) convertView.findViewById(R.id.nomeLojaitem);
        text.setText(n);

        Button btnremove = (Button) convertView.findViewById(R.id.btnremove);
        btnremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity_empresa.removelocal(position);
            }
        });




        return convertView;
    }



}
