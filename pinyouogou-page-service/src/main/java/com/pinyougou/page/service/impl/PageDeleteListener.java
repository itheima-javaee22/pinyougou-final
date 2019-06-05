package com.pinyougou.page.service.impl;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.page.service.ItemPageService;

@Component
public class PageDeleteListener implements MessageListener {

	@Autowired
	private ItemPageService itemPageService;
	
	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage)message;
		try {
			Long[] goodsIds=(Long[]) objectMessage.getObject();
			System.out.println("ItemDeleteListener监听接收到消息:"+goodsIds);
			boolean flag = itemPageService.deleteItemHtml(goodsIds);
			System.out.println("删除页面结果："+flag);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
