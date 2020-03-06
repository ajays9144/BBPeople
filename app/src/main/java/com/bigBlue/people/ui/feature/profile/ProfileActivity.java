package com.bigBlue.people.ui.feature.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bigBlue.people.R;
import com.bigBlue.people.model.ImageContainer;
import com.bigBlue.people.model.UserModel;
import com.bigBlue.people.ui.adapter.AdapterClickHandler;
import com.bigBlue.people.ui.adapter.ImageContainerAdapter;
import com.bigBlue.people.ui.feature.base.BaseActivity;
import com.bigBlue.people.ui.feature.last.LastActivity;
import com.bigBlue.people.ui.feature.signup.SignUpActivity;
import com.bigBlue.people.utils.Constants;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stfalcon.frescoimageviewer.ImageViewer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The type Profile activity.
 */
public class ProfileActivity extends BaseActivity implements AdapterClickHandler {

    @BindView(R.id.txt_user_name)
    TextView mUserName;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.txt_title)
    TextView mTitle;
    @BindView(R.id.mProfileImage)
    CircleImageView mProfileImage;
    @BindView(R.id.edt_about)
    EditText mAbout;
    @BindView(R.id.image_container)
    RecyclerView mImageContainer;

    /**
     * The Default container.
     */
    ImageContainer defaultContainer = new ImageContainer(1, Constants.HEAD_CONTAINER, "");

    private boolean isImageUploading = false;
    private int uploadImageType = 1, currentArrayCount;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private FirebaseStorage mStorage;
    private StorageReference serviceStorageRef;
    private UserModel userModel;

    private ImageContainerAdapter imageContainerAdapter;
    private ArrayList<ImageContainer> imageContainersList = new ArrayList<>();

    /**
     * Create intent intent.
     *
     * @param context the context
     * @return the intent
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE.USERS);
        Fresco.initialize(ProfileActivity.this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initToolbar();
        initRecyclerView();

        initUserData();

        serviceStorageRef = mStorage.getReference().child(Constants.FIREBASE.IMAGES).child(user.getUid());
    }

    private boolean isValidate() {
        if (user == null) return false;
        if (user.getEmail() == null) return false;
        if (userModel == null) return false;
        if (mAbout.getText().toString().trim().isEmpty()) {
            showMessage(R.string.msg_enter_about);
            return false;
        } else if (mAbout.getText().toString().trim().contains(userModel.getPhoneNumber())) {
            showMessage(R.string.msg_about_can_not_contain_number);
            return false;
        } else if (mAbout.getText().toString().trim().toLowerCase().contains(user.getEmail().toLowerCase())) {
            showMessage(R.string.msg_about_can_not_contain_email);
            return false;
        }
        return true;
    }

    private void initToolbar() {
        mTitle.setText(R.string.title_profile);
    }

    private void initUserData() {
        if (user != null) {
            mUserName.setText(user.getDisplayName());
            Glide.with(ProfileActivity.this).load(user.getPhotoUrl()).error(R.drawable.ic_account)
                    .placeholder(R.drawable.ic_account).into(mProfileImage);
            updateDBdata();
        }
    }

    private void updateDBdata() {
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModel = dataSnapshot.getValue(UserModel.class);
                if (userModel != null) {
                    if (userModel.getAbout() != null)
                        mAbout.setText(userModel.getAbout());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage(databaseError.getMessage());
            }
        });

        databaseReference.child(user.getUid()).child(Constants.FIREBASE.IMAGES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!isImageUploading) {
                    imageContainersList.clear();
                    for (DataSnapshot db : dataSnapshot.getChildren()) {
                        ImageContainer container = new ImageContainer();
                        container.setImageUri(db.getValue(String.class));
                        container.setImageType(Constants.IMAGE_CONTAINER);
                        imageContainersList.add(container);
                    }
                    currentArrayCount = imageContainersList.size();
                    if (currentArrayCount < 4) {
                        imageContainersList.add(0, defaultContainer);
                    }
                    imageContainerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("onCancelled: ", databaseError.getMessage());
            }
        });
    }

    private void initRecyclerView() {
        imageContainerAdapter = new ImageContainerAdapter(ProfileActivity.this, imageContainersList);
        mImageContainer.setAdapter(imageContainerAdapter);
        imageContainerAdapter.setCallback(this);
    }

    private void addDefaultContainer() {
        if (currentArrayCount > Constants.MAX_IMAGE_UPLOAD_COUNT) {
            imageContainersList.remove(0);
        } else {
            if (!imageContainersList.contains(defaultContainer)) {
                imageContainersList.add(0, defaultContainer);
            }
        }
        imageContainerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    /**
     * Navigation to next.
     */
    @OnClick(R.id.btnNavigationNext)
    public void navigationToNext() {
        startActivity(LastActivity.createIntent(ProfileActivity.this));
    }

    /**
     * Update profile.
     */
    @OnClick(R.id.btnUpdateProfile)
    public void updateProfile() {
        if (isValidate()) {
            databaseReference.child(user.getUid()).child(Constants.ABOUT).setValue(mAbout.getText().toString().trim());
            uploadAllImageInDB();
        }
    }

    /**
     * User logout.
     */
    @OnClick(R.id.btnLogoutUser)
    public void userLogout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(SignUpActivity.createIntent(ProfileActivity.this).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    /**
     * Change profile image.
     */
    @OnClick(R.id.mProfileImage)
    public void changeProfileImage() {
        uploadImageType = 1;
        isImageUploading = true;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(ProfileActivity.this);
    }

    /**
     * On back select.
     */
    @OnClick(R.id.img_back)
    public void onBackSelect() {
        onBackPressed();
    }

    @Override
    public void onItemsClicked(int position) {
        buildImageViewer();
    }

    @Override
    public void onHeaderClicked(int position) {
        isImageUploading = false;
        uploadImageType = 2;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(ProfileActivity.this);
    }

    private void buildImageViewer() {
        ArrayList<ImageContainer> showBanner = new ArrayList<>();
        for (ImageContainer container : imageContainersList) {
            if (container.getImageType() == Constants.IMAGE_CONTAINER) {
                showBanner.add(container);
            }
        }

        new ImageViewer.Builder(ProfileActivity.this, showBanner)
                .setStartPosition(0)
                .setFormatter(new ImageViewer.Formatter<ImageContainer>() {
                    @Override
                    public String format(ImageContainer images) {
                        return images.getImageUri();
                    }
                }).show();
    }

    private void updatePhoto(FirebaseUser user, Uri profileImage) {
        if (user != null) {
            showLoading();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(profileImage).build();
            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        showMessage(R.string.msg_profile_image_updated);
                        initUserData();
                    } else {
                        showMessage(R.string.error_in_updating);
                    }
                    hideLoading();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            if (uploadImageType == 1) {
                updatePhoto(user, resultUri);
            } else {
                sendPhoto(resultUri);
            }
        }
    }

    private void uploadAllImageInDB() {
        DatabaseReference db = databaseReference.child(user.getUid()).child(Constants.FIREBASE.IMAGES);
        for (int i = 0; i < imageContainersList.size(); i++) {
            if (imageContainersList.get(i).getImageType() != Constants.HEAD_CONTAINER) {
                db.child(String.valueOf(i)).setValue(imageContainersList.get(i).getImageUri());
            }
        }
        showMessage(R.string.msg_profile_image_updated);
    }

    private void sendPhoto(Uri selectedImageUri) {
        showLoading();
        StorageReference photoRef = serviceStorageRef.child(selectedImageUri.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(selectedImageUri);
        uploadTask.addOnSuccessListener(ProfileActivity.this, taskSnapshot -> {
            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
        }).addOnFailureListener(e -> showMessage(getString(R.string.failed_to_upload_file)));
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return photoRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String generatedFilePath = task.getResult().toString();
                ImageContainer imageContainer = new ImageContainer();
                imageContainer.setImageType(Constants.IMAGE_CONTAINER);
                imageContainer.setImageUri(generatedFilePath);
                updateAdapterAndDB(imageContainer);
            } else {
                showMessage(R.string.failed_to_upload_file);
            }
            hideLoading();
        });
    }

    private void updateAdapterAndDB(ImageContainer container) {
        if (container != null) {
            imageContainersList.add(container);
            currentArrayCount = imageContainersList.size();
            addDefaultContainer();
        }
    }
}
