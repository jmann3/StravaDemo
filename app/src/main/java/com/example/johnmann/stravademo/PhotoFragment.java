package com.example.johnmann.stravademo;

//import android.app.Fragment;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.johnmann.stravademo.volley.VolleySingleton;

/**
 * Created by johnmann on 1/12/15.
 */
public class PhotoFragment extends Fragment {

    public static final String PHOTO_KEY = "photo_key";

    public static final String URL_KEY = "url_key";
    public static final String TITLE_KEY = "title_key";


    public static PhotoFragment newInstance(String url, String title) {
        PhotoFragment photoFragment = new PhotoFragment();

        Bundle args = new Bundle();
        args.putString(URL_KEY, url);
        args.putString(TITLE_KEY, title);
        photoFragment.setArguments(args);

        return photoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // get url & title for photo
        String imageURL = getArguments().getString(URL_KEY, null);
        String imageTitle = getArguments().getString(TITLE_KEY, "");

        View rootView = inflater.inflate(R.layout.photo_fragment, container, false);

        // load url and title into widgets
        NetworkImageView imageView = (NetworkImageView) rootView.findViewById(R.id.photo);
        ImageLoader imageLoader = VolleySingleton.getInstance(getActivity()).getImageLoader();

        imageView.setImageUrl(imageURL, imageLoader);

        TextView titleView = (TextView)rootView.findViewById(R.id.title);
        titleView.setText(imageTitle);

        return rootView;
    }
}
