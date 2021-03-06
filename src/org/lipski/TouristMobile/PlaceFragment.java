package org.lipski.TouristMobile;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.lipski.TouristMobile.dataAccess.PhotosBluetoothController;
import org.lipski.TouristMobile.dataAccess.PlaceBluetoothController;
import org.lipski.TouristMobile.session.UserSession;
import org.lipski.place.model.Place;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PlaceFragment extends Fragment {

    private static final String TAG = "Place-View";

    private TextView mTitleTextView;
    private TextView mAddressTextView;
    private TextView mOpenHourTextView;
    private TextView mCloseHourTextView;
    private TextView mGradeTextView;
    private TextView mDescriptionTextView;
    private RatingBar mRatingBar;
    private TextView mAddCommentTitle;
    private EditText mCommentEditText;
    private Button mAddCommentButton;
    private Button mShowOnMap;
    private Button mShowPhotos;

    private PlaceBluetoothController placeBluetoothController = new PlaceBluetoothController();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void updateFeedDisplay(final String placeName) {
        Log.i(TAG, "Entered updateFeedDisplay()");
        mTitleTextView = (TextView) getView().findViewById(R.id.title_view);
        mAddressTextView = (TextView) getView().findViewById(R.id.address_view);
        mOpenHourTextView = (TextView) getView().findViewById(R.id.openhour_view);
        mCloseHourTextView = (TextView) getView().findViewById(R.id.closehour_view);
        mGradeTextView = (TextView) getView().findViewById(R.id.grade_view);
        mDescriptionTextView = (TextView) getView().findViewById(R.id.description_view);
        mRatingBar = (RatingBar) getView().findViewById(R.id.ratingBar);
        mAddCommentTitle = (TextView) getView().findViewById(R.id.addCommentTitle);
        mCommentEditText = (EditText) getView().findViewById(R.id.commentEditText);
        mAddCommentButton = (Button) getView().findViewById(R.id.commentButton);
        mShowOnMap = (Button) getView().findViewById(R.id.showOnMapPlace);
        mShowPhotos = (Button) getView().findViewById(R.id.showPhotosButton);
        mShowOnMap.setVisibility(View.VISIBLE);
        mShowPhotos.setVisibility(View.VISIBLE);

        final Place place = placeBluetoothController.getPlaceByName(placeName);

        mTitleTextView.setText(place.getName());
        mAddressTextView.setText(place.getAddress());
        mOpenHourTextView.setText(place.getOpenHour().toString());
        mCloseHourTextView.setText(place.getCloseHour().toString());
        mDescriptionTextView.setText(place.getDescription());
        mRatingBar.setRating(3.0F);
        mRatingBar.setStepSize(1.0F);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if(fromUser) {
                        if(placeBluetoothController.gradePlace(place.getName(),rating)) {
                            Toast.makeText(getActivity().getApplicationContext(),"Place graded!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),"Grading error!", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        });

        mAddCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(placeBluetoothController.commentPlace(place.getName(),mCommentEditText.getText().toString())) {
                    Toast.makeText(getActivity().getApplicationContext(),"Place commented!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),"Comment error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "geo:0,0?q=" + place.getCity() + ", " + place.getAddress();
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().getApplicationContext().startActivity(mapIntent);
            }
        });

        mShowPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoFragment photoFragment = new PhotoFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, photoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                getFragmentManager().executePendingTransactions();
                photoFragment.updateFeedDisplay(place.getName());
            }
        });

        if(UserSession.getIsLoggedIn()) {
            mRatingBar.setVisibility(View.VISIBLE);
            mAddCommentButton.setVisibility(View.VISIBLE);
            mCommentEditText.setVisibility(View.VISIBLE);
            mAddCommentTitle.setVisibility(View.VISIBLE);
        }
    }

//    private String getFormattedDate(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        Integer hours = calendar.get(Calendar.HOUR_OF_DAY);
//        Integer minutes = calendar.get(Calendar.MINUTE);
//        String minutesText;
//        if(minutes==0) {
//            minutesText = "00";
//        } else {
//            minutesText = minutes.toString();
//        }
//        return hours.toString()+":"+minutesText;
//    }
}