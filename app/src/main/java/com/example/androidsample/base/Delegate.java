package com.example.androidsample.base;

/**
 * Created by Developer on 3/5/2018.
 */

public class Delegate {

    private interface PermissionInterface {
        void granted(int requestCode);

        void denied(int requestCode);
    }

    private interface DialogInterface {
        void complete(int result);
    }

    public abstract static class PermissionDelegate implements PermissionInterface {
        public void granted(int requestCode) {}

        public void denied(int requestCode) {}
    }

    public abstract static class DialogDelegate implements DialogInterface {
        public void complete(int result) {}
    }
}

