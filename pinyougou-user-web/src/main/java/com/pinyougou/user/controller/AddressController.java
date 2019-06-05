package com.pinyougou.user.controller;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.TbAddress;
import com.pinyougou.user.service.AddressService;

import domain.PageResult;
import domain.Result;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/address")
public class AddressController {

	@Reference
	private AddressService addressService;

	/**
	 * 返回全部列表
	 *
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbAddress> findAll() {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();//获取用户名
		return addressService.findAll(userId);//根据用户名查询地址
	}


	/**
	 * 返回全部列表
	 *
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows) {
		return addressService.findPage(page, rows);
	}

	/**
	 * 增加
	 *
	 * @param address
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbAddress address) {
		try {
			address.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
			//验证收货人是否填写
			if (address.getContact() == null || "".equals(address.getContact())) {
				return new Result(false, "请填写收货人！");
			}
			//验证所在地区是否填写
			if (address.getProvinceId() == null || "".equals(address.getProvinceId()) || address.getCityId() == null || "".equals(address.getCityId()) || address.getTownId() == null || "".equals(address.getTownId())) {
				return new Result(false, "请完整填写所在地区！");
			}
			//验证详细地址是否填写
			if (address.getAddress() == null || "".equals(address.getAddress())) {
				return new Result(false, "请填写详细地址！");
			}
			//验证联系电话是否填写
			if (address.getMobile() == null || "".equals(address.getMobile())) {
				return new Result(false, "请填写联系电话！");
			}
			addressService.add(address);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}

	/**
	 * 修改
	 *
	 * @param address
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbAddress address) {
		try {
			addressService.update(address);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}

	/**
	 * 获取实体
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbAddress findOne(Long id) {
		return addressService.findOne(id);
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			addressService.delete(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}

	/**
	 * 查询+分页
	 *
	 * @param address
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbAddress address, int page, int rows) {
		return addressService.findPage(address, page, rows);
	}

	/**
	 * 设置默认地址
	 */
	@RequestMapping("/updateDefault")
	public Result updateDefault(Long addressId) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			addressService.updateDefault(addressId,userId);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}
}
