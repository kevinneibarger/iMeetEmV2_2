package com.android.imeetem.services.beans;

import java.io.Serializable;

public class IMeetEmSearchCriteriaInfoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String gender;
	private int distance;
	private int young_age;
	private int old_age;
	private String wantkid;
	private String haskid;
	private String haircolor;
	private String eyecolor;
	private String ethnic;
	private String education;
	private String memberInfo;
	private String memberPhoto;
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getYoung_age() {
		return young_age;
	}
	public void setYoung_age(int young_age) {
		this.young_age = young_age;
	}
	public int getOld_age() {
		return old_age;
	}
	public void setOld_age(int old_age) {
		this.old_age = old_age;
	}
	public String getMemberInfo() {
		return memberInfo;
	}
	public void setMemberInfo(String memberInfo) {
		this.memberInfo = memberInfo;
	}
	public String getMemberPhoto() {
		return memberPhoto;
	}
	public void setMemberPhoto(String memberPhoto) {
		this.memberPhoto = memberPhoto;
	}
	public String getWantkid() {
		return wantkid;
	}
	public void setWantkid(String wantkid) {
		this.wantkid = wantkid;
	}
	public String getHaskid() {
		return haskid;
	}
	public void setHaskid(String haskid) {
		this.haskid = haskid;
	}
	public String getHaircolor() {
		return haircolor;
	}
	public void setHaircolor(String haircolor) {
		this.haircolor = haircolor;
	}
	public String getEyecolor() {
		return eyecolor;
	}
	public void setEyecolor(String eyecolor) {
		this.eyecolor = eyecolor;
	}
	public String getEthnic() {
		return ethnic;
	}
	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	
	
	
	

}
