package com.yahoo.abhisheka.gridimagesearch;

import java.io.Serializable;

public class AdvanceSettings implements Serializable{
	
	private static final long serialVersionUID = 4409040982051697431L;
	private String imageSize;
	private String colorFilter;
	private String siteFilter;
	private String imageType;
	public String getImageSize() {
		return imageSize;
	}
	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}
	public String getColorFilter() {
		return colorFilter;
	}
	public void setColorFilter(String colorFilter) {
		this.colorFilter = colorFilter;
	}
	public String getSiteFilter() {
		return siteFilter;
	}
	public void setSiteFilter(String siteFilter) {
		this.siteFilter = siteFilter;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

}
