package com.example.androidsample.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * Created by PC on 7/15/2017.
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    private ProgressDialog mProgressDlg = null;

    private int mRequesCode = 0;
    private Delegate.PermissionDelegate mPermissionDeleagete = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDlg = new ProgressDialog(this);
    }

    public void showProgress() {
        mProgressDlg.setCancelable(false);
        mProgressDlg.setMessage("Please wait");
        mProgressDlg.setIndeterminate(true);
        mProgressDlg.show();
    }

    public void showProgress(String message) {
        mProgressDlg.setCancelable(false);
        mProgressDlg.setMessage(message);
        mProgressDlg.setIndeterminate(true);
        mProgressDlg.show();
    }

    public void showProgress(boolean isCancelable) {
        mProgressDlg.setCancelable(isCancelable);
        mProgressDlg.setMessage("Please wait");
        mProgressDlg.setIndeterminate(true);
        mProgressDlg.show();
    }

    public void showProgress(String message, boolean isCancelable) {
        mProgressDlg.setCancelable(isCancelable);
        mProgressDlg.setMessage(message);
        mProgressDlg.setIndeterminate(true);
        mProgressDlg.show();
    }

    public void hideProgress() {
        mProgressDlg.dismiss();
    }

    // keyboard
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    // permission
    protected boolean checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (!checkPermission(permission)) {
                return false;
            }
        }

        return true;
    }

    protected boolean shouldShowRequestPermission(String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            return true;
        }

        return false;
    }

    protected boolean shouldShowRequestPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (shouldShowRequestPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    protected void askPermission(String permission, int requestCode) {
        if (shouldShowRequestPermission(permission)) {
            mRequesCode = requestCode;
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            mRequesCode = requestCode;
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    protected void askPermissions(String[] permissions, int requestCode) {
        if (shouldShowRequestPermissions(permissions)) {
            mRequesCode = requestCode;
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        } else {
            mRequesCode = requestCode;
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }

    protected void checkAndAskPermission(String permission, int requestCode) {
        if (!checkPermission(permission)) {
            askPermission(permission, requestCode);
        }
    }

    protected void checkAndAskPermissions(String[] permissions, int requestCode) {
        if (!checkPermissions(permissions)) {
            askPermissions(permissions, requestCode);
        }
    }

    protected void askPermission(String permission, int requestCode, Delegate.PermissionDelegate delegate) {
        if (shouldShowRequestPermission(permission)) {
        } else {
            mPermissionDeleagete = delegate;
            askPermission(permission, requestCode);
        }
    }

    protected void askPermissions(String[] permissions, int requestCode, Delegate.PermissionDelegate delegate) {
        if (shouldShowRequestPermissions(permissions)) {
        } else {
            mPermissionDeleagete = delegate;
            askPermissions(permissions, requestCode);
        }
    }

    protected void checkAndAskPermission(String permission, final int requestCode, Delegate.PermissionDelegate delegate) {
        if (!checkPermission(permission)) {
            mPermissionDeleagete = delegate;
            askPermission(permission, requestCode);
        } else if (delegate != null) {
            delegate.granted(requestCode);
        }
    }

    protected void checkAndAskPermissions(String[] permissions, final int requestCode, Delegate.PermissionDelegate delegate) {
        if (!checkPermissions(permissions)) {
            mPermissionDeleagete = delegate;
            askPermissions(permissions, requestCode);
        } else if (delegate != null) {
            delegate.granted(requestCode);
        }
    }

    protected void permissionConfirmDialog(final String permission, final int requestCode) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false)
                .setMessage(permission)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkAndAskPermission(permission, requestCode);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mRequesCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mPermissionDeleagete != null) {
                    mPermissionDeleagete.granted(requestCode);
                }
            } else if (mPermissionDeleagete != null){
                mPermissionDeleagete.denied(requestCode);
            }
            mPermissionDeleagete = null;
        } else {

        }
    }

    protected void openSettingScreen() {
        Snackbar snackbar = Snackbar
                .make(getWindow().getDecorView().getRootView(), "Require All Permission", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    // interaction dialog
    public void showErrorDialog(String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create().show();
    }

    protected void showErrorDialog(String message, final Delegate.DialogDelegate dialogDelegate) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogDelegate.complete(RESULT_OK);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create().show();
    }

    protected void showConfirmDialog(String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create().show();
    }

    protected void showConfirmDialog(String message, final Delegate.DialogDelegate dialogDelegate) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogDelegate.complete(RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogDelegate.complete(RESULT_CANCELED);
                    }
                })
                .create().show();
    }

    protected void showConfirmDialog(String title, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

        if (title.isEmpty()) {
            alertBuilder.show();
        } else {
            alertBuilder.setTitle(title).show();
        }
    }

    protected void showConfirmDialog(String title, String message, final Delegate.DialogDelegate dialogDelegate) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogDelegate.complete(RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogDelegate.complete(RESULT_CANCELED);
                    }
                })
                .create();

        if (title.isEmpty()) {
            alertBuilder.show();
        } else {
            alertBuilder.setTitle(title).show();
        }
    }

    protected void showDialog(String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }

    protected void showDialog(String message, final Delegate.DialogDelegate dialogDelegate) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogDelegate.complete(RESULT_OK);
                    }
                })
                .create().show();
    }

    protected void showDialog(String title, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

        if (title.isEmpty()) {
            alertBuilder.show();
        } else {
            alertBuilder.setTitle(title).show();
        }
    }

    protected void showDialog(String title, String message, final Delegate.DialogDelegate dialogDelegate) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogDelegate.complete(RESULT_OK);
                    }
                })
                .create();

        if (title.isEmpty()) {
            alertBuilder.show();
        } else {
            alertBuilder.setTitle(title).show();
        }
    }
}
