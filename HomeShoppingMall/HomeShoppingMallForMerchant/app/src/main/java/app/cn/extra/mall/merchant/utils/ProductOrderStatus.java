package app.cn.extra.mall.merchant.utils;

/**
 * 
 * desc：商品订单状态的枚举。
 * 若商品订单状态有改变时，需要修改该枚举类。
 * 修改时先行全局搜索类名
 * @author L
 * time:2018年5月15日
 */
public enum ProductOrderStatus {
	// 0--待付款，1--待发货，2--待收货，3--待评价，4--已完成，5--待退款，6--已退款
	UNPAY("待付款", 0), 
	UNDELIVER("待发货", 1), 
	UNCOLLECT("待收货", 2), 
	UNEVALUATE("待评价", 3), 
	COMPLETE("已完成", 4),
	UNREFUND("待退款", 5),
	REFUND("已退款", 6);
	
	private String name ;
	private int id ;
	 
	private ProductOrderStatus( String name , int id ){
	    this.name = name ;
	    this.id = id ;
	}
	 
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public int getId() {
	    return id;
	}
	public void setId(int id) {
	    this.id = id;
	}
}
