package org.lipski.TouristMobile.session;

public class UserSession {

    private static Integer userId;
    private static String username= "mlipski";
    private static Boolean isLoggedIn=false;

    public static Integer getUserId() {
        return userId;
    }

    public static void setUserId(Integer userId) {
        UserSession.userId = userId;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserSession.username = username;
    }

    public static Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public static void setIsLoggedIn(Boolean isLoggedIn) {
        UserSession.isLoggedIn = isLoggedIn;
    }
}
