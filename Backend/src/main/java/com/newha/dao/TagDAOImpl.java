package com.newha.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.newha.mapper.TagMapper;
import com.newha.vo.HashTag;

@Repository
public class TagDAOImpl implements TagDAO {

	@Autowired
	TagMapper mapper;
	
	@Override
	public List<HashTag> selectAll(String keyword) {
		return mapper.selectAll(keyword);
	}
}
