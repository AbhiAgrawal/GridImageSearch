package com.yahoo.abhisheka.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class AdvanceSearchActivity extends Activity {

	Spinner spImageSize;
	Spinner spColorFilter;
	Spinner spImageType;
	EditText etSiteFilter;
	Button btSave;
	AdvanceSettings advanceSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advance_search);
		advanceSettings = (AdvanceSettings) getIntent().getSerializableExtra(
				"settings");
		setUpViews();
	}

	private void setUpViews() {
		spImageSize = (Spinner) findViewById(R.id.spImageSize);
		spColorFilter = (Spinner) findViewById(R.id.spColorFilter);
		spImageType = (Spinner) findViewById(R.id.spImageType);
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		btSave = (Button) findViewById(R.id.btSave);

		addDataToSpinner(spImageSize, R.array.image_size_array);
		addDataToSpinner(spColorFilter, R.array.color_filter_array);
		addDataToSpinner(spImageType, R.array.image_type_array);
		spImageSize.setSelection(getPosition(spImageSize, advanceSettings.getImageSize()));
		spColorFilter.setSelection(getPosition(spColorFilter, advanceSettings.getColorFilter()));
		spImageType.setSelection(getPosition(spImageType, advanceSettings.getImageType()));
		if(!SearchUtil.isNullOrEmpty(advanceSettings.getSiteFilter())){
			etSiteFilter.setText(advanceSettings.getSiteFilter());
		}
	}

	private int getPosition(Spinner spinner, String searchFilter) {
		SpinnerAdapter adapter = spinner.getAdapter();
		if(SearchUtil.isNullOrEmpty(searchFilter))
			return 0;
		for(int i = 0; i<adapter.getCount() ; i++){
			if(searchFilter.equalsIgnoreCase((String)adapter.getItem(i))){
				return i;
			}
		}
		return 0;
	}

	private void addDataToSpinner(Spinner spinner, int textArrayResid) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, textArrayResid, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}

	public void onSave(View v) {
		advanceSettings.setImageSize((String) spImageSize.getSelectedItem());
		advanceSettings.setImageType((String) spImageType.getSelectedItem());
		advanceSettings.setColorFilter((String) spColorFilter.getSelectedItem());
		advanceSettings.setSiteFilter(etSiteFilter.getText().toString());

		Intent i = new Intent();
		i.putExtra("settings", advanceSettings);
		// submit my result to parent activity
		setResult(RESULT_OK, i);
		finish();

	}
}
