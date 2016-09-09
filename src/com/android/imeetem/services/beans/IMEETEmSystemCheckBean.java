/**
 * 
 */
package com.android.imeetem.services.beans;

import java.io.Serializable;

/**
 * @author kevinscomp
 * 
 */
public class IMEETEmSystemCheckBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private IMEETEmSystemCheckBean() {
	}

	public static IMEETEmSystemCheckBean getInstance() {
		return new IMEETEmSystemCheckBean();
	}

	private String sysCheckSystemType;
	private String sysCheckSystemVersion;
	private String sysCheckSystemStatus;
	private String sysCheckSystemTitleMessage;
	private String sysCheckSystemMessage;
	private String sysCheckSystemDate;

	public String getSysCheckSystemType() {
		return sysCheckSystemType;
	}

	public void setSysCheckSystemType(String sysCheckSystemType) {
		this.sysCheckSystemType = sysCheckSystemType;
	}

	public String getSysCheckSystemVersion() {
		return sysCheckSystemVersion;
	}

	public void setSysCheckSystemVersion(String sysCheckSystemVersion) {
		this.sysCheckSystemVersion = sysCheckSystemVersion;
	}

	public String getSysCheckSystemStatus() {
		return sysCheckSystemStatus;
	}

	public void setSysCheckSystemStatus(String sysCheckSystemStatus) {
		this.sysCheckSystemStatus = sysCheckSystemStatus;
	}

	public String getSysCheckSystemTitleMessage() {
		return sysCheckSystemTitleMessage;
	}

	public void setSysCheckSystemTitleMessage(String sysCheckSystemTitleMessage) {
		this.sysCheckSystemTitleMessage = sysCheckSystemTitleMessage;
	}

	public String getSysCheckSystemMessage() {
		return sysCheckSystemMessage;
	}

	public void setSysCheckSystemMessage(String sysCheckSystemMessage) {
		this.sysCheckSystemMessage = sysCheckSystemMessage;
	}

	public String getSysCheckSystemDate() {
		return sysCheckSystemDate;
	}

	public void setSysCheckSystemDate(String sysCheckSystemDate) {
		this.sysCheckSystemDate = sysCheckSystemDate;
	}

	public String toString() {

		StringBuilder bld = new StringBuilder();

		if (sysCheckSystemType != null) {
			bld.append("\n System Type: " + sysCheckSystemType + "\n");
		}

		if (sysCheckSystemVersion != null) {
			bld.append("\n System Version: " + sysCheckSystemVersion + "\n");
		}

		if (sysCheckSystemStatus != null) {
			bld.append("\n System Status: " + sysCheckSystemStatus + "\n");
		}

		if (sysCheckSystemTitleMessage != null) {
			bld.append("\n System Title: " + sysCheckSystemTitleMessage + "\n");
		}

		if (sysCheckSystemMessage != null) {
			bld.append("\n System Message: " + sysCheckSystemMessage + "\n");
		}

		if (sysCheckSystemDate != null) {
			bld.append("\n System Date: " + sysCheckSystemDate + "\n");
		}
		return bld.toString();
	}
}
