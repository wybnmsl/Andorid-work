package com.example.itime;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD = 901;
    public static final int REQUEST_CODE_EDIT = 902;
    private AppBarConfiguration mAppBarConfiguration;
    private FloatingActionButton fab;
    private ListView listViewTime;
    private TextView textViewDays,textViewJudge;
    private List<Time> listTime=new ArrayList<>();
    private TimeAdapter timeAdapter;
    private  Toolbar toolbar;
    private SaveTime time_saver;
    private String title,ddl,days;
    private int editPosition;
    private long day_left;
    private long timeMillis_now,timeMillis_set;
    private static final int COLOR= 200;
    private static final int COLORBK = 201;
    private int color;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);

      final DrawerLayout drawer = findViewById(R.id.drawer_layout);
      NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_jishi:
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_zhutise:
                        Intent intent = new Intent(MainActivity.this, ColorActivity.class);
                        startActivityForResult(intent, COLOR);
                        break;
                }
                return false;
            }
        });




        init();

        listViewTime=(ListView) findViewById(R.id.list_view_main);
        textViewJudge=findViewById(R.id.textView_judge);

        timeAdapter=new TimeAdapter(MainActivity.this,R.layout.list_view_main_time,listTime);
        listViewTime.setAdapter(timeAdapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, DownCountActivity.class);
                startActivityForResult(intent,REQUEST_CODE_ADD);
            }
        });
        listViewTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                editPosition=position;
                Time item=(Time) timeAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",item.getTitle());
                bundle.putString("ddl",item.getDdl());
                bundle.putInt("photoId",item.getCoverResourceId());
                bundle.putInt("position",editPosition);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);



            }


        });


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_CODE_ADD: {
                if (resultCode == 1) {
                    String biaoti = data.getStringExtra("biaoti");
                    String riqi = data.getStringExtra("riqi");
                    if (resultCode == 1) {

                        //随机生成封面图片
                        Random r = new Random();

                        int num = (int) (Math.random() * 8 + 1);
                        switch (num) {
                            case 1:
                                listTime.add(new Time(biaoti, riqi, R.drawable.imagine2));
                                break;
                            case 2:
                                listTime.add(new Time(biaoti, riqi, R.drawable.imagine3));
                                break;
                            case 3:
                                listTime.add(new Time(biaoti, riqi, R.drawable.imagine4));
                                break;
                            case 4:
                                listTime.add(new Time(biaoti, riqi, R.drawable.imagine5));
                                break;
                            case 5:
                                listTime.add(new Time(biaoti, riqi, R.drawable.imagine6));
                                break;
                            case 6:
                                listTime.add(new Time(biaoti, riqi, R.drawable.imagine7));
                                break;
                            case 7:
                                listTime.add(new Time(biaoti, riqi, R.drawable.imagine8));
                                break;
                            case 8:
                                listTime.add(new Time(biaoti, riqi, R.drawable.imagine9));
                                break;
                        }

                        String str = "保存成功";
                        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

                        timeAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
            case 0:
                if(resultCode==3){
                    String title=data.getStringExtra("title");
                    String ddl=data.getStringExtra("ddl");
                    //day=data.getLongExtra("day",0);
                    Time time =listTime.get(editPosition);
                    time.setTitle(title);
                    time.setDdl(ddl);
                    timeAdapter.notifyDataSetChanged();
                    String str = "修改成功";
                    Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                    break;
                }
                if (resultCode == 10){
                    //处理删除事件
                        int edit = data.getIntExtra("edit",100000);
                        if (edit == 100000){
                            Toast.makeText(MainActivity.this, "cnm",Toast.LENGTH_SHORT).show();
                        }else {
                            listTime.remove(edit);
                            timeAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        }
                    break;
                }
            case COLOR:
                if (resultCode == COLORBK){
                    color = data.getIntExtra("color",
                            ContextCompat.getColor(this, R.color.colorPrimary));
//                    Toast.makeText(MainActivity.this, "kkp",Toast.LENGTH_SHORT).show();
                    toolbar.setBackgroundColor(color);
                    this.getWindow().setColorMode(color);
                }
                break;
            case 233:
                Toast.makeText(this, "建议一键退学", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        time_saver.save();
    }

    private void init(){
        time_saver=new SaveTime(this);
        listTime=time_saver.load();
    }
    //获取系统当前时间

    class TimeAdapter extends ArrayAdapter<Time> {

        private int resourceId;

        public TimeAdapter(Context context, int resource, List<Time> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @NonNull
        @Override
        //listview的数据加载
        public View getView(int position, View convertView, ViewGroup parent) {
            Time time = getItem(position);//获取当前项的实例
            //days=String.valueOf(day);
            //days=days+"    天";

            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ImageView imageView = view.findViewById(R.id.image_view_cover);
            BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(time.getCoverResourceId());
            final StackBlurManager stackBlurManager = new StackBlurManager(bitmapDrawable.getBitmap());
            imageView.setImageBitmap(stackBlurManager.process(70));
            ((TextView) view.findViewById(R.id.text_view_name)).setText(time.getTitle());
            ((TextView) view.findViewById(R.id.text_view_ddl)).setText(time.getDdl());
            try{
                Calendar cal = Calendar.getInstance();
                timeMillis_now=cal.getTimeInMillis();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    cal.setTime(Objects.requireNonNull(new SimpleDateFormat("yyyy年MM月dd日HH时mm分").parse(time.getDdl())));
                }
                timeMillis_set=cal.getTimeInMillis();
                day_left=(timeMillis_set-timeMillis_now)/(1000*24*60*60);
            }catch(ParseException e){
                e.printStackTrace();
            }

            ((TextView) view.findViewById(R.id.textView_howManyDays)).setText(String.valueOf(day_left)+"   天");

            return view;
        }
    }


}
