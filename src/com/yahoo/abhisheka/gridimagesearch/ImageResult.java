package com.yahoo.abhisheka.gridimagesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8817930986706886390L;

	public ImageResult(JSONObject jsonObject) {
		super();
		try {
			this.fullUrl = jsonObject.getString("url");
			this.thumbnailUrl = jsonObject.getString("tbUrl");
		} catch (JSONException e) {
			this.fullUrl = null;
			this.thumbnailUrl = null;
			e.printStackTrace();
		}

	}

	private String fullUrl;
	private String thumbnailUrl;

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	@Override
	public String toString() {
		return "ImageResult [fullUrl=" + fullUrl + ", thumbnailUrl="
				+ thumbnailUrl + "]";
	}

	public static List<ImageResult> fromJsonArray(JSONArray jsonImageResult) {

		List<ImageResult> imageResults = new ArrayList<ImageResult>();
		for (int i = 0; i < jsonImageResult.length(); i++) {
			try {
				imageResults.add(new ImageResult(jsonImageResult
						.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return imageResults;
	}

}
