package com.pinyougou.manager.controller;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.entity.TbGoods;
import com.pinyougou.entity.TbItem;
import com.pinyougou.entitygroup.Goods;

import domain.PageResult;
import domain.Result;
import service.GoodsService;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	//@Reference(timeout=40000)
	//private ItemPageService itemPageService;
	
	//@Reference
	//private ItemSearchService itemSearchService;
	
	@Reference
	private GoodsService goodsService;
	
	@Autowired
	private Destination queueSolrDestination;//用于发送solr导入的消息
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows){
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
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
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	@Autowired
	private Destination queueSolrDeleteDestination;//用户索引库中删除记录
	@Autowired
	private Destination topicPageDeleteDestination;//用于删除静态网页的消息
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			//itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			jmsTemplate.send(queueSolrDeleteDestination,new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});
			//删除页面
			jmsTemplate.send(topicPageDeleteDestination,new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}
	
	@Autowired
	private Destination topicPageDestination;//用于生成商品详细页的消息目标(发布订阅)
	/**
	 * 更新状态
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids,String status) {
		try {
			goodsService.updateStatus(ids, status);
			//按照SPU ID查询SKU列表（如果审核通过）
			if(status.equals("1")) {
				List<TbItem> itemList = goodsService.findItemListByGoodsIdAndStatus(ids, status);
				//调用搜索接口实现数据批量导入至solr
				
					//itemSearchService.importList(itemList);
					final String jsonString = JSON.toJSONString(itemList);
					System.out.println("jsonString"+jsonString);
					jmsTemplate.send(queueSolrDestination,new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							System.out.println("进入createMessage了");
							return session.createTextMessage(jsonString);
						}
					});
					
				//静态页面生成
				for(final Long goodsId:ids) {
					//itemPageService.genItemHtml(goodsId);
					jmsTemplate.send(topicPageDestination,new MessageCreator() {
						
						@Override
						public Message createMessage(Session session) throws JMSException {					
							return session.createTextMessage(goodsId+"");
						}
					});
				}
			}
			
			return new Result(true,"更新状态成功");
		}catch(Exception e) {
			e.printStackTrace();
			return new Result(false,"更新状态失败");
		}
	}
	
	/**
	 * 生成静态页（测试）
	 * @param goodsId
	 */
	@RequestMapping("/genHtml")
	public void genHtml(Long goodsId) {
		//itemPageService.genItemHtml(goodsId);
	}
	
}
