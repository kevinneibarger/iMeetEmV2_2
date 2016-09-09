/**
 * 
 */
package com.android.imeetem.services.beans;

import java.io.Serializable;

/**
 * @author kevinscomp
 *
 */
public class IMEETEmMemberInfoBean implements Serializable {

	private IMEETEmMemberInfoBean() {}
	
	public static IMEETEmMemberInfoBean getInstance() {
		return new IMEETEmMemberInfoBean();
	}
	
	private static final long serialVersionUID = 1L;

	private int memId;
	private String memFBId;
	private String memAcctStatus;
	private java.sql.Date createDate;
	private java.sql.Timestamp lastActivity;
	private String emailId;
	private String memImage; // Members picture, not sure what to store here...
	private String memImageLoc; // Physical location of the members image
	private String memImageFolderLoc; // Folder where the members image resides
	private java.sql.Date memImageDate;
	private String memImageStatus;
	private int birthYear;
	private int birthMonth;
	private int birthDay;
	private String memFName;
	private String memLName;
	private String memGender;
	private String memInfo; // Tag line information
	private java.sql.Date memInfoDate;
	private String memInfoStatus;
	private String memLatitude;
	private String memLongitude;
	private int memTotalCredit;
	private String instDistance;
	private String instGender;
	private int instYngAge;
	private int instOldAge;
	
	public int getMemId() {
		return memId;
	}
	public void setMemId(int memId) {
		this.memId = memId;
	}
	public String getMemFBId() {
		return memFBId;
	}
	public void setMemFBId(String memFBId) {
		this.memFBId = memFBId;
	}
	public String getMemAcctStatus() {
		return memAcctStatus;
	}
	public void setMemAcctStatus(String memAcctStatus) {
		this.memAcctStatus = memAcctStatus;
	}
	public java.sql.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.sql.Date createDate) {
		this.createDate = createDate;
	}
	public java.sql.Timestamp getLastActivity() {
		return lastActivity;
	}
	public void setLastActivity(java.sql.Timestamp lastActivity) {
		this.lastActivity = lastActivity;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMemImage() {
		return memImage;
	}
	public void setMemImage(String memImage) {
		this.memImage = memImage;
	}
	public String getMemImageLoc() {
		return memImageLoc;
	}
	public void setMemImageLoc(String memImageLoc) {
		this.memImageLoc = memImageLoc;
	}
	public String getMemImageFolderLoc() {
		return memImageFolderLoc;
	}
	public void setMemImageFolderLoc(String memImageFolderLoc) {
		this.memImageFolderLoc = memImageFolderLoc;
	}
	public java.sql.Date getMemImageDate() {
		return memImageDate;
	}
	public void setMemImageDate(java.sql.Date memImageDate) {
		this.memImageDate = memImageDate;
	}
	public String getMemImageStatus() {
		return memImageStatus;
	}
	public void setMemImageStatus(String memImageStatus) {
		this.memImageStatus = memImageStatus;
	}
	public int getBirthYear() {
		return birthYear;
	}
	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}
	public int getBirthMonth() {
		return birthMonth;
	}
	public void setBirthMonth(int birthMonth) {
		this.birthMonth = birthMonth;
	}
	public int getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(int birthDay) {
		this.birthDay = birthDay;
	}
	public String getMemFName() {
		return memFName;
	}
	public void setMemFName(String memFName) {
		this.memFName = memFName;
	}
	public String getMemLName() {
		return memLName;
	}
	public void setMemLName(String memLName) {
		this.memLName = memLName;
	}
	public String getMemGender() {
		return memGender;
	}
	public void setMemGender(String memGender) {
		this.memGender = memGender;
	}
	public String getMemInfo() {
		return memInfo;
	}
	public void setMemInfo(String memInfo) {
		this.memInfo = memInfo;
	}
	public java.sql.Date getMemInfoDate() {
		return memInfoDate;
	}
	public void setMemInfoDate(java.sql.Date memInfoDate) {
		this.memInfoDate = memInfoDate;
	}
	public String getMemInfoStatus() {
		return memInfoStatus;
	}
	public void setMemInfoStatus(String memInfoStatus) {
		this.memInfoStatus = memInfoStatus;
	}
	public String getMemLatitude() {
		return memLatitude;
	}
	public void setMemLatitude(String memLatitude) {
		this.memLatitude = memLatitude;
	}
	public String getMemLongitude() {
		return memLongitude;
	}
	public void setMemLongitude(String memLongitude) {
		this.memLongitude = memLongitude;
	}
	public int getMemTotalCredit() {
		return memTotalCredit;
	}
	public void setMemTotalCredit(int memTotalCredit) {
		this.memTotalCredit = memTotalCredit;
	}
	public String getInstDistance() {
		return instDistance;
	}
	public void setInstDistance(String instDistance) {
		this.instDistance = instDistance;
	}
	public String getInstGender() {
		return instGender;
	}
	public void setInstGender(String instGender) {
		this.instGender = instGender;
	}
	public int getInstYngAge() {
		return instYngAge;
	}
	public void setInstYngAge(int instYngAge) {
		this.instYngAge = instYngAge;
	}
	public int getInstOldAge() {
		return instOldAge;
	}
	public void setInstOldAge(int instOldAge) {
		this.instOldAge = instOldAge;
	}

	
}
