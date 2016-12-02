package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.models.DAO.DAOFactory;
import com.test.admin.conurbations.models.DAO.WelfareDAO;
import com.test.admin.conurbations.network.MyRequest;
import com.test.admin.conurbations.network.RequestCallback;
import com.test.admin.conurbations.network.RequestUrl;


/**
 * Created by waly6 on 2015/11/18.
 */
public class WelfarePresenter {

    private WelfareDAO welfareDAO;

    public WelfarePresenter() {
        welfareDAO = DAOFactory.getWelfareDAO();
    }


    public void getWelfareData() {
        MyRequest.getInstance().requestUrl(RequestUrl.URL_DATA_FULI,new RequestCallback() {
            @Override
            public void onResponse(Object o, int i) {
                welfareDAO.jsonToWelfare(o);
            }
        });
    }
}
