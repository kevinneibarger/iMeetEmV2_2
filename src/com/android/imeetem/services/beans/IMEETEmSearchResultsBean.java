/**
 * 
 */
package com.android.imeetem.services.beans;

import java.io.Serializable;

/**
 * @author kevinscomp
 * 
 */
public class IMEETEmSearchResultsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String searchResultStatus;
	private String searchResultMemId;
	private String searchResultMemFbId;
	private String searchResultMemAcctStatus;
	private String searchResultLastActivity;
	private String searchResultMemImg;
	private String searchResultMemImgLoc;
	private String searchResultMemImgLocFld;
	private String searchResultMemName;
	private String searchResultMemAge;
	private String noMatches;
	private String searchResultMemEyes;
	private String searchResultMemHair;
	private String searchResultMemWantsKids;
	private String searchResultMemHasKids;
	private String searchResultMemEducation;
	private String searchResultMemEthnicity;
	private String searchResultMemDistance;

	private IMEETEmSearchResultsBean() {
	}

	public static IMEETEmSearchResultsBean getInstance() {
		return new IMEETEmSearchResultsBean();
	}

	public String getSearchResultStatus() {
		return searchResultStatus;
	}

	public void setSearchResultStatus(String searchResultStatus) {
		this.searchResultStatus = searchResultStatus;
	}

	public String getSearchResultMemId() {
		return searchResultMemId;
	}

	public void setSearchResultMemId(String searchResultMemId) {
		this.searchResultMemId = searchResultMemId;
	}

	public String getSearchResultMemFbId() {
		return searchResultMemFbId;
	}

	public void setSearchResultMemFbId(String searchResultMemFbId) {
		this.searchResultMemFbId = searchResultMemFbId;
	}

	public String getSearchResultMemAcctStatus() {
		return searchResultMemAcctStatus;
	}

	public void setSearchResultMemAcctStatus(String searchResultMemAcctStatus) {
		this.searchResultMemAcctStatus = searchResultMemAcctStatus;
	}

	public String getSearchResultLastActivity() {
		return searchResultLastActivity;
	}

	public void setSearchResultLastActivity(String searchResultLastActivity) {
		this.searchResultLastActivity = searchResultLastActivity;
	}

	public String getSearchResultMemImg() {
		return searchResultMemImg;
	}

	public void setSearchResultMemImg(String searchResultMemImg) {
		this.searchResultMemImg = searchResultMemImg;
	}

	public String getSearchResultMemImgLoc() {
		return searchResultMemImgLoc;
	}

	public void setSearchResultMemImgLoc(String searchResultMemImgLoc) {
		this.searchResultMemImgLoc = searchResultMemImgLoc;
	}

	public String getSearchResultMemImgLocFld() {
		return searchResultMemImgLocFld;
	}

	public void setSearchResultMemImgLocFld(String searchResultMemImgLocFld) {
		this.searchResultMemImgLocFld = searchResultMemImgLocFld;
	}

	public String getSearchResultMemName() {
		return searchResultMemName;
	}

	public void setSearchResultMemName(String searchResultMemName) {
		this.searchResultMemName = searchResultMemName;
	}

	public String getSearchResultMemAge() {
		return searchResultMemAge;
	}

	public void setSearchResultMemAge(String searchResultMemAge) {
		this.searchResultMemAge = searchResultMemAge;
	}

	public String toString() {

		StringBuilder bld = new StringBuilder();

		bld.append("\n --------- Member Match Info ------------ \n");
		if (searchResultStatus != null) {
			bld.append("Search Result Status: " + searchResultStatus);
		}

		if (searchResultMemId != null) {
			bld.append("Search Result Member Id: " + searchResultMemId);
		}

		if (searchResultMemAcctStatus != null) {
			bld.append("Search Result Member Acct Status: " + searchResultMemAcctStatus);
		}

		if (searchResultLastActivity != null) {
			bld.append("Search Result Last Activity: " + searchResultLastActivity);
		}

		if (searchResultMemImg != null) {
			bld.append("Search Result Member Img: " + searchResultMemImg);
		}

		if (searchResultMemImgLoc != null) {
			bld.append("Search Result Member Img Location: " + searchResultMemImgLoc);
		}

		if (searchResultMemImgLocFld != null) {
			bld.append("Search Result Member Img Location Folder: " + searchResultMemImgLocFld);
		}

		if (searchResultMemName != null) {
			bld.append("Search Result Member Name: " + searchResultMemName);
		}

		if (searchResultMemAge != null) {
			bld.append("Search Result Member Age: " + searchResultMemAge);
		}

		bld.append("\n ---------------------------------------- \n");

		return bld.toString();
	}

	public String getNoMatches() {
		return noMatches;
	}

	public void setNoMatches(String noMatches) {
		this.noMatches = noMatches;
	}

	public String getSearchResultMemEyes() {
		return searchResultMemEyes;
	}

	public void setSearchResultMemEyes(String searchResultMemEyes) {
		this.searchResultMemEyes = searchResultMemEyes;
	}

	public String getSearchResultMemHair() {
		return searchResultMemHair;
	}

	public void setSearchResultMemHair(String searchResultMemHair) {
		this.searchResultMemHair = searchResultMemHair;
	}

	public String getSearchResultMemWantsKids() {
		return searchResultMemWantsKids;
	}

	public void setSearchResultMemWantsKids(String searchResultMemWantsKids) {
		this.searchResultMemWantsKids = searchResultMemWantsKids;
	}

	public String getSearchResultMemHasKids() {
		return searchResultMemHasKids;
	}

	public void setSearchResultMemHasKids(String searchResultMemHasKids) {
		this.searchResultMemHasKids = searchResultMemHasKids;
	}

	public String getSearchResultMemEducation() {
		return searchResultMemEducation;
	}

	public void setSearchResultMemEducation(String searchResultMemEducation) {
		this.searchResultMemEducation = searchResultMemEducation;
	}

	public String getSearchResultMemEthnicity() {
		return searchResultMemEthnicity;
	}

	public void setSearchResultMemEthnicity(String searchResultMemEthnicity) {
		this.searchResultMemEthnicity = searchResultMemEthnicity;
	}

	public String getSearchResultMemDistance() {
		return searchResultMemDistance;
	}

	public void setSearchResultMemDistance(String searchResultMemDistance) {
		this.searchResultMemDistance = searchResultMemDistance;
	}
}
