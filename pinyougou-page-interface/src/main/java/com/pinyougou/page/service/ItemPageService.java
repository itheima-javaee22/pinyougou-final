package com.pinyougou.page.service;

/**
 * 商品详细页面
 * @author 86189
 *
 */
public interface ItemPageService {
    /**
     * 生成商品详细页面
     * @param goodsId
     * @return
     */
	public boolean genItemHtml(Long goodsId);
	
	/**
	* 删除商品详细页
	* @param goodsId
	* @return
	*/
	public boolean deleteItemHtml(Long[] goodsIds);
}
