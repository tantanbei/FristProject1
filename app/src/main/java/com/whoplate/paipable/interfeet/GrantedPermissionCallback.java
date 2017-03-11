package com.whoplate.paipable.interfeet;

import android.support.annotation.NonNull;

public interface GrantedPermissionCallback {
    //success = if all permissions were granted, permissions[] list of granted permission
    void GrantedPermission(final boolean success, @NonNull final String[] permissions);
}
