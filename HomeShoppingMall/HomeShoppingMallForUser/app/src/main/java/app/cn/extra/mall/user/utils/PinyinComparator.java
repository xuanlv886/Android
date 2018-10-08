package app.cn.extra.mall.user.utils;


import java.util.Comparator;

import app.cn.extra.mall.user.vo.GetCityList;

/**
 * 根据拼音来排列ListView里面的数据类
 */
public class PinyinComparator implements Comparator<GetCityList.DataBean.OpenCityListBean> {
    @Override
    public int compare(GetCityList.DataBean.OpenCityListBean o1, GetCityList.DataBean.OpenCityListBean o2) {
        if (o1.getAcSortLetters().equals("@")
                || o2.getAcSortLetters().equals("#")) {
            return -1;
        } else if (o1.getAcSortLetters().equals("#")
                || o2.getAcSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getAcSortLetters().compareTo(o2.getAcSortLetters());
        }
    }

}
