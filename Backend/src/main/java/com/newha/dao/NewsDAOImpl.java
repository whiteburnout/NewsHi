package com.newha.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.newha.mapper.NewsMapper;
import com.newha.vo.HashTag;
import com.newha.vo.News;
import com.newha.vo.NewsImage;
import com.newha.vo.Post;
import com.newha.vo.PostTag;
import com.newha.vo.User;
import com.newha.vo.UserGoodNews;
import com.newha.vo.UserScrapNews;

@Repository
public class NewsDAOImpl implements NewsDAO {
	
	@Autowired
	NewsMapper mapper; 
	
	@Override
	public int insertNews(News news) {
		return mapper.insertNews(news);
	}
	
	@Override
	public int insertImages(ArrayList<NewsImage> images) {
		return mapper.insertImages(images);
	}
	
	@Override
	public List<News> selectByUrl(String url) {
		return mapper.selectByUrl(url);
	}
	
	@Override
	public int insertPost(Post post) {
		return mapper.insertPost(post);
	}
	
	@Override
	public List<Post> selectPostByName(String name) {
		return mapper.selectPostByName(name);
	}
	
	@Override
	public User selectUserById(String id) {
		return mapper.selectUserById(id);
	}
	
	@Override
	public int insertUserScrapNews(UserScrapNews post) {
		return mapper.insertUserScrapNews(post);
	}
	
	@Override
	public int insertHashTag(String tag) {
		return mapper.insertHashTag(tag);
	}
	
	@Override
	public List<UserScrapNews> selectUserScrapNewsByPostNo(String postNo) {
		return mapper.selectUserScrapNewsByPostNo(postNo);
	}
	
	@Override
	public List<HashTag> selectHashTagByName(String name) {
		return mapper.selectHashTagByName(name);
	}
	
	@Override
	public int insertPostTag(PostTag tag) {
		return mapper.insertPostTag(tag);
	}
	
	@Override
	public int deletePost(String postNo) {
		return mapper.deletePost(postNo);
	}
	
	@Override
	public int deleteScrap(String scarpNo) {
		return mapper.deleteScrap(scarpNo);
	}
	
	@Override
	public int updatePost(Post post) {
		return mapper.updatePost(post);
	}
	
	@Override
	public int updateScrap(UserScrapNews scrap) {
		return mapper.updateScrap(scrap);
	}
	
	@Override
	public List<Post> selectPostById(String id) {
		return mapper.selectPostById(id);
	}

	@Override
	public List<UserScrapNews> selectUserScrapNews(String userNo) {
		return mapper.selectUserScrapNews(userNo);
	}

	@Override
	public News selectNews(String newsNo) {
		
		return mapper.selectNews(newsNo);
    }
	@Override
	public int increaseLike(String scrapNo) {
		return mapper.increaseLike(scrapNo);
	}
	
	@Override
	public int increaseDisLike(String scrapNo) {
		return mapper.increaseDisLike(scrapNo);
	}
	
	@Override
	public int decreaseLike(String scrapNo) {
		return mapper.decreaseLike(scrapNo);
	}
	
	@Override
	public int decreaseDisLike(String scrapNo) {
		return mapper.decreaseDisLike(scrapNo);
	}
	
	@Override
	public int updateLike(UserGoodNews userGoodNews) {
		return mapper.updateLike(userGoodNews);
	}
	
	@Override
	public int insertUserGoodNews(UserGoodNews userGoodNews) {
		return mapper.insertUserGoodNews(userGoodNews);
	}
	
	@Override
	public int updateDisLike(UserGoodNews userGoodNews) {
		return mapper.updateDisLike(userGoodNews);
	}
	
	@Override
	public int updateSave(UserGoodNews userGoodNews) {
		return mapper.updateSave(userGoodNews);
	}
	
	@Override
	public UserGoodNews selectLikeNews(UserGoodNews userGoodNews) {
		return mapper.selectLikeNews(userGoodNews);
	}
	
	@Override
	public UserGoodNews selectDisLikeNews(UserGoodNews userGoodNews) {
		return mapper.selectDisLikeNews(userGoodNews);
	}
	
	@Override
	public List<UserGoodNews> selectUserGoodNewsByUserNo(String userNo) {
		return mapper.selectUserGoodNewsByUserNo(userNo);
	}
	
	@Override
	public News selectNewsByScrapNo(String scrapNo) {
		return mapper.selectNewsByScrapNo(scrapNo);
	}
	
	@Override
	public List<NewsImage> selectNewImageByNewsNo(String newsNo) {
		return mapper.selectNewImageByNewsNo(newsNo);
	}
	
	@Override
	public List<News> selectNewsByTagName(String tagName) {
		return mapper.selectNewsByTagName(tagName);
	}
	
	@Override
	public List<UserScrapNews> selectUserScrapNewsByTagName(String tagName) {
		return mapper.selectUserScrapNewsByTagName(tagName);
	}
	
	@Override
	public List<News> selectNewsByUserNo(String userNo) {
		return mapper.selectNewsByUserNo(userNo);
	}
	
	@Override
	public List<UserScrapNews> selectUserScrapNewsByUserNo(String userNo) {
		return mapper.selectUserScrapNewsByUserNo(userNo);
	}
	
	@Override
	public List<Post> selectPostByTag(String tag) {
		return mapper.selectPostByTag(tag);
	}
	
	@Override
	public List<Post> selectPostByLikeName(String name) {
		return mapper.selectPostByLikeName(name);
	}
	
	@Override
	public UserScrapNews selectUserScrapNewsByScrapNo(String scrapNo) {
		return mapper.selectUserScrapNewsByScrapNo(scrapNo);
	}
	
	@Override
	public List<News> selectAllNews() {
		return mapper.selectAllNews();
	}
	
	@Override
	public List<UserScrapNews> selectUserScrapNewsByNewsNo(String newsNo) {
		return mapper.selectUserScrapNewsByNewsNo(newsNo);
	}
	
	@Override
	public User selectUserByName(String name) {
		return mapper.selectUserByName(name);
	}
	
	@Override
	public News selectNewsByNewsNo(String newsNo) {
		return mapper.selectNewsByNewsNo(newsNo);
	}
	
	@Override
	public User selectUserByUserNo(String userNo) {
		return mapper.selectUserByUserNo(userNo);
	}
	
	@Override
	public List<User> selectUserByTag(String userNo) {
		return mapper.selectUserByTag(userNo);
	}
}
