package com.newha.vo;

public class Board {
	private String boardPostNo;
	private String boardNo;
	private String userNo;
	private String title;
	private String content;
	private String date;
	private String visit_cnt;
	private String is_notice;

	public String getBoardPostNo() {
		return boardPostNo;
	}

	public void setBoardPostNo(String boardPostNo) {
		this.boardPostNo = boardPostNo;
	}

	public String getBoardNo() {
		return boardNo;
	}

	public void setBoardNo(String boardNo) {
		this.boardNo = boardNo;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getVisit_cnt() {
		return visit_cnt;
	}

	public void setVisit_cnt(String visit_cnt) {
		this.visit_cnt = visit_cnt;
	}

	public String getIs_notice() {
		return is_notice;
	}

	public void setIs_notice(String is_notice) {
		this.is_notice = is_notice;
	}

	@Override
	public String toString() {
		return "Board [boardPostNo=" + boardPostNo + ", boardNo=" + boardNo + ", userNo=" + userNo + ", title=" + title
				+ ", content=" + content + ", date=" + date + ", visit_cnt=" + visit_cnt + ", is_notice=" + is_notice
				+ "]";
	}

	public Board(String boardPostNo, String boardNo, String userNo, String title, String content, String date,
			String visit_cnt, String is_notice) {
		super();
		this.boardPostNo = boardPostNo;
		this.boardNo = boardNo;
		this.userNo = userNo;
		this.title = title;
		this.content = content;
		this.date = date;
		this.visit_cnt = visit_cnt;
		this.is_notice = is_notice;
	}

	public Board() {
	}

}
