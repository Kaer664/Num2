package edevelopmentandroidcompanydomain.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.TrafficLightConfigActionBean;
import util.Address;
import util.HttpUtil;

public class RoadTrafficLightActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private Spinner spinnerSort;
    private Button btnQuery;
    private ListView listViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_traffic_light);
        init();
    }

    /**
     * 初始化控件值然后
     * 绑定监听事件
     */
    private void init() {
        spinnerSort = findViewById(R.id.spinnerSort);
        btnQuery = findViewById(R.id.btnQuery);
        listViewInfo = findViewById(R.id.listViewInfo);
        btnQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnQuery:
                startThread();
                break;
        }
    }

    private static final int MSG_SUCCESS = 0x001;
    private static final int MSG_FAIL = 0x002;

    /**
     * 这个方法主要用于启动线程 访问网络数据，发送handler消息
     */
    private void startThread() {
        new Thread() {
            @Override
            public void run() {
                List<String> tL = new ArrayList<>();
                try {
                    String t1 = HttpUtil.postJson(Address.GetTrafficLightConfigAction, "TrafficLightId", "1");
                    String t2 = HttpUtil.postJson(Address.GetTrafficLightConfigAction, "TrafficLightId", "2");
                    String t3 = HttpUtil.postJson(Address.GetTrafficLightConfigAction, "TrafficLightId", "3");
                    tL.add(t1);
                    tL.add(t2);
                    tL.add(t3);
                    Message msg= handler.obtainMessage(MSG_SUCCESS);
                    msg.obj=tL;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    Log.i("TestNum","有异常出现请检查网络权限  检查网络状态 检查网址参数");
                }
            }
        }.start();
    }

    private List<Map<String,String>> list_data;
    private SimpleAdapter adapter;
    private Gson gson=new Gson();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SUCCESS:
                    List<String> data= (List<String>) msg.obj;
                    list_data=parserTraffic(data);
                    adapter=new SimpleAdapter(RoadTrafficLightActivity.this,list_data,R.layout.road_traffic_light_item,
                            new String[]{"RoadID","red","green","yellow"},new int[]{R.id.tv_item_road,R.id.tv_item_red_time,R.id.tv_item_green_time,R.id.tv_item_yellow_time});
                    listViewInfo.setAdapter(adapter);
                    sort("red",1);
                    adapter.notifyDataSetChanged();
                    spinnerSort.setOnItemSelectedListener(RoadTrafficLightActivity.this);
                    break;
                case MSG_FAIL:
                    break;
            }
        }
    };

    /**
     * 这个方法主要用于排序
     * @param name  需要排序的名称
     * @param rule   正序或者倒序
     */
    private void sort(final String name, final int rule){
        Collections.sort(list_data, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> m1, Map<String, String> m2) {
                Integer t1=Integer.parseInt(m1.get(name).trim());
                Integer t2=Integer.parseInt(m2.get(name).trim());
                return rule*t1.compareTo(t2);
            }
        });
    }

    /**
     *
     * @param data  需要解析的json数据
     * @return  返回List<Map<String,String>> 数据  SimpleAdapter设置界面
     */
    private List<Map<String,String>> parserTraffic(List<String> data){
        List<Map<String,String>> list_data=new ArrayList<>();
        for(int i=0;i<data.size();i++){
            Map<String,String> map=new HashMap<>();
            TrafficLightConfigActionBean bean=gson.fromJson(data.get(i), TrafficLightConfigActionBean.class);
            map.put("green",bean.getServerInfo().getGreenTime());
            map.put("red",bean.getServerInfo().getRedTime());
            map.put("yellow",bean.getServerInfo().getYellowTime());
            map.put("RoadID",String.valueOf(i+1));
            list_data.add(map);
        }
        return list_data;
    }

    /**
     * 下面的两个方法主要用于Spinner的排序
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                Log.i("TestNum",view.toString());
                sort("red",1);
                adapter.notifyDataSetChanged();
                break;
            case 1:
                Log.i("TestNum",view.toString());
                sort("red",-1);
                adapter.notifyDataSetChanged();
                break;
            case 2:
                Log.i("TestNum",view.toString());
                sort("green",1);
                adapter.notifyDataSetChanged();
                break;
            case 3:
                Log.i("TestNum",view.toString());
                sort("green",-1);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
