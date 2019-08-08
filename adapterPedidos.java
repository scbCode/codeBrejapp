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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class adapterPedidos extends ArrayAdapter<PedidoReceiver> {

    private int resourceLayout;
    private Context mContext;

    public adapterPedidos(Context context, int resource, List<PedidoReceiver> items) {
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

        final PedidoReceiver pedido = getItem(position);
        final TextView stat = (TextView) vw.findViewById(R.id.txtstatus);
        stat.setTypeface(MainActivity.fontBGB);

        final Button status = (Button) vw.findViewById(R.id.btnstatus) ;
        final Button recusar = (Button) vw.findViewById(R.id.btnrecusar) ;

        if (pedido.status.equals("solicitando")){
            status.setText("Aceitar pedido");
            status.setVisibility(View.VISIBLE);
            recusar.setVisibility(View.VISIBLE);
            stat.setTextColor(0xFFCC0000);

        }
        else
        if (pedido.status.equals("recebido")){
            recusar.setVisibility(View.GONE);
            status.setVisibility(View.VISIBLE);
            status.setText("Saiu para entrega?");
            stat.setText("Status: Pedido Aceito");
            stat.setTextColor(0xFFCC0000);

        }
        else
        if (pedido.status.equals("acaminho")){
            recusar.setVisibility(View.GONE);
            status.setVisibility(View.INVISIBLE);
            recusar.setVisibility(View.VISIBLE);
            recusar.setText("ENCERRAR");
            stat.setTextColor(0xFFCC0000);

            stat.setText("Status: Saiu para entrega!");
        }
        else
        if (pedido.status.equals("RECUSADO")){
            recusar.setVisibility(View.VISIBLE);
            recusar.setText("ENCERRAR");
            status.setVisibility(View.INVISIBLE);
            stat.setText("RECUSADO");
            stat.setTextColor(0xFFCC0000);

        }

        else
        if (pedido.status.equals("entregue")){
            recusar.setVisibility(View.VISIBLE);
            recusar.setText("ENCERRAR");
            status.setVisibility(View.INVISIBLE);
            stat.setText("Chegou");
            stat.setTextColor(0xFF06AE00);
        }




        TextView endpedido = (TextView) vw.findViewById(R.id.endpedido);
        TextView useremail = (TextView) vw.findViewById(R.id.emauluser);
        TextView totalpedido = (TextView) vw.findViewById(R.id.totalpedido);
        TextView frete = (TextView) vw.findViewById(R.id.fretepedido);
        TextView lista = (TextView) vw.findViewById(R.id.listapedido);
        TextView pag = (TextView) vw.findViewById(R.id.formapag);
        TextView h = (TextView) vw.findViewById(R.id.hora);
        TextView troco = (TextView) vw.findViewById(R.id.txttroco);

        try {
            if (pedido.troco != null && pedido.troco.length() > 0)
                troco.setText("Troco para: R$ " + pedido.troco);
        }catch (Exception e){}

        try {
        endpedido.setTypeface(MainActivity.fontBGR);
        useremail.setTypeface(MainActivity.fontBGR);
        totalpedido.setTypeface(MainActivity.fontBGR);
        frete.setTypeface(MainActivity.fontBGR);
        lista.setTypeface(MainActivity.fontBGR);
        pag.setTypeface(MainActivity.fontBGR);
        h.setTypeface(MainActivity.fontBGR);



        for (int i = 0 ; i < MainActivity_delivery.bairroslist.size();i++){
            if (MainActivity_delivery.bairroslist.get(i).equals(pedido.bairro)){
                Double t = Double.parseDouble(MainActivity_delivery.bairrosvals.get(i).toString());
                String total= String.format("%.2f", t);
                frete.setText("Frete: R$ "+total);
            }
        }

        pag.setText(pedido.pagamento.replace("|",""));
        }catch (Exception e ){

        }

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(String.valueOf(pedido.timestamp)));
        Date resultdateExp = new Date(calendar.getTimeInMillis());//GET DATE EXP.
        String dateInexp = formatter.format(resultdateExp);
        h.setText(""+dateInexp);

        final String emailf=MainActivity_delivery.listausers.get(position);
        String email=MainActivity_delivery.listausers.get(position);
        for(int i =0; i < email.length();i++){
                email  = email.replace("AAA",".");
                email  = email.replace("BBB","@");
        }
        useremail.setText(email);
        endpedido.setText("Endereço:\n"+pedido.end+"\nBairro: "+pedido.bairro);
        Double t = Double.parseDouble(pedido.total.toString());
        String total= String.format("%.2f", t);
        totalpedido.setText("Total: R$ "+total);
        int quantd = 0;

        String list = "Lista:\n";
        for (int i = 0; i < pedido.lista.size();i++){
            quantd=pedido.lista.get(i).quantidade+1;
            list+= quantd+ " x "+pedido.lista.get(i).NomeProduto+"\n    "+pedido.lista.get(i).Descricao+""+"\n";
        }


        final String finalEmail = email;

        recusar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pedido.status.equals("entregue") || pedido.status.equals("acaminho") ) {
                    FirebaseDatabase.getInstance().getReference().child("LojasPedidos_hist").child(pedido.loja)
                            .child("Pedidos").child(emailf).child(MainActivity_delivery.idspedido.get(position)).setValue(pedido).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseDatabase.getInstance().getReference().child("LojasPedidos").child(pedido.loja).child(emailf).child("Pedidos").child(MainActivity_delivery.idspedido.get(position))
                                    .child("status").setValue("finalizadoaceito");
//                            FirebaseDatabase.getInstance().getReference().child("LojasPedidos").child(pedido.loja)
//                                    .child(emailf).child("Pedidos").child(MainActivity_delivery.idspedido.get(position))
//                                    .removeValue();
                        }
                    });

            }
                else
                if (!pedido.status.equals("RECUSADO") && pedido.status !=null ) {
                    FirebaseDatabase.getInstance().getReference().child("LojasPedidos").child(pedido.loja).child(emailf).child("Pedidos").child(MainActivity_delivery.idspedido.get(position))
                            .child("status").setValue("RECUSADO");
                }else {
                    FirebaseDatabase.getInstance().getReference().child("LojasPedidos").child(pedido.loja).child(emailf).child("Pedidos").child(MainActivity_delivery.idspedido.get(position))
                            .child("status").setValue("recusadofinal");
//                    FirebaseDatabase.getInstance().getReference().child("LojasPedidos").child(pedido.loja).child(emailf).child("Pedidos")
//                            .child(MainActivity_delivery.idspedido.get(position)).removeValue();
                }
            }
        });


        Button btnchat = (Button) vw. findViewById(R.id.btnchat);
        btnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentMain = new Intent(getContext(),
                        MainActivity_chat.class);
                intentMain.putExtra("Chatuser", emailf);
                intentMain.putExtra("Loja", pedido.loja);

                getContext().startActivity(intentMain);

            }
        });



        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stt = "";

                if (pedido.status.equals("solicitando")){
                    stt = "recebido";
                }
                else
                if (pedido.status.equals("recebido")){
                    stt = "acaminho";
                }

                /////
                if (pedido.status.equals("solicitando")){
                    status.setText("Aceitar pedido");
                }
                else
                if (pedido.status.equals("recebido")){
                    status.setText("Saiu para entrega?");
                    recusar.setVisibility(View.GONE);

                    stat.setText("Status: Aceito");
                }
                else
                if (pedido.status.equals("acaminho")){
                    status.setVisibility(View.GONE);
                    recusar.setVisibility(View.GONE);

                    stat.setText("Status: Saiu para entrega!");
                }

                FirebaseDatabase.getInstance().getReference().child("LojasPedidos").child(pedido.loja).child(emailf).child("Pedidos").child(MainActivity_delivery.idspedido.get(position))
                .child("status").setValue(stt);
            }
        });

        lista.setText(list);

        return vw;
    }



}