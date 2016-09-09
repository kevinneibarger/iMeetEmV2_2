/**
 * 
 */
package com.android.imeetem.services.beans;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

/**
 * @author kneibarger
 * 
 */
public class IMEETEmFriendsListBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String groupType;
	private String winkEmAvail;
	private String dateRequested;
	private String memId;
	private String memImageUrl;
	private String memImageLoc;
	private String memImageLocFolder;
	private String memFname;
	private String memAge;
	private String memMsgDetail;
	private String memMsgStatus;
	private Drawable memImgDrawable;
	private boolean sectionHeader;
	private String memDistance;

	// new variables as on 5/20/2014 - via Hartley
	private String memberHairColor;
	private String memberEyeColor;
	private boolean memberHasKids;
	private boolean memberWantsKids;
	private String memberEducation;
	private String memberEthnicity;

	public static IMEETEmFriendsListBean getInstance() {
		return new IMEETEmFriendsListBean();
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getWinkEmAvail() {
		return winkEmAvail;
	}

	public void setWinkEmAvail(String winkEmAvail) {
		this.winkEmAvail = winkEmAvail;
	}

	public String getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(String dateRequested) {
		this.dateRequested = dateRequested;
	}

	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}

	public String getMemImageUrl() {
		return memImageUrl;
	}

	public void setMemImageUrl(String memImageUrl) {
		this.memImageUrl = memImageUrl;
	}

	public String getMemImageLoc() {
		return memImageLoc;
	}

	public void setMemImageLoc(String memImageLoc) {
		this.memImageLoc = memImageLoc;
	}

	public String getMemImageLocFolder() {
		return memImageLocFolder;
	}

	public void setMemImageLocFolder(String memImageLocFolder) {
		this.memImageLocFolder = memImageLocFolder;
	}

	public String getMemFname() {
		return memFname;
	}

	public void setMemFname(String memFname) {
		this.memFname = memFname;
	}

	public String getMemAge() {
		return memAge;
	}

	public void setMemAge(String memAge) {
		this.memAge = memAge;
	}

	public Drawable getMemImgDrawable() {
		return memImgDrawable;
	}

	public void setMemImgDrawable(Drawable memImgDrawable) {
		this.memImgDrawable = memImgDrawable;
	}

	public boolean isSectionHeader() {
		return sectionHeader;
	}

	public void setSectionHeader(boolean sectionHeader) {
		this.sectionHeader = sectionHeader;
	}

	public String getMemMsgDetail() {
		return memMsgDetail;
	}

	public void setMemMsgDetail(String memMsgDetail) {
		this.memMsgDetail = memMsgDetail;
	}

	public String getMemMsgStatus() {
		return memMsgStatus;
	}

	public void setMemMsgStatus(String memMsgStatus) {
		this.memMsgStatus = memMsgStatus;
	}

	public String toString() {

		StringBuilder bld = new StringBuilder();

		if (groupType != null) {
			bld.append("Group Type: " + groupType + "\n");
		}

		if (winkEmAvail != null) {
			bld.append("Wink Avail: " + winkEmAvail + "\n");
		}

		if (dateRequested != null) {
			bld.append("Date Requested: " + dateRequested + "\n");
		}

		if (memId != null) {
			bld.append("Member Id: " + memId + "\n");
		}

		if (memImageUrl != null) {
			bld.append("Member Image URL: " + memImageUrl + "\n");
		}

		if (memImageLoc != null) {
			bld.append("Member Image Loc: " + memImageLoc + "\n");
		}

		if (memImageLocFolder != null) {
			bld.append("Member Image Location Folder: " + memImageLocFolder
					+ "\n");
		}

		if (memFname != null) {
			bld.append("Member First Name: " + memFname + "\n");
		}

		if (memAge != null) {
			bld.append("Member Age: " + memAge + "\n");
		}
		if (memMsgDetail != null) {
			bld.append("Member 1 Time Message: " + memMsgDetail + "\n");
		}

		if (sectionHeader) {
			bld.append("We HAVE a section header!\n");
		} else {
			bld.append("We DO NOT HAVE a section header, no requests!\n");
		}

		return bld.toString();
	}

	public String getMemDistance() {
		return memDistance;
	}

	public void setMemDistance(String memDistance) {
		this.memDistance = memDistance;
	}

	public String getMemberHairColor() {
		return memberHairColor;
	}

	public void setMemberHairColor(String memberHairColor) {
		this.memberHairColor = memberHairColor;
	}

	public String getMemberEyeColor() {
		return memberEyeColor;
	}

	public void setMemberEyeColor(String memberEyeColor) {
		this.memberEyeColor = memberEyeColor;
	}

	public boolean isMemberHasKids() {
		return memberHasKids;
	}

	public void setMemberHasKids(boolean memberHasKids) {
		this.memberHasKids = memberHasKids;
	}

	public boolean isMemberWantsKids() {
		return memberWantsKids;
	}

	public void setMemberWantsKids(boolean memberWantsKids) {
		this.memberWantsKids = memberWantsKids;
	}

	public String getMemberEducation() {
		return memberEducation;
	}

	public void setMemberEducation(String memberEducation) {
		this.memberEducation = memberEducation;
	}

	public String getMemberEthnicity() {
		return memberEthnicity;
	}

	public void setMemberEthnicity(String memberEthnicity) {
		this.memberEthnicity = memberEthnicity;
	}

}
