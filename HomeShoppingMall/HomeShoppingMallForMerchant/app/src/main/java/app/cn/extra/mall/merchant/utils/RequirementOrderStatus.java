package app.cn.extra.mall.merchant.utils;

/**
 * 
 * desc：需求订单状态的枚举。
 * 若需求订单状态有改变时，需要修改该枚举类。
 * 修改时先行全局搜索类名
 * @author L
 * time:2018年5月15日
 */
public enum RequirementOrderStatus {
	
	UNCONFIRM("待确认", 0), 
	DOING("进行中", 1), 
	CPMPLETE("已完成", 2), 
	PICKING_UP_GOODS("取货中", 3),
	UN_CHECK_GOODS("待验货", 4),
	DELIVERY_GOODS("送货中", 5),
	EVALUATE("已评价", 6),
	UNRECEIPT("待接单", 7);
	
	
	private String name ;
	private int id ;
	 
	private RequirementOrderStatus( String name , int id ){
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
