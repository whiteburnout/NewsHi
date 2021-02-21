package com.newha.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.newha.service.BoardService;
import com.newha.service.JwtService;
import com.newha.service.S3Service;
import com.newha.service.UserService;
import com.newha.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

//http://localhost/swagger-ui.html

@Api("UserController V1")
@RestController
@CrossOrigin
public class UserController {

	@Autowired
	public JavaMailSender javaMailSender;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService service;

	@Autowired
	private BoardService boardservice;

	@Autowired
	private S3Service s3service;

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@ApiOperation(value = "이메일 중복검사", notes = "'success'는 사용 가능한 아이디, 'fail'은 이미 사용중인 아이디", response = Map.class)
	@GetMapping(value = "/idcheck") // 이메일체크
	public ResponseEntity<Map<String, String>> selectid(
			@ApiParam(value = "id", required = true) @RequestParam String id) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			int result = service.selectId(id);
			if (result == 0) {
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

	@ApiOperation(value = "name 중복검사", notes = "'success'는 사용 가능한 name, 'fail'은 이미 사용중인 name", response = Map.class)
	@GetMapping(value = "/namecheck") // 이메일체크
	public ResponseEntity<Map<String, String>> selectname(
			@ApiParam(value = "name", required = true) @RequestParam String name) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			int result = service.selectName(name);
			if (result == 0) {
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

	@ApiOperation(value = "이메일 인증", notes = "입력값으로 id(email) 주면 이메일 발송. 리턴값은 confirm: 인증번호 ", response = Map.class)
	@GetMapping(value = "/emailauth") // 이메일 인증
	public ResponseEntity<Map<String, Integer>> emailAuth(
			@ApiParam(value = "String", required = true) @RequestParam String id) throws MessagingException {
		int confirm = (int) ((Math.random() * (9999 - 1000)) + 1000);
		// System.out.println(confirm);
		HttpStatus status = null;
		Map<String, Integer> map = new HashMap<>();
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			message.setSubject("뉴하 이메일 인증입니다.");
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(id));
			message.setText("인증번호: " + confirm);
			message.setSentDate(new Date());
			javaMailSender.send(message);
			map.put("confirm", confirm);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Integer>>(map, status);
	}

	@ApiOperation(value = "내가 구독한 큐레이터", notes = "아이디 입력하면 구독한 큐레이터 thumbnail_path, name 리턴", response = ArrayList.class)
	@GetMapping(value = "/subscribe") // 내가 구독한 큐레이터
	public ResponseEntity<ArrayList<Map<String, String>>> subscribe(
			@ApiParam(value = "String", required = true) @RequestParam String id) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int userNo = service.userNo(id);
		HttpStatus status = null;
		try {
			List<Integer> l = service.follow(userNo);
			for (int i = 0; i < l.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				User user = service.selectUser(l.get(i));
				map.put("id", user.getId());
				map.put("name", user.getName());
				map.put("thumbnail_path", user.getThumbnail_path());
				list.add(map);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ArrayList<Map<String, String>>>(list, status);
	}

	@ApiOperation(value = "내 정보 태그 리스트", notes = "태그리스트", response = ArrayList.class)
	@GetMapping(value = "/tagList") // 내가 구독한 큐레이터
	public ResponseEntity<ArrayList<Map<String, String>>> tagList(
			@ApiParam(value = "String", required = true) @RequestParam String id) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int userNo = service.userNo(id);
		HttpStatus status = null;
		try {
			List<String> l = service.tagList(userNo);
			for (int i = 0; i < l.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", l.get(i));
				list.add(map);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ArrayList<Map<String, String>>>(list, status);
	}

	@ApiOperation(value = "내 정보 태그 리스트 수정", notes = "태그리스트", response = ArrayList.class)
	@GetMapping(value = "/tagListUpdate") // 내가 구독한 큐레이터
	public ResponseEntity<ArrayList<Map<String, String>>> tagListUpdate(
			@ApiParam(value = "String", required = true) @RequestParam String id,
			@ApiParam(value = "List<Map<String,String>>", required = true) @RequestParam List<String> list) {
		ArrayList<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
		HttpStatus status = null;
		try {
			int userNo = service.userNo(id);
			service.tagDelete(userNo);
			for (int i = 0; i < list.size(); i++) {
				service.insertTag(id, list.get(i));
			}
			List<String> l = service.tagList(userNo);
			for (int i = 0; i < l.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", l.get(i));
				list2.add(map);
			}
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ArrayList<Map<String, String>>>(list2, status);
	}

	@ApiOperation(value = "큐레이터 구독", notes = "큐레이터 구독 결과'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@PostMapping(value = "/subsc")
	public ResponseEntity<Map<String, String>> insert(
			@ApiParam(value = "String", required = true) @RequestParam String id,
			@ApiParam(value = "String", required = true) @RequestParam String id2) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			service.subscribe(id, id2);
			map.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "큐레이터 구독 취소", notes = "큐레이터 구독 취소 결과'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@PostMapping(value = "/subscdelete")
	public ResponseEntity<Map<String, String>> curatordelete(
			@ApiParam(value = "String", required = true) @RequestParam String id,
			@ApiParam(value = "String", required = true) @RequestParam String id2) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			service.subscdelete(id, id2);
			map.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "회원가입", notes = "회원가입 성공 결과'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@PostMapping(value = "/join")
	public ResponseEntity<Map<String, String>> insert(@RequestBody List<Map<String, Object>> list) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		User u = new User();
		
		System.out.println(list.get(0).toString());
		try {
			u.setId((String) list.get(0).get("id"));
			u.setName((String) list.get(0).get("name"));
			u.setPassword((String) list.get(0).get("password"));
			u.setPlatformType((String) list.get(0).get("platformType"));
			u.setThumbnail_path((String) list.get(0).get("thumbnail_path"));
			u.setUserNo(null);

			service.insert(u);

			String tag[] = ((String) list.get(1).get("tag")).split("#");
			for (int j = 1; j < tag.length; j++) {
				service.insertTag(u.getId(), tag[j]);
			}
			boardservice.boardCreate(u.getId()); // 회원가입과 동시에 개인 게시판 생성
			map.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "회원 탈퇴", notes = "탈퇴 결과'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@DeleteMapping(value = "/delete")
	public ResponseEntity<Map<String, String>> delete(
			@ApiParam(value = "id", required = true) @RequestParam String id) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			service.delete(id);
			map.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "회원 이름 수정", notes = "수정 결과'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@PutMapping(value = "/updateName")
	public ResponseEntity<Map<String, String>> updateName(
			@ApiParam(value = "String", required = true) @RequestParam String id,
			@ApiParam(value = "String", required = true) @RequestParam String name) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			service.updateName(id, name);
			map.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "회원 비밀번호 수정", notes = "수정 결과'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@PutMapping(value = "/updatePassword")
	public ResponseEntity<Map<String, String>> updatePassword(
			@ApiParam(value = "String", required = true) @RequestParam String id,
			@ApiParam(value = "String", required = true) @RequestParam String oldpassword,
			@ApiParam(value = "String", required = true) @RequestParam String newpassword) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			service.updatePassword(id, oldpassword, newpassword);
			map.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}
	
	@ApiOperation(value = "회원 비밀번호 수정", notes = "수정 결과'success' 또는 'fail' 문자열을 리턴", response = Map.class)
	@PutMapping(value = "/changePassword")
	public ResponseEntity<Map<String, String>> changePassword(
			@ApiParam(value = "String", required = true) @RequestParam String id, 
			@ApiParam(value = "String", required = true) @RequestParam String password
			) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			service.changePassword(id, password);
			map.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}
	
	@ApiOperation(value = "파일 업로드", notes = "'SUCCESS' 또는 'FAIL' 문자열을 리턴", response = Map.class)
	@PostMapping("/upload")
	public ResponseEntity<Map<String, String>> upload(@RequestParam String id,
			@RequestParam String thumbnail_path
			) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		String userNo = String.valueOf(service.userNo(id));

		try {
			service.thumbnailPath(userNo, thumbnail_path);
			status = HttpStatus.ACCEPTED;
			map.put("message", SUCCESS);
		} catch (Exception e) {
			map.put("message", FAIL);
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	// 토근 유효여부 검사
	@ApiOperation(value = "토큰유효성검사", notes = "'success' 또는 'fail', httpstatus, userInfo 리턴", response = Map.class)
	@GetMapping(value = "/user")
	public ResponseEntity<Map<String, Object>> getInfo(@ApiParam(value = "id", required = true) @RequestParam String id,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		System.out.println(id);
		if (jwtService.isUsable(request.getHeader("access-token"))) {
			logger.info("사용 가능한 토큰!!!");
			try {
				// 로그인 사용자 정보.
				User user = service.userInfo(id);
				user.setPassword("");
				resultMap.put("userInfo", user);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			} catch (Exception e) {
				logger.error("정보조회 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			logger.error("사용 불가능 토큰!!!");
			resultMap.put("message", "FAIL");
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	// 로그인
	@ApiOperation(value = "로그인", notes = "'success' 또는 'fail', token 리턴", response = Map.class)
	@PostMapping(value = "/user/login")
	public ResponseEntity<Map<String, Object>> login(@ApiParam(value = "id", required = true) @RequestBody User user) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			User loginUser = service.login(user);
			if (loginUser != null) {
				String token = jwtService.create("id", loginUser.getId(), "access-token");// key, data, subject
				logger.debug("로그인 토큰정보 : {}", token);
				resultMap.put("access-token", token);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			} else {
				resultMap.put("message", FAIL);
				status = HttpStatus.ACCEPTED;
			}
		} catch (Exception e) {
			logger.error("로그인 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	// 소셜로그인
	@ApiOperation(value = "소셜로그인", notes = "'success' 또는 'fail', token 리턴", response = Map.class)
	@PostMapping(value = "/user/socialLogin")
	public ResponseEntity<Map<String, Object>> socialLogin(
			@ApiParam(value = "id", required = true) @RequestBody User user) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			int a = service.selectId(user.getId());
			if (a == 0) {
				service.socialInsert(user);
				String tag[] = {"속보", "정치", "경제", "사회", "생활/문화", "세계/국제", "IT/과학", "오피니언"};
				for (int j = 0; j < tag.length; j++) {
					service.insertTag(user.getId(), tag[j]);
				}
				boardservice.boardCreate(user.getId()); // 회원가입과 동시에 개인 게시판 생성
			}
			System.out.println(a);
			User loginUser = service.socialLogin(user);
			if (loginUser != null) {
				String token = jwtService.create("id", loginUser.getId(), "access-token");// key, data, subject
				logger.debug("로그인 토큰정보 : {}", token);
				resultMap.put("access-token", token);
				resultMap.put("message", SUCCESS);
				System.out.println(token);
				status = HttpStatus.ACCEPTED;
			} else {
				resultMap.put("message", FAIL);
				status = HttpStatus.ACCEPTED;
			}

		} catch (Exception e) {
			logger.error("로그인 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	// 사람검색
	@ApiOperation(value = "유저 검색", notes = "유저 List 리턴", response = List.class)
	@GetMapping(value = "/search/people")
	public ResponseEntity<List<Map<String, String>>> searchUser(@ApiParam(value = "keyword", required = true) @RequestParam String keyword) {
		List<User> list = new ArrayList<User>();
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		HttpStatus status = null;
		try {
			 list = service.searchUser(keyword + "%");
			 for (User user : list) {
				Map<String, String> m = new HashMap<String, String>();
				m.put("userNo", user.getUserNo());
				m.put("name", user.getName());
				m.put("thumbnail_path", user.getThumbnail_path());
				m.put("id", user.getId());
				m.put("platformType", user.getPlatformType());
				result.add(m);
			}
			 status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<List<Map<String, String>>>(result, status);
	}

	@ApiOperation(value = "유저 정보 by id", notes = "name, thumbnail_path 반환", response = Map.class)
	@GetMapping(value = "/sidebarUser")
	public ResponseEntity<Map<String, String>> sidebarUser(
			@ApiParam(value = "id", required = true) @RequestParam String id) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		System.out.println(id);
		try {
			User u = service.userInfo(id);
			map.put("name", u.getName());
			map.put("thumbnail_path", u.getThumbnail_path());
			map.put("platformtype", u.getPlatformType());
			map.put("userNo", u.getUserNo());
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			map.put("message", FAIL);
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}

	@ApiOperation(value = "유저 정보 by userNo", notes = "name, thumbnail_path 반환", response = Map.class)
	@GetMapping(value = "/userInfo")
	public ResponseEntity<Map<String, String>> userInfo(
			@ApiParam(value = "userNo", required = true) @RequestParam int userNo) {
		Map<String, String> map = new HashMap<>();
		HttpStatus status = null;
		try {
			User u = service.selectUser(userNo);
			map.put("name", u.getName());
			map.put("thumbnail_path", u.getThumbnail_path());
			map.put("platformtype", u.getPlatformType());
			map.put("id", u.getId());
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			map.put("message", FAIL);
		}
		return new ResponseEntity<Map<String, String>>(map, status);
	}
	
	@ApiOperation(value = "큐레이터 추천", notes = "회원가입때 태그 기반으로 큐레이터를 추천", response = List.class)
	@GetMapping(value = "/userrecommend")
	public ResponseEntity<List<Map<String, String[]>>> recomendPeople(
			@ApiParam(value = "userNo", required = true) @RequestParam String id) {
		List<Map<String, String[]>> result = new ArrayList<Map<String,String[]>>();
		HttpStatus status = null;
		try {
			List<User> list = new ArrayList<User>();
			String userNo = Integer.toString(service.userNo(id));
			list = service.selectUserByTag(userNo);
			for (User user : list) {
				if(user.getId().equals(id))//자기 자신은 제외
					continue;
				ArrayList<Integer> follows = service.follow(Integer.parseInt(userNo));
				boolean flag = false;
				for (Integer follow : follows) {//이미 구독중인지 확인
					if(user.getUserNo().equals(Integer.toString(follow)))
						flag = true;
				}
				if(flag)//구독중이면 제외
					continue;
				
				Map<String, String[]> m = new HashMap<String, String[]>();
				m.put("userNo", new String[] {user.getUserNo()});
				m.put("name", new String[] {user.getName()});
				m.put("thumnail_path", new String[] {user.getThumbnail_path()});
				m.put("id", new String[] {user.getId()});
				m.put("platformType", new String[] {user.getPlatformType()});
				
				List<String> temp = service.selectJoinTagByuserNo(user.getUserNo());
				String tags [] = new String[temp.size()];
				int cnt = 0;
				for (String s : temp) {
					tags[cnt++] = s;
				}
				m.put("tag", tags);
				result.add(m);
			}
			
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Map<String, String[]>>>(result, status);
	}
}