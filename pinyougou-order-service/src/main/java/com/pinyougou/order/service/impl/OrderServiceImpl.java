package com.pinyougou.order.service.impl;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.entity.TbOrder;
import com.pinyougou.entity.TbOrderExample;
import com.pinyougou.entity.TbOrderExample.Criteria;
import com.pinyougou.entity.TbOrderItem;
import com.pinyougou.entity.TbPayLog;
import com.pinyougou.entitygroup.Cart;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.order.service.OrderService;

import domain.PageResult;
import util.IdWorker;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	
	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private TbPayLogMapper tbPayLogMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbOrder order) {
		//得到购物车数据
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());
		List<String> orderIdList=new ArrayList<>();//订单ID列表
		double total_money=0;//总金额（元）
		for (Cart cart : cartList) {
			long orderId = idWorker.nextId();
			System.out.println("sellerId:"+cart.getSellerId());
			TbOrder tborder=new TbOrder();//新创建订单对象
			tborder.setOrderId(orderId);//订单 ID
			tborder.setUserId(order.getUserId());//用户名
			tborder.setPaymentType(order.getPaymentType());//支付类型
			tborder.setStatus("1");//状态：未付款
			tborder.setCreateTime(new Date());//订单创建日期
			tborder.setUpdateTime(new Date());//订单更新日期
			tborder.setReceiverAreaName(order.getReceiverAreaName());//地址
			tborder.setReceiverMobile(order.getReceiverMobile());//手机号
			tborder.setReceiver(order.getReceiver());//收货人
			tborder.setSourceType(order.getSourceType());//订单来源
			tborder.setSellerId(cart.getSellerId());//商家 ID 
			//循环购物车明细
			double money=0;
			for (TbOrderItem orderItem : cart.getOrderItemList()) {
				orderItem.setId(idWorker.nextId());
				orderItem.setOrderId(orderId);
				orderItem.setSellerId(cart.getSellerId());
				money+=orderItem.getTotalFee().doubleValue();//总金额
				orderItemMapper.insert(orderItem);
			}
			tborder.setPayment(new BigDecimal(money));
			orderMapper.insert(tborder);
			orderIdList.add(orderId+"");//添加到订单列表
			total_money+=money;//总金额
		}
		if("1".equals(order.getPaymentType())) {//如果是微信支付
			TbPayLog payLog = new TbPayLog();
			String outTradeNo = idWorker.nextId()+"";//支付订单号
			payLog.setOutTradeNo(outTradeNo);//支付订单号
			payLog.setCreateTime(new Date());
			//订单号列表，逗号分隔
			String ids=orderIdList.toString().replace("[", "").replace("]","").replace(" ", "");
			payLog.setOrderList(ids);//订单号列表，逗号分离
			payLog.setPayType("1");//支付类型
			payLog.setTotalFee((long)(total_money*100));//总金额
			payLog.setTradeState("0");//支付状态
			payLog.setUserId(order.getUserId());//用户ID
			tbPayLogMapper.insert(payLog);
			redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);
			
			                     
		}
		redisTemplate.boundHashOps("cartList").delete(order.getUserId());	
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
			if(order.getOrderId()!=null ){
				criteria.andOrderIdEqualTo(order.getOrderId());
			}
	
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		@Override
		public TbPayLog searchPayLogFromRedis(String userId) {
			
			return (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
		}

		@Override
		public void updateOrderStatus(String out_trade_no, String transaction_id) {
			//修改支付日志状态
			TbPayLog payLog = tbPayLogMapper.selectByPrimaryKey(out_trade_no);
			payLog.setPayTime(new Date());
			payLog.setTradeState("1");//已支付
			payLog.setTransactionId(transaction_id);//交易号
			tbPayLogMapper.updateByPrimaryKey(payLog);
			//修改订单状态
			String orderList=payLog.getOrderList();//获取订单号列表
			String[] orderIds=orderList.split(",");//获取订单号数组
			for (String orderId : orderIds) {
				TbOrder order = orderMapper.selectByPrimaryKey(Long.parseLong(orderId));
				if(order!=null) {
					order.setStatus("2");//已付款
					orderMapper.updateByPrimaryKey(order);
				}
			}
			//清除redis数据
			redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
		}

		@Override
		public List<TbOrder> findByTime(String startDate, String endDate) {
			TbOrderExample example = new TbOrderExample();
			Criteria criteria = example.createCriteria();
			startDate = startDate+" 00:00:00";
			endDate = endDate+" 00:00:00";
			Timestamp date_start = Timestamp.valueOf(startDate);
			Timestamp date_end = Timestamp.valueOf(endDate);
			System.out.println("date_start"+date_start);
			System.out.println("end_start"+date_end);
		
	    	criteria.andCreateTimeBetween(date_start, date_end);
			List<TbOrder> orderList = orderMapper.selectByExample(example);
			for (TbOrder tbOrder : orderList) {
				System.out.println("时间内的orderId:"+tbOrder.getOrderId());
			}
			return orderList;
		}

		
}
