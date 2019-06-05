package com.pinyougou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.entity.TbItem;
import com.pinyougou.search.service.ItemSearchService;
@Service(timeout=5000)
public class ItemSearchServiceImpl implements ItemSearchService {

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	@Override
	public Map search(Map searchMap) {
		//关键字空格处理
		String keywords = (String) searchMap.get("keywords");
		searchMap.put("keywords", keywords.replace(" ", ""));
		Map map=new HashMap();
		//查询列表
		map.putAll(searchList(searchMap));
		//分组查询商品分类列表
		List<String> categoryList = searchCategoryList(searchMap);
		map.put("categoryList", categoryList);
		//查询品牌和规格列表
		String category = (String) searchMap.get("category");
	    if(category.equals("")) {
	    	if(categoryList.size()>0) {
				map.putAll(searchBrandAndSpecList(categoryList.get(0)));
			}
	    }else {
	    	map.putAll(searchBrandAndSpecList(category));
	    }
		
		
		return map;
	}
	
	private Map searchList(Map searchMap) {
		Map map = new HashMap<>();
		/*Query query=new SimpleQuery("*:*");
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);*/
		HighlightQuery query = new SimpleHighlightQuery();
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");//设置高亮的域
		highlightOptions.setSimplePrefix("<em style='color:red'>");
		highlightOptions.setSimplePostfix("</em>");
		query.setHighlightOptions(highlightOptions);//设置高亮选项
		
		//按照关键字查询
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		//按商品分类过滤
		if(!"".equals(searchMap.get("category"))) {
			Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
			FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		//按品牌过滤
		if(!"".equals(searchMap.get("brand"))) {
			Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
			FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		//按规格过滤
		if(searchMap.get("spec")!=null) {
			Map<String, String> specMap = (Map<String,String>) searchMap.get("spec");
			for (String key : specMap.keySet()) {
				FilterQuery filterQuery = new SimpleFilterQuery();
				Criteria filterCriteria = new Criteria("item_spec_"+key).is(specMap.get(key));
				query.addCriteria(filterCriteria);
			}
		}
		
		//按价格过滤
		if(!"".equals(searchMap.get("price"))) {
			String[] price = ((String)searchMap.get("price")).split("-");
			if(!price[0].equals("0")) {//区间起点不等于0
				Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
				FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		    if(!price[1].equals("*")) {//区间终点不等于*
			    Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);  
		        FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
		        query.addFilterQuery(filterQuery);
		    }
		}
		
		//分页查询
		Integer pageNo = (Integer) searchMap.get("pageNo");//提取页码
		if(pageNo == null) {
			pageNo=1;
		}
		Integer pageSize = (Integer) searchMap.get("pageSize");//每页记录数
		if(pageSize == null) {
			pageSize=20;
		}
		query.setOffset((pageNo-1)*pageSize);
		query.setRows(pageSize);
		
		//排序
		String sortValue = (String) searchMap.get("sort");//ASC DESC
		String sortField = (String) searchMap.get("sortField");
		System.out.println("sortValue是："+sortValue+",sortField是："+sortField);
		if(sortValue!=null && !sortValue.equals("")) {
			if (sortValue.equals("ASC")) {
			Sort sort = new Sort(Sort.Direction.ASC,"item_"+sortField);
			query.addSort(sort);
			}
			if (sortValue.equals("DESC")) {
				Sort sort = new Sort(Sort.Direction.DESC,"item_"+sortField);
				query.addSort(sort);
			}
	
			
		}
		
		//获取高亮结果
		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        for (HighlightEntry<TbItem> highlightEntry : page.getHighlighted()) {
			TbItem item = highlightEntry.getEntity();//获取原实体类
			if(highlightEntry.getHighlights().size()>0 && highlightEntry.getHighlights().get(0).getSnipplets().size()>0) {
				String title = highlightEntry.getHighlights().get(0).getSnipplets().get(0);
				item.setTitle(title);
			}				
		} 
        
        
        map.put("rows", page.getContent());
        map.put("totalPages", page.getTotalPages());//总页数
        map.put("total", page.getTotalElements());//总记录数
        return map;
	}

	/**
	 * 分组查询商品分类列表
	 * @param searchMap
	 * @return
	 */
	private List<String> searchCategoryList(Map searchMap){
		List<String> list = new ArrayList<>();
		Query query = new SimpleQuery("*:*");
		//根据关键字查询
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));//where 
		query.addCriteria(criteria);
		//设置分组选项
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");//group by
		query.setGroupOptions(groupOptions);
		//获取分组页
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
		//获取分组结果对象
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		//获取分组入口页
		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
		//获取分组入口集合
		List<GroupEntry<TbItem>> entryList = groupEntries.getContent();
		for (GroupEntry<TbItem> entry : entryList) {
			//将分组的结果添加到返回值中
			list.add(entry.getGroupValue());
		}
		return list;
	}
	
	private Map searchBrandAndSpecList(String category) {
		Map map = new HashMap<>();
		Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);//获取模板ID
		System.out.println("typeId::"+typeId);
		if(typeId != null) {
			//根据模板ID查询品牌列表
			List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
		
			map.put("brandList", brandList);
			//根据模板ID查询规格列表
			List specList = (List)redisTemplate.boundHashOps("specList").get(typeId);
		
			map.put("specList", specList);
		
		}
		return map;
	}

	@Override
	public void importList(List list) {
		
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
		
	}

	@Override
	public void deleteByGoodsIds(List goodsIdList) {
		System.out.println("删除商品ID"+goodsIdList);
		Query query = new SimpleQuery();
		Criteria criteria = new Criteria("item_goodsid").in(goodsIdList);
		query.addCriteria(criteria);
		solrTemplate.delete(query);
		solrTemplate.commit();
	}
	
}
