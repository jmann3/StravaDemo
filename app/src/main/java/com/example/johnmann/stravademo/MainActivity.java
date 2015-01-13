package com.example.johnmann.stravademo;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.johnmann.stravademo.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static final String Library_Congress_Collections = "http://loc.gov/pictures/collections/?fo=json";

    private ProgressDialog progressDialog;

    int mCount;
    List<CollectionInfo> mImageUrls = new ArrayList<>();
    ViewPager mViewPager;
    PhotoFragmentAdapter mPhotoFragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the pager
        mViewPager = (ViewPager)findViewById(R.id.pager);

        // show progress dialog to indicate images are being loaded
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading images from Library of Congress.  Please wait...");
        progressDialog.show();

        // retrieve list of photo urls and settup adapter
        JsonObjectRequest request = new JsonObjectRequest(Library_Congress_Collections, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray collections = response.getJSONArray("collections");

                    for (int i = 0; i < collections.length(); i++) {
                        String banner_image = ((JSONObject)collections.get(i)).getString("banner");
                        String banner_title = ((JSONObject)collections.get(i)).getString("title");

                        mImageUrls.add(new CollectionInfo(banner_image, banner_title));
                    }

                    mCount = mImageUrls.size();

                    // settup the adapter
                    mPhotoFragmentAdapter = new PhotoFragmentAdapter(getSupportFragmentManager());
                    mViewPager.setAdapter(mPhotoFragmentAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
            }
        });

        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     *  Settup adapter to feed images to viewpager
     */
    public class PhotoFragmentAdapter extends FragmentStatePagerAdapter {


        public PhotoFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            CollectionInfo collectionInfo = mImageUrls.get(position);
            return PhotoFragment.newInstance(collectionInfo.getUrl(), collectionInfo.getTitle());
        }

        @Override
        public int getCount() {
            return mCount;
        }
    }

    public class CollectionInfo {
        String url;
        String title;

        public CollectionInfo(String url, String title) {
            this.url = url;
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
