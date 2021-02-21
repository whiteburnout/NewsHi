package com.newha.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newha.service.NewsService;
import com.newha.vo.News;
import com.newha.vo.NewsImage;
import com.newha.vo.Post;
import com.newha.vo.PostTag;
import com.newha.vo.UserGoodNews;
import com.newha.vo.UserScrapNews;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("UserController V1")
@RestController
@CrossOrigin
@EnableTransactionManagement
public class NewsController {

	@Autowired
	NewsService service;

	// WebDriver 설정
	private WebDriver driver;

	// Properties 설정
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
//  window chromedriver
	public static String WEB_DRIVER_PATH = System.getProperty("user.dir") + "\\chromedriver_window.exe";
//  aws 서버용 chromedriver
//	public static String WEB_DRIVER_PATH = "/home/ubuntu/s04p12a307/Backend/chromedriver_linux";
	
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Transactional
	@ApiOperation(value = "기사 파싱", notes = "url을 받아 기사를 파싱", response = Map.class)
	@PostMapping(value = "/article")
	public ResponseEntity<Map<String, String>> getArticle(
			@ApiParam(value = "List", required = true) @RequestBody List<Map<String, String>> m) throws IOException {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			for (int i = 0; i < m.size(); i++) {
				String url = m.get(i).get("url");
				System.out.println(url);
				if (service.selectByUrl(url).size() != 0)// 이미 존재하는 기사 생략
					continue;

				News news = new News();
				// JSOUP
				Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).execute();
				Document document = response.parse();

				System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

				// Selenium Driver SetUp
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless");
				options.addArguments("--no-sandbox");
				options.addArguments("--disable-dev-shm-usage");
				driver = new ChromeDriver(options);
				try {
					// get page
					driver.get(url);
					driver.findElement(By.linkText("요약봇")).click();
					Thread.sleep(800);
					String summary = driver.findElement(By.className("_contents_body")).getText();// 요약봇
					news.setArticle_bot_summary(summary);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					driver.close();
				}
				Elements temp = document.select(".end_photo_org");// 이미지
				ArrayList<String> img = new ArrayList<String>();// 이미지 리스트
				for (Element el5 : temp) {
					Elements eImg = el5.select("img");
					img.add(eImg.attr("abs:src"));
				}

				Elements temp2 = document.select(".img_desc");// 이미지 캡션
				ArrayList<String> caption = new ArrayList<String>();// 이미지 캡션 리스트
				for (Element el5 : temp2) {
					caption.add(el5.text());
				}

				String content = document.select("#articleBodyContents").text();// 본문
				String title = document.select("#articleTitle").text();// 제목
				String subtitle = document.select(".media_end_summary").text();// 부제
				String date = document.select(".t11").get(0).text();// 날짜
				String company = document.select(".press_logo > a").select("img").attr("title");// 언론사
				news.setContent(content);
				news.setTitle(title);
				if (subtitle.length() > 0)// 서브타이틀 존재하면
					news.setSubtitle(subtitle);
				news.setArticle_date(date);
				news.setCompany(company);
				news.setUrl(url);
				if (img.size() > 0)// 이미지가 존재하면
					news.setImage_path(img.get(0));
				if (caption.size() > 0)// 이미지 존재하고 이미지 캡션이 존재할경우
					news.setArticle_image_caption(caption.get(0));

				service.insertNews(news);

				if (img.size() > 0) {// 이미지가 2개이상 존재할 경우 news_image에 넣어준다
					String newsNo = service.selectByUrl(url).get(0).getNewsNo();
					ArrayList<NewsImage> images = new ArrayList<NewsImage>();
					for (String s : img) {
						images.add(new NewsImage(newsNo, s));
					}
					int cnt = 0;
					for (String s : caption) {// 캡션이 존재할경우 같이 넣어줌
						images.get(cnt++).setImage_caption(s);
					}
					service.insertImages(images);
				}
			}

			String name = m.get(0).get("name");
			String id = m.get(0).get("id");
			service.insertPost(new Post(name, id));

