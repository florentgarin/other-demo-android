package com.training.clientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;

import com.training.firstapp.services.SampleRPCInterface;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private Button callRPC;
    private SampleRPCInterface srvProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callRPC = findViewById(R.id.callRPC);
        callRPC.setOnClickListener(v-> {
            if(srvProxy != null) {
                try {
                    srvProxy.basicTypes(3, 4, true, 3.14f, 23.55, "hello rpc");
                    
                    var profile = srvProxy.getProfile();
                    Log.i("MainActivity", profile.getFirstName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent service = new Intent();
        service.setClassName("com.training.firstapp",
                "com.training.firstapp.services.SampleRPCService");

        var bound = bindService(service, this, BIND_AUTO_CREATE);
        Log.i("MainActivity", bound + "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        srvProxy = SampleRPCInterface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        srvProxy = null;
    }
}