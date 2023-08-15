package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	
	private Connection conn;
	private PreparedStatement pstmt; // SQL injection과 같은 해킹 기법을 방어하기 위해 PreparedStatement를 사용함.
	private ResultSet rs;
	
	//mysql에 접근하게 해주는 것
	public BbsDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS";
			String dbID = "root";
			String dbPassword = "ekagmlmm32";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	public String getDate() {
		String SQL = "SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문을 실행 준비 단계로 만듦.
			rs = pstmt.executeQuery(); // 실행 결과를 가져옴
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; // DB 오류
	}
	
	public int getNext() {
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문을 실행 준비 단계로 만듦.
			rs = pstmt.executeQuery(); // 실행 결과를 가져옴
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 첫번째 게시물인 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류 (게시글 숫자로서 적절하지 않은 숫자 -1)
	}
	
	public int write(String bbsTitle, String userID, String bbsContent) {
		String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문을 실행 준비 단계로 만듦.
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1); // bbsAvailable에서 삭제가 안 된 글이니 값 == 1
			return pstmt.executeUpdate(); // 양수의 값 반환
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류 (게시글 숫자로서 적절하지 않은 숫자 -1)
	}
	
	public ArrayList<Bbs> getList(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문을 실행 준비 단계로 만듦.
			pstmt.setInt(1,  getNext() - (pageNumber - 1) * 10); 
			rs = pstmt.executeQuery(); // 실행 결과를 가져옴
			while (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문을 실행 준비 단계로 만듦.
			pstmt.setInt(1,  getNext() - (pageNumber - 1) * 10); 
			rs = pstmt.executeQuery(); // 실행 결과를 가져옴
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Bbs getBbs(int bbsID) {
		String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문을 실행 준비 단계로 만듦.
			pstmt.setInt(1,  bbsID); 
			rs = pstmt.executeQuery(); // 실행 결과를 가져옴
			if (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문을 실행 준비 단계로 만듦.
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate(); // 양수의 값 반환
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류 (게시글 숫자로서 적절하지 않은 숫자 -1)
	}

	public int delete(int bbsID) {
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); // SQL문을 실행 준비 단계로 만듦.
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate(); // 양수의 값 반환
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
}