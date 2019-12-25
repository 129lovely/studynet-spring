﻿package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import common.Common;
import common.Paging;
import common.Paging_study;
import dao.BoardDAO;
import dao.StudyDAO;
import service.BoardService;
import service.StudyService;
import service.UserService;
import vo.BoardVO;
import vo.StudyMemberVO;
import vo.StudyVO;
import vo.UserVO;

@Controller
public class StudyController {

	StudyService studyService;
	UserService userService;
	BoardService boardService;
	BoardDAO boardDAO;
	StudyDAO studyDAO;

	public void setBoardDAO(BoardDAO boardDAO) {
		this.boardDAO = boardDAO;
	}

	public void setStudyService(StudyService studyService) {
		this.studyService = studyService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}

	public void setStudyDAO(StudyDAO studyDAO) {
		this.studyDAO = studyDAO;
	}

	// 마이페이지 스터디룸 - 1 
	// 유저가 참여 중인 스터디의 idx를 모두 가져온 뒤 해당하는 idx의 스터디 정보와 유저의 참여 상태를 가져와야 함
	// 일단은 이동만 ^^...
	@RequestMapping("/study_myinfo.do")
	public String user_study_list(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		UserVO user = (UserVO) session.getAttribute("user");
		
		StudyMemberVO member=studyService.studyMemStatus(user.getIdx());
		List<StudyVO> list=(ArrayList<StudyVO>)studyService.studyMemList(member.getStudy_idx());
		
		model.addAttribute("member", member);
		model.addAttribute("user", user);
		model.addAttribute("list", list);
		
		return Common.Study.VIEW_PATH + "study_myinfo.jsp";
	}

	// 마이페이지 회원 정보
	@RequestMapping("/user_myinfo.do")
	public String user_myinfo () {
		return Common.User.VIEW_PATH + "user_myinfo.jsp";
	}
	
	// 스터디 만들기 페이지 - 1 ( 생성 안내 페이지로 이동 )
	@RequestMapping("/study_create_caution.do")
	public String study_create_caution ( ) {

		return Common.Study.VIEW_PATH + "study_create_caution.jsp";
	}

	// 스터디 만들기 페이지 - 2 ( 생성 폼 페이지로 이동 )
	@RequestMapping("/study_create_form.do")
	public String create_form ( ) {

		return Common.Study.VIEW_PATH + "study_create.jsp";
	}

	// 스터디 만들기 페이지 - 3 ( vo 정보 DB로 전송 )
	@RequestMapping("/study_insert.do")
	public String study_insert ( StudyVO vo, HttpServletRequest request) {


		ServletContext application = request.getServletContext();

		// 대표 사진은 따로 저장해주기 위해 절대 경로를 만든다.
		String webPath = "/resources/images/study_profile";
		String savePath = application.getRealPath(webPath);
		System.out.println(savePath);

		MultipartFile photo_file = vo.getPhoto_file();
		String photo = "no_photo";

		if ( ! photo_file.isEmpty() ) {

			// 실제 파일명으로 변경 
			photo = photo_file.getOriginalFilename();

			File saveFile = new File(savePath, photo);

			if ( ! saveFile.exists() ) {	// 저장할 경로가 존재하지 않는다면 새로 생성
				saveFile.mkdirs();

			} else {	// 동일 파일명 처리
				long time = System.currentTimeMillis();
				photo = String.format("%d_%s", time, photo);
				saveFile = new File(savePath, photo);
			}

			try {	// 로컬 파일로 복사 
				photo_file.transferTo(saveFile);
			} catch (Exception e) {
				e.printStackTrace();
			} 


		} else {
			// 지정된 파일이 없을 경우 샘플에서 가져온다. 
			if( vo.getPurpose().equals("공모전") ) {
				photo = "preview01.jpg";
			} else if( vo.getPurpose().equals("취업준비") ) {
				photo = "preview02.jpg";
			} else if( vo.getPurpose().equals("기상/습관") ) {
				photo = "preview03.jpg";
			} else {
				photo = "preview04.jpg";
			}
		}

		vo.setPhoto(photo);

		int res = studyService.insert( vo );	

		return "redirect:study_list.do";
	}

	// 스터디 찾기 페이지 목록
	@RequestMapping("/study_list.do")
	public String study_list(Model model, Integer page, HttpServletRequest request ) {
		int nowPage = 1;

		if( page != null ) {
			nowPage = page; // ~.do?page=3 처럼 입력할 경우
		}

		//		System.out.println(nowPage + ": now page");
		//		System.out.println(page + ": page");

		//한페이지에서 표시되는 게시물의 시작과 끝번호를 계산
		//1페이지라면 1 ~ 5번 게시물까지만 보여줘야 한다.
		//2페이지라면 6 ~ 10번 게시물까지만 보여줘야한다.
		int start = (nowPage -1) * Common.StudyPaging.BLOCKLIST + 1;
		int end = start + Common.StudyPaging.BLOCKLIST - 1;

		//start와 end를 map에 저장
		Map map = new HashMap();
		map.put("start", start);
		map.put("end", end);

		//게시글 전체목록 가져오기
		List<StudyVO> list = null;
		list = studyDAO.selectList( map );	

		//전체 게시물 수 구하기
		int row_total = studyDAO.getRowTotal();

		//페이지 메뉴 생성하기
		//ㄴ ◀1 2 3 4 5▶
		String pageMenu = Paging.getPaging(
				"study_list.do", nowPage, row_total, Common.StudyPaging.BLOCKLIST, 
				Common.StudyPaging.BLOCKPAGE, null);

		//request영역에 list바인딩
		model.addAttribute("list", list);
		model.addAttribute("pageMenu", pageMenu);
		model.addAttribute("row_total", row_total);

		//세션에 등록되어 있던 show정보를 없앤다
		request.getSession().removeAttribute("show");

		return Common.Study.VIEW_PATH + "study_list.jsp";
	}


