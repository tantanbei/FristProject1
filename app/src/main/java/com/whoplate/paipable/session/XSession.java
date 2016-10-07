package com.whoplate.paipable.session;


import com.whoplate.paipable.db.Pref;

public class XSession {

    static public boolean IsValid() {
        int userId = Pref.Get(Pref.USERID, 0);
        return !(userId == 0);
    }

    static public void Logout() {
        Pref.Set(Pref.USERID, 0);
        Pref.Set(Pref.USERNAME, "");
        Pref.Set(Pref.USERPHONE, "");
        Pref.Save();
    }
}
