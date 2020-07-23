package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.INudeDetailListView;
import com.test.admin.conurbations.model.entity.PageDetail;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/11/18.
 */
public class NudeDetailListPresenter extends BasePresenter<INudeDetailListView> {

    @Inject
    public NudeDetailListPresenter() {
    }

    public void getNudeDetail(String url) {
        final PageDetail pageDetail = new PageDetail();

        Thread thread = new Thread(() -> {
            List<String> nudeDetail = pageDetail.getNudeDetail(url);
            mvpView.setNodeDetailData(nudeDetail);
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
