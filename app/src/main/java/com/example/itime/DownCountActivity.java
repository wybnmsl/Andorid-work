package com.example.itime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DownCountActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener,TimePicker.OnTimeChangedListener{

    private EditText editText_biaoti, editText_beizhu;
    private int year, month, day, hour, minute;
    private Toolbar toolbar;
    private LinearLayout llDate, llTime;
    private TextView tvDate, tvTime;

    private StringBuffer date, time;
    private Context context;
    private String title,ddl;
    private int editPosition;
    private List<SelectButton> selectButtons = new ArrayList<>();
    private ListView listView;
    private ListView listView1;

    private ButtonAdapter adapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_count);
        toolbar = findViewById(R.id.toolbar);
        editText_biaoti = findViewById(R.id.editText_biaoti);
        editText_beizhu = findViewById(R.id.editText_beizhu);
        listView=findViewById(R.id.listView_button);
        context = this;
        date = new StringBuffer();
        time = new StringBuffer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        initView();
        initDateTime();

        title=getIntent().getStringExtra("title");
        ddl=getIntent().getStringExtra("ddl");
        editPosition=getIntent().getIntExtra("edit_position",0);

        if(title!=null){
            editText_biaoti.setText(title);
        }
        if(ddl!=null){
            tvDate.setText(ddl);
        }

        selectButtons.add(new SelectButton("日期","长按使用日期计算器",R.drawable.ic_date));
        selectButtons.add(new SelectButton("重复设置","",R.drawable.ic_replay));
        selectButtons.add(new SelectButton("添加标签"," ",R.drawable.ic_bookmark));

        adapter = new ButtonAdapter(DownCountActivity.this, R.layout.select_button, selectButtons);
        ((ListView) findViewById(R.id.listView_button)).setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (date.length() > 0) { //清除上次记录的日期
                                    date.delete(0, date.length());
                                }
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                                builder2.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (time.length() > 0) { //清除上次记录的日期
                                            time.delete(0, time.length());
                                        }
                                        time=time.append(String.valueOf(hour)).append("时").append(String.valueOf(minute)).append("分");


                                        dialog.dismiss();
                                    }
                                });
                                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog2 = builder2.create();
                                View dialogView2 = View.inflate(context, R.layout.dialog_time, null);
                                TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
                                timePicker.setCurrentHour(hour);
                                timePicker.setCurrentMinute(minute);
                                timePicker.setIs24HourView(true); //设置24小时制

                                timePicker.setOnTimeChangedListener(DownCountActivity.this);
                                dialog2.setTitle("设置时间");
                                dialog2.setView(dialogView2);
                                dialog2.show();
                                date=date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日");
                                dialog.dismiss();
                            }


                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        final AlertDialog dialog = builder.create();
                        View dialogView = View.inflate(context, R.layout.dialog_date, null);
                        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

                        dialog.setTitle("设置日期");
                        dialog.setView(dialogView);
                        dialog.show();
                        //初始化日期监听事件
                        datePicker.init(year, month - 1, day, DownCountActivity.this);
                        break;
                    case 1:{
                        AlertDialog.Builder dialog1 = new AlertDialog.Builder(DownCountActivity.this);
                        final AlertDialog dialog_zhouqi = dialog1.create();
                        View view1=View.inflate(DownCountActivity.this,R.layout.item_aler, null);
                        listView1 = (ListView)view1.findViewById(R.id.list_alter);
                        final String[] M = {"每年","每月","每周","每天","自定义"};
                        final ArrayAdapter<String> repeatadapter = new ArrayAdapter<String>(DownCountActivity.this,android.R.layout.simple_list_item_1,M);
                        listView1.setAdapter(repeatadapter);
                        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                selectButtons.set(1, new SelectButton("重复设置",M[position],R.drawable.ic_replay));
                                Log.d("what", M[position]);
                                adapter.notifyDataSetChanged();
                                dialog_zhouqi.dismiss();
                            }
                        });
                        dialog_zhouqi.setView(view1);
                        dialog_zhouqi.show();
                        break;
                    }
                    case 2:{
                        AlertDialog.Builder dialog1 = new AlertDialog.Builder(DownCountActivity.this);
                        final AlertDialog dialog_zhouqi = dialog1.create();
                        View view1=View.inflate(DownCountActivity.this,R.layout.item_aler, null);
                        listView1 = (ListView)view1.findViewById(R.id.list_alter);
                        final String[] M = {"学习","考试","生日","纪念日","自定义"};
                        final ArrayAdapter<String>  tagadapter = new ArrayAdapter<String>(DownCountActivity.this,android.R.layout.simple_list_item_1,M);
                        listView1.setAdapter(tagadapter);
                        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                selectButtons.set(2, new SelectButton("添加标签",M[position],R.drawable.ic_bookmark));
                                Log.d("what", M[position]);
                                adapter.notifyDataSetChanged();
                                dialog_zhouqi.dismiss();
                            }
                        });
                        dialog_zhouqi.setView(view1);
                        dialog_zhouqi.show();
                        break;
                    }
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_myaction, menu); // 参数1为布局文件(add_myaction.xml)
        return true;
    }

    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }

    private void initView() {
        llDate = (LinearLayout) findViewById(R.id.ll_date);
        tvDate = (TextView) findViewById(R.id.tv_date);
        llTime = (LinearLayout) findViewById(R.id.ll_time);
        tvTime = (TextView) findViewById(R.id.tv_time);

    }


    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear+1;
        this.day = dayOfMonth;
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
    }
    //设置监听事件
    public boolean onOptionsItemSelected(MenuItem item) {
        String str = "";
        String judge_biaoti = editText_biaoti.getText().toString().trim();
        String judge_beizhu = editText_beizhu.getText().toString().trim();
        String riQi=date.toString().trim()+time.toString().trim();

        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent();
                setResult(233, intent);
                finish();
                break;
            case R.id.item1:
                if (judge_biaoti.length() == 0) {
                    Toast.makeText(DownCountActivity.this, "标题不能为空", Toast.LENGTH_LONG).show();
                } else {
                    intent = new Intent(this,MainActivity.class);
                    intent.putExtra("biaoti", editText_biaoti.getText().toString());
                    intent.putExtra("riqi",riQi);
                    intent.putExtra("editPosition",editPosition);

                    if (judge_beizhu.length() != 0)
                        intent.putExtra("beizhu", editText_beizhu.getText().toString());

                    setResult(1, intent);
                    DownCountActivity.this.finish();
                }
                break;
        }
        return true;
    }
//listview选择功能的adapter
    class ButtonAdapter extends ArrayAdapter<SelectButton> {

        private int resourceId;

        public ButtonAdapter(Context context, int resource, List<SelectButton> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SelectButton selectButton = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.button_image)).setImageResource(selectButton.getImageId());
            ((TextView) view.findViewById(R.id.textView_buttonName)).setText(selectButton.getName());
            ((TextView) view.findViewById(R.id.textView_buttonDetail)).setText(selectButton.getDetail());
            return view;
        }
    }

}




