package com.pinyougou.search.service.impl;

import java.util.List;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.entity.TbItem;
import com.pinyougou.search.service.ItemSearchService;

@Component
public class ItemSearchListener implements MessageListener {

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage=(TextMessage) message;	
		try {		
			String text = textMessage.getText();
			System.out.println("监听到消息:"+text);
			List<TbItem> list = JSON.parseArray(text,TbItem.class);
			for (TbItem item : list) {
				System.out.println(item.getId()+""+item.getTitle());
				Map specMap = JSON.parseObject(item.getSpec());
				item.setSpecMap(specMap);
			}
			itemSearchService.importList(list);//导入solr
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
