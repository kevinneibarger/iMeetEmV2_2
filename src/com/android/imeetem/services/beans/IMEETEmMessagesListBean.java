package com.android.imeetem.services.beans;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class IMEETEmMessagesListBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String memId;
	private String memName;
	private String memImage;
	private String memMessage;
	private String memMessLastActivity;
	private String memMessageStatus;
	private Drawable memImgDrawable;
	private String age;
	private String memMessageHeader;
	private String memDistance;
	private String memImgURL;

	// new variables as on 5/20/2014 - via Hartley
	private String memberHairColor;
	private String memberEyeColor;
	private boolean memberHasKids;
	private boolean memberWantsKids;
	private String memberEducation;
	private String memberEthnicity;

	public static IMEETEmMessagesListBean getInstance() {
		return new IMEETEmMessagesListBean();
	}

	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public String getMemImage() {
		return memImage;
	}

	public void setMemImage(String memImage) {
		this.memImage = memImage;
	}

	public String getMemMessage() {
		return memMessage;
	}

	public void setMemMessage(String memMessage) {
		this.memMessage = memMessage;
	}

	public String getMemMessLastActivity() {
		return memMessLastActivity;
	}

	public void setMemMessLastActivity(String memMessLastActivity) {
		this.memMessLastActivity = memMessLastActivity;
	}

	public String getMemMessageStatus() {
		return memMessageStatus;
	}

	public void setMemMessageStatus(String memMessageStatus) {
		this.memMessageStatus = memMessageStatus;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Drawable getMemImgDrawable() {
		return memImgDrawable;
	}

	public void setMemImgDrawable(Drawable memImgDrawable) {
		this.memImgDrawable = memImgDrawable;
	}

	public String getMemMessageHeader() {
		return memMessageHeader;
	}

	public void setMemMessageHeader(String memMessageHeader) {
		this.memMessageHeader = memMessageHeader;
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

	public String getMemImgURL() {
		return memImgURL;
	}

	public void setMemImgURL(String memImgURL) {
		this.memImgURL = memImgURL;
	}

}
