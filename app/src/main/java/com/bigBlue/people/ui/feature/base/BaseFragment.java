package com.bigBlue.people.ui.feature.base;

import android.app.ProgressDialog;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bigBlue.people.utils.HelperUtils;

/**
 * The type Base fragment.
 */
public class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    /**
     * Show loading.
     */
    public void showLoading() {
        mProgressDialog = HelperUtils.showLoadingDialog(getContext());
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
        Toast toast = Toast.makeText(getContext(), error, Toast.LENGTH_LONG);
        if (!toast.getView().isShown()) {
            toast.show();
        }
    }
}
