package com.newha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newha.dao.BoardDAO;
import com.newha.vo.Board;
import com.newha.vo.BoardComment;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	BoardDAO dao;
	
	@Override
	public void boardInsert(Board b) {
		dao.boardInsert(b);
	}

	@Override
	public void boardCreate(String id) {
		dao.boardCreate(id);
	}

	@Override
	public List<Integer> boardListNo(int id) {
		return dao.boardListNo(id);
	}

	@Override
	public Board selectBoard(String boardPostNo) {
		return dao.selectBoard(boardPostNo);
	}

	@Override
	public void boardUpdate(Board b) {
		dao.boardUpdate(b);
	}

	@Override
	public void boardDelete(String boardPostNo) {
		dao.boardDelete(boardPostNo);
	}

	@Override
	public void visitCnt(String boardPostNo) {
		dao.visitCnt(boardPostNo);
	}

	@Override
	public void boardCommentInsert(String boardPostNo, String id, String content) {
		dao.boardCommentInsert(boardPostNo, id, content);
	}

	@Override
	public List<Integer> boardCommentList(String boardPostNo) {
		return dao.boardCommentList(boardPostNo);
	}

	@Override
	public BoardComment boardComment(String commentNo) {
		return dao.boardComment(commentNo);
	}

	@Override
	public String boardCommentCount(String temp) {
		return dao.boardCommentCount(temp);
	}

	@Override
	public void boardCommentDelete(String commentNo) {
		dao.boardCommentDelete(commentNo);
		
	}

	@Override
	public void boardCommentUpdate(BoardComment bc) {
		dao.boardCommentUpdate(bc);
	}

}
