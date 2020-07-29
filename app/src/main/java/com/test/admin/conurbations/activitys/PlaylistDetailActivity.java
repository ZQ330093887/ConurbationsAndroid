package com.test.admin.conurbations.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.SongAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.FragPlaylistDetailBinding;
import com.test.admin.conurbations.fragments.BottomDialogFragment;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.Album;
import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.player.playqueue.PlayHistoryLoader;
import com.test.admin.conurbations.presenter.PlayListDetailPresenter;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.utils.LogUtil;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.utils.SaveBitmapUtils;
import com.test.admin.conurbations.utils.StatusBarUtil;
import com.test.admin.conurbations.utils.rom.OnlinePlaylistUtils;
import com.test.admin.conurbations.utils.rom.UIUtils;
import com.test.admin.conurbations.views.AlertDialog;
import com.test.admin.conurbations.widget.statuslayoutmanage.OnStatusChildClickListener;
import com.test.admin.conurbations.widget.statuslayoutmanage.StatusLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailActivity extends BaseActivity<FragPlaylistDetailBinding> implements IPlayListDetailView {
    private Album mAlbum;
    private NewsList mPlaylist;
    private Artist mArtist;
    private String title;
    private String coverUrl;
    private String pid;
    private SongAdapter mAdapter;
    private ArrayList<Music> musicList;
    private PlayListDetailPresenter mPresenter;
    private StatusLayoutManager statusLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_playlist_detail;
    }

    @Override
    protected void initData(Bundle bundle) {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, mBinding.toolbar);
        StatusBarUtil.darkMode(this, false);
        initEmptyView();
        setToolbarTitle();
        initView();
        loadingData();
    }

    private void setToolbarTitle() {
        mPlaylist = getIntent().getParcelableExtra("playlist");
        mArtist = getIntent().getParcelableExtra("artist");
        mAlbum = (Album) getIntent().getSerializableExtra("album");

        if (musicList == null) {
            musicList = new ArrayList<>();
        }

        if (mPlaylist != null) {
            title = mPlaylist.name;
            pid = mPlaylist.pid;
            coverUrl = mPlaylist.coverUrl;
            SaveBitmapUtils.loadImageView(this, coverUrl, mBinding.albumArt);
        }

        if (mArtist != null) {
            title = mArtist.name;
            pid = mArtist.artistId;
            coverUrl = mArtist.picUrl;
            SaveBitmapUtils.loadImageView(this, coverUrl, mBinding.albumArt);
        }

        if (mAlbum != null) {
            title = mAlbum.name;
            pid = mAlbum.albumId;
            coverUrl = mAlbum.cover;
            SaveBitmapUtils.loadImageView(this, coverUrl, mBinding.albumArt);
        }
        initToolbar(mBinding.toolbar, title, "");
    }

    private void initView() {
        mPresenter = new PlayListDetailPresenter(this);
        mAdapter = new SongAdapter(this);
        mAdapter.setList(musicList);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.fab.setOnClickListener(v -> PlayManager.play(0, musicList, pid));
        mAdapter.setOnItemClickListener((music, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                if (mPlaylist != null) {
                    PlayManager.play(position, musicList, mPlaylist.pid);
                }
                if (mArtist != null) {
                    PlayManager.play(position, musicList, mArtist.artistId);
                }
                if (mAlbum != null) {
                    PlayManager.play(position, musicList, mAlbum.albumId);
                }

                mAdapter.notifyDataSetChanged();
                NavigationHelper.navigateToPlaying(PlaylistDetailActivity.this, view.findViewById(R.id.iv_cover));
            } else {
                BottomDialogFragment bottomDialogFragment = BottomDialogFragment.newInstance(musicList.get(position), mPlaylist.type);
                bottomDialogFragment.pid = mPlaylist.pid;
                bottomDialogFragment.setRemoveSuccessListener(music1 -> removeMusic(position));
                bottomDialogFragment.show(this);
            }
        });
    }

    private void loadingData() {

        if (mPlaylist != null) {
            if (mPlaylist.musicList != null && mPlaylist.musicList.size() > 0) {
                showPlaylistSongs(mPlaylist.musicList);
            } else {
                mPresenter.loadPlaylistSongs(mPlaylist);
            }
        }
        if (mArtist != null) {
            mPresenter.loadArtistSongs(mArtist);
        }
        if (mAlbum != null) {
            mPresenter.loadAlbumSongs(mAlbum);
        }
    }


    @Override
    public void showPlaylistSongs(List<Music> songList) {
        statusLayoutManager.showSuccessLayout();
        if (songList != null) {
            musicList.addAll(songList);
            mAdapter.setList(musicList);
        }

        mAdapter.notifyDataSetChanged();
        if (coverUrl == null) {
            if (mPlaylist != null && !TextUtils.isEmpty(mPlaylist.coverUrl)) {
                coverUrl = mPlaylist.coverUrl;
            }

            if (mArtist != null && !TextUtils.isEmpty(mArtist.picUrl)) {
                coverUrl = mArtist.picUrl;
            }

            if (musicList.size() > 0 && coverUrl == null) {
                coverUrl = musicList.get(0).coverUri;
            }
            SaveBitmapUtils.loadImageView(this, coverUrl, mBinding.albumArt);
        }
        if (mAdapter.list ==null || mAdapter.list.size() == 0) {
            //显示空状态
            statusLayoutManager.showEmptyLayout();
        }
    }

    @Override
    public void removeMusic(int position) {
        musicList.remove(position);
        mAdapter.notifyItemRemoved(position);
        if (mAdapter.list ==null || mAdapter.list.size() == 0) {
            //显示空状态
            statusLayoutManager.showEmptyLayout();
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_playlist_detail, menu);
        if (mPlaylist == null) {
            menu.removeItem(R.id.action_rename_playlist);
            menu.removeItem(R.id.action_delete_playlist);
        }
        if (mPlaylist != null) {
            if (mPlaylist.pid.equals(Constants.PLAYLIST_HISTORY_ID)) {
                menu.removeItem(R.id.action_rename_playlist);
            } else if (mPlaylist.pid.equals(Constants.PLAYLIST_LOVE_ID)) {
                menu.removeItem(R.id.action_rename_playlist);
                menu.removeItem(R.id.action_delete_playlist);
            }

            if (mPlaylist.type.equals(Constants.PLAYLIST_BD_ID)) {
                //百度电台
                menu.removeItem(R.id.action_rename_playlist);
                menu.removeItem(R.id.action_delete_playlist);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_playlist) {
            LogUtil.e("action_delete_playlist");
            if (mPlaylist != null) {
                boolean isHistory = UIUtils.INSTANCE.deletePlaylist(mPlaylist, this);
                if (isHistory) {
                    musicList.clear();
                    PlayHistoryLoader.clearPlayHistory();
                    mAdapter.notifyDataSetChanged();
                    if (mAdapter.list == null || mAdapter.list.size() <= 0) {
                        statusLayoutManager.showEmptyLayout();
                    }
                    RxBus.getDefault().post(new Event(mPlaylist, Constants.PLAYLIST_HISTORY_ID));

                } else if (mPresenter != null) {
                    OnlinePlaylistUtils.INSTANCE.deletePlaylist(mPlaylist, s -> {
                        onBackPress();
                        return null;
                    });
                }
            }
        } else if (id == R.id.action_rename_playlist) {
            LogUtil.e("action_rename_playlist");
            if (mPlaylist != null) {
                AlertDialog alertDialog = new AlertDialog(this).builder();
                alertDialog.setTitle(getString(R.string.playlist_rename));
                alertDialog.setEditer().setVisibility(View.VISIBLE);
                alertDialog.setNegativeButton("取消", null);
                alertDialog.setPositiveButton("确认", v -> {
                    String title = alertDialog.setEditer().getText().toString();
                    if (!title.equals(mPlaylist.name)) {
                        mPresenter.renamePlaylist(mPlaylist, title);
                    }
                });
                alertDialog.show();
            }
        } else if (id == R.id.action_batch) {
//            Intent intent = new Intent(this, EditSongListActivity.class);
//            intent.putParcelableArrayListExtra("song_list", musicList);
//            startActivity(intent);
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchMusicActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("is_playlist", true);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化空View
    private void initEmptyView() {
        if (statusLayoutManager == null) {
            statusLayoutManager = new StatusLayoutManager.Builder(mBinding.mainContent)
                    .setDefaultEmptyClickViewVisible(false)
                    .setOnStatusChildClickListener(new OnStatusChildClickListener() {
                        @Override
                        public void onEmptyChildClick(View view) {
                        }

                        @Override
                        public void onErrorChildClick(View view) {
                            statusLayoutManager.showLoadingLayout();
                            loadingData();
                        }

                        @Override
                        public void onCustomerChildClick(View view) {
                        }
                    }).build();
        }
        statusLayoutManager.showLoadingLayout();
    }

    @Override
    public void success(int type) {
        onBackPress();
    }

    private void onBackPress() {
        this.onBackPressed();
    }
}
