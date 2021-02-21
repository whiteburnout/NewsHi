package com.newha.vo;

public class PostTag {

	private String scrapNo;
	private String tagNo;
	
	public PostTag() {}
	
	public PostTag(String scrapNo, String tagNo) {
		this.scrapNo = scrapNo;
		this.tagNo = tagNo;
	}

	public String getScrapNo() {
		return scrapNo;
	}

	public void setScrapNo(String scrapNo) {
		this.scrapNo = scrapNo;
	}

	public String getTagNo() {
		return tagNo;
	}

	public void setTagNo(String tagNo) {
		this.tagNo = tagNo;
	}
	
}
