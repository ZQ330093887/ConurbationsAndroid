package com.test.admin.conurbations.models.DAO;

import com.test.admin.conurbations.models.VO.Welfare;
import com.test.admin.conurbations.utils.JsonUtil;

import java.util.List;

/**
 * Created by zhouqiong on 2015/10/30.
 */
public class WelfareDAO {


    private List<Welfare> repaymentListList;

    private static WelfareDAO welfareDAO;

    private WelfareDAO() {

    }

    public static WelfareDAO instance() {
        if (welfareDAO == null) {
            synchronized (WelfareDAO.class) {
                if (welfareDAO == null) {
                    welfareDAO = new WelfareDAO();
                }
            }
        }
        return welfareDAO;
    }


    public List<Welfare> getRepaymentListList() {
        return repaymentListList;
    }

    public void setRepaymentListList(List<Welfare> repaymentListList) {
        this.repaymentListList = repaymentListList;
    }

    public List<Welfare> jsonToWelfare(Object object) {
        Welfare createLoan = JsonUtil.get(object, Welfare.class);
        return null;
    }


    public void clear() {
        welfareDAO = null;
    }
}
