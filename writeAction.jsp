<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bbs.BbsDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page" />
<jsp:setProperty name="bbs" property="bbsTitle"/>
<jsp:setProperty name="bbs" property="bbsContent"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
<title>JSP 게시판 웹 사이트</title>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
</head>
<body>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String) session.getAttribute("userID");
		}
		if (userID == null){
			PrintWriter script = response.getWriter(); 
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href = 'login.jsp'"); 
			script.println("</script>");
		} else {
			if (bbs.getBbsTitle() == null || bbs.getBbsContent() == null) {
						PrintWriter script = response.getWriter(); //script 문장을 넣을 수 있게
						script.println("<script>");
						script.println("alert('입력이 안 된 사항이 있습니다..')");
						script.println("history.back()"); //이전 페이지로 사용자를 돌려보냄 
						script.println("</script>");
					}
					else {
						BbsDAO bbsDAO = new BbsDAO(); // DB 접근 객체
						int result = bbsDAO.write(bbs.getBbsTitle(), userID, bbs.getBbsContent());
						if (result == -1) {
							PrintWriter script = response.getWriter(); //script 문장을 넣을 수 있게
							script.println("<script>");
							script.println("alert('글쓰기에 실패했습니다.')");
							script.println("history.back()"); //이전 페이지로 사용자를 돌려보냄 
							script.println("</script>");
						} 		
						else { 
							PrintWriter script = response.getWriter(); //script 문장을 넣을 수 있게
							script.println("<script>");
							script.println("location.href = 'bbs.jsp'"); 
							script.println("</script>");
						}
					}
		}

	%>
</body>
</html>