package org.lipski.TouristMobile;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.ArrayList;

public class PlaceListActivity extends Activity implements ItemsFragment.SelectionListener{

    private ItemsFragment mFriendsFragment;
    private PlaceFragment mPlaceFragment;
    private EventFragment mEventFragment;

    private ArrayList<String> objects;
    private String objectType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objects = getIntent().getExtras().getStringArrayList("list");
        objectType = getIntent().getExtras().getString("type");
        if(objectType.equals("place")) {
            setContentView(R.layout.fragment);
        } else {
            setContentView(R.layout.event_fragment);
        }
        // If the layout is single-pane, create the FriendsFragment
        // and add it to the Activity

        if (!isInTwoPaneMode()) {
            mFriendsFragment = new ItemsFragment();
            mFriendsFragment.setItemsArray(getObjectsNamesArray(objects));

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, mFriendsFragment);
            fragmentTransaction.commit();
        } else {
            // Otherwise, save a reference to the FeedFragment for later use
            if(objectType.equals("place")) {
                mPlaceFragment = (PlaceFragment) getFragmentManager()
                        .findFragmentById(R.layout.feed_frag);
            } else {
                mEventFragment = (EventFragment) getFragmentManager()
                        .findFragmentById(R.layout.event_fragment);
            }

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

        if (mPlaceFragment == null)
            mPlaceFragment = new PlaceFragment();

        if(mEventFragment == null)
            mEventFragment = new EventFragment();

        // If in single-pane mode, replace single visible Fragment

        if (!isInTwoPaneMode()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if(objectType.equals("place")) {
                fragmentTransaction.replace(R.id.fragment_container, mPlaceFragment);
            } else {
                fragmentTransaction.replace(R.id.fragment_container, mEventFragment);
            }
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            getFragmentManager().executePendingTransactions();
        }
        // Update Twitter feed display on FriendFragment
        if(objectType.equals("place")) {
            mPlaceFragment.updateFeedDisplay(objects.get(position));
        } else {
            mEventFragment.updateFeedDisplay(objects.get(position));
        }
    }

    private String[] getObjectsNamesArray(ArrayList<String> objects) {
        String[] objectsArray = new String[objects.size()];
        for(String object:objects) {
            objectsArray[objects.indexOf(object)] = object;
        }
        return objectsArray;
    }

}