package fristproject1.sample.com.fristproject1.session;

import fristproject1.sample.com.fristproject1.db.Pref;

public class XSession {

    static public boolean IsValid() {
        int userId = Pref.Get(Pref.USERID, 0);
        if (userId == 0) {
            return false;
        } else {
            return true;
        }
    }

    static public void Logout() {
        Pref.Set(Pref.USERID, 0);
        Pref.Set(Pref.USERNAME, "");
        Pref.Set(Pref.USERPHONE, "");
    }
}
