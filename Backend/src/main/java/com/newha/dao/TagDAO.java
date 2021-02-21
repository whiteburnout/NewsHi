package com.newha.dao;

import java.util.List;

import com.newha.vo.HashTag;

public interface TagDAO {
	public List<HashTag> selectAll(String keyword);
}
