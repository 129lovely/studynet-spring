<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
</head>
<body>
	<jsp:include page="../header.jsp"></jsp:include>
    <div class="body-bgcolor-set">
        <div class="community-writing">
            <div class="inner-box pt190">
                <form class="contents-box board">
                    <h3 class="section-title blue">커뮤니티 글 작성</h3>
                    <p class="section-discription tal mb40">이용약관을 준수하여 올바른 커뮤니티 사용을 부탁드립니다.</p>
                    
                    <!--제목-->
                    <div class="line-bottom flex-box">
                        <label for="board-title">제목</label>
                        <input id="board-title" type="text" placeholder="제목을 입력해주세요." />
                    </div>
                    
                    <!--텍스트-->
                    <div class="note-my-custom">
                        <textarea class="summernote-community-writing-box"></textarea>	
                    </div>
                    
                    <!--버튼-->
                    <div class="btn-box tac">
                        <input class="my-btn yellow-black" type="button" value="올리기" />
                        <input class="my-btn yellow-black" type="button" value="취소" />
                    </div>
                </form>
            </div>
        </div>
    </div>
    <jsp:include page="../footer.jsp"></jsp:include>
</body>

</html>