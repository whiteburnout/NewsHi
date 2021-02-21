package com.newha.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newha.vo.Board;
import com.newha.vo.BoardComment;

public interface BoardMapper {
	public void boardInsert(Board b);
	public void boardCreate(String id);
	public List<Integer> boardListNo(int userNo);
	public Board selectBoard(String boardPostNo);
	public void boardUpdate(Board b);
	public void boardDelete(String boardPostNo);
	public void visitCnt(String boardPostNo);
	public void boardCommentInsert(@Param("boardPostNo")String boardPostNo, @Param("id")String id, @Param("content")String content);
	public List<Integer> boardCommentList(String boardPostNo);
	public BoardComment boardComment(String commentNo);
	public String boardCommentCount(String temp);
	public void boardCommentDelete(String commentNo);
	public void boardCommentUpdate(BoardComment bc);

}
 