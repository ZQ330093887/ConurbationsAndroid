package com.test.admin.conurbations.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.android.material.navigation.NavigationView;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.ActivityMainBinding;
import com.test.admin.conurbations.fragments.BaseFragment;
import com.test.admin.conurbations.fragments.IndexFragment;
import com.test.admin.conurbations.fragments.MusicMainFragment;
import com.test.admin.conurbations.fragments.NBAFragment;
import com.test.admin.conurbations.fragments.PictureFragment;
import com.test.admin.conurbations.fragments.SearchFragment;
import com.test.admin.conurbations.fragments.VideoFragment;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.entity.CityWeather;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.utils.PhotoCameralUtil;
import com.test.admin.conurbations.utils.PrefUtils;
import com.test.admin.conurbations.utils.StatusBarUtils;
import com.test.admin.conurbations.utils.imageUtils.ImageUtil;
import com.test.admin.conurbations.views.CircleImageView;
import com.test.admin.conurbations.views.MaterialSearchView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;


public class MainActivity extends BaseActivity<ActivityMainBinding>
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private List<Fragment> mFragments;
    private CircleImageView mCircleImageView;
    private Bitmap mHeadPhotoBitmap;
    private Disposable subscribe;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle bundle) {
        subscribe = RxBus.getDefault().toObservable(Event.class).subscribe(event -> {
            if (event.eventType.equals(Constants.STATUE_BAR_COLOR)) {
                int barColor = (int) event.body;
                StatusBarUtils.setWindowStatusBarColor(getBaseActivity(), barColor);
            }
        });
        initToolbar(mBinding.toolbarMainToolbar, getResources().getString(R.string.app_name), getResources().getString(R.string.guard_msg));
        //根据定位获取天气信息
        getWeather();
        //初始化底部导航
        initMainBottomTab();
        //初始化主Activity(底部导航对应的Fragment)
        initAddFragment();
        //初始化左边侧滑栏
        initLeftDrawerToggleMenu();
    }

    @Override
    protected boolean isWipeBackEnabled() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mBinding.dlMainLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.dlMainLayout.closeDrawer(GravityCompat.START);
        } else if (mBinding.viewMainSearchMaterialSearch.isOpen()) {
            mBinding.viewMainSearchMaterialSearch.closeSearch();
        } else if (isNavigatingMain()) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        } else {
            super.onBackPressed();
        }
    }

    private void initAddFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(createFragment(new IndexFragment(), Constants.testColors[0]));
        mFragments.add(createFragment(new PictureFragment(), Constants.testColors[1]));
        mFragments.add(createFragment(new MusicMainFragment(), Constants.testColors[2]));
        mFragments.add(createFragment(new NBAFragment(), Constants.testColors[3]));
        mFragments.add(createFragment(new VideoFragment(), Constants.testColors[4]));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_main_content, mFragments.get(0))
                .commit();
    }

    private void initLeftDrawerToggleMenu() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.dlMainLayout, mBinding.toolbarMainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.dlMainLayout.addDrawerListener(toggle);
        toggle.syncState();

        //初始化左边侧滑栏上部分（头像部分）
        mCircleImageView = mBinding.nvMainView.getHeaderView(0).findViewById(R.id.circle_image_view);

        mBinding.nvMainView.setNavigationItemSelectedListener(this);
        mBinding.nvMainView.getHeaderView(0).setOnClickListener(this);
        mCircleImageView.setOnClickListener(this);
        mBinding.viewMainSearchMaterialSearch.setOnQueryTextListener(queryTextListener);
        mBinding.viewMainSearchMaterialSearch.adjustTintAlpha(0.8f);

        //处理换头像等逻辑
        mHeadPhotoBitmap = BitmapFactory.decodeFile(Constants.pathFileName);
        if (mHeadPhotoBitmap != null) {
            mCircleImageView.setImageBitmap(mHeadPhotoBitmap);
        } else {
            mCircleImageView.setImageResource(R.mipmap.my_bg);
        }
    }

    private void initMainBottomTab() {
        //用TabItemBuilder构建一个导航按钮
        mBinding.bnbMainView
                .addItem(new BottomNavigationItem(android.R.drawable.ic_menu_send, "干货").setActiveColor(Constants.toolBarColors[0]))
                .addItem(new BottomNavigationItem(android.R.drawable.ic_menu_compass, "美图").setActiveColor(Constants.toolBarColors[1]))
                .addItem(new BottomNavigationItem(android.R.drawable.ic_menu_crop, "音乐").setActiveColor(Constants.toolBarColors[2]))
                .addItem(new BottomNavigationItem(android.R.drawable.ic_menu_month, "体育").setActiveColor(Constants.toolBarColors[3]))
                .addItem(new BottomNavigationItem(android.R.drawable.ic_menu_rotate, "视频").setActiveColor(Constants.toolBarColors[4]))
                .setFirstSelectedPosition(0)
                .setMode(BottomNavigationBar.MODE_SHIFTING)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .initialise();

        mBinding.bnbMainView.setTabSelectedListener(tabItemSelectListener);
    }

    private Fragment createFragment(BaseFragment baseFragment, int content) {
        Bundle bundle = new Bundle();
        bundle.putInt("content", content);
        baseFragment.setArguments(bundle);
        return baseFragment;
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
            startActivity(ContactsActivity.class);
            return true;
        } else if (id == R.id.action_Image) {

        } else if (id == R.id.action_search) {
            int sp = mBinding.bnbMainView.getCurrentSelectedPosition();
            if (sp == 2) {
                Intent intent = new Intent(this, SearchMusicActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("is_playlist", true);
                startActivity(intent);
            } else {
                mBinding.viewMainSearchMaterialSearch.openSearch();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化左边侧滑栏menu并设置点击事件
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            startActivity(NudePhotosActivity.class);
        } else if (id == R.id.nav_fiction) {

        } else if (id == R.id.nav_slideshow) {//NBA直播

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_about) {//关于
            startActivity(AboutActivity.class);
        } else if (id == R.id.nav_feedback) {//意见反馈

        } else if (id == R.id.nav_clear_cache) {//清除缓存
            startActivity(OtherActivity.class);
        }

        mBinding.dlMainLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    BottomNavigationBar.OnTabSelectedListener tabItemSelectListener = new BottomNavigationBar.OnTabSelectedListener() {
        @Override
        public void onTabSelected(int position) {
            Log.d("onTabSelected", "onTabSelected: " + position);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mBinding.toolbarMainToolbar.setBackgroundColor(Constants.toolBarColors[position]);
            mBinding.nvMainView.setBackgroundColor(Constants.testColors[position]);
            mBinding.nvMainView.getHeaderView(0).setBackgroundColor(Constants.toolBarColors[position]);
            //transaction.setCustomAnimations(R.anim.push_up_in,R.anim.push_up_out);
            transaction.replace(R.id.fl_main_content, mFragments.get(position));
            transaction.commit();
        }

        @Override
        public void onTabUnselected(int position) {

        }

        @Override
        public void onTabReselected(int position) {

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

    private boolean isNavigatingMain() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fl_main_content);
        return (currentFragment instanceof MusicMainFragment || currentFragment instanceof IndexFragment
                || currentFragment instanceof PictureFragment || currentFragment instanceof NBAFragment
                || currentFragment instanceof VideoFragment);
    }

    private void getWeather() {
        String city = PrefUtils.getString(this, "city", "北京");
        GankService gankService = ApiManager.getInstance().create(GankService.class, Constants.BASE_PLAYER_URL);
        ApiManager.request(gankService.getCityWeather(city),
                new RequestCallBack<CityWeather>() {
                    @Override
                    public void success(CityWeather result) {
                        if (result != null && result.error == 0) {
                            CityWeather.ResultsBean resultsBean = result.results.get(0);
                            CityWeather.ResultsBean.WeatherDataBean weatherDataBean = resultsBean.weather_data.get(0);
                            initToolbar(mBinding.toolbarMainToolbar, resultsBean.currentCity,
                                    weatherDataBean.weather + "  " + weatherDataBean.temperature);
                        }
                    }

                    @Override
                    public void error(String msg) {
                    }
                });
    }

    //点击侧滑菜单选择图片

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        Bundle mPhotoBundle = picToView.getExtras();
        if (mPhotoBundle != null) {
            mHeadPhotoBitmap = mPhotoBundle.getParcelable("data");
            mCircleImageView.setImageBitmap(mHeadPhotoBitmap);
            ImageUtil.saveBitmap(mHeadPhotoBitmap, Constants.pathFileName);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.nv_main_view) {
            PhotoCameralUtil.showHendPhotoDialog(MainActivity.this, Constants.pathFileName);//调用选择头像的Dialog
        } else if (id == R.id.circle_image_view) {
            if (mHeadPhotoBitmap != null) {
                Intent intent = new Intent(MainActivity.this, PersonalInformationActivity.class);
                intent.putExtra(PersonalInformationActivity.PHOTOBUNDLE, mHeadPhotoBitmap);
                ActivityCompat.startActivity(MainActivity.this, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, mCircleImageView, TRANSLATE_VIEW).toBundle());
            } else {
                PhotoCameralUtil.showHendPhotoDialog(MainActivity.this, Constants.pathFileName);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
            subscribe = null;
        }
    }
}
