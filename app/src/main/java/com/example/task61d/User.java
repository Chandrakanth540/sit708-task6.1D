package com.example.task61d;

public class User {
    private static User currentUser;

    private int id;
    private String username;
    private String password;
    private String interests;
    private String email;
    private String phone;

    public void setId(int id) {
        this.id = id;
    }


    public User(String username, String email, String password, String phone, String interests) {
        this.email=email;
        this.username = username;
        this.password = password;
        this.interests = interests;
        this.phone=phone;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    public  String getCurrentemail() {
        return email;
    }
    public String getInterests(){
        return interests;
    }


}
