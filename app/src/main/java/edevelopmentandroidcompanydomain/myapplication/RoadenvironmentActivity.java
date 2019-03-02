package edevelopmentandroidcompanydomain.myapplication;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import bean.AllSenseBean;
import bean.LightSenseBean;
import util.Address;
import util.HttpUtil;

public class RoadenvironmentActivity extends AppCompatActivity {
    private Button btnquery1, btnquery2;
    private TextView tvPMValue, tvTemperatureValue, tvHumidityValue, tvCO2Value, tvIllumination, tvenvironmentTips1, tvenvironmentTips2, tvIlluminationTips;
    private VideoView vvRoadenvironment;
    private Handler handler;
    private Timer timer = null;
    private TimerTask timerTask = null;
    private boolean mainFlag;
    private boolean goon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadenvironment);
        initview();
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "movie.mp4");
            vvRoadenvironment.setVideoPath(file.getPath()); //指定视频文件的路径
        } catch (Exception e) {
            Log.i("==道路环境界面===", "视频加载失败");
        }
        handler = new Handler();
        btnquery1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        query1();
                    }
                }).start();
            }
        });
        btnquery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        query2();
                    }
                }).start();
            }
        });
    }

    private void initview() {
        tvPMValue = findViewById(R.id.tvPMValue);
        tvTemperatureValue = findViewById(R.id.tvTemperatureValue);
        tvHumidityValue = findViewById(R.id.tvHumidityValue);
        tvCO2Value = findViewById(R.id.tvCO2Value);
        tvenvironmentTips1 = findViewById(R.id.tvenvironmentTips1);
        tvenvironmentTips2 = findViewById(R.id.tvenvironmentTips2);
        tvIlluminationTips = findViewById(R.id.tvIlluminationTips);
        tvIllumination = findViewById(R.id.tvIllumination);
        btnquery1 = findViewById(R.id.btnquery1);
        btnquery2 = findViewById(R.id.btnquery2);
        vvRoadenvironment = findViewById(R.id.vvRoadenvironment);
    }

    public void query1() {
        mainFlag = true;
        goon = true;
        while (goon) {
            try {
                String httpUtil = HttpUtil.postJson(Address.GetAllSense, null);
                Gson gson = new Gson();
                AllSenseBean allSenseBean = gson.fromJson(httpUtil, AllSenseBean.class);
                final int PMValue = allSenseBean.getServerInfo().get_$Pm25254();
                final int TemperatureValue = allSenseBean.getServerInfo().getTemperature();
                final int HumidityValue = allSenseBean.getServerInfo().getHumidity();
                final int CO2Value = allSenseBean.getServerInfo().getCo2();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvPMValue.setText(PMValue + "");
                        tvTemperatureValue.setText(TemperatureValue + "");
                        tvHumidityValue.setText(HumidityValue + "");
                        tvCO2Value.setText(CO2Value + "");
                        try {
                            if (PMValue > 200) {
                                tvenvironmentTips1.setText("PM2.5超标");
                                if (vvRoadenvironment.isPlaying() == true) {//视频是否正在播放
                                    vvRoadenvironment.resume();
                                } else {
                                    vvRoadenvironment.start();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("==道路环境界面===", "视频播放失败");
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                Log.i("==道路环境界面===", "查询环境信息失败");
                mainFlag = false;
            }
            if (mainFlag == true) {
                goon = false;
            }
        }
    }

    public void query2() {
        mainFlag = true;
        goon = true;
        while (goon) {
            try {
                String sresult = HttpUtil.postJson(Address.GetLightSenseValve, null);
                Gson gson = new Gson();
                LightSenseBean lightSenseBean = gson.fromJson(sresult, LightSenseBean.class);
                final int dowmIllumination = lightSenseBean.getDown();
                final int upIllumination = lightSenseBean.getUp();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvIllumination.setText(dowmIllumination + "");
                        if (dowmIllumination < 1000) {
                            tvIlluminationTips.setText("光照强度太低了");
                        }
                        if (upIllumination > 2500) {
                            tvIlluminationTips.setText("亮瞎了我的24K钛合金狗眼");
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("==道路环境界面===", "查询光照信息失败");
                mainFlag = false;
            }
            if (mainFlag == true) {
                goon = false;
            }
        }
    }

    private void startTimer() {
        if (timer == null && timerTask == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    query1();
                    Log.i("测试", "1");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    query2();
                    Log.i("测试", "2");
                }
            };
            timer.schedule(timerTask, 0, 10000);//10秒刷新
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}
