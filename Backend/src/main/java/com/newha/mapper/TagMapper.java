package com.newha.mapper;

import java.util.List;

import com.newha.vo.HashTag;

public interface TagMapper {
	public List<HashTag> selectAll(String keyword);
}
