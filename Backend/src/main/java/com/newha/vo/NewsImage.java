package com.newha.vo;

public class NewsImage {

	private String newsNo;
	private String news_image;
	private String image_caption;
	
	public NewsImage() {}
	
	

	public NewsImage(String newsNo, String news_image) {
		this.newsNo = newsNo;
		this.news_image = news_image;
	}



	public String getNewsNo() {
		return newsNo;
	}

	public void setNewsNo(String newsNo) {
		this.newsNo = newsNo;
	}

	public String getNews_image() {
		return news_image;
	}

	public void setNews_image(String news_image) {
		this.news_image = news_image;
	}

	public String getImage_caption() {
		return image_caption;
	}

	public void setImage_caption(String image_caption) {
		this.image_caption = image_caption;
	}
	
}
