package com.test.admin.conurbations.activitys;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.SongAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.fragments.BottomDialogFragment;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.presenter.NeteasePlayListPresenter;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.utils.SaveBitmapUtils;
import com.test.admin.conurbations.utils.StatusBarUtils;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.util.List;

import javax.inject.Inject;

public class NeteasePlayListActivity extends BaseSubActivity<Music, NeteasePlayListPresenter>
        implements INewInformationView {

    private NewsList newsList;
    public static final String PLAY_LIST = "play_list";

    @Inject
    SongAdapter mSongAdapter;

    @Override
    protected void initView() {
        StatusBarUtils.setWindowStatusBarColor(getBaseActivity(), R.color.theme_primary);
        getActivityComponent().inject(this);
        initIntent();
    }

    @Override
    protected void loadingData() {
        setRefreshLayoutEnableIsFalse();
        refreshList(1);
    }

    @Override
    public void setNewInfoData(List<NewsList> result) {
        mDataList.addAll(result.get(0).musicList);
        mSongAdapter.setList(mDataList);
        mSongAdapter.notifyDataSetChanged();
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {

        int id = Integer.valueOf(newsList.pid);
        int[] ids = new int[]{id};
        if (newsList.type.equals(Constants.PLAYLIST_WY_ID)) {
            mPresenter.getNeteaseRank(ids, null);
        } else {
            mPresenter.loadQQList(ids, null);
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        mSongAdapter.setHeaderView(getRecycleHeaderView(newsList));
        mSongAdapter.setOnItemClickListener(this::onItemClick);
        showHeaderInfo(newsList);
        return mSongAdapter;
    }

    public void onItemClick(Music music, View view, int position) {
        if (view.getId() == R.id.iv_more) {
            BottomDialogFragment.newInstance(music, music.type).show(this);
        } else {
            PlayManager.play(position - 1, mSongAdapter.list, newsList.name + newsList.pid);
            mSongAdapter.notifyDataSetChanged();
            NavigationHelper.navigateToPlaying(this, view.findViewById(R.id.iv_cover));
        }
    }

    private void showHeaderInfo(NewsList playlist) {
        if (playlist != null) {
            ImageView mIvCover = mSongAdapter.getHeaderView().findViewById(R.id.iv_cover);
            TextView mTvTitle = mSongAdapter.getHeaderView().findViewById(R.id.tv_title);
            TextView mTvDesc = mSongAdapter.getHeaderView().findViewById(R.id.tv_comment);
            ImageView mIvBackground = mSongAdapter.getHeaderView().findViewById(R.id.coverBgIv);

            SaveBitmapUtils.loadBitmap(this, playlist.coverUrl, bitmap -> {
                mIvCover.setImageBitmap(bitmap);
                mIvBackground.setImageBitmap(bitmap);
                mIvBackground.setImageDrawable(SaveBitmapUtils.createBlurredImageFromBitmap(bitmap));
            });

            mTvTitle.setText(playlist.name);
            mTvDesc.setText(playlist.des);
        }
    }

    private void initIntent() {
        newsList = getIntent().getParcelableExtra(PLAY_LIST);

        String title = "音乐";
        if (newsList != null && !TextUtils.isEmpty(title)) {
            title = newsList.name;
        }
        initToolbar(mBinding.toolbarBaseToolbar, title, "");
    }
}
