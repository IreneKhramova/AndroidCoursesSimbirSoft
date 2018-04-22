package com.example.irene.androidcourses;

public class Validator {

    public boolean isNameValid(String name) {
        if (name == null || name.length() < 4) {
            return false;
        }
        return true;
    }

    public boolean isPhoneValid(String phone) {
        if (!(android.util.Patterns.PHONE.matcher(phone).matches())) {
            return false;
        }
        return true;
    }

    public boolean isEmailValid(String email) {
        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            return false;
        }
        return true;
    }

    public boolean isPasswordValid(String passwd) {
        int passwdSize = passwd.length();
        if (passwdSize < 6) {
            return false;
        }
        return true;
    }
}
