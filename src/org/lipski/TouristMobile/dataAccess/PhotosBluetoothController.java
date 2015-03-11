package org.lipski.TouristMobile.dataAccess;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import org.lipski.event.model.Event;
import org.lipski.place.model.Place;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PhotosBluetoothController {
    private static BluetoothAdapter btAdapter = null;
    private static BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static String address = "40:2C:F4:F0:9B:DB";

    public ArrayList<byte[]> getPhotosByPlaceName(String name) {
        if(btAdapter==null) {
            btAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        btAdapter.cancelDiscovery();

        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<byte[]> photosList = new ArrayList<byte[]>();
        String message = "getphotos;" + name + "\n";
        byte[] msgBuffer = message.getBytes();
        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(btSocket.getInputStream());
            photosList = (ArrayList<byte[]>) objectInputStream.readObject();
            objectInputStream.close();
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
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try     {
            btSocket.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return photosList;
    }
}
