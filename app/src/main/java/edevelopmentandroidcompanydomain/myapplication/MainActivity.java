package edevelopmentandroidcompanydomain.myapplication;

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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.AllSenseBean;
import bean.BusStationIdBean;
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

    //这个变量用于startThread方法中控制线程，线程运行标识符
    private Boolean sign=false;

    /**
     * start方法用于开启线程
     */
    @Override
    protected void onStart() {
        super.onStart();
        sign=true;
    }

    /**
     * onStop用于应用退出关闭线程
     */
    @Override
    protected void onStop() {
        super.onStop();
        sign=false;
    }
    /**
     * 此方法用于调用线程在给得到网络数据
     */
    private void startThread(String s) {
        new Thread(){
            @Override
            public void run() {
              while (sign){
                  Log.i("TestNum",String.valueOf(sign));
                  try {
                      Map<String,String> info=new HashMap<>();
                      //得到环境信息
                      String sense=HttpUtil.postJson(Address.GetAllSense,"","");
                      //发送公家车站台信息
                      String station1=HttpUtil.postJson(Address.GetBusStationInfo,"BusStationId","1");
                      String station2=HttpUtil.postJson(Address.GetBusStationInfo,"BusStationId","2");
                      Message msg=handler.obtainMessage(HANDLER_SUCCESS_INT);
                      info.put("sense",sense);
                      info.put("station1",station1);
                      info.put("station2",station2);
                      msg.obj=info;
                      handler.sendMessage(msg);
                  } catch (IOException e) {
                      Log.i("TestNum","返回错误 请检查权限或者网络状态");
                  }
              }
                Log.i("TestNum",String.valueOf(sign));
            }
        }.start();
    }
    private Gson gson=new Gson();


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_SUCCESS_INT:
                    Map <String,String> info= (Map<String, String>) msg.obj;
                    String sense=info.get("sense");
                    String station1=info.get("station1");
                    String station2=info.get("station2");
                    Map<String,String> map=parserSense(sense);  //方法解析完数据 返回map数据
                    tvPMValue.setText(map.get("pmValue"));
                    tvCO2Value.setText(map.get("co2Value"));
                    tvHumidityValue.setText(map.get("humidityValue"));
                    tvTemperatureValue.setText(map.get("temperatureValue"));
                    List<String> distance1=parserStation(station1);
                    List<String> distance2=parserStation(station2);
                    tvNum1Bus1.setText(distance1.get(0));
                    tvNum1Bus2.setText(distance1.get(1));
                    tvNum2Bus1.setText(distance2.get(0));
                    tvNum2Bus2.setText(distance2.get(1));
                    break;
                case HANDLER_ERROR_INT:
                    break;
            };
        }
    };

    /**
     *
     * @param station1  解析的数据
     * @return   返回为list  数据
     */
    private List<String> parserStation(String station1){
        List<BusStationIdBean> busStationIdBeans = gson.fromJson(station1,
                new TypeToken<List<BusStationIdBean>>() {
                }.getType());
        List<String> data=new ArrayList<>();
        for(int i=0;i<busStationIdBeans.size();i++){
            int busID=busStationIdBeans.get(i).getBusId();
            int distance=busStationIdBeans.get(i).getDistance();
            data.add(String.valueOf(distance));
        }
        return data;
    }
    /**
     * 方法主要用与解析出sense的值，然后通过Map返回出来
     * @param sense  传入的json值
     * @return  返回Map值
     */
    private Map<String, String> parserSense(String sense){
        AllSenseBean allSense=gson.fromJson(sense, AllSenseBean.class);
        AllSenseBean.ServerInfoBean infoBean=allSense.getServerInfo();
        String pmValue=String.valueOf(infoBean.get_$Pm25254());
        String co2Value=String.valueOf(infoBean.getCo2());
        String humidityValue=String.valueOf(infoBean.getHumidity());
        String lightIntensityValue=String.valueOf(infoBean.getLightIntensity());
        String temperatureValue=String.valueOf(infoBean.getTemperature());
        Map<String,String> map=new HashMap<>();
        map.put("pmValue",pmValue);
        map.put("co2Value",co2Value);
        map.put("humidityValue",humidityValue);
        map.put("lightIntensityValue",lightIntensityValue);
        map.put("temperatureValue",temperatureValue);
        return map;
    }

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
        switch (i){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}
