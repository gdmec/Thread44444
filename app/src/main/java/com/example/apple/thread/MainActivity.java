package com.example.apple.thread;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView tv1;
    private int seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1);
        Date theLastDay = new Date(117,5,23);
        Date today = new Date();
        seconds = (int)(theLastDay.getTime()-today.getTime())/1000;
    }

    public void anr(View v){
        for (int i = 0; i <10000 ; i++) {
            BitmapFactory.decodeResource(getResources(),R.drawable.android);
        }
    }
    public void threadclass(View v){
        class ThreadSample extends  Thread{
            Random rm;
            public ThreadSample(String name){
                super(name);
                rm = new Random();
            }

            @Override
            public void run() {
                super.run();
                for (int i = 0; i <10 ; i++) {
                    tv1.setText(getName()+":"+i);
                    System.out.println(getName()+":"+i);
                    try{
                        sleep(rm.nextInt(1000));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                System.out.println(getName()+"完成");
            }
        }
        ThreadSample t1 = new ThreadSample("线程1");
        ThreadSample t2 = new ThreadSample("线程2");
        t1.start();
        t2.start();
    }

    public void runnableinterface(View v){
        class RunnableSample implements Runnable{
            Random rm;
            String name;
            public RunnableSample(String name){
                this.name=name;
                rm = new Random();
            }
            @Override
            public void run() {
                for (int i = 0; i <10 ; i++) {
                    System.out.println(name+":"+i);
                    try{
                        Thread.sleep(rm.nextInt(1000));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                System.out.println(name+"完成");
            }
        }
        Thread t1 = new Thread(new RunnableSample("线程1"));
        Thread t2 = new Thread(new RunnableSample("线程2"));
        t1.start();
        t2.start();
    }

    public void timertask(View v){
        class Mythread extends TimerTask{
            Random rm;
            String name;
            public Mythread(String name){
                this.name = name;
                rm = new Random();
            }
            @Override
            public void run() {
                for (int i = 0; i <10 ; i++) {
                    System.out.println(name+":"+i);
                    try{
                        Thread.sleep(rm.nextInt(1000));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                System.out.println(name+"完成");
            }
        }
        Timer timer1 =new Timer();
        Timer timer2 = new Timer();
        Mythread t1 = new Mythread("线程1");
        Mythread t2 = new Mythread("线程2");
        timer1.schedule(t1,0,10000);
        timer2.schedule(t2,5000,10000);
    }

    private void showmsg(String str){
        tv1.setText(str);
    }
    public void handlermessage(View v){
        final Handler myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        showmsg(msg.arg1+msg.getData().getString("attach"));
                }
            }
        } ;

        class Mytask extends TimerTask{
            private int countdown;
            private double achievement1=1,achievement2=1;
            public Mytask(int seconds){
                this.countdown = seconds;
            }
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what =1;
                msg.arg1 = countdown--;
                achievement1 = achievement1*1.01;
                achievement2 = achievement2*1.02;
                Bundle bundle = new Bundle();
                bundle.putString("attach","\n努力多1%,收获:"+achievement1+"\n努力多2%,收获:"+achievement2);
                msg.setData(bundle);
                myHandler.sendMessage(msg);
            }
        }
        Timer timer1 = new Timer();
        Mytask t1 = new Mytask(seconds);
        timer1.schedule(t1,0,1000);
    }

    public void asynctask(View v){
        class LearnHard extends AsyncTask<Long,String,String>{
            private Context context;
            private int duration  =10;
            private int count =0;
            @Override
            protected String doInBackground(Long... longs) {
                long num = longs[0].longValue();
                while (count<duration){
                    num--;
                    count++;
                    String status = "离毕业还有"+num+"秒,努力学习"+count+"秒";
                    publishProgress(status);
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                return "这"+count+"秒还是有收获的";
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                showmsg(values[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                showmsg(s);
            }
        }
        LearnHard learnhard = new LearnHard();
        learnhard.execute((long)seconds);
    }
}
