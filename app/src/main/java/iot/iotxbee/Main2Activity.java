package iot.iotxbee;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import iot.iotxbee.utils.DataManager;
import iot.iotxbee.utils.Utils;

import static iot.iotxbee.utils.Utils.validateIP;

public class Main2Activity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private EditText ip,token;
    private DataManager dataManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        coordinatorLayout =findViewById(R.id.id_coord_setting);
        ip=findViewById(R.id.edt_ip);
        token=findViewById(R.id.edt_token);
        dataManager=new DataManager(this);
        if(dataManager.getDetails().get(DataManager.KEY_IP)==null){
            ip.setText(Utils.ip);
        }else{
            ip.setText(dataManager.getDetails().get(DataManager.KEY_IP));
        }
        if(dataManager.getDetails().get(DataManager.KEY_TOKEN)==null){
            token.setText(Utils.token);
        }else{
            token.setText(dataManager.getDetails().get(DataManager.KEY_TOKEN));
        }
    }
    void popSnack(String msg){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
    public void save(View view){
        String _ip=ip.getText().toString();
        String _token=token.getText().toString();
        if(validate(_ip,_token)){
                dataManager.createdata(_ip,_token);
                popSnack("data has been changed!");

        }else{
            popSnack("invalid data!");
        }
    }
    boolean validate(String ips,String tok){
        if(!ips.isEmpty() && !tok.isEmpty() && validateIP(ips)){
            return true;
        }
        return false;
    }
    public void back(View view){
        finish();
    }
}
