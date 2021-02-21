package com.newha.vo;

public class News {

	private String newsNo;
	private String title;
	private String subtitle;
	private String content;
	private String image_path;
	private String url;
	private String article_date;
	private String article_bot_summary;
	private String article_image_caption;
	private String company;
	
	public News() {}

	public String getNewsNo() {
		return newsNo;
	}

	public void setNewsNo(String newsNo) {
		this.newsNo = newsNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getArticle_date() {
		return article_date;
	}

	public void setArticle_date(String article_date) {
		this.article_date = article_date;
	}

	public String getArticle_bot_summary() {
		return article_bot_summary;
	}

	public void setArticle_bot_summary(String article_bot_summary) {
		this.article_bot_summary = article_bot_summary;
	}

	public String getArticle_image_caption() {
		return article_image_caption;
	}

	public void setArticle_image_caption(String article_img_caption) {
		this.article_image_caption = article_img_caption;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
		
}
