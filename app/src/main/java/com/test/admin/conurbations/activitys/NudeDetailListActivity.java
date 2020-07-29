package com.test.admin.conurbations.activitys;

import android.content.Intent;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.BaseListAdapter;
import com.test.admin.conurbations.adapter.NudeDetailListAdapter;
import com.test.admin.conurbations.presenter.NudeDetailListPresenter;
import com.test.admin.conurbations.utils.StatusBarUtils;
import com.test.admin.conurbations.widget.ILayoutManager;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ZQiong on 2017/9/11.
 */

public class NudeDetailListActivity extends BaseSubActivity<String, NudeDetailListPresenter> implements INudeDetailListView {

    public final static String TITLE = "title";
    public final static String URL = "url";
    public final static String IMAGE = "image";

    private String url;
    private List<String> imgList;

    @Inject
    NudeDetailListAdapter nudeDetailAdapter;

    @Override
    protected void initView() {
        StatusBarUtils.setWindowStatusBarColor(getBaseActivity(), R.color.theme_primary);
        getActivityComponent().inject(this);
        initIntent();
    }

    @Override
    protected void loadingData() {
        refreshList(1);
    }


    private void initIntent() {
        url = getIntent().getStringExtra(URL);
        imgList = getIntent().getStringArrayListExtra(IMAGE);
        String title = getIntent().getStringExtra(TITLE);
        title = TextUtils.isEmpty(title) ? "肉肉" : title;
        initToolbar(mBinding.toolbarBaseToolbar, title, "");
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void refreshList(int page) {
        if (imgList != null && imgList.size() > 0) {
            setNodeDetailData(imgList);
        } else {
            if (mPresenter != null) {
                mPresenter.getNudeDetail(url);
            }
        }
    }

    @Override
    protected BaseListAdapter setUpAdapter() {
        if (nudeDetailAdapter != null) {
            nudeDetailAdapter.setOnItemListener(onItemClickListener);
        }
        return nudeDetailAdapter;
    }

    @Override
    public void setNodeDetailData(List<String> itemList) {
        setRefreshLayoutEnableIsFalse();
        mStatusManager.showSuccessLayout();
        if (itemList != null && itemList.size() > 0) {
            mDataList.addAll(itemList);
            nudeDetailAdapter.setList(mDataList);
            nudeDetailAdapter.notifyDataSetChanged();
        } else {
            mStatusManager.showEmptyLayout();
        }
    }

    /**
     * item点击事件
     */
    NudeDetailListAdapter.OnItemClickListener onItemClickListener = (view, href) -> {
        Intent intent = new Intent(view.getContext(), ShowGoodsImageActivity.class);
        ArrayList<String> imgUrls = new ArrayList<>();
        imgUrls.add(href);
        intent.putExtra(ShowGoodsImageActivity.IMAGE_URL, imgUrls);
        view.getContext().startActivity(intent);
    };
}
