package brejapp.com.brejapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Pedido implements Serializable {

  public String end;
  public String status;
    public String pagamento;
  public String bairro;
  public String troco;
  public String total;public String loja;
  public Map<String, String> timestamp;
  public ArrayList<Itemcesta> lista;

  public Pedido(){

  }


  public Pedido(String end,String status, String pagamento, ArrayList<Itemcesta> lista,Map<String, String> timestamp,String total,String loja,String bairro,String troco){
    this.end=end;
    this.status=status;
    this.pagamento=pagamento;
    this.lista=lista;
    this.timestamp=timestamp;
    this.total=total;
      this.loja=loja;
    this.bairro=bairro;
    this.troco=troco;
  }

}
