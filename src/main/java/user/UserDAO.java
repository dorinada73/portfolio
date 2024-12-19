package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public UserDAO() {
		try {
			// DB 연결처리
			String dbURL = "jdbc:mysql://localhost:3306/BBS";
			String dbID = "root";
			String dbPassword = "woo7375";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword, Password(?) enc_Password FROM USER WHERE userID = ?";

		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userPassword);
			pstmt.setString(2, userID);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).contentEquals(rs.getString(2)))
					return 1; // 인증성공
				else
					return 0; // 패스워드불일치
			}
			return -1; // 사용자가존재하지않음
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -2; // DB오류
	}

	public int join(User user) {
		String SQL = "INSERT INTO USER (userID, userPassword, userName, userGender, userEmail ) VALUES (?, Password(?), ?, ?, ?)";

		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB등록오류
	}

}
