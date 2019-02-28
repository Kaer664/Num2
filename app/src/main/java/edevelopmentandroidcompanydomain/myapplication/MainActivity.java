package edevelopmentandroidcompanydomain.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Address;
import util.HttpUtil;

//OnItemClickListener
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView leftListView;
    //底部右侧的TextView
    private TextView tvNum1Bus1,tvNum1Bus2,tvNum2Bus1,tvNum2Bus2;
    //底部左侧的TextView 主要表示环境的数值
    private TextView tvPMValue,tvTemperatureValue,tvHumidityValue,tvCO2Value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLeft();
        init();
        startThread("");
    }

    private static final int HANDLER_SUCCESS_INT=0x001;
    private static final int HANDLER_ERROR_INT=0x002;
    /**
     * 此方法用于调用线程在给得到网络数据
     */
    private void startThread(String s) {
        new Thread(){
            @Override
            public void run() {
                try {
                    String sense=HttpUtil.postJson(Address.GetAllSense,"","");
                    Message msg=handler.obtainMessage(HANDLER_SUCCESS_INT);
                    handler.sendMessage(msg);
                } catch (IOException e) {

                }
            }
        }.start();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_SUCCESS_INT:
                    Log.i("TestNum","连接成功  下面可以进行下一步计划");
                    break;
                case HANDLER_ERROR_INT:
                    break;
            };
        }
    };

    private void init() {
        tvNum1Bus1=findViewById(R.id.tvNum1Bus1);
        tvNum1Bus2=findViewById(R.id.tvNum1Bus2);
        tvNum2Bus1=findViewById(R.id.tvNum2Bus1);
        tvNum2Bus2=findViewById(R.id.tvNum2Bus2);

        tvPMValue=findViewById(R.id.tvPMValue);
        tvTemperatureValue=findViewById(R.id.tvTemperatureValue);
        tvHumidityValue=findViewById(R.id.tvHumidityValue);
        tvCO2Value=findViewById(R.id.tvCO2Value);
    }

    List<Map<String,Object>> left_data=new ArrayList<>();
    private void initLeft() {
        leftListView=findViewById(R.id.leftListView);
            Map<String,Object> map=new HashMap<>();
            map.put("img1",R.drawable.item_left);
            map.put("tvText","我的座驾");
            left_data.add(map);

        Map<String,Object> map2=new HashMap<>();
        map2.put("img1",R.drawable.item_left);
        map2.put("tvText","我的路况");
        left_data.add(map2);

        Map<String,Object> map3=new HashMap<>();
        map3.put("img1",R.drawable.item_left);
        map3.put("tvText","停车查询");
        left_data.add(map3);

        Map<String,Object> map4=new HashMap<>();
        map4.put("img1",R.drawable.item_left);
        map4.put("tvText","公交查询");
        left_data.add(map4);

        SimpleAdapter sa=new SimpleAdapter(this,left_data,R.layout.left_list_item,
                new String[]{"img1","tvText"},new int[]{R.id.left_img1,R.id.left_tv1});
        leftListView.setAdapter(sa);
        leftListView.setOnItemClickListener(this);
    }

    /**
     * 这个方法主要时用于ListView的item的点击事件
     * @param adapterView
     * @param view
     * @param i  点击的条目数   emmmmm现在方法大概只用到这个参数
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=null;
        switch (i){
            case 0:
                intent=new Intent(this,AccountActivity.class);
                startActivity(intent);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                intent=new Intent(this,BusQueryActivity.class);
                startActivity(intent);
                break;
        }
    }
}
