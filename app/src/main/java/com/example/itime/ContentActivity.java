package com.example.itime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class ContentActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_EDIT = 903;

    private TextView textView_contentTitle,textView_contentDdl,textView_contentCount;
    private int editPosition,photoId;
    private String ddl,title;
    private CountDownTimer countDownTimer;
    private Chronometer countUpTimer;
    private TimerTask timerTask;
    private long timeMillis_now,timeMillis_set,diff;
    private ImageView imageView;
    private long day;
    public static String dateFormatYMDofChinese = "yyyy年MM月dd日";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView_contentCount=findViewById(R.id.textView_contentCount);
        textView_contentTitle=findViewById(R.id.textView_contentTitle);
        textView_contentDdl=findViewById(R.id.textView_contentDdl);
        imageView=findViewById(R.id.imageView_toolbar);

        AppBarLayout appBarLayout=findViewById(R.id.app_bar);
        Bundle bundle=getIntent().getExtras();
        title=bundle.getString("title");
        ddl=bundle.getString("ddl");
        editPosition =bundle.getInt("position");
        photoId=bundle.getInt("photoId");

        imageView.setImageResource(photoId);
        /*AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
        alpha.setDuration(0);
        alpha.setFillAfter(true);
        imageView.startAnimation(alpha);*/
        //getWindow().setBackgroundDrawableResource(photoId);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        transToMillis();
        initData();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_edit://进入编辑界面
                Intent intent=new Intent(ContentActivity.this,AddNewActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("ddl",ddl);
                setResult(RESULT_OK, intent);
                startActivityForResult(intent,1);

                break;
            case R.id.item_delete:
                new android.app.AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("询问")
                        .setMessage("是否删除该计时？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ContentActivity.this,MainActivity.class);
                                intent.putExtra("info",0);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create().show();
                break;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case 1:{
                if(resultCode==1)
                {
                    Intent intent = new Intent();
                    String title=data.getStringExtra("biaoti");
                    String riqi=data.getStringExtra("riqi");

                    intent.putExtra("title",title);
                    intent.putExtra("ddl",riqi);
                    intent.putExtra("day",day);
                    setResult(3,intent);

                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

                break;
            }


        }
    }


    public void initData() {
        textView_contentTitle.setText(title);
        textView_contentDdl.setText(ddl);


            countDownTimer = new CountDownTimer(diff, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (!ContentActivity.this.isFinishing()) {


                        day = millisUntilFinished / (1000 * 24 * 60 * 60); //单位天

                        long hour = (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60);
                        //单位时
                        long minute = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60);
                        //单位分
                        long second = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;
                        //单位秒
                        textView_contentCount.setText(day + "天" + hour + "小时" + minute + "分钟" + second + "秒");
                    }
                }
                /**
                 * 倒计时结束后调用的
                 */
                @Override
                public void onFinish() {

                }
            };
            countDownTimer.start();



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public void transToMillis() {

        try{
        Calendar calendar = Calendar.getInstance();
        timeMillis_now=calendar.getTimeInMillis();
        calendar.setTime(new SimpleDateFormat("yyyy年MM月dd日").parse(ddl));
        timeMillis_set=calendar.getTimeInMillis();
        diff=timeMillis_set-timeMillis_now;
        }catch(ParseException e){
            e.printStackTrace();
        }

    }

    /**
     * 取指定日期为星期几
     *
     * @param strDate  指定日期
     * @param inFormat 指定日期格式
     * @return String   星期几
     */
    public static String getWeekNumber(String strDate, String inFormat) {
        String week = "星期日";
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat(inFormat);
        try {
            calendar.setTime(df.parse(strDate));
        } catch (Exception e) {
            return "错误";
        }
        int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (intTemp) {
            case 0:
                week = "星期日";
                break;
            case 1:
                week = "星期一";
                break;
            case 2:
                week = "星期二";
                break;
            case 3:
                week = "星期三";
                break;
            case 4:
                week = "星期四";
                break;
            case 5:
                week = "星期五";
                break;
            case 6:
                week = "星期六";
                break;
        }
        return week;
    }

}
