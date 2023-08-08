<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.User" scope="page" />
<jsp:setProperty name="user" property="userID"/>
<jsp:setProperty name="user" property="userPassword"/>
<jsp:setProperty name="user" property="userName"/>
<jsp:setProperty name="user" property="userGender"/>
<jsp:setProperty name="user" property="userEmail"/>
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
		if (userID != null){
			PrintWriter script = response.getWriter(); 
			script.println("<script>");
			script.println("alert('이미 로그인이 되어있습니다.')");
			script.println("location.href = 'main.jsp'"); 
			script.println("</script>");
		}
		if (user.getUserID() == null || user.getUserPassword() == null || user.getUserName() == null
		|| user.getUserGender() == null || user.getUserEmail() == null) {
			PrintWriter script = response.getWriter(); //script 문장을 넣을 수 있게
			script.println("<script>");
			script.println("alert('입력이 안 된 사항이 있습니다..')");
			script.println("history.back()"); //이전 페이지로 사용자를 돌려보냄 
			script.println("</script>");
		}
		else {
			UserDAO userDAO = new UserDAO(); // DB 접근 객체
			int result = userDAO.join(user);
			if (result == -1) {
				PrintWriter script = response.getWriter(); //script 문장을 넣을 수 있게
				script.println("<script>");
				script.println("alert('이미 존재하는 아이디입니다.')");
				script.println("history.back()"); //이전 페이지로 사용자를 돌려보냄 
				script.println("</script>");
			} 		
			else { 
				session.setAttribute("userID", user.getUserID());
				PrintWriter script = response.getWriter(); //script 문장을 넣을 수 있게
				script.println("<script>");
				script.println("location.href = 'main.jsp'"); 
				script.println("</script>");
			}
		}

	%>
</body>
</html>