package com.example.kandl.beacontest;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.Region;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private BeaconRegion region;
    private TextView tvId;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvId = (TextView)findViewById(R.id.tvId);

        beaconManager = new BeaconManager(this);

        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener(){
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<com.estimote.coresdk.recognition.packets.Beacon> beacons) {
                if(!beacons.isEmpty()){
                    Beacon nearestBeacon = beacons.get(0);
                    Log.d("Airport", "Nearest places" + nearestBeacon.getRssi());

                    tvId.setText(nearestBeacon.getRssi() + "");
                    if(!isConnected && nearestBeacon.getRssi() > -70){
                        isConnected = true;
                        Log.d("TAG", "잘되는데.............");

                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog .setTitle("알림")
                                .setMessage("비콘 연결")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                    }
                    else{
                        Log.d("Tag", "왜종료돼..?");
                        Toast.makeText(MainActivity.this, "연결 종료", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        region = new BeaconRegion("ranged region", UUID.fromString("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"), null,null);
    }
    @Override
    protected void onResume(){
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
            @Override
            public void onServiceReady(){
                beaconManager.startRanging(region);
            }
        });
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
}
