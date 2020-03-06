package com.bigBlue.people.ui.feature.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bigBlue.people.R;
import com.bigBlue.people.model.UserModel;
import com.bigBlue.people.ui.feature.base.BaseActivity;
import com.bigBlue.people.ui.feature.login.LoginActivity;
import com.bigBlue.people.ui.feature.profile.ProfileActivity;
import com.bigBlue.people.utils.Constants;
import com.bigBlue.people.utils.HelperUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * The type Sign up activity.
 */
public class SignUpActivity extends BaseActivity implements SignUpView {

    @BindView(R.id.edt_name)
    EditText mName;
    @BindView(R.id.edt_email)
    EditText mEmail;
    @BindView(R.id.edt_phone)
    EditText mPhone;
    @BindView(R.id.edt_password)
    EditText mPassword;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    /**
     * Create intent intent.
     *
     * @param context the context
     * @return the intent
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            navigationToProfile();
        }
    }

    private boolean isValidate() {
        if (mName.getText().toString().trim().isEmpty()) {
            showMessage(R.string.msg_enter_name);
            return false;
        } else if (!HelperUtils.isNameValid(mName.getText().toString().trim())) {
            showMessage(R.string.msg_enter_valid_name);
            return false;
        } else if (mEmail.getText().toString().trim().isEmpty()) {
            showMessage(R.string.msg_enter_email);
            return false;
        } else if (!HelperUtils.isEmailValid(mEmail.getText().toString().trim())) {
            showMessage(R.string.msg_enter_valid_email);
            return false;
        } else if (mPhone.getText().toString().trim().isEmpty()) {
            showMessage(R.string.msg_enter_phone);
            return false;
        } else if (mPassword.getText().toString().trim().isEmpty()) {
            showMessage(R.string.msg_enter_password);
            return false;
        }
        return true;
    }

    /**
     * Sign up.
     */
    @OnClick(R.id.btnSignUpNavigate)
    public void signUp() {
        if (isValidate()) {
            userSignUp();
        }
    }

    /**
     * Login.
     */
    @OnClick(R.id.btnLoginNavigate)
    public void login() {
        startActivity(LoginActivity.createIntent(SignUpActivity.this));
    }

    @Override
    public void userSignUp() {
        showLoading();
        firebaseAuth.createUserWithEmailAndPassword(mEmail.getText().toString().trim(), mPassword.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideLoading();
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            updateUserDetails();
                        } else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUserDetails() {
        if (firebaseUser != null) {
            updateNameAndMobile(firebaseUser);
            navigationToProfile();
        }
    }

    private void updateNameAndMobile(FirebaseUser user) {
        showLoading();
        UserModel userModel = new UserModel(mPhone.getText().toString().trim());
        databaseReference.child(Constants.FIREBASE.USERS).child(firebaseUser.getUid()).setValue(userModel);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName.getText().toString().trim()).build();
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {
                    showMessage(R.string.error_in_updating);
                }
                hideLoading();
            }
        });
    }

    private void navigationToProfile() {
        startActivity(ProfileActivity.createIntent(SignUpActivity.this).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
