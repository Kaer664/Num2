package edevelopmentandroidcompanydomain.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AccountActivity extends AppCompatActivity {

    private int rechargeCarId=0;
    private int queryCarId=0;
    private Spinner mSpinnerQuery;
    private Button mBtnQuery;
    private Spinner mSpinnerRecharge;
    private EditText mEtRecharge;
    private Button mBtnRecharge;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        init();
    }

    private void init() {
        mSpinnerQuery = (Spinner) findViewById(R.id.spinner_query);
        mSpinnerQuery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                queryCarId=i;
                Log.i("test","queryCarId"+queryCarId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mBtnQuery = (Button) findViewById(R.id.btn_query);
        mSpinnerRecharge = (Spinner) findViewById(R.id.spinner_recharge);
        mSpinnerRecharge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rechargeCarId=i;
                Log.i("test","rechargeCarId:"+rechargeCarId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mEtRecharge = (EditText) findViewById(R.id.et_recharge);
        mBtnRecharge = (Button) findViewById(R.id.btn_recharge);
    }
}
