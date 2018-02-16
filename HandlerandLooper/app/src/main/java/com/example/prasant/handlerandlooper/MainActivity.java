package com.example.prasant.handlerandlooper;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    //@BindView(R.id.progressBar1)
    ProgressBar progressBar1;

    private final Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int val=msg.getData().getInt("Arg1");
            Log.i("VALUE",String.valueOf(val));
            progressBar1.setProgress(val);
        }
    };
    Thread thread1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar1=findViewById(R.id.progressBar1);
        thread1=new Thread(new MyThread());
        thread1.start();
    }

    class MyThread implements Runnable{

        @Override
        public void run() {

            for(int i=1;i<=100;i++){
                Message message=Message.obtain();
                if(message!=null) {
                    Bundle bundle=new Bundle();
                    bundle.putInt("Arg1",i);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
