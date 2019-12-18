<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
    
    <link rel="stylesheet" href="https://unpkg.com/swiper/css/swiper.min.css">
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.css">
	<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.8/summernote.css">
    <link type="text/css" rel="stylesheet" href="${ pageContext.request.contextPath }/resources/css/reset.css">
    <link type="text/css" rel="stylesheet" href="${ pageContext.request.contextPath }/resources/css/common.css?ver=2">
    
    <script src="https://unpkg.com/swiper/js/swiper.min.js"></script>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.js"></script>
	<script src="http://netdna.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.js"></script>
	
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/resources/js/summernote.js"></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/resources/js/summernote-ko-KR.js"></script>
    <script type="text/javascript" src="${ pageContext.request.contextPath }/resources/js/sample.js"></script>
    
    <!-- 로그인 여부에 따라 헤더 메뉴 변경 -->
    <script type="text/javascript">
    	window.onload = function () {

    		if ( "${ !empty sessionScope.user }" ) {
    			document.getElementById("member").style.display = "flex";
    			document.getElementById("not_member").style.display = "none";
    		}
    		
    		if ( "${ empty sessionScope.user }" ) {
    			document.getElementById("member").style.display = "none";
    			document.getElementById("not_member").style.display = "flex";
    		}
    	}
    	
    </script>
</head>
<body>
	<!-- 헤더 메뉴 -->
    <header class="header-box fixed-full">
    	<div class="user-menu">
        	<div class="inner-box">
	            <nav>
					<ul class="flex-box" id="not_member">
						<li><a href="user_login_form.do"><span>로그인</span></a></li>
						<li><a href="user_join_caution.do"><span>회원가입</span></a></li>
					</ul>	
					<ul class="flex-box" id="member">
						<li><a href="user_logout.do"><span>로그아웃</span></a></li>
						<li><a href="user_myinfo.do"><span>마이페이지</span></a></li>
					</ul>
	            </nav>
        	</div>
		</div>
		<!-- 일반 브라우저 메뉴 -->
        <div class="main-menu">
            <div class="inner-box flex-box">
            	<h1><a href="index.do"><span class="icon icon-logo"></a></span></h1>
	        	<nav>
	        		<ul class="flex-box">
	        			<li><a href="community_list.do"><span>커뮤니티</span></a></li>
						<li><a href="study_list.html"><span>스터디 찾기</span></a></li>
						<li><a href="study_create_form.do"><span>스터디 만들기</span></a></li>
	        		</ul>
	        	</nav>
	        </div>
		</div>
		<!-- 모바일 메뉴 -->
		<!-- <div class="mobile-main-menu">
            <div class="inner-box flex-box">
					<h1><span class="icon icon-logo"></span></h1>
					<span class="icon icon-mobile-menu"></span>
			</div>
		</div>
		<div class="mobile-main-menu-dropbox">
			<div class="inner-box">
				<nav>
					<ul class="flex-box">
						<li><a href="javascript:void(0);"><span>커뮤니티</span></a></li>
						<li><a href="javascript:void(0);"><span>스터디 찾기</span></a></li>
						<li><a href="javascript:void(0);"><span>스터디 만들기</span></a></li>
					</ul>
				</nav>
			</div>
		</div> -->
	</header>
	
	<!-- 실시간 채팅 -->
	<!-- <div class="chat-box">
		<div class="inner-box contents-box flex-box">
				<a href="javascript:void(0);"><span class="icon icon-chat"></span></a>
		</div>
	</div> -->
</body>
</html>