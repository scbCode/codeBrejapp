package brejapp.com.brejapp;

public class notifySimples {


    public   String title;
    public  String msg;
    public  String urlimg;
    public  String nometopic;
    public  String nome;
    public   String bigImg;
    public   String time;
    public   String id;
    public   String Tipo;
    public   String cor;
    public   String preco;
    public   String uni;

    public notifySimples(){

    }

    public notifySimples(String title,
        String msg,
        String nometopic,
        String urlimg,
        String nome,
        String bigImg,
        String time,    String id,String Tipo,String cor,String preco,String uni){

        this.title=title;
        this.msg=msg;
        this.urlimg=urlimg;
        this.nometopic=nometopic;
        this.nome=nome;
        this.bigImg=bigImg;
        this.time=time;
        this.id=id;
        this.Tipo=Tipo;
        this.cor=cor;
        this.preco=preco;
        this.uni=uni;

    }
}
