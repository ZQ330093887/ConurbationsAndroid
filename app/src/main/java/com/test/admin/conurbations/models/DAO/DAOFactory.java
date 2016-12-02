package com.test.admin.conurbations.models.DAO;

/**
 * Created by waly6 on 2015/10/22.
 */
public class DAOFactory {

    public static final void clearDAOs() {

        WelfareDAO.instance().clear();
    }


    public static final WelfareDAO getWelfareDAO(){
        return WelfareDAO.instance();
    }
}
