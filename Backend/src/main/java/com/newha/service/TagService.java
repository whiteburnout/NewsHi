package com.newha.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.newha.vo.HashTag;

@Service
public interface TagService {
	public List<HashTag> searchTag(String keyword);
}
