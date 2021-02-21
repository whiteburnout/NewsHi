package com.newha.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.newha.service.BoardService;
import com.newha.service.S3Service;
import com.newha.service.UserService;
import com.newha.vo.Board;
import com.newha.vo.BoardComment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("BoardController V1")
@RestController
@CrossOrigin
public class BoardController {
	@Autowired
	private BoardService service;

	@Autowired
	private UserService userservice;
	
	@Autowired
	private S3Service s3service;

	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@ApiOperation(value = "게시글 정보 insert", notes = "게시글 정보 insert 결과'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@PostMapping(value = "/boardInsert")
	public ResponseEntity<Map<String, String>> boardInsert(
			@ApiParam(value = "Board", required = true) @RequestBody List<Map<String, Object>> list) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		
		Board b = new Board();
		b.setContent((String)list.get(0).get("content"));
		b.setTitle((String)list.get(0).get("title"));
				
		try {
			String userNo = Integer.toString(userservice.userNo((String)list.get(1).get("id")));
			b.setUserNo(userNo);
			service.boardInsert(b);
			map.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}
	
	@ApiOperation(value = "s3에 파일 업로드", notes = "'SUCCESS' 또는 'FAIL' 문자열을 리턴", response = Map.class)
	@PostMapping("/uploadFile")
	public ResponseEntity<Map<String, String>> uploadFile(@RequestBody MultipartFile file) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			System.out.println("start");
			File f = new File(file.getOriginalFilename());
			file.transferTo(f);
			s3service.uploadOnS3(file.getOriginalFilename(), f);
			status = HttpStatus.ACCEPTED;
			map.put("message", SUCCESS); 
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}
	
	@ApiOperation(value = "게시판 리스트", notes = "성공/실패 여부에 따라 http 상태코드 출력", response = Map.class)
	@GetMapping(value = "/boardList")
	public ResponseEntity<List<Map<String, String>>> boardList(
			@ApiParam(value = "String", required = true) @RequestParam String id) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int userNo = userservice.userNo(id);
		HttpStatus status = null;
		try {
			List<Integer> l = service.boardListNo(userNo);
			if(l.size() == 0) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("message", "게시글이 없습니다.");
				list.add(map);
			}
			for (int i = 0; i < l.size(); i++) {
				String temp = Integer.toString(l.get(i));
				Map<String, String> map = new HashMap<String, String>();

				Board board = service.selectBoard(temp);
				String boardCommentCount = service.boardCommentCount(temp);
				map.put("boardPostNo", board.getBoardPostNo());
				map.put("title", board.getTitle());
				map.put("date", board.getDate());
				map.put("visit_cnt", board.getVisit_cnt());
				map.put("userNo", board.getUserNo());
				map.put("content", board.getContent());
				map.put("is_notice", board.getIs_notice());
				map.put("boardCommentCount", boardCommentCount); // 댓글 갯수
				
				list.add(map);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Map<String, String>>>(list, status);
	}

	@ApiOperation(value = "게시판 업데이트", notes = "성공/실패 여부에 따라 http 상태코드 출력", response = Map.class)
	@PutMapping(value = "/boardUpdate")
	public ResponseEntity<Map<String, String>> boardUpdate(
			@ApiParam(value = "Board", required = true) @RequestBody Board b) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			service.boardUpdate(b);
			map.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			map.put("message", FAIL);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "게시판 삭제", notes = "성공/실패 여부에 따라 http 상태코드 출력", response = Map.class)
	@DeleteMapping(value = "/boardDelete")
	public ResponseEntity<Map<String, String>> boardDelete(
			@ApiParam(value = "String", required = true) @RequestParam String boardPostNo) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			service.boardDelete(boardPostNo);
			map.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}


	@ApiOperation(value = "게시글", notes = "성공/실패 여부에 따라 http 상태코드 출력", response = Map.class)
	@GetMapping(value = "/boardCommentList")
	public ResponseEntity<List<Map<String, String>>> boardCommentList(
			@ApiParam(value = "int", required = true) @RequestParam String boardPostNo) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		HttpStatus status = null;
		
		try {
			List<Integer> l = service.boardCommentList(boardPostNo);

			for (int i = 0; i < l.size(); i++) {
				String commentNo = Integer.toString(l.get(i));
				BoardComment bc = service.boardComment(commentNo);
				Map<String, String> temp = new HashMap<String, String>();
				temp.put("BoardPostNo", bc.getBoardPostNo());
				temp.put("CommentNo", bc.getCommentNo());
				temp.put("Content", bc.getContent());
				temp.put("Date", bc.getDate());
				temp.put("UserNo", bc.getUserNo());
				list.add(temp);
			}
			
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		service.visitCnt(boardPostNo);
		return new ResponseEntity<List<Map<String, String>>>(list, status);
	}
	
	@ApiOperation(value = "게시글 댓글 insert", notes = "게시글 댓글 insert 결과'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@PostMapping(value = "/boardCommentInsert")
	public ResponseEntity<List<Map<String, String>>> boardCommentInsert(
			@ApiParam(value = "String", required = true) @RequestParam String boardPostNo,
			@ApiParam(value = "String", required = true) @RequestParam String id,
			@ApiParam(value = "String", required = true) @RequestParam String content) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		HttpStatus status = null;
		try {
			service.boardCommentInsert(boardPostNo, id, content);
			List<Integer> l = service.boardCommentList(boardPostNo);
			for (int i = 0; i < l.size(); i++) {
				String commentNo = Integer.toString(l.get(i));
				BoardComment bc = service.boardComment(commentNo);
				System.out.println(bc.getBoardPostNo());
				Map<String, String> temp = new HashMap<String, String>();
				temp.put("BoardPostNo", bc.getBoardPostNo());
				temp.put("CommentNo", bc.getCommentNo());
				temp.put("Content", bc.getContent());
				temp.put("Date", bc.getDate());
				temp.put("UserNo", bc.getUserNo());
				list.add(temp);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Map<String, String>>>(list, status);
	}
	
	@ApiOperation(value = "댓글 삭제", notes = "성공/실패 여부에 따라 http 상태코드 출력", response = Map.class)
	@DeleteMapping(value = "/boardCommentDelete")
	public ResponseEntity<Map<String, String>> boardCommentDelete(
			@ApiParam(value = "String", required = true) @RequestParam String commentNo) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			service.boardCommentDelete(commentNo);
			map.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}
	
	@ApiOperation(value = "댓글 수정", notes = "성공/실패 여부에 따라 http 상태코드 출력", response = Map.class)
	@PutMapping(value = "/boardCommentUpdate")
	public ResponseEntity<Map<String, String>> boardCommentUpdate(
			@ApiParam(value = "Board", required = true) @RequestBody BoardComment bc) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			service.boardCommentUpdate(bc);
			map.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}
	
	
	
}
