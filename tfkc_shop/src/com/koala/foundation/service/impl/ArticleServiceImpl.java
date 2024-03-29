package com.koala.foundation.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koala.core.query.PageObject;
import com.koala.core.query.support.IPageList;
import com.koala.core.query.support.IQueryObject;
import com.koala.core.tools.CommUtil;
import com.koala.core.tools.WebForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.koala.core.dao.IGenericDAO;
import com.koala.core.mv.JModelAndView;
import com.koala.core.query.GenericPageList;
import com.koala.foundation.domain.Article;
import com.koala.foundation.domain.query.ArticleQueryObject;
import com.koala.foundation.service.IArticleService;
import com.koala.foundation.service.ISysConfigService;
import com.koala.foundation.service.IUserConfigService;

@Service
@Transactional
public class ArticleServiceImpl implements IArticleService {
	
	
	
	@Resource(name = "articleDAO")
	private IGenericDAO<Article> articleDao;
	
	

	public boolean save(Article article) {
		/**
		 * init other field here
		 */
		try {
			this.articleDao.save(article);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Article getObjById(Long id) {
		Article article = this.articleDao.get(id);
		if (article != null) {
			return article;
		}
		return null;
	}

	public boolean delete(Long id) {
		try {
			this.articleDao.remove(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean batchDelete(List<Serializable> articleIds) {
		// TODO Auto-generated method stub
		for (Serializable id : articleIds) {
			delete((Long) id);
		}
		return true;
	}

	public IPageList list(IQueryObject properties) {
		if (properties == null) {
			return null;
		}
		    String query = properties.getQuery();
		String construct = properties.getConstruct();
		      Map params = properties.getParameters();
		GenericPageList pList = new GenericPageList(Article.class, construct,
				query, params, this.articleDao);
		if (properties != null) {
			PageObject pageObj = properties.getPageObj();
			if (pageObj != null)
				pList.doList(
						pageObj.getCurrentPage() == null ? 0 : pageObj
								.getCurrentPage(),
						pageObj.getPageSize() == null ? 0 : pageObj
								.getPageSize());
		} else
			pList.doList(0, -1);
		return pList;
	}

	public boolean update(Article article) {
		try {
			this.articleDao.update(article);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Article> query(String query, Map params, int begin, int max) {
		return this.articleDao.query(query, params, begin, max);

	}

	@Override
	public Article getObjByProperty(String construct, String propertyName,
			Object value) {
		// TODO Auto-generated method stub
		try {
			return this.articleDao.getBy(construct, propertyName, value);
		} catch (Exception e) {
			Article obj = new Article();
			obj.setTitle("文章错误");
			return obj;
		}
	}

	
}
