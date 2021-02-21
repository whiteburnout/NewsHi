package com.newha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newha.service.TagService;
import com.newha.vo.HashTag;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin
public class TagController {
	
	@Autowired
	TagService service;

	@ApiOperation(value = "태그 검색", notes = "태그 List 리턴", response = List.class)
	@GetMapping(value = "/search/tag")
	List<HashTag> searchTag(@ApiParam(value = "keyword", required = true)@RequestParam String keyword){
		return service.searchTag(keyword + "%");
	}
}
