﻿package com.koala.foundation.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.koala.core.query.support.IPageList;
import com.koala.core.query.support.IQueryObject;

import com.koala.foundation.domain.Article;

public interface IArticleService {
	
	
	/**
	 * 保存一个Article，如果保存成功返回true，否则返回false
	 * 
	 * @param instance
	 * @return 是否保存成功
	 */
	boolean save(Article instance);

	/**
	 * 根据一个ID得到Article
	 * 
	 * @param id
	 * @return
	 */
	Article getObjById(Long id);

	/**
	 * 删除一个Article
	 * 
	 * @param id
	 * @return
	 */
	boolean delete(Long id);

	/**
	 * 批量删除Article
	 * 
	 * @param ids
	 * @return
	 */
	boolean batchDelete(List<Serializable> ids);

	/**
	 * 通过一个查询对象得到Article
	 * 
	 * @param properties
	 * @return
	 */
	IPageList list(IQueryObject properties);

	
	
	/**
	 * 更新一个Article
	 * 
	 * @param id
	 *            需要更新的Article的id
	 * @param dir
	 *            需要更新的Article
	 */
	boolean update(Article instance);

	/**
	 * 
	 * @param query
	 * @param params
	 * @param begin
	 * @param max
	 * @return
	 */
	List<Article> query(String query, Map params, int begin, int max);

	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	Article getObjByProperty(String construct, String propertyName, Object value);
	
	
	
}
