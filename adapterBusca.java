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

import brejapp.com.brejapp.R;

public class adapterBusca extends ArrayAdapter<Item> {

    private int resourceLayout;
    private Context mContext;

    public adapterBusca(Context context, int resource, List<Item> items) {
        super(context, resource, items);

        this.resourceLayout = resource;
        this.mContext = context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vw = convertView;

        if (vw == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            vw = vi.inflate(resourceLayout, null);
        }

        final Item model = getItem(position);


        final ImageView deliveryon = (ImageView) vw.findViewById(R.id.deliveryOn);
        deliveryon.setVisibility(View.INVISIBLE);

        for (int i = 0; i < MainActivity.deliveryOn.size(); i++){

            Log.i("DEL","DEL ON");

            if (MainActivity.deliveryOn.get(i).equals(model.supermercado)){
                deliveryon.setVisibility(View.VISIBLE);

            }

        }


        TextView txtnome = (TextView) vw.findViewById(R.id.nomeItem);
        TextView txtDesc = (TextView) vw.findViewById(R.id.descricao);
        TextView textsuperm = (TextView) vw.findViewById(R.id.superm);

        txtnome.setTypeface(MainActivity.fontA);

        textsuperm.setText(model.supermercado+" "+model.detalsup);
        txtDesc.setText(model.Descricao);
        txtnome.setText(model.NomeProduto);

        TextView dist = (TextView) vw.findViewById(R.id.txtdist);

        try {
            if (MainActivity.lat == null) dist.setText("");
            else
                for (int l = 0; l < MainActivity.lojas.size(); l++) {
                    if (MainActivity.lojas.get(l).Nome.equals(model.supermercado)) {
                        Double d = (MainActivity.logdistances.get(l));
                        Double df = (MainActivity.logdistances.get(l) / 1000);
                        Log.i("DIST", "DIST X " + MainActivity.logdistances.get(l));
                        if (d >= 1000)
                            dist.setText("Distância: " + String.format("%.2f", df) + " km");
                        else if (d < 1000)
                            dist.setText("Distância: " + String.format("%.2f", d) + " m");
                    }
                }

        }catch (Exception e){

        }

        TextView txtpreco = (TextView) vw.findViewById(R.id.Preco);

        TextView txtprecodec = (TextView) vw.findViewById(R.id.precoDec);

        TextView txtexp = (TextView) vw.findViewById(R.id.dataitem);

        final Calendar cal = Calendar.getInstance(); // Get Calendar Instance
        Calendar calend = Calendar.getInstance();
        Date dataAgora = calend.getTime();

        //GET DATA INICIO ITEM
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dtstrg = model.datai.toString()+"/2019";
        Date date1= null;

        try {
            date1 = new SimpleDateFormat("MM/dd/yyyy").parse(dtstrg);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.setTime(date1);
        cal.add(Calendar.DATE, Integer.parseInt(model.expirar));

        Log.i("time"," time "+cal.getTimeInMillis() +" "+ dataAgora.getTime());
        long diff = cal.getTimeInMillis() - dataAgora.getTime();


        //CONVERT PRECO ITEM , - .
        String precoitem = model.Preco;
        int positionpoint = 0;
        String preco= "";
        String precoDec= "";

        if (precoitem != null) {

            if (precoitem.length() == 4) positionpoint = 1;
            else
            if (precoitem.length() == 5) positionpoint = 2;
            else
            if (precoitem.length() == 6) positionpoint = 3;
            else
            if (precoitem.length() == 7) positionpoint = 4;

            //CONVERT PRECO ITEM , - .
            if (positionpoint == 1) {
                preco = String.valueOf(model.Preco.charAt(0));
                precoDec = String.valueOf(model.Preco.charAt(2)) + String.valueOf(model.Preco.charAt(3));
                txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36);
                txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            } else if (positionpoint == 2) {
                preco = String.valueOf(model.Preco.charAt(0)) + String.valueOf(model.Preco.charAt(1));
                precoDec = String.valueOf(model.Preco.charAt(3)) + String.valueOf(model.Preco.charAt(4));
                txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36);
                txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            } else if (positionpoint == 3) {
                preco = String.valueOf(model.Preco.charAt(0)) + String.valueOf(model.Preco.charAt(1)) + String.valueOf(model.Preco.charAt(2));
                precoDec = String.valueOf(model.Preco.charAt(4)) + String.valueOf(model.Preco.charAt(5));
                txtpreco.setPadding(0, 0, 0, 0);
                txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
                txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            } else if (positionpoint == 4) {
                txtpreco.setPadding(0, 0, 0, 0);
                preco = String.valueOf(String.valueOf(model.Preco.charAt(0)) + String.valueOf(model.Preco.charAt(1)) + String.valueOf(model.Preco.charAt(2)) + String.valueOf(model.Preco.charAt(3)));
                precoDec = String.valueOf(String.valueOf(model.Preco.charAt(5)) + String.valueOf(model.Preco.charAt(6)));
                txtpreco.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
                txtprecodec.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            }

        }

        //edit text preço
        txtpreco.setText(preco + ",");
        txtprecodec.setText(precoDec);

        TextView txttipo = (TextView) vw.findViewById(R.id.unidade);
        txttipo.setText("" + model.unidade);

        TextView txtsuperm = (TextView) vw.findViewById(R.id.superm);
        txtsuperm.setText( model.supermercado);

        if (model.corsuper != null) {
            int corsuper = Color.parseColor("" + model.corsuper);
            txtsuperm.setTextColor(corsuper);
        }

        ImageView icon = (ImageView) vw.findViewById(R.id.iconItemdash);
        Glide.with(getContext()).load(model.urlic).into(icon);

        vw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity myact = new MainActivity();
                myact.clickitem(model,position);
            }
        });

        return vw;

    }



}