	// 스터디 찾기에서 검색기능 and 검색결과 레코드 개수 (페이징 적용)
	@RequestMapping("/study_list_search.do")
	public String study_list_search( Model model, Integer page, HttpServletRequest request, 
			String search, int search_option, String[] purpose ) {
		int nowPage = 1;

		if( page != null ) {
			nowPage = page; // ~.do?page=3 처럼 입력할 경우
		}

		//한페이지에서 표시되는 게시물의 시작과 끝번호를 계산
		//1페이지라면 1 ~ 10번 게시물까지만 보여줘야 한다.
		//2페이지라면 11 ~ 20번 게시물까지만 보여줘야한다.
		int start = (nowPage -1) * Common.StudyPaging.BLOCKLIST + 1;
		int end = start + Common.StudyPaging.BLOCKLIST - 1;

		//start와 end를 map에 저장
		Map map = new HashMap();
		map.put("start", start);
		map.put("end", end);
		
		List<StudyVO> list = null; // 전체 리스트 데이터 저장
		int row_total = 0; // 전체 리스트 갯수 저장
		
		map.put("search_option", search_option);
		map.put("search", search);
		map.put("array", purpose);

		//게시글 전체목록 가져오기
		list = (List<StudyVO>) studyService.search_list_condition(map).get("list");
		//전체 게시물 수 구하기
		row_total = (int) studyService.search_list_condition(map).get("cnt");

		//페이지 메뉴 생성하기
		//ㄴ ◀1 2 3 4 5▶
		String pageMenu = Paging_study.getPaging(
				"study_list_search.do" , nowPage, row_total,
				Common.StudyPaging.BLOCKLIST, Common.StudyPaging.BLOCKPAGE,
				search, purpose, search_option);

		//request영역에 list바인딩
		model.addAttribute("list", list);

		model.addAttribute("pageMenu", pageMenu);
		model.addAttribute("row_total", row_total);

		return Common.Study.VIEW_PATH + "study_list.jsp";

	}

	//스터디 상세 페이지
	@RequestMapping("/study_list_detail.do")
	public String study_list_detail(Model model, int idx,HttpServletRequest request) {
		StudyVO study=studyService.showStudyDetail(idx);
		UserVO user=userService.select_userName(idx);

		model.addAttribute("study",study);
		model.addAttribute("user", user);
		return Common.Study.VIEW_PATH + "study_list_detail.jsp";
	}

	//스터디 참가 신청 페이지 이동
	@RequestMapping("/study_apply_caution.do")
	public String study_apply_caution(Model model, int study_idx) {
		StudyVO vo = studyService.showStudyDetail(study_idx);
		model.addAttribute("vo", vo); // 데이터 바인딩
		return Common.Study.VIEW_PATH + "study_apply_caution.jsp";
	}

	// 스터디 수정하기 폼
	@RequestMapping("/study_create_modify_form.do")
	public String study_create_mod(Model model, int idx) {
		StudyVO study = studyService.showStudyDetail(idx);

		model.addAttribute("study", study);
		return Common.Study.VIEW_PATH + "study_create_modify.jsp";
	}

	// 스터디 수정하기
	@RequestMapping("/study_create_modify.do")
	public String study_create_modify(int idx) {
		int res = studyService.studyModify(idx);

		return "study_list_detail.do?idx=" + idx;
	}

	//스터디 지원
	@RequestMapping("/study_apply.do")
	public String study_apply(String introduce, int study_idx, HttpServletRequest request){

		UserVO user = (UserVO) request.getSession().getAttribute("user");
		Map map = new HashMap();
		map.put("user_idx", user.getIdx());
		map.put("study_idx", study_idx);
		map.put("introduce", introduce);

		int res = studyService.study_apply(map);
		return "redirect:study_list_detail.do?idx=" + study_idx; 

	}

	// 아이디 비번 찾기 페이지로 이동
	@RequestMapping("/user_find.do")
	public String user_find(HttpServletRequest request ) {

		return Common.User.VIEW_PATH + "user_find.jsp";
	}

	// 스터디 중복 체크
	@RequestMapping("/study_check.do")
	@ResponseBody
	public String study_check( int study_idx, int user_idx ){
		Map map = new HashMap();
		map.put("user_idx", user_idx);
		map.put("study_idx", study_idx);

		String resStr = "fail";
		
		StudyMemberVO vo = studyService.selectOne_member(map);
		if( vo != null ) {
			return resStr;
		}

		return resStr = "success";
	}

	// 스터디 룸 개별 페이지 
	@RequestMapping("/study_room_detail.do")
	public String study_room_detail () {
		return Common.Study.VIEW_PATH + "study_room.jsp";
	}
}
