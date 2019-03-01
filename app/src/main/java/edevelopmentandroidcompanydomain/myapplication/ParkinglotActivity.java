package edevelopmentandroidcompanydomain.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import bean.FreeParkBean;
import util.Address;
import util.HttpUtil;

public class ParkinglotActivity extends AppCompatActivity {

    private Button btnquery;
    private ImageView car2img;
    private int fullparkingId = 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parkinglot);
        initview();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1110) {
                    switch (msg.obj.toString()) {
                        case "40":
                            car2img.setImageDrawable(getResources().getDrawable(R.drawable.item_left));
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        //  fullparkingId = getCarId(car2img);

        btnquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String httpUtil = HttpUtil.postJson(Address.GetParkFree, null);
                            Gson gson = new Gson();
                            FreeParkBean freeParkBean = gson.fromJson(httpUtil, FreeParkBean.class);
                            int FreeparkId = freeParkBean.getParkFreeId();
                            Message message = Message.obtain();
                            message.what = 1110;
                            message.obj = FreeparkId;
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }

    private void initview() {
        btnquery = findViewById(R.id.btnquery);
        car2img = findViewById(R.id.car2img);
    }

//    private int getCarId(View view) {
//        int index = 0;
//        switch (view.getId()) {
//            case R.id.car2img:
//                index = 2;
//                break;
//            default:
//                break;
//        }
//        return index;
//    }
}
