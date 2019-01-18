package com.test.admin.conurbations.activitys;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.SongAdapter;
import com.test.admin.conurbations.fragments.BottomDialogFragment;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.presenter.PlayListPresenter;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.utils.StatusBarUtils;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.util.List;

import javax.inject.Inject;

public class BaiduMusicListActivity extends BaseSubActivity<Music, PlayListPresenter>
        implements IBaiduPlayList {

    private NewsList newsList;
    public static final String PLAY_LIST = "play_list";

    @Inject
    SongAdapter mSongAdapter;

    @Override
    protected void initView() {
        StatusBarUtils.setWindowStatusBarColor(getBaseActivity(), R.color.theme_primary);
        getActivityComponent().inject(this);
        initIntent();
        setRefreshLayoutEnableRefresh(false);
    }

    @Override
    protected void loadingData() {
        refreshList(1);
    }

    @Override
    public void showOnlineMusicList(List<Music> musicList) {
        mStatusManager.showSuccessLayout();
        if (musicList != null && musicList.size() > 0) {
            mBinding.refreshLayout.finishLoadMore(true);
            mDataList.addAll(musicList);
            mSongAdapter.setList(mDataList);
            mSongAdapter.notifyDataSetChanged();
        } else {
            if (mSongAdapter.list == null || mSongAdapter.list.size() <= 0) {
                mStatusManager.showEmptyLayout();
            } else {
                mBinding.refreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        mPresenter.loadOnlineMusicList(newsList.pid, 10, (page - 1) * 10);
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        mSongAdapter.setHeaderView(getRecycleHeaderView(newsList));
        mSongAdapter.setOnItemClickListener(this::onItemClick);
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

    private void initIntent() {
        newsList = getIntent().getParcelableExtra(PLAY_LIST);

        String title = "音乐";
        if (newsList != null && !TextUtils.isEmpty(title)) {
            title = newsList.name;
        }
        initToolbar(mBinding.toolbarBaseToolbar, title, "");
    }
}
