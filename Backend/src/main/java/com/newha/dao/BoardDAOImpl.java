package com.newha.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.newha.mapper.BoardMapper;
import com.newha.vo.Board;
import com.newha.vo.BoardComment;

@Repository
public class BoardDAOImpl implements BoardDAO{

	@Autowired
	BoardMapper mapper;
	
	@Override
	public void boardInsert(Board b) {
		mapper.boardInsert(b);
	}

	@Override
	public void boardCreate(String id) {
		mapper.boardCreate(id);
	}

	@Override
	public List<Integer> boardListNo(int id) {
		return mapper.boardListNo(id);
	}

	@Override
	public Board selectBoard(String boardPostNo) {
		return mapper.selectBoard(boardPostNo);
	}

	@Override
	public void boardUpdate(Board b) {
		mapper.boardUpdate(b);
	}

	@Override
	public void boardDelete(String boardPostNo) {
		mapper.boardDelete(boardPostNo);
	}

	@Override
	public void visitCnt(String boardPostNo) {
		mapper.visitCnt(boardPostNo);
	}

	@Override
	public void boardCommentInsert(String boardPostNo, String id, String content) {
		mapper.boardCommentInsert(boardPostNo, id, content);
	}

	@Override
	public List<Integer> boardCommentList(String boardPostNo) {
		return mapper.boardCommentList(boardPostNo);
	}

	@Override
	public BoardComment boardComment(String commentNo) {
		return mapper.boardComment(commentNo);
	}

	@Override
	public String boardCommentCount(String temp) {
		return mapper.boardCommentCount(temp);
	}

	@Override
	public void boardCommentDelete(String commentNo) {
		mapper.boardCommentDelete(commentNo);
		
	}

	@Override
	public void boardCommentUpdate(BoardComment bc) {
		mapper.boardCommentUpdate(bc);
	}
	
}
