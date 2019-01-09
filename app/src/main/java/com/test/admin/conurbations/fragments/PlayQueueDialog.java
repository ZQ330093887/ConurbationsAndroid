package com.test.admin.conurbations.fragments;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.QueueAdapter;
import com.test.admin.conurbations.databinding.DialogPlayqueueBinding;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.player.playqueue.PlayQueueManager;
import com.test.admin.conurbations.presenter.PlayQueuePresenter;
import com.test.admin.conurbations.utils.AlertDialogUtils;
import com.test.admin.conurbations.utils.DisplayUtils;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.rom.UIUtils;
import com.test.admin.conurbations.widget.SolidApplication;

import java.util.List;

/**
 * Created by zhouqiong on 2016/9/23.
 */

public class PlayQueueDialog extends BottomSheetDialogFragment implements IPlayQueueContract {


    private PlayQueuePresenter mPresenter;
    private DialogPlayqueueBinding mBinding;
    private QueueAdapter mAdapter;
    private AppCompatActivity mContext;

    private BottomSheetBehavior<View> mBehavior;

    public static PlayQueueDialog newInstance() {
        PlayQueueDialog fragment = new PlayQueueDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = SolidApplication.getInstance().screenSize.y / 7 * 4;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mBehavior.setPeekHeight(params.height != 0 ? params.height : (int) DisplayUtils.px2dp(200, getContext()));
        //默认全屏展开
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PlayQueuePresenter(this);
        mAdapter = new QueueAdapter(mContext);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.dialog_playqueue, null, false);

        mBinding.recyclerViewSongs.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerViewSongs.setAdapter(mAdapter);
        mBinding.recyclerViewSongs.scrollToPosition(PlayManager.getCurrentPosition());
        initListener();

        dialog.setContentView(mBinding.getRoot());
        mPresenter.loadSongs();
        mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void initListener() {
        mBinding.tvPlayMode.setOnClickListener(v -> {
            UIUtils.INSTANCE.updatePlayMode(mBinding.ivPlayMode, true);
            mBinding.tvPlayMode.setText(PlayQueueManager.INSTANCE.getPlayMode());
        });
        mBinding.ivPlayMode.setOnClickListener(v -> {
            UIUtils.INSTANCE.updatePlayMode((ImageView) v, true);
            mBinding.tvPlayMode.setText(PlayQueueManager.INSTANCE.getPlayMode());
        });

        mBinding.clearAll.setOnClickListener(v -> AlertDialogUtils.showTipsDialog(mContext,
                mContext.getString(R.string.playlist_queue_clear), v1 -> {
                    mPresenter.clearQueue();
                    dismiss();
                }));

        mAdapter.setOnItemClickListener((view, position) -> {
            if (view.getId() == R.id.container) {
                PlayManager.play(position);
                mAdapter.notifyDataSetChanged();
            } else if (view.getId() == R.id.iv_more) {
                PlayManager.removeFromQueue(position);
                if (PlayManager.getPlayList().size() == 0) {
                    dismiss();
                } else {
                    mAdapter.setList(PlayManager.getPlayList());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void updatePlayMode() {
        UIUtils.INSTANCE.updatePlayMode(mBinding.ivPlayMode, false);
        mBinding.tvPlayMode.setText(PlayQueueManager.INSTANCE.getPlayMode());
    }

    @Override
    public void showSongs(List<Music> songs) {
        updatePlayMode();
        mAdapter.setList(songs);
        mAdapter.notifyDataSetChanged();
        //滚动到正在播放的位置

        mBinding.recyclerViewSongs.scrollToPosition(PlayManager.position());

        if (songs.isEmpty()) {
            // TODO: 2018/12/13 这里显示空状态
            ToastUtils.getInstance().showToast("这里显示空状态");
        }
    }

    public void showIt(AppCompatActivity context) {
        mContext = context;
        FragmentManager fm = context.getSupportFragmentManager();
        show(fm, "dialog");
    }
}
