package brejapp.com.brejapp;

/**
 * Created by Rayza on 08/02/2018.
 */
public class Item {

    public String NomeProduto;
    public String Preco;
    public String Descricao;
    public String Tipo;
    public String iconproduto;
    public int idicon;
    public String iditem;
    public String marca;
    public String datai;
    public String unidade;
    public String supermercado;
    public String corsuper;
    public String urlic;
    public String urlimg;
    public String Cidade;
    public String expirar;
    public String cod;
    public String tagbusca;
    public int precoorder;
    public String detalsup;

    public double time;
    public Object loja;
    public Object tags;

    public Item() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Item(String NomeProduto, String Prec, String Descricao, String Tipo, String id, String uni, String supermercado, String c, String urlic, String urliimg, String data, String expi, double t, String cod, Object loja, Object tags, String tagbusca, int precoorder, String detalsupdetalsup,String marca) {

        this.NomeProduto = NomeProduto;
        this.Preco = Prec;
        this.Descricao = Descricao;
        this.Tipo = Tipo;
        this.idicon = idicon;
        this.iditem = id;
        this.unidade = uni;
        this.supermercado = supermercado;
        this.corsuper = c;
        this.urlic = urlic;
        this.Cidade = Cidade;
        this.urlimg = urliimg;
        this.datai = data;
        this.expirar = expi;
        this.time = t*-1;
        this.cod = cod;
        this. loja = loja;
        this.tags = tags;
        this.tagbusca = tagbusca;
        this. precoorder=precoorder;
        this. detalsup=detalsup;
        this. marca=marca;
    }





}