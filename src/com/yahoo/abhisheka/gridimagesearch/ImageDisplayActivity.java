package com.yahoo.abhisheka.gridimagesearch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ShareActionProvider;

import com.loopj.android.image.SmartImageTask.OnCompleteListener;
import com.loopj.android.image.SmartImageView;

public class ImageDisplayActivity extends Activity {
	private ShareActionProvider miShareAction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		ImageResult image = (ImageResult) (getIntent()
				.getSerializableExtra("image"));
		SmartImageView smartImageView = (SmartImageView) findViewById(R.id.ivResult);
		smartImageView.setImageUrl(image.getFullUrl(),
				new OnCompleteListener() {
					@Override
					public void onComplete() {
						// Setup share intent now that image has loaded
						setupShareIntent();
					}
				});
	}

	public void setupShareIntent() {
		// Fetch Bitmap Uri locally
		ImageView ivImage = (ImageView) findViewById(R.id.ivResult);
		Uri bmpUri = getLocalBitmapUri(ivImage); // see previous remote images
													// section
		// Create share intent as described above
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
		shareIntent.setType("image/*");
		// Attach share event to the menu item provider
		miShareAction.setShareIntent(shareIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate menu resource file.
		getMenuInflater().inflate(R.menu.menu_share_image, menu);
		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.miShare);
		// Fetch reference to the share action provider
		miShareAction = (ShareActionProvider) item.getActionProvider();
		// Return true to display menu
		return true;
	}

	// Returns the URI path to the Bitmap displayed in specified ImageView
	public Uri getLocalBitmapUri(ImageView imageView) {
		// Extract Bitmap from ImageView drawable
		Drawable drawable = imageView.getDrawable();
		Bitmap bmp = null;
		if (drawable instanceof BitmapDrawable) {
			bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		} else {
			return null;
		}
		// Store image to default external storage directory
		Uri bmpUri = null;
		try {
			File file = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
					"share_image_" + System.currentTimeMillis() + ".png");
			file.getParentFile().mkdirs();
			FileOutputStream out = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
			bmpUri = Uri.fromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmpUri;
	}

}
