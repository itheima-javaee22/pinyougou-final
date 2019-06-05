package com.pinyougou.cart.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.TbPayLog;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeChatPayService;

import domain.Result;

/**
 * 支付控制层
 * @author 86189
 *
 */
@RestController
@RequestMapping("/pay")
public class PayController {

	@Reference
	private WeChatPayService weChatPayService;
	
	@Reference
	private OrderService orderService;
	
	/**
	 * 生成二维码
	 * @return
	 */
	@RequestMapping("/createNative")
	public Map createNative() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		TbPayLog payLog = orderService.searchPayLogFromRedis(username);
		if(payLog!=null) {
			return weChatPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee()+"");
		}else {
			return new HashMap<>();
		}
		
	}
	
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String out_trade_no) {
		int x=0;
		Result result = null;
		while(true) {
			//调用查询接口
			Map<String, String> map = weChatPayService.queryPayStatus(out_trade_no);
			if(map==null) {
				result = new Result(false, "支付出错");
				break;
			}
			if(map.get("trade_state").equals("SUCCESS")) {//如果成功
				result = new Result(true, "支付成功");
				//修改订单状态
				orderService.updateOrderStatus(out_trade_no, map.get("transaction_id"));
				break;
				
			}
			try {
				Thread.sleep(3000);
			}catch (Exception e) {
				e.printStackTrace();
			}
			x++;
			if(x>=100) {
				result = new Result(false, "支付超时");
				break;
			}
		}
		return result;
	}
	
	
}
