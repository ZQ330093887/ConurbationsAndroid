package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;

import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.HotSearchAdapter;
import com.test.admin.conurbations.adapter.SearchHistoryAdapter;
import com.test.admin.conurbations.adapter.SongAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.ActivityMusicSearchBinding;
import com.test.admin.conurbations.fragments.BottomDialogFragment;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.db.DaoLitepal;
import com.test.admin.conurbations.model.entity.HotSearchBean;
import com.test.admin.conurbations.model.entity.SearchHistoryBean;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.presenter.SearchMusicPresenter;
import com.test.admin.conurbations.utils.AnimationUtils;
import com.test.admin.conurbations.utils.CommonUtil;
import com.test.admin.conurbations.utils.LogUtil;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.utils.StatusBarUtils;
import com.test.admin.conurbations.widget.statuslayoutmanage.OnStatusChildClickListener;
import com.test.admin.conurbations.widget.statuslayoutmanage.StatusLayoutManager;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

/**
 * 搜索音乐
 * Created by zhouqiong on 2015/11/3 0003.
 */

public class SearchMusicActivity extends BaseActivity<ActivityMusicSearchBinding> implements ISearchMusicView {

    /**
     * 搜索信息
     */
    private String queryString;

    /**
     * 适配器
     */
    private SearchHistoryAdapter historyAdapter;
    private HotSearchAdapter hotSearchAdapter;
    private SongAdapter mAdapter;
    /**
     * Presenter
     */

    private SearchMusicPresenter mPresenter;

    /**
     * 分页偏移量
     */
    private int mCurrentCounter = 10;
    private int limit = 10;
    private int mOffset = 0;
    private boolean isSearchOnline = false;

