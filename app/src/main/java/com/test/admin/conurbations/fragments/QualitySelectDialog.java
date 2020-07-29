package com.test.admin.conurbations.fragments;

import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.QualityAdapter;
import com.test.admin.conurbations.databinding.DialogQualityBinding;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.utils.rom.UIUtils;

import java.util.ArrayList;

/**
 * Created by zhouqiong on 2016/9/23.
 */

public class QualitySelectDialog extends BottomSheetDialogFragment {


    private DialogQualityBinding mBinding;
    private QualityAdapter mAdapter;
    private AppCompatActivity mContext;
    private Music music;
    private BottomSheetBehavior<View> mBehavior;
    private boolean isDownload;

    public static QualitySelectDialog newInstance(Music music) {
        QualitySelectDialog fragment = new QualitySelectDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("music", music);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.dialog_quality, null, false);
        initItems();
        dialog.setContentView(mBinding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
        return dialog;
    }

    private void initItems() {
        music = getArguments().getParcelable("music");

        ArrayList<QualityItem> qualities = new ArrayList<>();
        qualities.add(new QualityItem("标准品质", 128000));
        if (music != null) {
            if (music.high) {
                qualities.add(new QualityItem("较高品质", 192000));
            }
            if (music.hq) {
                qualities.add(new QualityItem("HQ品质", 320000));
            }
            if (music.sq) {
                qualities.add(new QualityItem("无损品质", 999000));
            }
        }

        mAdapter = new QualityAdapter(mContext, music);
        mAdapter.setList(qualities);
        mBinding.bottomSheetRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.bottomSheetRv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mBinding.bottomSheetRv.setAdapter(mAdapter);
        if (isDownload) {
            mBinding.downloadView.setVisibility(View.VISIBLE);
        }
        if (!music.isDl) {
            mBinding.downloadTv.setClickable(false);
            mBinding.downloadTv.setTextColor(Color.GRAY);
        } else {
            mBinding.downloadTv.setClickable(true);
            mBinding.downloadTv.setTextColor(Color.WHITE);
        }
        mBinding.downloadTv.setOnClickListener(v -> {
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            UIUtils.INSTANCE.downloadMusic(mContext, music, false, mContext);
        });

        mBinding.cacheTv.setOnClickListener(v -> {
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            UIUtils.INSTANCE.downloadMusic(mContext, music, true, mContext);
        });
        mAdapter.setOnItemClickListener(item -> {
            QualitySelectDialog.QualityItem quality = (QualityItem) item;
            music.quality = quality.quality;
            mAdapter.notifyDataSetChanged();
            if (changeSuccessListener != null) {
                changeSuccessListener.invoke(quality.name);
            }
            if (!isDownload) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                PlayManager.play(PlayManager.position());
            }
        });
    }

    public QualitySelectDialog setDownload(boolean download) {
        isDownload = download;
        return this;
    }


    public void show(AppCompatActivity context) {
        mContext = context;
        FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
        ft.add(this, getTag());
        ft.commitAllowingStateLoss();
    }

    public static class QualityItem {
        public String name;
        public int quality;

        public QualityItem(String name, int quality) {
            this.name = name;
            this.quality = quality;
        }
    }

    public interface ChangeSuccessListener {
        public void invoke(String name);
    }

    private ChangeSuccessListener changeSuccessListener;

    public QualitySelectDialog setChangeSuccessListener(ChangeSuccessListener listener) {
        changeSuccessListener = listener;
        return this;
    }
}


