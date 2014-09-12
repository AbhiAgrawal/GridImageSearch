package com.yahoo.abhisheka.gridimagesearch;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

	String strQuery;
	GridView gvResults;
	SearchView svActionSearch;
	List<ImageResult> imageResults = new ArrayList<ImageResult>();
	private static final String GOOGLE_API_URL = "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&start=%s&v=1.0&q=%s&userip=INSERT-USER-IP";
	ImageResultArrayAdapter imageResultArrayAdapter;
	AdvanceSettings advanceSettings = new AdvanceSettings();
	int start = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setUpViews();
		imageResultArrayAdapter = new ImageResultArrayAdapter(this,
				imageResults);
		gvResults.setAdapter(imageResultArrayAdapter);
		setUpListeners();

	}

	private void setUpListeners() {
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent,
					int position, long rowId) {
				Intent i = new Intent(getApplicationContext(),
						ImageDisplayActivity.class);
				ImageResult image = imageResults.get(position);
				i.putExtra("image", image);
				startActivity(i);
			}

		});

		gvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				customLoadMoreDataFromApi(page);
				// or customLoadMoreDataFromApi(totalItemsCount);
			}
		});

	}

	public void customLoadMoreDataFromApi(int offset) {
		multipleAsyncCallsToGoogleApi(strQuery);
	}

	public void onAdvanceSearch(MenuItem mi) {
		Intent iAdvanceSearch = new Intent(this, AdvanceSearchActivity.class);
		iAdvanceSearch.putExtra("settings", advanceSettings);
		startActivityForResult(iAdvanceSearch,
				IntentConstants.ADVANCE_SEARCH_INTENT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IntentConstants.ADVANCE_SEARCH_INTENT) {
			if (resultCode == RESULT_OK) {
				this.advanceSettings = (AdvanceSettings) data
						.getSerializableExtra("settings");
				Toast.makeText(this, advanceSettings.getImageSize(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void setUpViews() {
		gvResults = (GridView) findViewById(R.id.gvResults);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_simple, menu);

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		svActionSearch = (SearchView) menu.findItem(R.id.svActionSearch)
				.getActionView();
		// Assumes current activity is the searchable activity
		svActionSearch.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		svActionSearch.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				strQuery = query;
				performSearch(strQuery);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String query) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		return true;
	}

	private void performSearch(String query) {
		start = 0;
		imageResults.clear();
		multipleAsyncCallsToGoogleApi(strQuery);
	}

	private void multipleAsyncCallsToGoogleApi(String strQuery) {
		asynCallToGoogleApi(strQuery);
		asynCallToGoogleApi(strQuery);
		asynCallToGoogleApi(strQuery);
		asynCallToGoogleApi(strQuery);
		asynCallToGoogleApi(strQuery);
	}

	private void asynCallToGoogleApi(String strQuery) {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		// https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=barack&obama&userip=INSERT-USER-IP
		String apiUrlCall = String.format(GOOGLE_API_URL, start,
				Uri.encode(strQuery));
		start += 8;
		apiUrlCall = addQueryParams(apiUrlCall);
		Log.d("DEBUG", apiUrlCall.toString());
		asyncHttpClient.get(apiUrlCall, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				JSONArray jsonImageResults = null;
				try {
					jsonImageResults = response.getJSONObject("responseData")
							.getJSONArray("results");

					imageResults.addAll(ImageResult
							.fromJsonArray(jsonImageResults));
					imageResultArrayAdapter.notifyDataSetChanged();
					Log.d("DEBUG", imageResults.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
	}

	private String addQueryParams(String apiUrlCall) {
		if (!SearchUtil.isNullOrEmpty(advanceSettings.getColorFilter())) {
			apiUrlCall = apiUrlCall + "&imgcolor="
					+ advanceSettings.getColorFilter();
		}
		if (!SearchUtil.isNullOrEmpty(advanceSettings.getImageSize())) {
			apiUrlCall = apiUrlCall + "&imgsz="
					+ advanceSettings.getImageSize();
		}
		if (!SearchUtil.isNullOrEmpty(advanceSettings.getImageType())) {
			apiUrlCall = apiUrlCall + "&imgtype="
					+ advanceSettings.getImageType();
		}
		if (!SearchUtil.isNullOrEmpty(advanceSettings.getSiteFilter())) {
			apiUrlCall = apiUrlCall + "&as_sitesearch="
					+ advanceSettings.getSiteFilter();
		}

		return apiUrlCall;
	}

}
