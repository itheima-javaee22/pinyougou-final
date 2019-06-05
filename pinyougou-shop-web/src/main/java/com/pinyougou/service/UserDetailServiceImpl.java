package com.pinyougou.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pinyougou.entity.TbSeller;
import service.SellerService;

/**
 * 认证类
 * 
 * @author Trik
 *
 */
public class UserDetailServiceImpl implements UserDetailsService {

	private SellerService sellerService;	
	
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
		}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    System.out.println("来了");
	    
	    
		List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		TbSeller seller = sellerService.findOne(username);
		System.out.println(seller.getPassword());
		if(seller!=null) {
		  if(seller.getStatus().equals("1")) {
			  return new User(username,seller.getPassword(),grantedAuths);
		  }else {
			  return null;
		  }
		}else {
			return null;
		}
		
	}

}
