package com.bigBlue.people.ui.feature.last;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bigBlue.people.R;
import com.bigBlue.people.ui.adapter.LastPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * The type Last activity.
 */
public class LastActivity extends AppCompatActivity {


    @BindView(R.id.img_back)
    ImageView mBack;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.txt_title)
    TextView mTitle;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mPager;

    /**
     * Create intent intent.
     *
     * @param context the context
     * @return the intent
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, LastActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_activity);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initToolbar();
        initViewPager();
    }

    private void initToolbar() {
        mTitle.setText(R.string.title_last);
        mBack.setVisibility(View.VISIBLE);
    }

    /**
     * On back select.
     */
    @OnClick(R.id.img_back)
    public void onBackSelect() {
        onBackPressed();
    }

    private void initViewPager() {
        LastPagerAdapter adapter = new LastPagerAdapter(LastActivity.this, getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mPager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
