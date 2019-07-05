package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.entity.MenuModel;
import com.test.admin.conurbations.model.entity.PageModel;

import java.util.List;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface INudePhotosView {

    void setNodePhotoData(List<MenuModel> list);

    void setNodeDetailData(PageModel pageModel);

    void showError(String message);

    void setCacheNudePhotos(PageModel pageModel);
}
