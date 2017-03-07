package com.test.admin.conurbations.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.fragments.BaseFragment;
import com.test.admin.conurbations.fragments.FragmentHelp;
import com.test.admin.conurbations.fragments.FragmentIndex;
import com.test.admin.conurbations.fragments.FragmentPrettyPictures;
import com.test.admin.conurbations.fragments.FragmentTeam;
import com.test.admin.conurbations.fragments.SearchFragment;
import com.test.admin.conurbations.utils.PhotoCameralUtil;
import com.test.admin.conurbations.utils.imageUtils.ImageUtil;
import com.test.admin.conurbations.views.CircleImageView;
import com.test.admin.conurbations.views.MaterialSearchView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import me.majiajie.pagerbottomtabstrip.Controller;
import me.majiajie.pagerbottomtabstrip.PagerBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.TabItemBuilder;
import me.majiajie.pagerbottomtabstrip.TabLayoutMode;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectListener;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.tab)
    PagerBottomTabLayout pagerBottomTabLayout;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;

    private Controller controller;
    private List<Fragment> mFragments;
    private CircleImageView circleImageView;
    private Bitmap headPhotoBitmap;
    private Bundle photoBundle;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mToolbar, getResources().getString(R.string.app_name), getResources().getString(R.string.guard_msg));
        //初始化底部导航
        initMainBottomTab();
        //初始化主Activity(底部导航对应的Fragment)
        initAddFragment();
        //初始化左边侧滑栏
        initLeftDrawerToggleMenu();
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView.isOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void initAddFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(createFragment(new FragmentIndex(), Constants.testColors[0]));
        mFragments.add(createFragment(new FragmentPrettyPictures(), Constants.testColors[1]));
        mFragments.add(createFragment(new FragmentTeam(), Constants.testColors[2]));
        mFragments.add(createFragment(new FragmentHelp(), Constants.testColors[3]));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // transaction.setCustomAnimations(R.anim.push_up_in,R.anim.push_up_out);
        transaction.add(R.id.frameLayout_content_main, mFragments.get(0));
        transaction.commit();
    }

    private void initLeftDrawerToggleMenu() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //初始化左边侧滑栏上部分（头像部分）
        circleImageView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.circle_image_view);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0).setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        searchView.setOnQueryTextListener(queryTextListener);
        searchView.adjustTintAlpha(0.8f);

        //处理换头像等逻辑
        headPhotoBitmap = BitmapFactory.decodeFile(Constants.pathFileName);
        if (headPhotoBitmap != null) {
            circleImageView.setImageBitmap(headPhotoBitmap);
        } else {
            circleImageView.setImageResource(R.mipmap.my_bg);
        }
    }


    private void initMainBottomTab() {
        //用TabItemBuilder构建一个导航按钮
        TabItemBuilder tabItemBuilder = new TabItemBuilder(this).create()
                .setDefaultIcon(android.R.drawable.ic_menu_send)
                .setText("干货")
                .setSelectedColor(Constants.testColors[0])
                .setTag("A")
                .build();

        //构建导航栏,得到Controller进行后续控制
        controller = pagerBottomTabLayout.builder()
                .addTabItem(tabItemBuilder)
                .addTabItem(android.R.drawable.ic_menu_compass, "美图", Constants.testColors[1])
                .addTabItem(android.R.drawable.ic_menu_crop, "新闻", Constants.testColors[2])
                .addTabItem(android.R.drawable.ic_menu_help, "帮助", Constants.testColors[3])
                .setMode(TabLayoutMode.HIDE_TEXT)
                .setMode(TabLayoutMode.CHANGE_BACKGROUND_COLOR)
                .setMode(TabLayoutMode.HIDE_TEXT | TabLayoutMode.CHANGE_BACKGROUND_COLOR)
                .build();

      /*  controller.setMessageNumber("A",2);
        controller.setDisplayOval(0,true);*/
        controller.addTabItemClickListener(tabItemSelectListener);
    }

    private Fragment createFragment(BaseFragment baseFragment, int content) {
        Fragment fragment = baseFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("content", content);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //设置menu菜单点击事件（更多）
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, ContactsActivity.class));
            return true;
        } else if (id == R.id.action_Image) {
            startActivity(new Intent(MainActivity.this, TelegramGalleryActivity.class));
        } else if (id == R.id.action_search) {
            searchView.openSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化左边侧滑栏menu并设置点击事件
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            startActivity(new Intent(MainActivity.this, ItemListActivity.class));
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        } else if (id == R.id.nav_send) {
            customFeedbackStyle();
            FeedbackAPI.openFeedbackActivity(this);
        } else if (id == R.id.nav_recommend) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    OnTabItemSelectListener tabItemSelectListener = new OnTabItemSelectListener() {
        @Override
        public void onSelected(int index, Object tag) {
            Log.i("asd", "onSelected:" + index + "   TAG: " + tag.toString());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mToolbar.setBackgroundColor(Constants.toolBarColors[index]);
            navigationView.setBackgroundColor(Constants.testColors[index]);
            navigationView.getHeaderView(0).setBackgroundColor(Constants.toolBarColors[index]);
            //transaction.setCustomAnimations(R.anim.push_up_in,R.anim.push_up_out);
            transaction.replace(R.id.frameLayout_content_main, mFragments.get(index));
            transaction.commit();
        }

        @Override
        public void onRepeatClick(int index, Object tag) {
            Log.i("asd", "onRepeatClick:" + index + "   TAG: " + tag.toString());
        }
    };

    MaterialSearchView.OnQueryTextListener queryTextListener = new MaterialSearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra(SearchFragment.CLASS_SEARCH, query);
            startActivity(intent);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    public void customFeedbackStyle() {
        Map<String, String> map = new ArrayMap<String, String>();
        map.put("enableAudio", "0");
        map.put("themeColor", "#D91D36");
        map.put("pageTitle", "用户反馈");
        map.put("toAvatar", "http://www.iyi8.com/uploadfile/2015/1024/20151024114500805.jpg");
        FeedbackAPI.setUICustomInfo(map);
    }

    //点击侧滑菜单选择图片

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
            case Constants.PHOTO_REQUEST_TAKEPHOTO:
                PhotoCameralUtil.startPhotoZoom(MainActivity.this, Uri.fromFile(new File(Constants.pathFileName)), 150);
                break;

            case Constants.PHOTO_REQUEST_GALLERY:
                if (data != null)
                    PhotoCameralUtil.startPhotoZoom(MainActivity.this, data.getData(), 150);
                break;

            case Constants.PHOTO_REQUEST_CUT:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    //将进行剪裁后的图片显示到UI界面上
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setPicToView(Intent picToView) {
        photoBundle = picToView.getExtras();
        if (photoBundle != null) {
            headPhotoBitmap = photoBundle.getParcelable("data");
            circleImageView.setImageBitmap(headPhotoBitmap);
            ImageUtil.saveBitmap(headPhotoBitmap, Constants.pathFileName);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        {
            if (id == R.id.nav_view) {
                PhotoCameralUtil.showHendPhotoDialog(MainActivity.this, Constants.pathFileName);//调用选择头像的Dialog
            } else if (id == R.id.circle_image_view) {
                if (headPhotoBitmap != null) {
                    Intent intent = new Intent(MainActivity.this, PersonalInformationActivity.class);
                    intent.putExtra("photoBundle", headPhotoBitmap);
                    ActivityCompat.startActivity(MainActivity.this, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, circleImageView, TRANSLATE_VIEW).toBundle());
                } else {
                    PhotoCameralUtil.showHendPhotoDialog(MainActivity.this, Constants.pathFileName);
                }
            }
        }
    }
}
