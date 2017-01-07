package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.test.admin.conurbations.R;

import java.util.List;

/**
 * Created by zhouqiong on 2016/12/30.
 */
public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected static final int VIEW_TYPE_LOAD_MORE_FOOTER = 100;
    protected boolean isLoadMoreFooterShown;
    public List<T> list;
    protected int mLastPosition = -1;
    private static final int DELAY = 138;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE_FOOTER) {
            return onCreateLoadMoreFooterViewHolder(parent);
        }
        return onCreateNormalViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (isLoadMoreFooterShown && position == getItemCount() - 1) {
            if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
        }
        if (position < list.size()) {
            T item = list.get(position);
            bindDataToItemView(holder, item);
        }
        //showItemAnim(holder.itemView,position);
    }

    protected abstract void bindDataToItemView(final BaseViewHolder vh, final T item);

    protected View inflateItemView(ViewGroup viewGroup, int layoutId) {
        return inflateItemView(viewGroup, layoutId, false);
    }

    protected View inflateItemView(ViewGroup viewGroup, int layoutId, boolean attach) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, attach);
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size() + (isLoadMoreFooterShown ? 1 : 0);
        } else {
            return (isLoadMoreFooterShown ? 1 : 0);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadMoreFooterShown && position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE_FOOTER;
        }
        return getDataViewType(position);
    }

    protected int getDataViewType(int position) {
        return 0;
    }

    protected abstract BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType);

    protected BaseViewHolder onCreateLoadMoreFooterViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_pull_to_refresh_footer, parent, false);
        return new LoadMoreFooterViewHolder(view);
    }

    public void onLoadMoreStateChanged(boolean isShown) {
        this.isLoadMoreFooterShown = isShown;
        if (isShown) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    private class LoadMoreFooterViewHolder extends BaseViewHolder {
        public LoadMoreFooterViewHolder(View view) {
            super(view);
        }
    }

    //每个item添加一个动画
    public void showItemAnim(final View view, final int position) {
        final Context context = view.getContext();
        if (position > mLastPosition) {
            view.setAlpha(0);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(context,
                            R.anim.slide_in_right);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override public void onAnimationStart(Animation animation) {
                            view.setAlpha(1);
                        }
                        @Override public void onAnimationEnd(Animation animation) {}
                        @Override public void onAnimationRepeat(Animation animation) {}
                    });
                    view.startAnimation(animation);
                }
            },DELAY * position);
            mLastPosition = position;
        }
    }
}
