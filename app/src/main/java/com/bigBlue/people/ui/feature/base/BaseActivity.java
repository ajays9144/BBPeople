package com.bigBlue.people.ui.feature.base;

import android.app.ProgressDialog;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bigBlue.people.utils.HelperUtils;

/**
 * The type Base activity.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    /**
     * Show loading.
     */
    public void showLoading() {
        mProgressDialog = HelperUtils.showLoadingDialog(this);
    }

    /**
     * Hide loading.
     */
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    /**
     * Show message.
     *
     * @param error the error
     */
    public void showMessage(int error) {
        showMessage(getString(error));
    }

    /**
     * Show message.
     *
     * @param error the error
     */
    public void showMessage(String error) {
        Toast toast = Toast.makeText(this, error, Toast.LENGTH_LONG);
        if (!toast.getView().isShown()) {
            toast.show();
        }
    }
}
