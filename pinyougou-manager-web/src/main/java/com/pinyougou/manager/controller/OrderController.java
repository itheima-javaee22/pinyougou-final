package com.pinyougou.manager.controller;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.TbOrder;
import com.pinyougou.order.service.OrderService;

import domain.Excel;
import domain.PageResult;
import domain.Result;
import util.ExportExcelUtil;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/order")
public class OrderController {

	@Reference
	private OrderService orderService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbOrder> findAll(){			
		return orderService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return orderService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param order
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbOrder order){
		try {
			orderService.add(order);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param order
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbOrder order){
		try {
			orderService.update(order);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbOrder findOne(Long id){
		return orderService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			orderService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbOrder order, int page, int rows  ){
		return orderService.findPage(order, page, rows);		
	}
	
	/**
	 * 将数据导出至excel
	 * @param ids
	 * @return
	 */
	@RequestMapping("/exportExcel")
	public Result exportExcel(Long[] ids) {
		System.out.println("进入controller");
	try {
		ExportExcelUtil<Excel> util = new ExportExcelUtil<Excel>();
		 // 准备数据
        List<Excel> excelList = new ArrayList<Excel>();
		for (Long id : ids) {
			
			TbOrder order = orderService.findOne(id);
		
			
			Excel new_order = new Excel(order.getOrderId()+"",order.getUserId()+"",order.getReceiver()+"",order.getReceiverMobile()+"",
					order.getPayment()+"",order.getPaymentType()+"",order.getSourceType()+"",order.getStatus()+"");	
	        //判断支付方式，将字段值转化为相应描述值	
			if("1".equals(new_order.getPaymentType())) {
				new_order.setPaymentType("在线支付");
			}else if("2".equals(new_order.getPaymentType())){
				new_order.setPaymentType("货到付款");
			}else {
				new_order.setPaymentType("无");
			}
			//判断订单来源,将字段值转化为相应描述值	
			if("1".equals(new_order.getSourceType())) {
				new_order.setSourceType("app端");
			}else if("2".equals(new_order.getSourceType())) {
				new_order.setSourceType("pc端");
			}else if("3".equals(new_order.getSourceType())) {
				new_order.setSourceType("M端");
			}else if("4".equals(new_order.getSourceType())) {
				new_order.setSourceType("微信端");
			}else if("5".equals(new_order.getSourceType())) {
				new_order.setSourceType("手机qq端");
			}else {
				new_order.setSourceType("无");
			}
			//判断状态,将字段值转化为相应描述值	
			if("1".equals(new_order.getStatus())) {
				new_order.setStatus("未付款");
			}else if("2".equals(new_order.getStatus())) {
				new_order.setStatus("已付款");
			}else if("3".equals(new_order.getStatus())) {
				new_order.setStatus("未发货");
			}else if("4".equals(new_order.getStatus())) {
				new_order.setStatus("已发货");
			}else if("5".equals(new_order.getStatus())) {
				new_order.setStatus("交易成功");
			}else if("6".equals(new_order.getStatus())) {
				new_order.setStatus("交易关闭");
			}else if("7".equals(new_order.getStatus())) {
				new_order.setStatus("待评价");
			}else {
				new_order.setStatus("无");
			}
			System.out.println("new_order是："+new_order.getOrderId()+new_order.getUserId()+new_order.getReceiver()+new_order.getReveiverMobile()+new_order.getPayment()+new_order.getPaymentType()+new_order.getSourceType()+new_order.getStatus());
			excelList.add(new_order);
		}
			
		    
       String[] columnNames = { "订单编号", "用户账号", "收货人","手机号","订单金额","支付方式","订单来源","订单状态" };
       
	   util.exportExcel("用户导出", columnNames, excelList, new FileOutputStream("D:/order.xls"), ExportExcelUtil.EXCEL_FILE_2003);
		  
	   return new Result(true, "数据导出至excel成功");
	   } catch (Exception e) {
		   e.printStackTrace();
		 return new Result(false, "数据导出至excel失败"); 			 
	      }
		
	}
	
	@RequestMapping("/findByTime")
	public List<TbOrder> findByTime(String startData,String endData){
		System.out.println("startData"+startData);
		System.out.println("endData"+endData);
		return orderService.findByTime(startData, endData);
	}
	
	
	
	
	
}
