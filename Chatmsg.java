package brejapp.com.brejapp;

import java.util.Map;

public class Chatmsg {

        public String msg;
        public  long timestamp;
        public String remetent;
        public String viewMsg;


    public Chatmsg(){}


        public Chatmsg(String msg,  long timestamp, String remetent , String viewMsg){
            this.msg=msg;
            this.timestamp=timestamp;
            this.remetent=remetent;
            this.viewMsg=viewMsg;
        }

}