    private StatusLayoutManager statusLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_search;
    }

    @Override
    protected void initData(Bundle bundle) {
        initView();
        initRefreshLayout();
        initStatusLayoutManager();
        loadingData();
        listener();
    }

    private void initView() {
        StatusBarUtils.setWindowStatusBarColor(getBaseActivity(), R.color.theme_primary);
        initToolbar(mBinding.toolbar, "", "");
        showSearchOnStart();

        //初始化列表
        mPresenter = new SearchMusicPresenter(this);
        mAdapter = new SongAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.resultListRcv.setLayoutManager(layoutManager);
        mBinding.resultListRcv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private void showSearchOnStart() {

        mBinding.searchToolbarContainer.searchEditText.setText(queryString);
        if (TextUtils.isEmpty(queryString) || TextUtils.isEmpty(mBinding.searchToolbarContainer.searchEditText.getText())) {
            mBinding.searchToolbarContainer.getRoot().setTranslationX(100f);
            mBinding.searchToolbarContainer.getRoot().setAlpha(0f);
            mBinding.searchToolbarContainer.getRoot().setVisibility(View.VISIBLE);
            mBinding.searchToolbarContainer.getRoot().animate().translationX(0f).alpha(1f).setDuration(200).setInterpolator(new DecelerateInterpolator()).start();
        } else {
            mBinding.searchToolbarContainer.getRoot().setVisibility(View.VISIBLE);
            mBinding.searchToolbarContainer.getRoot().setAlpha(1f);
            mBinding.searchToolbarContainer.getRoot().setTranslationX(0f);
        }
    }


    private void loadingData() {

        //获取搜索历史
        mPresenter.getSearchHistory(this);

        if (!getIntent().getBooleanExtra("is_playlist", false)) {
            //获取热搜
            mPresenter.getHotSearchInfo();
        }
    }

    /**
     * 监听事件
     */
    private void listener() {
        mBinding.clearAllIv.setOnClickListener(v -> {
            DaoLitepal.clearAllSearch();
            historyAdapter.setList(new ArrayList<>());
            historyAdapter.notifyDataSetChanged();
        });
        mBinding.searchToolbarContainer.clearSearchIv.setOnClickListener(v -> {
            queryString = "";
            mBinding.searchToolbarContainer.searchEditText.setText("");
            mBinding.searchToolbarContainer.clearSearchIv.setVisibility(View.GONE);
        });

        mBinding.searchToolbarContainer.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = mBinding.searchToolbarContainer.searchEditText.getText().toString();
                mBinding.searchToolbarContainer.clearSearchIv.setVisibility(View.VISIBLE);
                if (newText.isEmpty()) {
                    mPresenter.getSearchHistory(SearchMusicActivity.this);
                    updateHistoryPanel(true);
                    mBinding.searchToolbarContainer.clearSearchIv.setVisibility(View.GONE);
                } else if (!isSearchOnline) {
                    searchLocal(newText);
                }
            }
        });

        mBinding.searchToolbarContainer.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getAction() == EditorInfo.IME_ACTION_SEARCH)) {
                isSearchOnline = true;
                search(mBinding.searchToolbarContainer.searchEditText.getText().toString());
                return true;
            }
            return false;
        });

        mAdapter.setOnItemClickListener((music, view, position) -> {
            if (view.getId() == R.id.iv_more) {
                BottomDialogFragment.newInstance(music, Constants.PLAYLIST_SEARCH_ID).show(SearchMusicActivity.this);
            } else {
                if (mAdapter.getList().size() < position) return;
                PlayManager.playOnline(music);
                NavigationHelper.navigateToPlaying(SearchMusicActivity.this, view.findViewById(R.id.iv_cover));
            }
        });
    }

    private void updateHistoryPanel(boolean isShow) {
        if (isShow) {
            mBinding.resultListRcv.setVisibility(View.GONE);
            mBinding.historyPanel.setVisibility(View.VISIBLE);
        } else {
            mBinding.resultListRcv.setVisibility(View.VISIBLE);
            mBinding.historyPanel.setVisibility(View.GONE);
        }
    }

    /**
     * 本地搜索
     *
     * @param query
     */
    private void searchLocal(String query) {
        if (!TextUtils.isEmpty(query)) {
            queryString = query;
            updateHistoryPanel(false);
            mPresenter.searchLocal(query, this);
        }
    }

    /**
     * 在线搜索
     */
    private void search(String query) {
        if (!TextUtils.isEmpty(query)) {
            queryString = query;
            mOffset = 0;
            statusLayoutManager.showLoadingLayout();
            mBinding.searchToolbarContainer.searchEditText.clearFocus();
            CommonUtil.hideInputView(mBinding.searchToolbarContainer.searchEditText);
            updateHistoryPanel(false);
            mPresenter.saveQueryInfo(query);
            mPresenter.search(query, limit, mOffset);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        CommonUtil.hideInputView(mBinding.searchToolbarContainer.searchEditText);
    }

    @Override
    public void showSearchResult(List<Music> list) {
        statusLayoutManager.showSuccessLayout();
        if (list != null && list.size() > 0) {
            if (mAdapter.getList() == null) {
                mAdapter.setList(list);
            } else {
                mAdapter.getList().addAll(list);
            }
            isSearchOnline = false;
//            mBinding.refreshLayout.finishLoadMore();
            mOffset++;
        } else {
//            mBinding.refreshLayout.finishLoadMoreWithNoMoreData();
        }
        mBinding.refreshLayout.finishLoadMore();
        if (mAdapter.getList() == null || mAdapter.getList().size() == 0) {
            statusLayoutManager.showEmptyLayout();
        }
        mAdapter.notifyDataSetChanged();
        LogUtil.e("search", mCurrentCounter + "--" + mCurrentCounter + "--" + mOffset);
    }

    @Override
    public void showHotSearchInfo(List<HotSearchBean> result) {
        if (result.size() > 0) {
            mBinding.hotSearchView.setVisibility(View.VISIBLE);
            AnimationUtils.animateView(mBinding.hotSearchView, true, 600);
        } else {
            mBinding.hotSearchView.setVisibility(View.GONE);
        }

        if (hotSearchAdapter == null) {
            hotSearchAdapter = new HotSearchAdapter(this);
            hotSearchAdapter.setList(result);


            mBinding.hotSearchRcv.setLayoutManager(new LinearLayoutManager(this));
            mBinding.hotSearchRcv.setAdapter(hotSearchAdapter);
            hotSearchAdapter.notifyDataSetChanged();

            hotSearchAdapter.setOnItemClickListener(item -> {
                isSearchOnline = true;
                mBinding.searchToolbarContainer.searchEditText.setText(item.title);
                mBinding.searchToolbarContainer.searchEditText.setSelection(item.title.length());
                search(item.title);
            });

        } else {
            hotSearchAdapter.setList(result);
            hotSearchAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showSearchHistory(List<SearchHistoryBean> result) {
        if (historyAdapter == null) {
            historyAdapter = new SearchHistoryAdapter(this);
            historyAdapter.setList(result);
            mBinding.historyRcv.setLayoutManager(new LinearLayoutManager(this));
            mBinding.historyRcv.setAdapter(historyAdapter);
            historyAdapter.notifyDataSetChanged();
            historyAdapter.setOnItemClickListener((item, view) -> {
                if (view.getId() == R.id.history_search) {
                    isSearchOnline = true;

                    mBinding.searchToolbarContainer.searchEditText.setText(item.title);
                    mBinding.searchToolbarContainer.searchEditText.setSelection(item.title.length());
                    search(item.title);
                } else if (view.getId() == R.id.deleteView) {
                    if (!TextUtils.isEmpty(item.title)) {
                        DaoLitepal.deleteSearchInfo(item.title);
                    }
                    historyAdapter.getList().remove(item);

                    historyAdapter.notifyDataSetChanged();
                }
            });
        } else {
            historyAdapter.setList(result);
            historyAdapter.notifyDataSetChanged();
        }
    }

    private void initStatusLayoutManager() {
        statusLayoutManager = new StatusLayoutManager.Builder(mBinding.refreshLayout)
                // 设置重试事件监听器
                .setOnStatusChildClickListener(new OnStatusChildClickListener() {
                    @Override
                    public void onEmptyChildClick(View view) {

                    }

                    @Override
                    public void onErrorChildClick(View view) {

                    }

                    @Override
                    public void onCustomerChildClick(View view) {

                    }
                })
                .build();
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        mBinding.refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        mBinding.refreshLayout.setEnableRefresh(false);
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            //成功获取更多数据
            if (!TextUtils.isEmpty(queryString)) {
                mPresenter.search(queryString, limit, mOffset);
            }
        });
    }


    /**
     * 移除重复歌曲
     */
    private List<Music> removeDuplicate(List<Music> list) {
        int i = 0;
        for (int var3 = list.size() - 1; i < var3; ++i) {
            int j = list.size() - 1;
            int var5 = i + 1;
            if (j >= var5) {
                while (true) {
                    if (Intrinsics.areEqual(list.get(j).title, list.get(i).title) && Intrinsics.areEqual(list.get(j).artist, list.get(i).artist) && Intrinsics.areEqual(list.get(j).album, list.get(i).album)) {
                        list.remove(j);
                    }
                    if (j == var5) {
                        break;
                    }

                    --j;
                }
            }
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_search) {
            isSearchOnline = true;
            mBinding.searchToolbarContainer.searchEditText.getText().toString().trim();
            search(queryString);
        }
        return super.onOptionsItemSelected(item);
    }
}
