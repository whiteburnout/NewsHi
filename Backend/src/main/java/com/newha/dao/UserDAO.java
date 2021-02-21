package com.newha.dao;

import java.util.ArrayList;
import java.util.List;

import com.newha.vo.User;

public interface UserDAO {
	public ArrayList<User> selectAll();
	public ArrayList<Integer> follow(int userNo);
	
	public void insert(User u);
	public void delete(String id);
	public void update(User u);
	
	public User selectUser(int userNo);
	public User selectOne(User u);
	public User selectOneById(String id);
	public List<User> selectAllByKeyword(String keyword);
	public int selectId(String id);
	public int userNo(String id);
	public List<User> selectUserByTag(String userNo);

	public void thumbnailPath(String userNo, String thumbnail_path);
	public void insertTag(String id, String tag);
	public List<String> selectJoinTagByuserNo(String userNo);
	public void subscribe(String id, String id2);
	public void subscdelete(String id, String id2);
	public int selectName(String name);
	public void socialInsert(User user);
	public void updatePassword(String id, String oldpassword, String newpassword);
	public void updateName(String id, String name);
	public List<String> tagList(int userNo);
	public void tagDelete(int userNo);
	public User socialLogin(User u);
	public void changePassword(String id, String password);

}