			List<Post> posts = service.selectPostByName(name);

			String postNo = posts.get(posts.size() - 1).getPostNo();
			String userNo = service.selectUserById(id).getUserNo();

			// UserScrapNew에 게시물을 넣어줌
			for (int i = 0; i < m.size(); i++) {
				String url = m.get(i).get("url");
				String summary = m.get(i).get("summary");
				String newsNo = service.selectByUrl(url).get(0).getNewsNo();
				UserScrapNews post = new UserScrapNews();
				post.setCurator_summary(summary);
				post.setNewsNo(newsNo);
				post.setPostNo(postNo);
				post.setUserNo(userNo);
				service.insertUserScrapNews(post);
			}

			// Hash태그에 태그를 넣어주고(db에서 중복방지) 해당 태그들과 게시물을 태그 연결(post_tag 테이블)
			List<UserScrapNews> temp = service.selectUserScrapNewsByPostNo(postNo);
			ArrayList<UserScrapNews> list = new ArrayList<UserScrapNews>(temp);
			for (int i = 0; i < m.size(); i++) {
				String tag[] = m.get(i).get("tags").split("#");
				for (int j = 1; j < tag.length; j++) {
					service.insertHashTag(tag[j]);
				}

				for (int j = 1; j < tag.length; j++) {
					String tagNo = service.selectHashTagByName(tag[j]).get(0).getTagNo();
					System.out.println(list.get(i).getScrapNo() + "," + tagNo);
					service.insertPostTag(new PostTag(list.get(i).getScrapNo(), tagNo));
				}
			}
			status = HttpStatus.ACCEPTED;
			map.put("message", SUCCESS);
			map.put("postNo", postNo);
		} catch (IOException e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			e.printStackTrace();
			map.put("message", FAIL);
		}

		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "포스트 삭제", notes = "삭제 결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@DeleteMapping(value = "/article/post")
	public ResponseEntity<Map<String, String>> deletePost(
			@ApiParam(value = "String", required = true) @RequestParam String postNo) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			if (service.deletePost(postNo) > 0) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "스크랩 삭제", notes = "삭제 결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@DeleteMapping(value = "/article/scrap")
	public ResponseEntity<Map<String, String>> deleteScrap(
			@ApiParam(value = "String", required = true) @RequestParam String scrapNo) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			if (service.deleteScrap(scrapNo) > 0) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "포스트 수정(이름)", notes = "수정 결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@PutMapping(value = "/article/post")
	public ResponseEntity<Map<String, String>> updatePost(
			@ApiParam(value = "post", required = true) @RequestBody Post post) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			if (service.updatePost(post) > 0) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "스크랩 수정", notes = "수정 결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@PutMapping(value = "/article/scrap")
	public ResponseEntity<Map<String, String>> updateScrap(
			@ApiParam(value = "post", required = true) @RequestBody UserScrapNews scrap) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			if (service.updateScrap(scrap) > 0) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "포스트 목록 (아이디)", notes = "해당 id가 작성한 포스트 목록을 반환", response = List.class)
	@GetMapping(value = "/article/post")
	public ResponseEntity<List<Post>> getPost(@ApiParam(value = "String", required = true) @RequestParam String id) {
		HttpStatus status = null;
		List<Post> list = new ArrayList<Post>();
		try {
			list = service.selectPostById(id);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Post>>(list, status);
	}
	
	@ApiOperation(value = "포스트 목록 이름으로 검색", notes = "포스트 이름으로 검색", response = List.class)
	@GetMapping(value = "/article/postByName")
	public ResponseEntity<List<Post>> getPostByName(@ApiParam(value = "String", required = true) @RequestParam String name) {
		HttpStatus status = null;
		List<Post> list = new ArrayList<Post>();
		try {
			list = service.selectPostByLikeName(name + "%");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Post>>(list, status);
	}
	
	@ApiOperation(value = "포스트 목록 태그로 검색", notes = "포스트 태그로 검색", response = List.class)
	@GetMapping(value = "/article/postByTag")
	public ResponseEntity<List<Post>> getPostByTag(@ApiParam(value = "String", required = true) @RequestParam String tag) {
		HttpStatus status = null;
		List<Post> list = new ArrayList<Post>();
		try {
			list = service.selectPostByTag(tag + "%");
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Post>>(list, status);
	}


	@ApiOperation(value = "스크랩 목록", notes = "해당 포스트에 포함된 스크랩 목록을 반환", response = List.class)
	@GetMapping(value = "/article/scrap")
	public ResponseEntity<List<UserScrapNews>> getScrap(
			@ApiParam(value = "String", required = true) @RequestParam String postNo) {
		HttpStatus status = null;
		List<UserScrapNews> list = new ArrayList<UserScrapNews>();
		try {
			list = service.selectUserScrapNewsByPostNo(postNo);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<UserScrapNews>>(list, status);
	}
	
	@ApiOperation(value = "포스트 뉴스 목록", notes = "해당 포스트에 포함된 기사 목록을 반환(이미지까지 모두 포함)", response = List.class)
	@GetMapping(value = "/article/scraplist")
	public ResponseEntity<List<Map<String, String[]>>> getNews(
			@ApiParam(value = "String", required = true) @RequestParam String postNo) {
		HttpStatus status = null;
		List<UserScrapNews> list = new ArrayList<UserScrapNews>();
		List<Map<String, String[]>> result = new ArrayList<Map<String,String[]>>();
		try {
			list = service.selectUserScrapNewsByPostNo(postNo);
			for (UserScrapNews userScrapNews : list) {
				Map<String, String[]> m = new HashMap<String, String[]>();
				News temp = service.selectNews(userScrapNews.getNewsNo());
				List<NewsImage> images = service.selectNewImageByNewsNo(userScrapNews.getNewsNo());
				
				m.put("newsNo", new String[] {temp.getNewsNo()});
				m.put("title", new String[] {temp.getTitle()});
				m.put("subtitle", new String[] {temp.getSubtitle()});
				m.put("content", new String[] {temp.getContent()});
				m.put("image_path", new String[] {temp.getImage_path()});
				m.put("url", new String[] {temp.getUrl()});
				m.put("article_date", new String[] {temp.getArticle_date()});
				m.put("article_bot_summary", new String[] {temp.getArticle_bot_summary()});
				m.put("article_image_catpion", new String[] {temp.getArticle_image_caption()});
				m.put("company", new String[] {temp.getCompany()});
				m.put("like_cnt", new String[] {userScrapNews.getLike_cnt()});
				m.put("dislike_cnt", new String[] {userScrapNews.getDislike_cnt()});
				m.put("curator_summary", new String[] {userScrapNews.getCurator_summary()});
				m.put("scrapNo", new String[] {userScrapNews.getScrapNo()});
				
				if(images != null) {
					String newsImages[] = new String[images.size()];
					String caption[] = new String[images.size()];
					int cnt = 0;
					for (NewsImage image : images) {
						newsImages[cnt] = image.getNews_image();
						caption[cnt++] = image.getImage_caption();
					}
					m.put("new_image", newsImages);
					m.put("image_caption", caption);
				}
				else {
					m.put("new_image", null);
					m.put("image_caption", null);
				}
				result.add(m);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Map<String, String[]>>>(result, status);
	}
	
	@ApiOperation(value = "태그로 기사 리스트 검색", notes = "태그로 검색된 기사 리스트 반환", response = List.class)
	@GetMapping(value = "/article/newsByTag")
	public ResponseEntity<List<Map<String, String[]>>> getNewsByTag(
			@ApiParam(value = "String", required = true) @RequestParam String tagName) {
		HttpStatus status = null;
		List<News> list = new ArrayList<News>();
		List<UserScrapNews> scrap = new ArrayList<UserScrapNews>();
		List<Map<String, String[]>> result = new ArrayList<Map<String,String[]>>();
		try {
			list = service.selectNewsByTagName(tagName);
			scrap = service.selectUserScrapNewsByTagName(tagName);
			for (News news : list) {
				Map<String, String[]> m = new HashMap<String, String[]>();
				m.put("newsNo", new String[] {news.getNewsNo()});
				m.put("title", new String[] {news.getTitle()});
				m.put("subtitle", new String[] {news.getSubtitle()});
				m.put("content", new String[] {news.getContent()});
				m.put("image_path", new String[] {news.getImage_path()});
				m.put("url", new String[] {news.getUrl()});
				m.put("article_date", new String[] {news.getArticle_date()});
				m.put("article_bot_summary", new String[] {news.getArticle_bot_summary()});
				m.put("article_image_caption", new String[] {news.getArticle_image_caption()});
				m.put("company", new String[] {news.getCompany()});
				
				ArrayList<String> temp1 = new ArrayList<String>();
				ArrayList<String> temp2 = new ArrayList<String>();
				for (UserScrapNews ucn : scrap) {
					String newsNo = ucn.getNewsNo();
					if(news.getNewsNo().equals(newsNo)) {
						temp1.add(ucn.getLike_cnt());
						temp2.add(ucn.getDislike_cnt());
					}
				}
				String [] like = new String[temp1.size()];
				String [] dislike = new String[temp2.size()];
				int cnt = 0;
				for (String s : temp1) {
					like[cnt++] = s;
				}
				cnt = 0;
				for (String s : temp2) {
					dislike[cnt++] = s;
				}
				
				m.put("like_cnt", like);
				m.put("dislike_cnt", dislike);
				result.add(m);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Map<String, String[]>>>(result, status);
	}
	
	@ApiOperation(value = "큐레이터 아이디로 기사리스트 받아오기", notes = "기사 리스트 반환", response = List.class)
	@GetMapping(value = "/article/newsById")
	public ResponseEntity<List<Map<String, String[]>>> getNewsById(
			@ApiParam(value = "String", required = true) @RequestParam String id) {
		HttpStatus status = null;
		List<News> list = new ArrayList<News>();
		List<UserScrapNews> scrap = new ArrayList<UserScrapNews>();
		List<Map<String, String[]>> result = new ArrayList<Map<String,String[]>>();
		try {
			String userNo = service.selectUserById(id).getUserNo();
			list = service.selectNewsByUserNo(userNo);
			scrap = service.selectUserScrapNewsByUserNo(userNo);
			
			for (News news : list) {
				Map<String, String[]> m = new HashMap<String, String[]>();
				m.put("newsNo", new String[] {news.getNewsNo()});
				m.put("title", new String[] {news.getTitle()});
				m.put("subtitle", new String[] {news.getSubtitle()});
				m.put("content", new String[] {news.getContent()});
				m.put("image_path", new String[] {news.getImage_path()});
				m.put("url", new String[] {news.getUrl()});
				m.put("article_date", new String[] {news.getArticle_date()});
				m.put("article_bot_summary", new String[] {news.getArticle_bot_summary()});
				m.put("article_image_caption", new String[] {news.getArticle_image_caption()});
				m.put("company", new String[] {news.getCompany()});
				
				ArrayList<String> temp1 = new ArrayList<String>();
				ArrayList<String> temp2 = new ArrayList<String>();
				
				for (UserScrapNews ucn : scrap) {
					String newsNo = ucn.getNewsNo();
					if(news.getNewsNo().equals(newsNo)) {
						temp1.add(ucn.getLike_cnt());
						temp2.add(ucn.getDislike_cnt());
					}
				}
				
				String [] like = new String[temp1.size()];
				String [] dislike = new String[temp2.size()];
				int cnt = 0;
				for (String s : temp1) {
					like[cnt++] = s;
				}
				cnt = 0;
				for (String s : temp2) {
					dislike[cnt++] = s;
				}
				
				m.put("like_cnt", like);
				m.put("dislike_cnt", dislike);
				result.add(m);
			}
			
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Map<String, String[]>>>(result, status);
	}
	
	@ApiOperation(value = "큐레이터 이름으로 기사리스트 받아오기", notes = "기사 리스트 반환", response = List.class)
	@GetMapping(value = "/article/newsByName")
	public ResponseEntity<List<Map<String, String[]>>> getNewsByName(
			@ApiParam(value = "String", required = true) @RequestParam String name) {
		HttpStatus status = null;
		List<News> list = new ArrayList<News>();
		List<UserScrapNews> scrap = new ArrayList<UserScrapNews>();
		List<Map<String, String[]>> result = new ArrayList<Map<String,String[]>>();
		try {
			String userNo = service.selectUserByName(name).getUserNo();
			list = service.selectNewsByUserNo(userNo);
			scrap = service.selectUserScrapNewsByUserNo(userNo);
			
			for (News news : list) {
				Map<String, String[]> m = new HashMap<String, String[]>();
				m.put("newsNo", new String[] {news.getNewsNo()});
				m.put("title", new String[] {news.getTitle()});
				m.put("subtitle", new String[] {news.getSubtitle()});
				m.put("content", new String[] {news.getContent()});
				m.put("image_path", new String[] {news.getImage_path()});
				m.put("url", new String[] {news.getUrl()});
				m.put("article_date", new String[] {news.getArticle_date()});
				m.put("article_bot_summary", new String[] {news.getArticle_bot_summary()});
				m.put("article_image_caption", new String[] {news.getArticle_image_caption()});
				m.put("company", new String[] {news.getCompany()});
				
				ArrayList<String> temp1 = new ArrayList<String>();
				ArrayList<String> temp2 = new ArrayList<String>();
				
				for (UserScrapNews ucn : scrap) {
					String newsNo = ucn.getNewsNo();
					if(news.getNewsNo().equals(newsNo)) {
						temp1.add(ucn.getLike_cnt());
						temp2.add(ucn.getDislike_cnt());
					}
				}
				
				String [] like = new String[temp1.size()];
				String [] dislike = new String[temp2.size()];
				int cnt = 0;
				for (String s : temp1) {
					like[cnt++] = s;
				}
				cnt = 0;
				for (String s : temp2) {
					dislike[cnt++] = s;
				}
				
				m.put("like_cnt", like);
				m.put("dislike_cnt", dislike);
				result.add(m);
			}
			
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Map<String, String[]>>>(result, status);
	}
	
	@ApiOperation(value = "내가 구독한 큐레이터의 최신 스크랩 목록", notes = "내가 구독한 큐레이터의 최신 스크랩 목록을 반환", response = List.class)
	@GetMapping(value = "/article/curatorscrap")
	public ResponseEntity<List<Map<String, String>>> getCuratorScrap(
			@ApiParam(value = "String", required = true) @RequestParam String id) {
		HttpStatus status = null;
		List<Map<String, String>> newsList = new ArrayList<Map<String, String>>();
		try {
				String userNo = service.selectUserById(id).getUserNo();;
				List<UserScrapNews> list = service.selectUserScrapNews(userNo);
				for (int j = 0; j < list.size(); j++) {
					Map<String, String> map = new HashMap<String, String>();
					News temp = service.selectNews(list.get(j).getNewsNo());
					map.put("scrapNo", list.get(j).getScrapNo());
					map.put("curator_summary", list.get(j).getCurator_summary());
					map.put("title", temp.getTitle());
					map.put("newsNo", temp.getNewsNo());
					map.put("image_path", temp.getImage_path());
					map.put("article_bot_summary", temp.getArticle_bot_summary());
					map.put("article_date", temp.getArticle_date());
					map.put("news_image_caption", temp.getArticle_image_caption());
					map.put("company", temp.getCompany());
					map.put("content", temp.getContent());
					map.put("subtitle", temp.getSubtitle());
					map.put("url", temp.getUrl());
					newsList.add(map);
				}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Map<String, String>>>(newsList, status);
	}

	@ApiOperation(value = "좋아요 증가", notes = "결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@GetMapping(value = "/article/increalike")
	public ResponseEntity<Map<String, String>> increaseLike(
			@ApiParam(value = "String", required = true) @RequestParam String scrapNo) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			if (service.increaseLike(scrapNo) > 0) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "좋아요 감소", notes = "결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@GetMapping(value = "/article/decrealike")
	public ResponseEntity<Map<String, String>> decreaseLike(
			@ApiParam(value = "String", required = true) @RequestParam String scrapNo) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			if (service.decreaseLike(scrapNo) > 0) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "싫어요 증가", notes = "결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@GetMapping(value = "/article/increadislike")
	public ResponseEntity<Map<String, String>> increaseDisLike(
			@ApiParam(value = "String", required = true) @RequestParam String scrapNo) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			if (service.increaseDisLike(scrapNo) > 0) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "싫어요 감소", notes = "결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@GetMapping(value = "/article/decreadislike")
	public ResponseEntity<Map<String, String>> decreaseDisLike(
			@ApiParam(value = "String", required = true) @RequestParam String scrapNo) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			if (service.decreaseDisLike(scrapNo) > 0) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "유저가 좋아요 누른 목록 등록/해제", notes = "결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@GetMapping(value = "/article/like")
	public ResponseEntity<Map<String, String>> updateLike(
			@ApiParam(value = "String", required = true) @RequestParam String scrapNo,
			@ApiParam(value = "String", required = true) @RequestParam String id) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			String userNo = service.selectUserById(id).getUserNo();
			UserGoodNews userGoodNews = new UserGoodNews();
			userGoodNews.setScrapNo(scrapNo);
			userGoodNews.setUserNo(userNo);
			service.insertUserGoodNews(userGoodNews);
			if (service.updateLike(userGoodNews) > 0) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "유저가 싫어요 누른 목록 등록/해제", notes = "결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@GetMapping(value = "/article/dislike")
	public ResponseEntity<Map<String, String>> updateDisLike(
			@ApiParam(value = "String", required = true) @RequestParam String scrapNo,
			@ApiParam(value = "String", required = true) @RequestParam String id) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			String userNo = service.selectUserById(id).getUserNo();
			UserGoodNews userGoodNews = new UserGoodNews();
			userGoodNews.setScrapNo(scrapNo);
			userGoodNews.setUserNo(userNo);
			service.insertUserGoodNews(userGoodNews);
			if (service.updateDisLike(userGoodNews) > 0) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "유저가 저장 누른 목록 등록/해제", notes = "결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@GetMapping(value = "/article/save")
	public ResponseEntity<Map<String, String>> updateSave(
			@ApiParam(value = "String", required = true) @RequestParam String scrapNo,
			@ApiParam(value = "String", required = true) @RequestParam String id) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			String userNo = service.selectUserById(id).getUserNo();
			UserGoodNews userGoodNews = new UserGoodNews();
			userGoodNews.setScrapNo(scrapNo);
			userGoodNews.setUserNo(userNo);
			service.insertUserGoodNews(userGoodNews);
			if (service.updateSave(userGoodNews) > 0) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "유저가 좋아요 눌럿는지 여부 파악", notes = "결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@GetMapping(value = "/article/islike")
	public ResponseEntity<Map<String, String>> isLike(
			@ApiParam(value = "String", required = true) @RequestParam String scrapNo,
			@ApiParam(value = "String", required = true) @RequestParam String id) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			String userNo = service.selectUserById(id).getUserNo();
			UserGoodNews userGoodNews = new UserGoodNews();
			userGoodNews.setScrapNo(scrapNo);
			userGoodNews.setUserNo(userNo);
			service.insertUserGoodNews(userGoodNews);
			userGoodNews = service.selectLikeNews(userGoodNews);
			if (userGoodNews.getIs_like().equals("1")) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "유저가 싫어요 눌럿는지 여부 파악", notes = "결과 'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@GetMapping(value = "/article/isdislike")
	public ResponseEntity<Map<String, String>> isDisLike(
			@ApiParam(value = "String", required = true) @RequestParam String scrapNo,
			@ApiParam(value = "String", required = true) @RequestParam String id) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			String userNo = service.selectUserById(id).getUserNo();
			UserGoodNews userGoodNews = new UserGoodNews();
			userGoodNews.setScrapNo(scrapNo);
			userGoodNews.setUserNo(userNo);
			service.insertUserGoodNews(userGoodNews);
			userGoodNews = service.selectLikeNews(userGoodNews);
			if (userGoodNews.getIs_dislike().equals("1")) {
				map.put("message", SUCCESS);
			} else {
				map.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	
	@ApiOperation(value = "저장한 기사리스트", notes = "저장한 기사 리스트를 반환", response = List.class)
	@GetMapping(value = "/article/savelist")
	public ResponseEntity<List<Map<String, String>>> saveList(
			@ApiParam(value = "String", required = true) @RequestParam String id) {
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		HttpStatus status = null;
		try {
			String userNo = service.selectUserById(id).getUserNo();
			
			List<UserGoodNews> list = service.selectUserGoodNewsByUserNo(userNo);
			for (UserGoodNews news : list) {
				Map<String, String> temp = new HashMap<String, String>();
				if(news.getIs_save().equals("1")) {
					String scrapNo = news.getScrapNo();
					News n = service.selectNewsByScrapNo(scrapNo);
					temp.put("newsNo", n.getNewsNo());
					temp.put("title", n.getTitle());
					temp.put("subtitle", n.getSubtitle());
					temp.put("content", n.getContent());
					temp.put("image_path", n.getImage_path());
					temp.put("url", n.getUrl());
					temp.put("article_date", n.getArticle_date());
					temp.put("article_bot_summary", n.getArticle_bot_summary());
					temp.put("article_image_caption", n.getArticle_image_caption());
					temp.put("company", n.getCompany());
					temp.put("scrapNo", scrapNo);
					temp.put("curator_summary",service.selectUserScrapNewsByScrapNo(scrapNo).getCurator_summary());
					result.add(temp);
				}
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Map<String, String>>>(result, status);
	}
	
	@ApiOperation(value = "최신 기사리스트(비로그인)", notes = "최신 기사 리스트와 해당 기사의 scrap 배열로 반환", response = List.class)
	@GetMapping(value = "/article")
	public ResponseEntity<List<Map<String, String>>> saveList() {
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		HttpStatus status = null;
		try {
			
			List<News> list = service.selectAllNews();
			for (News news : list) {
				Map<String, String> temp = new HashMap<String, String>();
				temp.put("newsNo", news.getNewsNo());
				temp.put("title", news.getTitle());
				temp.put("company", news.getCompany());
				temp.put("image_path", news.getImage_path());
				temp.put("article_bot_summary", news.getArticle_bot_summary());
				temp.put("content", news.getContent());
				
				List<UserScrapNews> scrapList = service.selectUserScrapNewsByNewsNo(news.getNewsNo());
				if(scrapList.size() > 0) {
					String scrapNo = scrapList.get(0).getScrapNo();
					String userNo = scrapList.get(0).getUserNo();
					String postNo = scrapList.get(0).getPostNo();
					String newsNo = scrapList.get(0).getNewsNo();
					String date = scrapList.get(0).getDate();
					String curator_summary = scrapList.get(0).getCurator_summary();
					String name = service.selectUserByUserNo(userNo).getName();
					
					temp.put("name", name);
					temp.put("scrapNo", scrapNo);
					temp.put("userNo", userNo);
					temp.put("postNo", postNo);
					temp.put("newsNo", newsNo);
					temp.put("date", date);
					temp.put("curator_summary", curator_summary);
				}
				result.add(temp);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Map<String, String>>>(result, status);
	}
	
	@ApiOperation(value = "newsNo로 news 가져오기", notes = "하나의 news 반환", response = List.class)
	@GetMapping(value = "/article/newsByNewsNo")
	public ResponseEntity<News> getNewsByNewsNo(@ApiParam(value = "String", required = true) @RequestParam String newsNo) {
		HttpStatus status = null;
		News news = new News();
		try {
			news = service.selectNews(newsNo);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<News>(news, status);
	}
}
