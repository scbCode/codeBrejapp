package brejapp.com.brejapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class adapterLojascesta extends ArrayAdapter<Itemloja> {

    private int resourceLayout;
    private Context mContext;

    public adapterLojascesta(Context context, int resource, List<Itemloja> items) {
        super(context, resource, items);

        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {

        View vw = v;



        if (vw == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            vw = vi.inflate(resourceLayout, null);
        }

        final Itemloja model = getItem(position);
        Typeface   mTypeface = MainActivity_cesta.mTypeface;
        Typeface   mTypeface2 = MainActivity_cesta.mTypeface2;

        int cont=0;
        String prods="Produtos: ";
        Double totalp=0.0;

        for (int ii=0;ii<MainActivity_cesta.mycestaloja.size();ii++) {
            if (MainActivity_cesta.mycestaloja.get(ii).equals(model.Nome))
                cont++;
        }
//
//
        for (int i=0;i<MainActivity_cesta.mycesta.size();i++) {
            if (MainActivity_cesta.mycesta.get(i).supermercado.equals(model.Nome)) {
                int qt =(MainActivity_cesta.mycesta.get(i).quantidade);
                if (qt==0)qt=1;
                prods+=MainActivity_cesta.mycesta.get(i).NomeProduto+" ";

                totalp += Double.parseDouble(MainActivity_cesta.mycesta.get(i).Preco)*qt;

            }
        }

        TextView total = (TextView) vw.findViewById(R.id.total);
        String pt=  String.format("%.2f", totalp);
        String t = String.valueOf(pt).replace(".",",");

        total.setText("R$ "+t);
        total.setTypeface(mTypeface2);

        ImageView  tagcred = (ImageView) vw.findViewById(R.id.checkcred);
        ImageView  tagdel = (ImageView) vw. findViewById(R.id.checkdev);
        ImageView  tagdin = (ImageView)  vw.findViewById(R.id.checkdin);

        tagdin.setImageResource(R.drawable.check);

        if (model.delivery==true)
            tagdel.setImageResource(R.drawable.check);
        else
            tagdel.setImageResource(R.drawable.checkoff);

        if (model.credito==true)
            tagcred.setImageResource(R.drawable.check);
        else
            tagcred.setImageResource(R.drawable.checkoff);



        TextView nomenegocio = (TextView) vw.findViewById(R.id.nomenegocio);
        nomenegocio.setTypeface(mTypeface);
        nomenegocio.setText(model.Nome);

        final TextView dist = (TextView) vw.findViewById(R.id.txtdistcesta);
        dist.setTypeface(mTypeface);
        try{
        if (MainActivity.logdistances.size()>0){
            for (int l=0;l<MainActivity.lojas.size();l++){
                if (MainActivity.lojas.get(l).Nome.equals(model.Nome)){
                    Double d =  (MainActivity.logdistances.get(l));
                    Double df =  (MainActivity.logdistances.get(l)/1000);
                    if(d>=1000)
                        dist.setText("Distância: " + String.format("%.2f", df)+" km");
                    else
                    if(d<1000)
                        dist.setText("Distância: " + String.format("%.2f", d)+" m");
                }
            }
        }else{
            dist.setText("");
        }}catch (Exception e){}

        TextView produtos = (TextView) vw.findViewById(R.id.txtproduts);
        produtos.setTypeface(mTypeface);
        produtos.setText(prods);

        TextView itens = (TextView) vw.findViewById(R.id.totalitem);
        itens.setTypeface(mTypeface);

        if (cont>1)
            itens.setText(cont+" itens");
        if (cont == 1)
            itens.setText(cont+" item");



        ImageView logo = (ImageView) vw.findViewById(R.id.imgloja);
        Glide.with(mContext).load(model.urlic).into(logo);


        vw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity_cesta.getloja(model,dist.getText().toString());
            }
        });

        return vw;
    }



}