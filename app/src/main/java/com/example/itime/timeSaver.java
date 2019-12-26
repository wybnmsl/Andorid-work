package com.example.itime;

import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class timeSaver {
    Context context;
    ArrayList<Time> times =new ArrayList<Time>();

    public timeSaver(Context context) {
        this.context = context;
    }

    public ArrayList<Time> getTimes() {
        return times;
    }
    public void save(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(context.openFileOutput("Serializable.txt",Context.MODE_PRIVATE));
            outputStream.writeObject(times);
            outputStream.close();    //对象的序列化

        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public ArrayList<Time> load(){
        try{
            ObjectInputStream inputStream = new ObjectInputStream(context.openFileInput("Serializable.txt"));
            times = (ArrayList<Time>) inputStream.readObject();
            inputStream.close();     //对象的反序列化
        }catch(Exception e){
            e.printStackTrace();
        }
        return times;
    }
}


