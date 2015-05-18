package com.example.jheron.gridimagesearch.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.jheron.gridimagesearch.R;
import com.example.jheron.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;


public class ImageDisplayActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        // remove action bar from the ImageDisplayActivity
        getSupportActionBar().hide();
        // pull out the url from the intent
        ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
        // find the image view
        ImageView  ivImageResult = (ImageView) findViewById(R.id.ivImageResult);
        // load the image itno tthe image view via picasso
        Picasso.with(this).load(result.fullurl).into(ivImageResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
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
}
