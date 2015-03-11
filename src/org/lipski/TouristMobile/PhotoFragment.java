package org.lipski.TouristMobile;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import org.lipski.TouristMobile.dataAccess.PhotosBluetoothController;

import java.util.ArrayList;

public class PhotoFragment extends Fragment{

    private static final String TAG = "Photo-View";
    private PhotosBluetoothController photosBluetoothController = new PhotosBluetoothController();
    private ArrayList<byte[]> photos;

    ImageView mImageView;
    Button mPreviousPhotoButton;
    Button mNextPhotoButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void updateFeedDisplay(final String placeName) {
        Log.i(TAG, "Entered updateFeedDisplay()");
        mImageView = (ImageView) getView().findViewById(R.id.photoView);
        mPreviousPhotoButton = (Button) getView().findViewById(R.id.previousPhotoButton);
        mNextPhotoButton = (Button) getView().findViewById(R.id.nextPhotoButton);

        photos = photosBluetoothController.getPhotosByPlaceName(placeName);
        Bitmap bitmap = getBitmapFromByteArray(photos.get(0));
        mImageView.setImageBitmap(bitmap);
    }

    private Bitmap getBitmapFromByteArray(byte[] photo) {
        return BitmapFactory.decodeByteArray(photo,0,photo.length);
    }

    public ArrayList<byte[]> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<byte[]> photos) {
        this.photos = photos;
    }
}
