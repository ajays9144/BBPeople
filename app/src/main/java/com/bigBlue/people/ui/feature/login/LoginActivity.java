package com.bigBlue.people.ui.feature.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bigBlue.people.R;
import com.bigBlue.people.ui.feature.base.BaseActivity;
import com.bigBlue.people.ui.feature.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * The type Login activity.
 */
public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.edt_email)
    EditText mEmail;
    @BindView(R.id.edt_password)
    EditText mPassword;

    private FirebaseAuth firebaseAuth;

    /**
     * Create intent intent.
     *
     * @param context the context
     * @return the intent
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private boolean isValidate() {
        if (mEmail.getText().toString().trim().isEmpty()) {
            showMessage(R.string.msg_enter_name);
            return false;
        } else if (mEmail.getText().toString().trim().isEmpty()) {
            showMessage(R.string.msg_enter_valid_email);
            return false;
        } else if (mPassword.getText().toString().trim().isEmpty()) {
            showMessage(R.string.msg_enter_name);
            return false;
        }
        return true;
    }

    /**
     * Navigation sign in.
     */
    @OnClick(R.id.btnSignInNavigate)
    public void navigationSignIn() {
        if (isValidate()) {
            onUserLogin();
        }
    }

    /**
     * Navigation sign up.
     */
    @OnClick(R.id.btnSignUpNavigate)
    public void navigationSignUp() {
        onBackPressed();
    }

    @Override
    public void onUserLogin() {
        showLoading();
        firebaseAuth.signInWithEmailAndPassword(mEmail.getText().toString().trim(), mPassword.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideLoading();
                        if (task.isSuccessful()) {
                            navigationToProfile();
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void navigationToProfile() {
        startActivity(ProfileActivity.createIntent(LoginActivity.this).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
