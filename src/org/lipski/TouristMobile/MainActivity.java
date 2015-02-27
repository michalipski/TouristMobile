package org.lipski.TouristMobile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class MainActivity extends Activity {
    Button button;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static String address = "40:2C:F4:F0:9B:DB";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        button = (Button) findViewById(R.id.button);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBTState();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothDevice device = btAdapter.getRemoteDevice(address);

                try {
                    btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e) {
                    AlertBox("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
                }

                btAdapter.cancelDiscovery();

                try {
                    btSocket.connect();
                } catch (IOException e) {
                    try {
                        btSocket.close();
                    } catch (IOException e2) {
                        AlertBox("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                    }
                }

                try {
                    outStream = btSocket.getOutputStream();
                } catch (IOException e) {
                    AlertBox("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
                }

                String message = "list\n";
                byte[] msgBuffer = message.getBytes();
                try {
                    outStream.write(msgBuffer);
                } catch (IOException e) {
                    String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
                    AlertBox("Fatal Error", msg);
                }
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(btSocket.getInputStream());
                    ArrayList<String> test = (ArrayList<String>) objectInputStream.readObject();
                    Intent listIntent = new Intent(MainActivity.this, PlaceListActivity.class);
                    Bundle placeList = new Bundle();
                    placeList.putStringArrayList("list",test);
                    listIntent.putExtras(placeList);
                    startActivity(listIntent);
                } catch (StreamCorruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (outStream != null) {
                    try {
                        outStream.flush();
                    } catch (IOException e) {
                        AlertBox("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
                    }
                }

                try     {
                    btSocket.close();
                } catch (IOException e2) {
                    AlertBox("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (outStream != null) {
            try {
                outStream.flush();
            } catch (Exception e) {
                AlertBox("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }
        try {
            btSocket.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void CheckBTState() {
        if(btAdapter==null) {
            AlertBox("Fatal Error", "Bluetooth Not supported. Aborting.");
        } else {
            if (!btAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    public void AlertBox( String title, String message ){
        new AlertDialog.Builder(this)
                .setTitle( title )
                .setMessage( message + " Press OK to exit." )
                .setPositiveButton("OK", new OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                }).show();
    }
}
