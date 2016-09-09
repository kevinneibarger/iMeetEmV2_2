/**
 * 
 */
package com.android.imeetem.util;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

/**
 * @author kevinscomp
 *
 */
public class MatchInfoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String imageUrl;
	private String memberText;
	private Drawable imageDrawable;
	private String searchStatus;
	private String memberId;
	private String memberFacebookId;
	private String memberAcctStatus;
	private java.sql.Date lastActivity;
	private String memberImage;
	private String memberImageLoc;
	private String memberImageFolderLoc;
	private String memberName;
	private String memberAge;
	private String memberDistance;
	private String member250CharText;
	private String memberInfoDetail;
	// new variables as on 5/20/2014 - via Hartley
	private String memberHairColor;
	private String memberEyeColor;
	private boolean memberHasKids;
	private boolean memberWantsKids;
	private String memberEducation;
	private String memberEthnicity;
	
	private MatchInfoBean() {}
	
	public static MatchInfoBean getInstance() {
		return new MatchInfoBean();
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public Drawable getImageDrawable() {
		return imageDrawable;
	}
	
	public void setImageDrawable(Drawable imageDrawable) {
		this.imageDrawable = imageDrawable;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberFacebookId() {
		return memberFacebookId;
	}

	public void setMemberFacebookId(String memberFacebookId) {
		this.memberFacebookId = memberFacebookId;
	}

	public String getMemberAcctStatus() {
		return memberAcctStatus;
	}

	public void setMemberAcctStatus(String memberAcctStatus) {
		this.memberAcctStatus = memberAcctStatus;
	}

	public java.sql.Date getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(java.sql.Date lastActivity) {
		this.lastActivity = lastActivity;
	}

	public String getMemberImage() {
		return memberImage;
	}

	public void setMemberImage(String memberImage) {
		this.memberImage = memberImage;
	}

	public String getMemberImageLoc() {
		return memberImageLoc;
	}

	public void setMemberImageLoc(String memberImageLoc) {
		this.memberImageLoc = memberImageLoc;
	}

	public String getMemberImageFolderLoc() {
		return memberImageFolderLoc;
	}

	public void setMemberImageFolderLoc(String memberImageFolderLoc) {
		this.memberImageFolderLoc = memberImageFolderLoc;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberAge() {
		return memberAge;
	}

	public void setMemberAge(String memberAge) {
		this.memberAge = memberAge;
	}

	public String toString() {
		
		StringBuilder bld = new StringBuilder();
		
		bld.append("\n -------- Match Info -------- \n");
		if (this.imageUrl != null) {
			bld.append("Image Url: "+this.imageUrl);
		}
		
		if (this.memberText != null) {
			bld.append("Match Text: "+this.memberText);
		}
		
		if (this.imageDrawable != null) {
			bld.append("Image Drawable: "+this.imageDrawable.toString());
		}
		
		if (this.searchStatus != null) {
			bld.append("Search Status: "+this.searchStatus);
		}
		
		if (this.memberId != null) {
			bld.append("Member ID: "+this.memberId);
		}
		
		if (this.memberFacebookId != null) {
			bld.append("Member Facebook ID: "+this.memberFacebookId);
		}
		
		if (this.memberAcctStatus != null) {
			bld.append("Member Acct Status: "+this.memberAcctStatus);
		}
		
		if (this.lastActivity != null) {
			bld.append("Last Activity: "+this.lastActivity);
		}
		
		if (this.memberImage != null) {
			bld.append("Member Image: "+this.memberImage);
		}
		
		if (this.memberImageLoc != null) {
			bld.append("Member Image Loc: "+this.memberImageLoc);
		}
		
		if (this.memberImageFolderLoc != null) {
			bld.append("Member Image Folder Loc: "+this.memberImageFolderLoc);
		}
		
		if (this.memberName != null) {
			bld.append("Member Name: "+this.memberName);
		}
		
		if (this.memberAge != null) {
			bld.append("Member Age: "+this.memberAge);
		}
		
		if (this.memberDistance != null) {
			bld.append("Member Distance: "+this.memberDistance);
		}
		
		if (this.memberInfoDetail != null) {
			bld.append("Member Info Detail: "+this.memberInfoDetail);
		}
		
		if (this.memberHairColor != null) {
			bld.append("Member Hair Color: "+this.memberHairColor);
		}
		
		if (this.memberEyeColor != null) {
			bld.append("Member Eye Color: "+this.memberEyeColor);
		}
		
		if (this.memberHasKids) {
			bld.append("Member Has Kids!\n");
		} else {
			bld.append("Member Does Not Have Kids\n");
		}
		
		if (this.memberWantsKids) {
			bld.append("Member Wants Kids\n");
		} else {
			bld.append("Member Does NOT Want Kids\n");
		}
		
		if (this.memberEducation != null) {
			bld.append("Member Education: "+this.memberEducation);
		}
		
		if (this.memberEthnicity != null) {
			bld.append("Member Ethnicity: "+this.memberEthnicity);
		}
		
		bld.append("\n ------------------------------- \n");
		return bld.toString();
	}

	public String getMemberDistance() {
		return memberDistance;
	}

	public void setMemberDistance(String memberDistance) {
		this.memberDistance = memberDistance;
	}

	public String getMember250CharText() {
		return member250CharText;
	}

	public void setMember250CharText(String member250CharText) {
		this.member250CharText = member250CharText;
	}

	public String getMemberText() {
		return memberText;
	}

	public void setMemberText(String memberText) {
		this.memberText = memberText;
	}

	public String getMemberInfoDetail() {
		return memberInfoDetail;
	}

	public void setMemberInfoDetail(String memberInfoDetail) {
		this.memberInfoDetail = memberInfoDetail;
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
