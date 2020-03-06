package com.bigBlue.people.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.bigBlue.people.R;
import com.bigBlue.people.ui.feature.last.CommentAndPostFragment;
import com.bigBlue.people.ui.feature.last.CommentsFragment;
import com.bigBlue.people.ui.feature.last.PostsFragment;

/**
 * The type Last pager adapter.
 */
public class LastPagerAdapter extends FragmentStatePagerAdapter {

    private final static int NUM_OF_PAGES = 3;

    private Context context;
    private int[] tabsTitle = new int[]{R.string.tab_txt_comment, R.string.tab_txt_comment_post, R.string.tab_txt_posts};

    /**
     * Instantiates a new Last pager adapter.
     *
     * @param context  the context
     * @param fm       the fm
     * @param behavior the behavior
     */
    public LastPagerAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CommentsFragment.newInstance();
            case 1:
                return CommentAndPostFragment.newInstance();
            case 2:
                return PostsFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_OF_PAGES;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(tabsTitle[position]);
    }
}
