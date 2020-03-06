package com.bigBlue.people.ui.feature.last;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bigBlue.people.R;
import com.bigBlue.people.connectivity.ApiServices;
import com.bigBlue.people.connectivity.RetrofitClientInstance;
import com.bigBlue.people.model.BaseModel;
import com.bigBlue.people.ui.adapter.BaseRecyclerViewAdapter;
import com.bigBlue.people.ui.feature.base.BaseFragment;
import com.bigBlue.people.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The type Comments fragment.
 */
public class CommentsFragment extends BaseFragment {

    @BindView(R.id.switcher)
    ViewSwitcher mSwitcher;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private BaseRecyclerViewAdapter recyclerViewAdapter;
    private List<BaseModel> commentsModels = new ArrayList<>();

    private Unbinder unbinder;

    /**
     * New instance comments fragment.
     *
     * @return the comments fragment
     */
    public static CommentsFragment newInstance() {
        return new CommentsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        initRecyclerView();

        getComments();
    }

    private void initRecyclerView() {
        recyclerViewAdapter = new BaseRecyclerViewAdapter(getContext(), commentsModels, Constants.ADAPTER.COMMENT);
        mRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void getComments() {
        mSwitcher.setDisplayedChild(0);
        ApiServices apiServices = RetrofitClientInstance.getRetrofitInstance().create(ApiServices.class);
        Call<List<BaseModel>> call = apiServices.getComments();
        call.enqueue(new Callback<List<BaseModel>>() {
            @Override
            public void onResponse(Call<List<BaseModel>> call, Response<List<BaseModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mSwitcher.setDisplayedChild(1);
                    commentsModels.addAll(response.body());
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<BaseModel>> call, Throwable t) {
                mSwitcher.setDisplayedChild(0);
                showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}