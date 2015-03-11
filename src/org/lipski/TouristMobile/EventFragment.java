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
import org.lipski.TouristMobile.dataAccess.EventBluetoothController;
import org.lipski.TouristMobile.dataAccess.PlaceBluetoothController;
import org.lipski.TouristMobile.session.UserSession;
import org.lipski.event.model.Event;
import org.lipski.place.model.Place;

import java.nio.charset.IllegalCharsetNameException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class EventFragment extends Fragment {

    private static final String TAG = "Event-View";

    private TextView mNameTextView;
    private TextView mDateTextView;
    private TextView mPlaceNameTextView;
    private TextView mAddressTextView;
    private TextView mCityTextView;
    private TextView mDescriptionTextView;
    private Button mGoToPlaceButton;
    private Button mGoToEventOnMapButton;

    private EventBluetoothController eventBluetoothController = new EventBluetoothController();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    void updateFeedDisplay(final String eventName) {
        Log.i(TAG, "Entered updateFeedDisplay()");
        mDescriptionTextView = (TextView) getView().findViewById(R.id.eventDescription);
        mNameTextView = (TextView) getView().findViewById(R.id.eventName);
        mDateTextView = (TextView) getView().findViewById(R.id.eventDate);
        mPlaceNameTextView = (TextView) getView().findViewById(R.id.placeNameEvent);
        mAddressTextView = (TextView) getView().findViewById(R.id.placeAddressEvent);
        mCityTextView = (TextView) getView().findViewById(R.id.placeCityEvent);
        mGoToEventOnMapButton = (Button) getView().findViewById(R.id.showOnMapButton);
        mGoToPlaceButton = (Button) getView().findViewById(R.id.goToPlaceButton);
        mGoToEventOnMapButton.setVisibility(View.VISIBLE);
        mGoToPlaceButton.setVisibility(View.VISIBLE);

        final Map<String,Object> eventMap = eventBluetoothController.getEventByName(eventName);
        Event event = (Event) eventMap.get("event");
        final Place place = (Place) eventMap.get("place");

        mGoToEventOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "geo:0,0?q=" + place.getCity() + ", " + place.getAddress();
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().getApplicationContext().startActivity(mapIntent);
            }
        });

        mGoToPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceFragment placeFragment = new PlaceFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, placeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                getFragmentManager().executePendingTransactions();
                placeFragment.updateFeedDisplay(place.getName());
            }
        });


        mNameTextView.setText(event.getName());
        mDateTextView.setText(event.getData().toString());
        mDescriptionTextView.setText(event.getDescription());
        mPlaceNameTextView.setText(place.getName());
        mAddressTextView.setText(place.getAddress());
        mCityTextView.setText(place.getCity());
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