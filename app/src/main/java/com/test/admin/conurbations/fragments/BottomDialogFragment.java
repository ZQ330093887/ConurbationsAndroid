package com.test.admin.conurbations.fragments;

import android.app.Dialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.BottomDialogItemAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.DialogLayoutBinding;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.Album;
import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.model.response.PopupItemBean;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.utils.CommonUtil;
import com.test.admin.conurbations.utils.MusicUtils;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.utils.rom.OnlinePlaylistUtils;
import com.test.admin.conurbations.utils.rom.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouqiong on 2016/9/23.
 */

public class BottomDialogFragment extends BottomSheetDialogFragment {


    public static final String PLAYLIST_TYPE = "playlist_type";
    public static final String MUSIC = "music";
    private AppCompatActivity mContext;
    private DialogLayoutBinding mBinding;
    private Music music;
    private BottomSheetBehavior<View> mBehavior;
    private String type = Constants.PLAYLIST_LOCAL_ID;
    private List<PopupItemBean> data;

    /**
     * 歌单id
     */
    public String pid = Constants.PLAYLIST_LOCAL_ID;


    public static BottomDialogFragment newInstance(Music music) {
        BottomDialogFragment fragment = new BottomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MUSIC, music);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BottomDialogFragment newInstance(Music music, String type) {
        BottomDialogFragment fragment = new BottomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MUSIC, music);
        bundle.putString(PLAYLIST_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }


    public void show(AppCompatActivity context) {
        mContext = context;
        FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
        ft.add(this, getTag());
        ft.commitAllowingStateLoss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.dialog_layout, null, false);

        music = getArguments().getParcelable(MUSIC);

        initItems();
        dialog.setContentView(mBinding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
        return dialog;

    }

    private void initItems() {
        if (music == null) return;

        mBinding.titleTv.setText(music.title);
        mBinding.subTitleTv.setText(CommonUtil.getArtistAndAlbum(music.artist, music.album));

        String type = getArguments().getString(PLAYLIST_TYPE);
        if (!TextUtils.isEmpty(type)) {
            this.type = type;
        }

        mutableMapOf();
        BottomDialogItemAdapter mAdapter = new BottomDialogItemAdapter(mContext);
        mAdapter.setList(data);
        mBinding.bottomSheetRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.bottomSheetRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this::onItemClick);

    }

    private void mutableMapOf() {
        SparseIntArray itemData = new SparseIntArray();
        itemData.put(R.string.popup_play_next, R.drawable.ic_queue_play_next);
        itemData.put(R.string.popup_add_to_playlist, R.drawable.ic_playlist_add);
        itemData.put(R.string.popup_album, R.drawable.ic_album);
        itemData.put(R.string.popup_artist, R.drawable.ic_art_track);
        itemData.put(R.string.popup_detail_edit, R.drawable.ic_mode_edit);
        itemData.put(R.string.popup_download, R.drawable.item_download);
        itemData.put(R.string.popup_delete, R.drawable.ic_delete);
        itemData.put(R.string.popup_share, R.drawable.ic_share_black);
        data = new ArrayList<>();

        if (music.type.equals(Constants.LOCAL)) {
            itemData.delete(R.string.popup_download);
            itemData.delete(R.string.popup_add_to_playlist);
        } else {
            itemData.delete(R.string.popup_detail_edit);
            if (!music.isDl || !music.isOnline) {
                itemData.delete(R.string.popup_download);
            }

            if (!type.equals(Constants.PLAYLIST_CUSTOM_ID) && !type.equals(Constants.PLAYLIST_IMPORT_ID) && music.isOnline) {
                itemData.delete(R.string.popup_delete);
            }
        }

        for (int i = 0; i < itemData.size(); i++) {
            data.add(new PopupItemBean(getString(itemData.keyAt(i)), itemData.valueAt(i)));
        }
    }

    public void onItemClick(PopupItemBean data) {
        switch (data.icon) {
            case R.drawable.ic_queue_play_next:
                PlayManager.nextPlay(music);
                break;
            case R.drawable.ic_playlist_add:
                if (!music.type.equals(Constants.LOCAL)) {
                    OnlinePlaylistUtils.INSTANCE.addToPlaylist(mContext, music);
                }
                break;
            case R.drawable.ic_art_track:
                turnToArtist();
                break;
            case R.drawable.ic_album:
                turnToAlbum();
                break;
            case R.drawable.ic_mode_edit:
                turnToEdit();
                break;
            case R.drawable.ic_delete:
                turnToDelete(music);
                break;
            case R.drawable.item_download:
                if (!music.type.equals(Constants.LOCAL)) {
                    if (!music.type.equals(Constants.LOCAL)) {
                        QualitySelectDialog.newInstance(music).setDownload(true).show(mContext);
                    }
                }
                break;
            case R.drawable.ic_share_black:
                MusicUtils.qqShare(mContext, PlayManager.getPlayingMusic());
                break;
        }
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void turnToDelete(Music music) {

        if (music.type.equals(Constants.LOCAL) || !music.isOnline) {

            UIUtils.INSTANCE.deleteSingleMusic(mContext, music, () -> {
                if (removeSuccessListener != null) {
                    removeSuccessListener.invoke(music);
                }
            });
        } else {
            OnlinePlaylistUtils.INSTANCE.disCollectMusic(pid, music, () -> {
                if (removeSuccessListener != null) {
                    removeSuccessListener.invoke(music);
                }
            });
        }
    }

    private void turnToEdit() {
//        Intent intent = new Intent(mContext, EditMusicActivity.class);
//        intent.putExtra("song", music);
//        startActivity(intent);
    }

    private void turnToArtist() {
        if (music != null && music.artistId != null && music.artist != null) {
            if (music.type != null && music.type.equals(Constants.LOCAL)) {
                Artist artist = new Artist();
                artist.artistId = music.artistId;
                artist.name = music.artist;
                artist.type = music.type;
                NavigationHelper.navigateToPlaylist(mContext, artist, null);
            } else {
                Artist artist = MusicUtils.getArtistInfo(music);
                if (artist != null) {
                    NavigationHelper.navigateToPlaylist(mContext, artist, null);
                }
            }
        }

    }

    private void turnToAlbum() {
        Album album = new Album();
        album.albumId = music.albumId;
        album.name = music.album;
        album.type = music.type;
        NavigationHelper.navigateToPlaylist(mContext, album, null);
    }

    public interface RemoveSuccessListener {
        public void invoke(Music music);
    }

    private RemoveSuccessListener removeSuccessListener;

    public BottomDialogFragment setRemoveSuccessListener(RemoveSuccessListener listener) {
        removeSuccessListener = listener;
        return this;
    }


}
