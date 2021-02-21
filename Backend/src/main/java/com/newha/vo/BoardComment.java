package com.newha.vo;

public class BoardComment {

	private String commentNo;
	private String boardPostNo;
	private String userNo;
	private String content;
	private String date;
	
	
	@Override
	public String toString() {
		return "BoardComment [commentNo=" + commentNo + ", boardPostNo=" + boardPostNo + ", userNo=" + userNo
				+ ", content=" + content + ", date=" + date + "]";
	}

	public BoardComment() {}
	
	public BoardComment(String commentNo, String boardPostNo, String userNo, String content, String date) {
		super();
		this.commentNo = commentNo;
		this.boardPostNo = boardPostNo;
		this.userNo = userNo;
		this.content = content;
		this.date = date;
	}
	public String getCommentNo() {
		return commentNo;
	}
	public void setCommentNo(String commentNo) {
		this.commentNo = commentNo;
	}
	public String getBoardPostNo() {
		return boardPostNo;
	}
	public void setBoardPostNo(String boardPostNo) {
		this.boardPostNo = boardPostNo;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
	
}
