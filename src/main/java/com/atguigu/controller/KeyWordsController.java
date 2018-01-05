package com.atguigu.controller;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.bean.OBJECT_T_MALL_SKU_KEYWORDS;
import com.atguigu.factory.MySqlSessionFactory;
import com.atguigu.mapper.ImportDataMapper;

import util.MySolrUtil;

@Controller
public class KeyWordsController {


	@RequestMapping("importData")
	@ResponseBody
	public String importData(Integer class_2_id) {
		SqlSessionFactory factory = MySqlSessionFactory.getFactory();
		ImportDataMapper impotDataMapper = factory.openSession().getMapper(ImportDataMapper.class);
		
		List<OBJECT_T_MALL_SKU_KEYWORDS> sku_list = impotDataMapper.select_sku(class_2_id);
		HttpSolrServer solr = MySolrUtil.getSolr("sku_solr");
		
		try {
			
			solr.addBeans(sku_list);
			solr.commit();
			
			solr.setParser(new XMLResponseParser());
			solr.setConnectionTimeout(10000);
			
		} catch (Exception e) {

		}
		return "卧槽，什么鬼";
	}
	

	@RequestMapping("search_keywords")
	@ResponseBody
	public List<OBJECT_T_MALL_SKU_KEYWORDS> query_solr(String keywords) throws SolrServerException {
		
		SolrQuery solrQuery = new SolrQuery();
		
		solrQuery.setQuery("copy_item:"+keywords);
		HttpSolrServer solr = MySolrUtil.getSolr("sku_solr");
		
		QueryResponse query = solr.query(solrQuery);
		List<OBJECT_T_MALL_SKU_KEYWORDS> list = query.getBeans(OBJECT_T_MALL_SKU_KEYWORDS.class);
		return list;
	}
	
	
}
