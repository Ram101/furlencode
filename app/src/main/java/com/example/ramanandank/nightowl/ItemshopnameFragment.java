package com.example.ramanandank.nightowl;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ramanandank.nightowl.dummy.DummyContent;
import com.example.ramanandank.nightowl.dummy.DummyContent.DummyItem;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class ItemshopnameFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private double mLat,mLon;
    private ArrayList<ShopListData> shopListDatas= new ArrayList<ShopListData>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemshopnameFragment() {
    }


    public static ItemshopnameFragment newInstance(int columnCount,double lat , double lon) {
        ItemshopnameFragment fragment = new ItemshopnameFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mLat = getArguments().getDouble("latitude");
            mLon = getArguments().getDouble("longitude");
        }
        shopListDatas = MainActivity.shopListDatas;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itemshopname_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyItemshopnameRecyclerViewAdapter(shopListDatas));
            final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent motionEvent) {

                    View child = rv.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                    if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                        Toast.makeText(getActivity(), "The Item Clicked is: " + rv.getChildPosition(child), Toast.LENGTH_SHORT).show();

                        return true;

                    }

                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView v, MotionEvent event) {
                    float x = event.getX() + v.getLeft();
                    float y = event.getY() + v.getTop();
                    LinearLayout child = (LinearLayout) v.findViewById(R.id.linear);
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        // Simulate motion on the card view.
                        child.drawableHotspotChanged(x, y);
                    }

                    // Simulate pressed state on the card view.
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            child.setPressed(true);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            child.setPressed(false);
                            break;
                    }

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });

        }
        return view;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
////        if (context instanceof OnListFragmentInteractionListener) {
////            mListener = (OnListFragmentInteractionListener) context;
////        } else {
////            throw new RuntimeException(context.toString()
////                    + " must implement OnListFragmentInteractionListener");
////        }
//    }

        @Override
        public void onDetach () {
            super.onDetach();
        }

        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p/>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */



}
