package com.tommorowsoft.progressdialog;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button annual, showProgress, notShowProgress;//MainActivity中的三个按钮

    final  static int MAX_PROGRESS=100;
    private int[]data=new  int[50];//该程序模拟填充长度为50的数组
    int progressStatus=0;//记录进度对话框的百分比
    int hasData;
    ProgressDialog pd1,pd2;
    //定义一个负责更新进度条的Handler
    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
                //表明消息是由该程序发送的
            if (msg.what==0x123){
                pd2.setProgress(progressStatus);

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        annual = (Button) findViewById(R.id.annual);
        showProgress = (Button) findViewById(R.id.showProgress);
        notShowProgress = (Button) findViewById(R.id.notShowProgress);
        annual.setOnClickListener(this);
        showProgress.setOnClickListener(this);
        notShowProgress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.annual:
                showSpinner(v);
                break;
            case R.id.showProgress:
                showIndeterminate();
                break;
            case R.id.notShowProgress:
                showProgress(v);
                break;
        }

    }

    public void showSpinner(View source){
        //调用静态方法显示进度条
        ProgressDialog.show(this,"任务执行中","任务执行中，请等待",false,true);

    }

    public void showIndeterminate(){
        pd1=new ProgressDialog(MainActivity.this);
        //设置对话框的标题
        pd1.setTitle("任务正在执行中");
        //设置对话框的内容
        pd1.setMessage("任务正在执行中, 敬请等待...");
        //设置对话框能用"取消"按钮关闭
        pd1.setCancelable(true);
        //设置对话框的进度风格
        pd1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //设置对话框的进度条能否显示进度
        pd1.setIndeterminate(true);
        pd1.show();
    }

    public void showProgress(View source){
        //将进度条的方程进度重设为0
        progressStatus=0;
        //重设开始填充数组
        hasData=0;
        pd2=new ProgressDialog(MainActivity.this);
        pd2.setMax(MAX_PROGRESS);
        //设置对话框的标题
        pd2.setTitle("任务完成百分比");
        //设置对话框的内容
        pd2.setMessage("耗时任务完成的百分比");
        //设置对话框不能用取消按钮关闭
        pd2.setCancelable(false);
        //设置对话框进度条的风格
        pd2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //设置对话框的进度条是否显示进度
        pd2.setIndeterminate(false);
        pd2.show();

        new Thread(){
            @Override
            public void run() {
                while (progressStatus<MAX_PROGRESS){
                    //获取耗时操作的完成百分比
                    progressStatus=MAX_PROGRESS*downWork()/data.length;
                    //发送空消息到Handler
                    handler.sendEmptyMessage(0x123);


                }
                //如果已经完成任务
                    if (progressStatus>=MAX_PROGRESS){
                        //关闭对话框
                        pd2.dismiss();
                    }
            }
        }.start();
    }
//模拟一个耗时操作
    public  int downWork(){
        //为元素赋值
        data[hasData++]= (int) (Math.random()*100);
        try{
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return hasData;
    }

}
