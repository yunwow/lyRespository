package com.koala.foundation.service.impl;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.koala.core.query.PageObject;
import com.koala.core.query.support.IPageList;
import com.koala.core.query.support.IQueryObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.koala.core.dao.IGenericDAO;
import com.koala.core.query.GenericPageList;
import com.koala.foundation.domain.GoodsLog;
import com.koala.foundation.service.IGoodsLogService;

@Service
@Transactional
public class GoodsLogServiceImpl implements IGoodsLogService{
	@Resource(name = "goodsLogDAO")
	private IGenericDAO<GoodsLog> goodsLogDao;
	
	public boolean save(GoodsLog goodsLog) {
		/**
		 * init other field here
		 */
		try {
			this.goodsLogDao.save(goodsLog);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public GoodsLog getObjById(Long id) {
		GoodsLog goodsLog = this.goodsLogDao.get(id);
		if (goodsLog != null) {
			return goodsLog;
		}
		return null;
	}
	
	public boolean delete(Long id) {
		try {
			this.goodsLogDao.remove(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean batchDelete(List<Serializable> goodsLogIds) {
		// TODO Auto-generated method stub
		for (Serializable id : goodsLogIds) {
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
		GenericPageList pList = new GenericPageList(GoodsLog.class, construct,query,
				params, this.goodsLogDao);
		if (properties != null) {
			PageObject pageObj = properties.getPageObj();
			if (pageObj != null)
				pList.doList(pageObj.getCurrentPage() == null ? 0 : pageObj
						.getCurrentPage(), pageObj.getPageSize() == null ? 0
						: pageObj.getPageSize());
		} else
			pList.doList(0, -1);
		return pList;
	}
	
	public boolean update(GoodsLog goodsLog) {
		try {
			this.goodsLogDao.update( goodsLog);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
	public List<GoodsLog> query(String query, Map params, int begin, int max){
		return this.goodsLogDao.query(query, params, begin, max);
		
	}
}
