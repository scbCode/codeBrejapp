package brejapp.com.brejapp;

public class Empresa {

    public String Nome;
    public String Tel;
    public String CNPJCPF;
    public String Desc;
    public String Categoria;
    public Object Lojas;
    public Object Locais;
    public String Horarios;
    public String UrlImg;
    public String TimeCreate;
    public String Email;
    public String Topico;
    public String Id;
    public String Uid;
    public String End;

    public  Empresa(){

    }

    public  Empresa(    String Nome,
                        String Tel,
                        String CNPJCPF,
                        String Desc,
                        String Categoria,
                        Object Lojas,
                        Object Locais,
                        String Horarios,
                        String UrlImg,
                        String Id,
                        String TimeCreate,String Email,
                                String Topico,String Uid,String End
    ){

        this.Nome = Nome;
        this.Tel = Tel;
        this.CNPJCPF = CNPJCPF;
        this.Desc = Desc;
        this.Categoria = Categoria;
        this.Lojas = Lojas;
        this.Locais = Locais;
        this.Horarios = Horarios;
        this.UrlImg = UrlImg;
        this.Id = Id;
        this.TimeCreate = TimeCreate;
        this.Email = Email;
        this.Topico = Topico;
        this.Uid = Uid;
        this.End = End;

    }
}
