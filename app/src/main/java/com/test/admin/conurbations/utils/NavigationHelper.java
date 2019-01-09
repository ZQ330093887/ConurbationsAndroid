package com.test.admin.conurbations.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.DownloadActivity;
import com.test.admin.conurbations.activitys.MainActivity;
import com.test.admin.conurbations.activitys.PlayerActivity;
import com.test.admin.conurbations.activitys.PlaylistDetailActivity;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.entity.Album;
import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.player.MusicPlayerService;
import com.test.admin.conurbations.player.PlayManager;

import java.io.File;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class NavigationHelper {

    public static void navigateToLocalMusic(Activity context, Pair<View, String> transitionViews) {
        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;

        if (transitionViews != null) {
            transaction.addSharedElement(transitionViews.first, transitionViews.second);
//            fragment = LocalMusicFragment.newInstance("local");
        } else {
            //            transaction.setCustomAnimations(R.anim.activity_fade_in,
            //                    R.anim.activity_fade_out, R.anim.activity_fade_in, R.anim.activity_fade_out);
//            fragment = LocalMusicFragment.newInstance("local");
        }
        Fragment it = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        transaction.hide(it);
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(fragment.getTag()).commit();
    }

    public static void navigateToDownload(AppCompatActivity context, boolean isCache, Pair<View, String> transitionViews) {
        Intent intent = new Intent(context, DownloadActivity.class);
        intent.putExtra("is_cache", isCache);
        if (transitionViews != null) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                    transitionViews.first, transitionViews.second);
            ActivityCompat.startActivity(context, intent, compat.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    /**
     * 扫描文件夹
     *
     * @param ctx
     * @param filePath
     */
    public static void scanFileAsync(Context ctx, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        ctx.sendBroadcast(scanIntent);
    }

    public static void navigateToPlaylist(Activity context, Album album, Pair<View, String> transitionViews) {
        Intent intent = new Intent(context, PlaylistDetailActivity.class);
        intent.putExtra("album", album);
        if (transitionViews != null) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                    transitionViews.first, transitionViews.second);
            ActivityCompat.startActivity(context, intent, compat.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    public static void navigateToPlaylist(Activity context, NewsList playlist, Pair<View, String> transitionViews) {
        Intent intent = new Intent(context, PlaylistDetailActivity.class);
        intent.putExtra("playlist", playlist);
        if (transitionViews != null) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                    transitionViews.first, transitionViews.second);
            ActivityCompat.startActivity(context, intent, compat.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    public static void navigateToPlaylist(Activity context, Artist artist, Pair<View, String> transitionViews) {
        Intent intent = new Intent(context, PlaylistDetailActivity.class);
        intent.putExtra("artist", artist);
        if (transitionViews != null) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                    transitionViews.first, transitionViews.second);
            ActivityCompat.startActivity(context, intent, compat.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    public static Intent getNowPlayingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constants.DEAULT_NOTIFICATION);
        return intent;
    }


    public static Intent getLyricIntent(Context context) {
        Intent intent = new Intent(MusicPlayerService.ACTION_LYRIC);
        intent.setComponent(new ComponentName(context, MusicPlayerService.class));
        return intent;
    }

    public static void navigateToPlaying(Activity context, View transitionView) {
        Intent intent = new Intent(context, PlayerActivity.class);
        if (transitionView != null) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(transitionView,
                    transitionView.getWidth() / 2, transitionView.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity(context, intent, compat.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    public static void navigateToSoundEffect(Activity context) {
        try {
            Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
            effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, PlayManager.getAudioSessionId());
            context.startActivityForResult(effects, 666);
        } catch (Exception e) {
            ToastUtils.getInstance().showToast("设备不支持均衡!");
        }
    }

}
