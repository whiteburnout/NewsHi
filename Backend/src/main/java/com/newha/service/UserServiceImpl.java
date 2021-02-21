package com.newha.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newha.dao.UserDAO;
import com.newha.vo.User;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserDAO dao;

	@Override
	public ArrayList<User> selectAll() {
		return dao.selectAll();
	}
	
	@Override
	public void insert(User u) {
		dao.insert(u);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public void update(User u) {
		dao.update(u);
	}

	@Override
	public User login(User u) {
		return dao.selectOne(u);
	}

	@Override
	public User userInfo(String id) {
		return dao.selectOneById(id);
	}
	
	@Override
	public List<User> searchUser(String keyword) {
		return dao.selectAllByKeyword(keyword);
	}

	@Override
	public int selectId(String id) {
		return dao.selectId(id);
	}

	@Override
	public int userNo(String id) {
		return dao.userNo(id);
	}

	@Override
	public User selectUser(int userNo) {
		return dao.selectUser(userNo);
	}

	@Override
	public ArrayList<Integer> follow(int userNo) {
		return dao.follow(userNo);
	}

	@Override
	public void thumbnailPath(String userNo, String thumbnail_path) {
		dao.thumbnailPath(userNo, thumbnail_path);
	}

	@Override
	public void insertTag(String id, String tag) {
		dao.insertTag(id, tag);
	}

	@Override
	public void subscribe(String id, String name) {
		dao.subscribe(id, name);
	}

	@Override
	public void subscdelete(String id, String id2) {
		dao.subscdelete(id, id2);
	}

	@Override
	public int selectName(String name) {
		return dao.selectName(name);
	}

	@Override
	public void socialInsert(User user) {
		dao.socialInsert(user);
	}

	@Override
	public void updatePassword(String id, String oldpassword, String newpassword) {
		dao.updatePassword(id, oldpassword, newpassword);
	}

	@Override
	public void updateName(String id, String name) {
		dao.updateName(id, name);
	}

	@Override
	public List<String> tagList(int userNo) {
		return dao.tagList(userNo);
	}

	@Override
	public void tagDelete(int userNo) {
		dao.tagDelete(userNo);
	}

	@Override
	public User socialLogin(User u) {
		return dao.socialLogin(u);
	}
	
	@Override
	public List<User> selectUserByTag(String userNo) {
		return dao.selectUserByTag(userNo);
	}

	@Override
	public void changePassword(String id, String password) {
		dao.changePassword(id, password);
	}
	
	@Override
	public List<String> selectJoinTagByuserNo(String userNo) {
		return dao.selectJoinTagByuserNo(userNo);
	}

}
