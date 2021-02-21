package com.newha.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.newha.mapper.UserMapper;
import com.newha.vo.User;

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	UserMapper mapper;

	@Override
	public ArrayList<User> selectAll() {
		return mapper.selectAll();
	}
	
	@Override
	public void insert(User u) {
		mapper.insert(u);
	}

	@Override
	public void delete(String id) {
		mapper.delete(id);
	}

	@Override
	public void update(User u) {
		mapper.update(u);
	}

	@Override
	public User selectOne(User u) {
		return mapper.selectOne(u);
	}

	@Override
	public User selectOneById(String id) {
		return mapper.selectOneById(id);
	}
	
	@Override
	public List<User> selectAllByKeyword(String keyword) {
		return mapper.selectAllByKeyword(keyword);
	}

	@Override
	public int selectId(String id) {
		return mapper.selectId(id);
	}

	@Override
	public int userNo(String id) {
		return mapper.userNo(id);
	}

	@Override
	public User selectUser(int userNo) {
		return mapper.selectUser(userNo);
	}

	@Override
	public ArrayList<Integer> follow(int userNo) {
		return mapper.follow(userNo);
	}

	@Override
	public void thumbnailPath(String userNo, String thumbnail_path) {
		mapper.thumbnailPath(userNo, thumbnail_path);
	}

	@Override
	public void insertTag(String id, String tag) {
		mapper.insertTag(id, tag);
	}

	@Override
	public void subscribe(String id, String name) {
		mapper.subscribe(id, name);
	}

	@Override
	public void subscdelete(String id, String id2) {
		mapper.subscdelete(id, id2);
	}

	@Override
	public int selectName(String name) {
		return mapper.selectName(name);
	}

	@Override
	public void socialInsert(User user) {
		mapper.socialInsert(user);
	}

	@Override
	public void updatePassword(String id, String oldpassword, String newpassword) {
		mapper.updatePassword(id, oldpassword, newpassword);
	}

	@Override
	public void updateName(String id, String name) {
		mapper.updateName(id, name);
	}

	@Override
	public List<String> tagList(int userNo) {
		return mapper.tagList(userNo);
	}

	@Override
	public void tagDelete(int userNo) {
		mapper.tagDelete(userNo);
	}

	@Override
	public User socialLogin(User u) {
		return mapper.socialLogin(u);
	}

	@Override
	public List<User> selectUserByTag(String userNo) {
		return mapper.selectUserByTag(userNo);
	}
	
	@Override
	public void changePassword(String id, String password) {
		mapper.changePassword(id, password);
	}
	
	@Override
	public List<String> selectJoinTagByuserNo(String userNo) {
		return mapper.selectJoinTagByuserNo(userNo);
	}
}
