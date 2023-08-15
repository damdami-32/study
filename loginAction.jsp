<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.User" scope="page" />
<jsp:setProperty name="user" property="userID"/>
<jsp:setProperty name="user" property="userPassword"/>
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
		UserDAO userDAO = new UserDAO();
		int result = userDAO.login(user.getUserID(), user.getUserPassword());
		if (result == 1) {
			session.setAttribute("userID", user.getUserID());
			PrintWriter script = response.getWriter(); //script 문장을 넣을 수 있게
			script.println("<script>");
			script.println("location.href = 'main.jsp'");
			script.println("</script>");
		} 		
		else if (result == 0) { // 비번 틀림
			PrintWriter script = response.getWriter(); //script 문장을 넣을 수 있게
			script.println("<script>");
			script.println("alert('비밀번호가 틀립니다.')");
			script.println("history.back()"); //이전 페이지로 사용자를 돌려보냄 
			script.println("</script>");
		}
		else if (result == -1) { // ID 미존재
			PrintWriter script = response.getWriter(); //script 문장을 넣을 수 있게
			script.println("<script>");
			script.println("alert('존재하지 않는 아이디입니다.')");
			script.println("history.back()"); //이전 페이지로 사용자를 돌려보냄 
			script.println("</script>");
		}
		else if (result == -2) {
			PrintWriter script = response.getWriter(); //script 문장을 넣을 수 있게
			script.println("<script>");
			script.println("alert('데이터베이스 오류가 발생했습니다.')");
			script.println("history.back()"); //이전 페이지로 사용자를 돌려보냄 
			script.println("</script>");
		}
	%>
</body>
</html>