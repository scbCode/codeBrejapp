package brejapp.com.brejapp;

import java.util.Map;

public class Chatmsgsend {


    public String msg;
    public String remetent;
    public String viewMsg;
    public Map<String, String> timestamp;

    public Chatmsgsend(){}
    public Chatmsgsend(String msg, Map<String, String> timestamp, String remetent , String viewMsg){
        this.msg=msg;
        this.timestamp=timestamp;
        this.remetent=remetent;
        this.viewMsg=viewMsg;
    }

}
