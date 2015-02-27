package org.lipski.TouristMobile;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.ArrayList;

public class PlaceListActivity extends Activity implements PlacesFragment.SelectionListener{

    private PlacesFragment mFriendsFragment;
    private FeedFragment mFeedFragment;

    private ArrayList<String> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);
        places = getIntent().getExtras().getStringArrayList("list");
        // If the layout is single-pane, create the FriendsFragment
        // and add it to the Activity

        if (!isInTwoPaneMode()) {
            mFriendsFragment = new PlacesFragment();
            mFriendsFragment.setPlacesArray(getPlacesNamesArray(places));

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, mFriendsFragment);
            fragmentTransaction.commit();
        } else {
            // Otherwise, save a reference to the FeedFragment for later use
            mFeedFragment = (FeedFragment) getFragmentManager()
                    .findFragmentById(R.layout.feed_frag);
        }
    }

    // If there is no fragment_container ID, then the application is in
    // two-pane mode

    private boolean isInTwoPaneMode() {
        return findViewById(R.id.fragment_container) == null;
    }

    // Display selected Twitter feed

    public void onItemSelected(int position) {

        // If there is no FeedFragment instance, then create one

        if (mFeedFragment == null)
            mFeedFragment = new FeedFragment();

        // If in single-pane mode, replace single visible Fragment

        if (!isInTwoPaneMode()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, mFeedFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            getFragmentManager().executePendingTransactions();
        }
        // Update Twitter feed display on FriendFragment
        mFeedFragment.updateFeedDisplay(places.get(position));
    }

    private String[] getPlacesNamesArray(ArrayList<String> places) {
        String[] placesArray = new String[places.size()];
        for(String place:places) {
            placesArray[places.indexOf(place)] = place;
        }
        return placesArray;
    }

}