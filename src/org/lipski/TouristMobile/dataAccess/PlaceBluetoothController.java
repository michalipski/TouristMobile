package org.lipski.TouristMobile.dataAccess;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import org.lipski.TouristMobile.session.UserSession;
import org.lipski.place.model.Place;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.UUID;

public class PlaceBluetoothController {
    private static BluetoothAdapter btAdapter = null;
    private static BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your server's MAC address
    private static String address = "40:2C:F4:F0:9B:DB";

    public Place getPlaceByName(String name) {
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
        Place place = new Place();
        String message = "getplace;" + name + "\n";
        byte[] msgBuffer = message.getBytes();
        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(btSocket.getInputStream());
            place = (Place) objectInputStream.readObject();
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
        return place;
    }

    public boolean gradePlace(String placeName, float rating) {
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

        boolean response = false;
        Integer grade = Math.round(rating);
        String message = "grade;"+ UserSession.getUsername() + ";" + placeName + ";" + grade.toString() + "\n";
        byte[] msgBuffer = message.getBytes();
        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(btSocket.getInputStream());
            response = (Boolean) objectInputStream.readObject();
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
        return response;
    }

    public boolean commentPlace(String placeName, String comment) {
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

        boolean response = false;
        String message = "comment;"+ UserSession.getUsername() + ";" + placeName + ";" + comment + "\n";
        byte[] msgBuffer = message.getBytes();
        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(btSocket.getInputStream());
            response = (Boolean) objectInputStream.readObject();
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
        return response;
    }
}
