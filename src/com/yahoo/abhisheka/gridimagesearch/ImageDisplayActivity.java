package com.yahoo.abhisheka.gridimagesearch;

import android.app.Activity;
import android.os.Bundle;

import com.loopj.android.image.SmartImageView;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		ImageResult image = (ImageResult) (getIntent().getSerializableExtra("image"));
		SmartImageView smartImageView = (SmartImageView) findViewById(R.id.ivResult);
		smartImageView.setImageUrl(image.getFullUrl());
	}

}
