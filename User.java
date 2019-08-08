package brejapp.com.brejapp;

public class User {

    public String Nome;
    public String Email;
    public String Uid;
    public String Token;
    public String plano;
    public Object topicosInscritos;
    public Object TopicosCriados ;
    static User mUser;

    public User(){

    }

    public User(String Nome, String Uid, String Email, Object TopicosCriados, Object topicosInscritos, String Token, String plano){
        this.Nome = Nome;
        this.Email = Email;
        this.Uid = Uid;
        this.Token = Token;
        this.plano = plano;
        this.topicosInscritos = topicosInscritos;
        this.TopicosCriados = TopicosCriados;
    }
}
