package com.test.admin.conurbations.activitys;

import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.MyMusicAdapter;
import com.test.admin.conurbations.adapter.PlayListAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.presenter.AllListPresenter;
import com.test.admin.conurbations.utils.StatusBarUtils;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 电台列表、歌手列表
 */
public class AllListActivity extends BaseSubActivity<NewsList, AllListPresenter> {

    private String type;
    public static final String PLAY_LIST_TYPE = "play_list_type";

    @Inject
    MyMusicAdapter musicAdapter;

    @Inject
    PlayListAdapter playListAdapter;

    @Override
    protected void initView() {
        StatusBarUtils.setWindowStatusBarColor(getBaseActivity(), R.color.theme_primary);
        getActivityComponent().inject(this);
        initIntent();
    }


    private void initIntent() {
        type = getIntent().getStringExtra(PLAY_LIST_TYPE);
        String title;
        if (type.equals(Constants.NETEASE_ARITIST_LIST)) {
            title = getString(R.string.hot_artist);
        } else {
            title = getString(R.string.radio);
        }
        initToolbar(mBinding.toolbarBaseToolbar, title, "");
    }

    @Override
    protected void loadingData() {
        setRefreshLayoutEnableIsFalse();
        mStatusManager.showSuccessLayout();
        if (type.equals(Constants.BAIDU_RADIO_LIST)) {
            ArrayList<NewsList> musicList = getIntent().getParcelableArrayListExtra("NewsList");
            if (musicList != null && musicList.size() > 0) {
                mDataList.addAll(musicList);
                musicAdapter.setList(mDataList);
                musicAdapter.notifyDataSetChanged();
            } else {
                mStatusManager.showEmptyLayout();
            }
        } else {
            ArrayList<Artist> musicList = getIntent().getParcelableArrayListExtra("Artist");
            if (musicList != null && musicList.size() > 0) {
                playListAdapter.setList(musicList);
                playListAdapter.notifyDataSetChanged();
            } else {
                mStatusManager.showEmptyLayout();
            }
        }
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        if (type.equals(Constants.BAIDU_RADIO_LIST)) {
            return musicAdapter;
        } else {
            return playListAdapter;
        }
    }
}
