package iot.iotxbee;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

import iot.iotxbee.thermometer.Thermometer;
import iot.iotxbee.utils.DataManager;

import static iot.iotxbee.utils.Utils.ip;
import static iot.iotxbee.utils.Utils.server;
import static iot.iotxbee.utils.Utils.token;

public class MainActivity extends AppCompatActivity {
    private Handler handler;
    private Thermometer thermometer;
    private FloatingActionButton detector;
    int delayhandler=1*1000;
    private Toolbar mToolbar;
    private DataManager dataManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        thermometer =findViewById(R.id.thermo);
        detector=findViewById(R.id.detection);

        AndroidNetworking.initialize(this);
        AndroidNetworking.enableLogging();
        dataManager=new DataManager(this);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.id_setting) {
            Intent intent=new Intent(this,Main2Activity.class);
            startActivity(intent);
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.id_sync) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(dataManager.getDetails().get(DataManager.KEY_IP)==null){
            dataManager.createdata(ip,token);
        }else{
            ip=dataManager.getDetails().get(DataManager.KEY_IP);
            token=dataManager.getDetails().get(DataManager.KEY_TOKEN);
            server="http://"+ip+":8080"+"/api/v1/"+token+"/attributes?clientKeys=temperature,mouvement";
            Log.d("ip",server);
        }
        handler=new Handler();
        handler.postDelayed(runnable,delayhandler);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
        AndroidNetworking.cancelAll();
    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            AndroidNetworking.get(server)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            // do anything with response
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    try {
                                        int temp = response.getInt("temperature");
                                        boolean detect = response.getBoolean("mouvement");
                                        if(detect){
                                            detector.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_light));
                                        }else{
                                            detector.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_red_dark));
                                        }
                                        Log.d("tempera",""+temp);
                                        thermometer.setCurrentTemp(temp);
                                        handler.postDelayed(runnable,delayhandler);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }



                                }
                            });
                        }
                        @Override
                        public void onError(ANError error) {
                            // handle error
                        }
                    });
        }
    };
}
