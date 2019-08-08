package brejapp.com.brejapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class adapterLojasfav extends ArrayAdapter<Itemloja> {

    private int resourceLayout;
    private Context mContext;
    String prods = "Produtos: ";int countfav;
    static  String viewfav;

    public adapterLojasfav(Context context, int resource, List<Itemloja> items) {
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
        //get lista produto
        final View finalVw = vw;
        FirebaseDatabase.getInstance().getReference().child("Catalogos").orderByChild("supermercado").equalTo(model.Nome).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Item i = postSnapshot.getValue(Item.class);
                    Log.i("super","super "+i.NomeProduto);
                    prods+= i.NomeProduto.replace("Cerveja","")+" ";
                }

                TextView produtos = (TextView) finalVw.findViewById(R.id.txtproduts);
                produtos.setText(prods);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final   ImageView iconrating = (ImageView) vw.findViewById(R.id.iconfav);
        iconrating.setBackgroundResource(R.drawable.starx);

        final   ImageView iconfav = (ImageView) vw.findViewById(R.id.iconfavloja);

        final View finalVw1 = vw;
        FirebaseDatabase.getInstance().getReference().child("Level").child(model.Nome)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        countfav= Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()))   ;
                        TextView totalinsc = (TextView) finalVw1.findViewById(R.id.txtfav);
                        if (countfav>5)
                            totalinsc.setText(countfav+"");
                        else
                            totalinsc.setText("Novo!");

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        ImageView  tagcred = (ImageView) vw.findViewById(R.id.checkcred);
        ImageView  tagdel = (ImageView) vw. findViewById(R.id.checkdev);
        ImageView  tagdin = (ImageView)  vw.findViewById(R.id.checkdin);

        tagdin.setImageResource(R.drawable.check);

        if (model.delivery==true && model.status==true)
            tagdel.setImageResource(R.drawable.check);
        else
            tagdel.setImageResource(R.drawable.checkoff);

        if (model.credito==true)
            tagcred.setImageResource(R.drawable.check);
        else
            tagcred.setImageResource(R.drawable.checkoff);


        TextView nomenegocio = (TextView) vw.findViewById(R.id.nomenegocio);
        nomenegocio.setText(model.Nome);

        try {
            final TextView dist = (TextView) vw.findViewById(R.id.txtdistcesta);
            if (MainActivity.logdistances.size() > 0) {
                for (int l = 0; l < MainActivity.lojas.size(); l++) {
                    if (MainActivity.lojas.get(l).Nome.equals(model.Nome)) {
                        Double d = (MainActivity.logdistances.get(l));
                        Double df = (MainActivity.logdistances.get(l) / 1000);
                        if (d >= 1000)
                            dist.setText("Distância: " + String.format("%.2f", df) + " km");
                        else if (d < 1000)
                            dist.setText("Distância: " + String.format("%.2f", d) + " m");
                    }
                }
            } else {
                dist.setText("");
            }
        }catch (Exception e){}
        ImageView logo = (ImageView) vw.findViewById(R.id.imgloja);
        Glide.with(mContext).load(model.urlic).into(logo);
        ImageView clickitem = (ImageView) vw.findViewById(R.id.clickitem);

        clickitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("click","click x");

                Intent intentMain = new Intent(getContext() ,
                        MainActivity_lojas.class);
                intentMain.putExtra("LojaView",model.Nome);
                getContext().startActivity(intentMain);

            }
        });

        return vw;
    }



}