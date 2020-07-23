package com.test.admin.conurbations.model.response;


import com.test.admin.conurbations.model.entity.Base;

import java.util.List;

/**
 * Created by ZQiong on 2019/6/11.
 */
public class GankImageData extends Base implements GankItem {
    public List<GankGirlImageItem> data;
}
