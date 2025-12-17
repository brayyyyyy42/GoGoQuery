package models;

import java.sql.Date;

public class msuser {

	private Integer UserID;
	private Date UserDOB;
	private String UserEmail;
	private String UserPassword;
	private String UserRole;
	private String UserGender;
	public msuser(Integer userID, Date userDOB, String userEmail, String userPassword, String userRole,
			String userGender) {
		super();
		UserID = userID;
		UserDOB = userDOB;
		UserEmail = userEmail;
		UserPassword = userPassword;
		UserRole = userRole;
		UserGender = userGender;
	}
	public Integer getUserID() {
		return UserID;
	}
	public void setUserID(Integer userID) {
		UserID = userID;
	}
	public Date getUserDOB() {
		return UserDOB;
	}
	public void setUserDOB(Date userDOB) {
		UserDOB = userDOB;
	}
	public String getUserEmail() {
		return UserEmail;
	}
	public void setUserEmail(String userEmail) {
		UserEmail = userEmail;
	}
	public String getUserPassword() {
		return UserPassword;
	}
	public void setUserPassword(String userPassword) {
		UserPassword = userPassword;
	}
	public String getUserRole() {
		return UserRole;
	}
	public void setUserRole(String userRole) {
		UserRole = userRole;
	}
	public String getUserGender() {
		return UserGender;
	}
	public void setUserGender(String userGender) {
		UserGender = userGender;
	} 
}
