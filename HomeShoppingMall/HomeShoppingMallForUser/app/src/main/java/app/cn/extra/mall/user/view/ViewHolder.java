package app.cn.extra.mall.user.view;


import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ViewHolder {

    // 首页
    public LinearLayout ll_item_recommend;
    public ImageView iv_item_recommend_one;
    public ImageView iv_item_recommend_two;
    public ImageView iv_item_recommend_three;
    public ImageView iv_item_recommend_four;

    //便捷购物
    public TextView tv_list_item_big_type;
    public TextView tv_grid_item_product_name;
    public ImageView img_grid_item_product_pic;

    //生活服务
    public ImageView iv_service_type_pic;
    public TextView tv_service_type_name;
    public ImageView iv_serview_content;


    // 筛选
    public TextView tv_industry_inside_common_group_title;
    public ImageView img_industry_inside_common_group_more;
    // 上传商品
    public CheckBox ck_item_support;
    // 行业
    public TextView tv_grid_item_tag;
    //自营商场商品大类列表
    public TextView item_add_brand;
    public TextView tv_nature_item_tag;
    //商品列表
    public TextView item_tv_shop_name;
    public TextView item_tv_shop_price;
    public TextView item_tv_shop_introduce;
    public ImageView item_iv_shop_pic;
    public ImageView item_iv_shop_car;
    public TextView item_tv_shop_recommend;
    public RelativeLayout item_rl_shop_recommend;
    public TextView tv_item_num;
    public TextView tv_money_ago;

    //订单管理界面-房间预定
    public ImageView iv_room_icon;
    public ImageView iv_room_type;
    public TextView tv_room_name;
    public TextView tv_room_price;
    public TextView tv_room_content;
    public TextView tv_room_date_check;
    public TextView tv_room_date_checkout;
    public TextView tv_room_nums;

    //订单管理界面-便捷购物
    public ImageView iv_all_order_icon;
    public TextView tv_all_order_name;
    public TextView tv_all_order_content;
    public TextView tv_all_order_current_price;
    public TextView tv_all_order_original_price;
    public TextView tv_all_order_num;
    public TextView tv_all_order_total_num;
    public TextView tv_all_order_total_price;
    public TextView tv_order_status;

    //个人中心-基隆会员
    public ImageView iv_item_house_icon;
    public ImageView iv_item_commodity;
    public TextView iv_item_house_name;

    //个人中心-我的客服
    public TextView tv_item_more_type;
    public TextView tv_item_more_content;

    //账号信息-地址管理
    public TextView tv_item_address_name;
    public TextView tv_item_address_phone;
    public TextView tv_item_address_detail;
    public TextView tv_item_address_del;
    public TextView tv_item_address_edit;
    public CheckBox ck_item_address_default;

    //首页-选择地址
    public ImageView iv_time_destination;
    public TextView tv_time_destination;


    // 货款通用界面 (待付款、待发货、待收货、退货/退款)
    public TextView tv_item_payment_store_name;
    public TextView tv_item_payment_send_time;
    public TextView tv_item_payment_goods_prices;
    public ImageView img_item_payment_goods_pic;
    public TextView tv_item_payment_goods_name;
    public TextView tv_item_payment_goods_nums;
    public TextView tv_item_payment_goods_moneys;
    public TextView tv_item_payment_goods_price;
    public TextView tv_item_payment_goods_content;
    public TextView tv_item_payment_goods_num;
    public Button btn_item_payment_goods_one;
    public Button btn_item_payment_goods_two;
    public RelativeLayout rl_bottom;
    public TextView tv_item_payment_goods_time;
    public TextView tv_item_payment_goods_order;
    public TextView tv_item_iv_goods_nums;
    public RelativeLayout rl_unpay;
    public Button btn_item_Unpay_goods_one;
    public Button btn_item_Unpay_goods_two;
    public Button btn_item_Unpay_goods_three;
    public RelativeLayout rl_content;

    // 收藏界面  房间收藏
    public ImageView img_item_collect_room_pic;
    public TextView tv_item_collect_goods_name;
    public TextView tv_item_collect_room_price;
    public TextView tv_item_collect_room_content;
    public Button btn_item_collect_room_one;


    //便捷购物  搜索界面
    public TextView list_item_history;

    //查找房间
    public TextView tv_item_function_context;

    //酒店详情介绍
    public TextView tv_item_hotel_service;
    public ImageView iv_item_hotel_service;

    // 店铺
    public ImageView img_grid_item_store;
    public TextView tv_grid_item_store_name;
    public TextView tv_grid_item_store_moneys;
    public TextView tv_grid_item_store_buy;
    // 意见反馈
    public CheckBox cb_state;
    // 我的客服
    public TextView tv_item_problem_name;
    public ImageView tv_item_problem_icon;
    public TextView tv_item_name;
    public TextView tv_item_content;
    // 商品详情
    public ImageView img_item_goods_pingjia_header;
    public ImageView iv_item_pingjia_pic;
    public TextView tv_item_goods_pingjia_name;
    public TextView tv_item_goods_pingjia_content;
    public TextView tv_item_goods_reply_content;
    public TextView tv_list_item_goods_classify_key;
    public TextView tv_list_item_goods_classify_value;
    public NewsGridView gv_pingjia_pic;

    // 我的房产
    public ImageView iv_huose_pic;
    public TextView tv_house_name;
    public TextView tv_house_content;
    public TextView tv_house_content_city;
    public TextView tv_house_type;
    // 支付页面
    public ImageView img_item_shop_pic;
    public TextView item_tv_goods_name;
    public TextView item_tv_goods_price;
    public TextView item_tv_goods_introduce;
    public TextView item_iv_goods_nums;
    // 优惠券
    public TextView tv_coupon_money;
    public TextView tv_coupon_content;
    public TextView tv_unused_type;
    public TextView tv_unused_time;
    public TextView tv_unused_use;
    public TextView tv_unused_abort_time;
    public LinearLayout ll_unused;
    public RelativeLayout rl_coupon;
    // 查找房间
    public TextView tv_item_room_type;
    public TextView tv_item_contents;
    public TextView tv_item_money;
    public TextView tv_item_room_number;
    public TextView tv_service_room_name;
    public ImageView iv_service_room_pic;
    public TextView tv_item_function_name;
    public TextView tv_item_room_num;
    public TextView tv_item_reserve;
    // 生活服务订单
    public ImageView iv_item_service_pic;
    public TextView tv_item_service_name;
    public TextView tv_item_service_money;
    public TextView tv_item_service_pay;
    public Button btn_item_service_delete;
    public TextView tv_item_service_time;
    // 获取所有可租汽车
    public ImageView iv_item_car;
    public TextView tv_item_car_name;
    public TextView tv_item_car_context;
    public TextView tv_item_car_money;
    public TextView tv_car_pay_type;
    public TextView tv_grid_item_tags;
    // 租车服务中  支付页面
    public ImageView iv_item_pay_pic;
    public TextView tv_item_pay_name;
    public CheckBox ck_item_pay_type;
    public RelativeLayout rl_item_pay_type;
    // 租车服务中  支付页面
    public TextView tv_record_prize_name;
    public TextView tv_record_prize_content;
    public TextView tv_record_prize_time;
    // 订单详情
    public ImageView iv_item_good_icon;
    public TextView tv_good_name;
    public TextView tv_item_good_current_price;
    public TextView tv_item_coupon_money;
    public TextView tv_item_good_content;
    public TextView tv_item_good_original_price;
    public TextView tv_item_all_order_num;
    public TextView tv_item_good_num;
    public TextView tv_item_good_total_price;
    public TextView tv_item_question;
    // 欠费清单
    public TextView tv_arrear_name;
    public TextView tv_arrear_money;
    public TextView tv_arrear_content;
    public TextView tv_arrear_type;
    // 积分抽奖
    public TextView tv_prize;
    // 费用明细
    public TextView tv_cast_name;
    public TextView tv_cast_money;
    public TextView item_tv_detail_time;
    public TextView item_tv_detail_nums;
    public TextView item_tv_detail_money;












}
