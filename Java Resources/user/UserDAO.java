package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	
	private Connection conn;
	private PreparedStatement pstmt; // SQL injection과 같은 해킹 기법을 방어하기 위해 PreparedStatement를 사용함.
	private ResultSet rs;
	
	//mysql에 접근하게 해주는 것
	public UserDAO() {
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
	
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?";
		try { //pstmt = 정해진 SQL 문장을 DB에 삽입하는 방식으로 인스턴스를 가져옴.
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID); //
			rs = pstmt.executeQuery(); //객체(=rs)에 실행한 결과를 넣어줌.
			if (rs.next()) {
				if (rs.getString(1).equals(userPassword)) { //SQL 변수에 담긴 비번과 접속을 시도한 비번(=매개변수 userPassword)이 일치하면 로그인 성공
					return 1; // 로그인 성공
				}
				else
					return 0; // 비밀번호 불일치
			}
			return -1; // 아이디가 없음.
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2; //DB 오류
	}
	public int join(User user) {
		String SQL = "INSERT INTO USER VALUES (?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID()); // ?에 들어가는 값
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate(); // 성공할 때는 항상 양수의 값이 리턴
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; //DB 오류
	}
}
