package com.example.jheron.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.example.jheron.gridimagesearch.R;
import com.example.jheron.gridimagesearch.models.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.EndlessScrollListener;
import adapters.ImageResultsAdapter;


public class SearchActivity extends ActionBarActivity {
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private String query;
    private String lastQuery = "";
    private AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        // creates data source to list
        imageResults = new ArrayList<ImageResult>();
        // attaches data source to adapter
        aImageResults = new ImageResultsAdapter(this, 1, imageResults);
        // link adapter to adapter view
        gvResults.setAdapter(aImageResults);
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // launch imageDisplayActivity
                // create an intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                // get image result to display
                ImageResult result = imageResults.get(position);
                // pass image result into intent
                i.putExtra("result",result);
                // launch the new activity
                startActivity(i);
            }
        });
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int position, int totalItemsCount) {
                customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        String searchurl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
        client.get(searchurl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    //imageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    // When you make changes to the adapter, it does notify the underlying data
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("INFO", imageResults.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    //JSON Developer https://developers.google.com/image-search/v1/jsondevguide#json_reference
    //
    //        https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
    //
    //        width, height, tvUrl, url
    //
    //        responseData => results => [x] => width
    //        responseData => results => [x] => height
    //        responseData => results => [x] => tbUrl
    //        responseData => results => [x] => title
    //        responseData => results => [x] => url

    public void onImageSearch(View v) {
        query = etQuery.getText().toString();
        client = new AsyncHttpClient();
        // https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
        String searchurl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
        client.get(searchurl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    if (query != lastQuery) {
                        imageResults.clear(); // clear the existing images from the array (in cases where it's a new search)
                        lastQuery = query;
                    }
                    //imageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    // When you make changes to the adapter, it does notify the underlying data
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("INFO", imageResults.toString());

            }
        });

        // Toast.makeText(this, "Search for: " + query, Toast.LENGTH_SHORT).show();
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
}
