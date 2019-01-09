package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.FragmentMusicMainBinding;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class MusicMainFragment extends BaseFragment<FragmentMusicMainBinding> {
    private PlayControlFragment controlFragment;

    private Disposable subscribe;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music_main;
    }

    @Override
    protected void initData(Bundle bundle) {

        initView();

        navigateLibrary.run();
        navigatePlay.run();
    }

    private void initView() {

        updatePlaySongInfo(PlayManager.getPlayingMusic());


        subscribe = RxBus.getDefault().toObservable(Event.class).subscribe(event -> {
            if (event.eventType.equals(Constants.META_CHANGED_EVENT)) {
                Music music = (Music) event.body;
                updatePlaySongInfo(music);
            }
        });

    }

    private void updatePlaySongInfo(Music music) {
        if (music != null) {
//            SaveBitmapUtils.loadBigImageView(this,music,);
        } else {
        }
    }


    private Runnable navigateLibrary = () -> {
        Fragment fragment = NewsInformationFragment.newInstance(getArguments().getInt("content"));
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();

    };

    private Runnable navigatePlay = () -> {
        controlFragment = PlayControlFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.controls_container, controlFragment).commitAllowingStateLoss();
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
            subscribe = null;
        }
    }

    @Override
    public void detachView() {

    }
}
