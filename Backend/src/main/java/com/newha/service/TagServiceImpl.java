package com.newha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newha.dao.TagDAO;
import com.newha.vo.HashTag;

@Service
public class TagServiceImpl implements TagService {

	@Autowired
	TagDAO dao;
	
	@Override
	public List<HashTag> searchTag(String keyword) {
		return dao.selectAll(keyword);
	}
	
}
