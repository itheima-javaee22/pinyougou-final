package com.pinyougou.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.entitygroup.Cart;

import domain.Result;

@RestController
@RequestMapping("/cart")
public class CartController {
  
	@Reference(timeout=6000)
	private CartService cartService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	/**
	 * 购物车列表
	 * @return
	 */
	@RequestMapping("/findCartList")
	public List<Cart> findCartList(){
		//当前登录人账号
     	String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("当前登录人："+username);
		//读取本地购物车
		String cartListString = util.CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		if(cartListString==null || cartListString.equals("")) {
			cartListString="[]";
		}
		List<Cart> cartList_cookie=JSON.parseArray(cartListString,Cart.class);
		if(username.equals("anonymousUser")) {//如果没登录	
			return cartList_cookie;
		}else {//已登录
			List<Cart> cartList_redis=cartService.findCartListFormRedis(username);//从redis中获取数据
			if(cartList_cookie.size()>0) {//如果本地存在购物车
				//合并购物车
				cartList_redis=cartService.mergeCartList(cartList_redis, cartList_cookie);
				//清除本地cookie的数据
				util.CookieUtil.deleteCookie(request, response, "cartList");
				//将合并后的数据存入redis
				cartService.saveCartList(username, cartList_redis);
			}
			return cartList_redis;
			
		}
		
	
	}
	/**
	 * 添加商品到购物车
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping("/addGoodsToCartList")
	@CrossOrigin(origins="http://localhost:9105")
	public Result addGoodsToCartList(Long itemId,Integer num) {
		
		//response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
		//response.setHeader("Access-Control-Allow-Credentials", "true");
		
		//当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("当前登录人："+username);
		try {
			List<Cart> cartList = findCartList();//获取购物车列表
			cartList = cartService.addGoodsToCartList(cartList, itemId, num);
			if(username.equals("anonymousUser")) {//没登录，保存到cookie
				util.CookieUtil.setCookie(request, response, "cartList", 
						JSON.toJSONString(cartList), 3600*24, "UTF-8");
				System.out.println("向cookie存入数据");
			}else {
				cartService.saveCartList(username, cartList);
			}
			
			return new Result(true, "添加到购物车成功");
		}catch (RuntimeException e) {
			e.printStackTrace();
			return new Result(false, e.getMessage());
			}catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "添加失败");
			}
	}
}
