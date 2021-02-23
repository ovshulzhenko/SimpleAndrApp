package com.example.myclientapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "";
    private IMyAidlInterface mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText edtX = (EditText)findViewById(R.id.editX);
        final EditText edtY = (EditText)findViewById(R.id.editY);
        final EditText edtR = (EditText)findViewById(R.id.edtResult);
        final Button btnCalc = (Button)findViewById(R.id.btnCalc);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sx = edtX.getText().toString();
                String sy = edtY.getText().toString();

                try
                {
                    int x = Integer.parseInt(sx);
                    int y = Integer.parseInt(sy);

                    int r = mService.add(x, y);

                    edtR.setText("" + r);
                } catch (NumberFormatException | RemoteException | NullPointerException e)
                {
                    edtR.setText("INV");
                    Log.e(TAG, "calling add failed");
                }



            }
        });

        Intent intent = new Intent();
        intent.setClassName("com.example.myapplication", "com.example.myapplication.MyService");
        try
        {
            bindService(intent, mConnection, BIND_AUTO_CREATE);
        } catch (SecurityException e)
        {
            Log.e(TAG, "bind to service failed by security");
        }

    }
}