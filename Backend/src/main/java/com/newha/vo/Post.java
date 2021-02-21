package com.newha.vo;

public class Post {
	private String postNo;
	private String name;
	private String id;
	
	public Post() {}
	
	public Post(String name, String id) {
		this.name = name;
		this.id = id;
	}

	public String getPostNo() {
		return postNo;
	}

	public void setPostNo(String postNo) {
		this.postNo = postNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
