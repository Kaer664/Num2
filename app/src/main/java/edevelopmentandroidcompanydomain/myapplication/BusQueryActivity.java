package edevelopmentandroidcompanydomain.myapplication;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bean.AllSenseBean;
import bean.BusStationBean;
import util.Address;
import util.HttpUtil;

public class BusQueryActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvStationid;
    private TextView mTvBus1;
    private TextView mTvBus2;
    private TextView mTvEnvironment;
    private Button mBtnStation1;
    private Button mBtnStation2;

    private Timer timer;
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x0001:
                    mTvStationid.setText("距"+msg.arg1+"号站台距离：");
                    Bundle bundle = msg.getData();
                    int bus1 = bundle.getInt("bus1");
                    int bus2 = bundle.getInt("bus2");
                    int temperature = bundle.getInt("temperature");
                    int pm = bundle.getInt("pm");
                    int humidity = bundle.getInt("humidity");
                    int co2 = bundle.getInt("co2");
                    mTvBus1.setText(bus1+"m");
                    mTvBus2.setText(bus2+"m");
                    mTvEnvironment.setText("pm2.5 : "+pm+" ug/m³ , 温度 : "+temperature+" °C , 湿度 : "+
                            humidity+" °C , CO2 : "+co2+" ug/m³");
                    Log.i("test","网络请求成功");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_query);
        init();
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        postsync(1);
    }

    private void init() {
        mTvStationid = (TextView) findViewById(R.id.tv_stationid);
        mTvBus1 = (TextView) findViewById(R.id.tv_bus1);
        mTvBus2 = (TextView) findViewById(R.id.tv_bus2);
        mTvEnvironment = (TextView) findViewById(R.id.tv_environment);
        mBtnStation1 = (Button) findViewById(R.id.btn_station1);
        mBtnStation1.setOnClickListener(this);
        mBtnStation2 = (Button) findViewById(R.id.btn_station2);
        mBtnStation2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (timer!=null){
            timer.cancel();
            timer=null;
        }
        switch (view.getId()){
            case R.id.btn_station1:
                postsync(1);
                break;
            case R.id.btn_station2:
                postsync(2);
                break;
        }
        mBtnStation1.setEnabled(!mBtnStation1.isEnabled());
        mBtnStation2.setEnabled(!mBtnStation2.isEnabled());
    }

    /**
     * 从网络获取数据，并且传给handler更改界面
     * @param id 站台id
     */
    public void postsync(final int id){
        timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                try {
                    Log.i("test","定时器："+id);
                    Gson gson = HttpUtil.getGson();

                    String json = HttpUtil.postJson(Address.GetBusStationInfo, "BusStationId", id + "");
                    List<BusStationBean> list=gson.fromJson(json,new TypeToken<List<BusStationBean>>(){}.getType());
                    int bus1 = list.get(0).getDistance();
                    int bus2 = list.get(1).getDistance();

                    String json1 = HttpUtil.postJson(Address.GetAllSense, null);
                    AllSenseBean senseBean = gson.fromJson(json1, AllSenseBean.class);
                    AllSenseBean.ServerInfoBean serverInfo = senseBean.getServerInfo();
                    int pm = serverInfo.get_$Pm25254();
                    int temperature = serverInfo.getTemperature();
                    int humidity = serverInfo.getHumidity();
                    int co2 = serverInfo.getCo2();

                    Message message = handler.obtainMessage(0x0001);
                    Bundle bundle = new Bundle();
                    bundle.putInt("bus1",bus1);
                    bundle.putInt("bus2",bus2);
                    bundle.putInt("pm",pm);
                    bundle.putInt("temperature",temperature);
                    bundle.putInt("humidity",humidity);
                    bundle.putInt("co2",co2);

                    message.arg1=id;
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask,0,1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
            timer=null;
        }
    }
}
