package com.pinyougou.pay.service;
/**
 * 微信支付接口
 * @author 86189
 *
 */

import java.util.Map;

public interface WeChatPayService {

	/**
	 * 生成二维码
	 * @param out_trade_no
	 * @param total_fee
	 * @return
	 */
	public Map createNative(String out_trade_no,String total_fee);
	
	/**
	 * 查询支付状态
	 * @param out_trade_no
	 * @return
	 */
	public Map queryPayStatus(String out_trade_no);
	
	/**
	 * 关闭支付
	 * @param out_trade_no
	 * @return
	 */
	public Map closePay(String out_trade_no);

}
