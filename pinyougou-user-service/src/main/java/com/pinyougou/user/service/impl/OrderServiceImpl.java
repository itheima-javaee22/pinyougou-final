package com.pinyougou.user.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.entity.TbItem;
import com.pinyougou.entity.TbOrder;
import com.pinyougou.entity.TbOrderExample;
import com.pinyougou.entity.TbOrderExample.Criteria;
import com.pinyougou.entity.TbOrderItem;
import com.pinyougou.entity.TbOrderItemExample;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.user.service.OrderService;

import domain.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	@Autowired
	private TbOrderItemMapper orderItemMapper;

	@Autowired
	private TbItemMapper itemMapper;

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(String userId,String status,int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		//根据用户ID查询订单
		TbOrderExample orderExample = new TbOrderExample();
		TbOrderExample.Criteria criteria = orderExample.createCriteria( );
		criteria.andUserIdEqualTo( userId );

		if (!status.equals("0")){
			if (status.equals( "1" )){
				criteria.andStatusEqualTo( "1");
			}else if (status.equals( "2" )){
				criteria.andStatusBetween( "2","3" );
			}else if (status.equals( "3" )){
				criteria.andStatusEqualTo( "4");
			}else if (status.equals( "4" )){
				criteria.andStatusBetween( "5","7" );
			}
		}
		List<TbOrder> orders = orderMapper.selectByExample( orderExample );

		//根据订单ID查询订单详情
		for (TbOrder order : orders) {
			TbOrderItemExample orderItemExample = new TbOrderItemExample();
			TbOrderItemExample.Criteria criteria1 = orderItemExample.createCriteria( );
			criteria1.andOrderIdEqualTo(order.getOrderId() );
			List<TbOrderItem> orderItems = orderItemMapper.selectByExample( orderItemExample );
			//设置商品详情
			for (TbOrderItem item : orderItems) {
				TbItem tbItem = itemMapper.selectByPrimaryKey( item.getItemId( ) );
				item.setItem( tbItem );
			}
			order.setOrderItemList( orderItems );
		}


		Page<TbOrder> page=(Page<TbOrder>) orderMapper.selectByExample(orderExample);


		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbOrder order) {
		orderMapper.insert(order);		
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
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}


	/**
	 * 分页
	 * @param userId
	 * @param status
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Map findAllPage(String userId, String status, int pageNum, int pageSize){
		PageHelper.startPage(pageNum, pageSize);

		Map map=new HashMap( );
		//根据用户ID查询订单
		TbOrderExample orderExample = new TbOrderExample();
		TbOrderExample.Criteria criteria = orderExample.createCriteria( );
		criteria.andUserIdEqualTo( userId );
		criteria.andStatusIsNotNull();
		if (!status.equals("0")){
			if (status.equals( "1" )){
				criteria.andStatusEqualTo( "1");
			}else if (status.equals( "2" )){
				criteria.andStatusBetween( "2","3" );
			}else if (status.equals( "3" )){
				criteria.andStatusEqualTo( "4");
			}else if (status.equals( "4" )){
				criteria.andStatusBetween( "5","7" );
			}
		}

		Page<TbOrder> page=(Page<TbOrder>) orderMapper.selectByExample(orderExample);
		List<TbOrder> result = page.getResult( );
		//根据订单ID查询订单详情
		for (TbOrder order : result) {
			TbOrderItemExample orderItemExample = new TbOrderItemExample();
			TbOrderItemExample.Criteria criteria1 = orderItemExample.createCriteria( );
			criteria1.andOrderIdEqualTo(order.getOrderId() );
			List<TbOrderItem> orderItems = orderItemMapper.selectByExample( orderItemExample);
			//设置商品详情
			for (TbOrderItem item : orderItems) {
				TbItem tbItem = itemMapper.selectByPrimaryKey( item.getItemId( ) );
				String spec = tbItem.getSpec( );
				spec=spec.replace( "{", "" );
				spec=spec.replace( "}", "" );
				spec=spec.replace( ":", "" );
				spec=spec.replace( '"', ' ' );
				tbItem.setSpec( spec );
				item.setItem( tbItem );
			}
			order.setOrderItemList( orderItems );
		}
		int pages = page.getPages( );
		long total = page.getTotal( );
		page.getPageNum();

		map.put( "orderList",result );
		map.put( "pages",pages );
		map.put( "total",total );
		map.put( "page",page );
		return map;
	}

	
}
