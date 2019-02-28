package util;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.sax.StartElementListener;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {

    private AlertDialog alertDialog;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo== null){
            showDialog2(context);
        }else{
            showDialog1(context);
        }
    }
    private void showDialog1(Context context){
        if (alertDialog!=null){
            alertDialog.dismiss();
            alertDialog=null;
        }
        alertDialog=new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("有网络连接")
                .setPositiveButton("确定",null)
                .show();
    }
    private void showDialog2(final Context context){
        if (alertDialog!=null){
            alertDialog.dismiss();
            alertDialog=null;
        }
        alertDialog=new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("无网络连接")
                .setNegativeButton("确定",null)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);
                    }
                })
                .show();
    }
}
