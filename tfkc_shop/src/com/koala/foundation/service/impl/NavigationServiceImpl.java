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
import com.koala.foundation.domain.Navigation;
import com.koala.foundation.service.INavigationService;

@Service
@Transactional
public class NavigationServiceImpl implements INavigationService{
	@Resource(name = "navigationDAO")
	private IGenericDAO<Navigation> navigationDao;
	
	public boolean save(Navigation navigation) {
		/**
		 * init other field here
		 */
		try {
			this.navigationDao.save(navigation);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Navigation getObjById(Long id) {
		Navigation navigation = this.navigationDao.get(id);
		if (navigation != null) {
			return navigation;
		}
		return null;
	}
	
	public boolean delete(Long id) {
		try {
			this.navigationDao.remove(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean batchDelete(List<Serializable> navigationIds) {
		// TODO Auto-generated method stub
		for (Serializable id : navigationIds) {
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
		GenericPageList pList = new GenericPageList(Navigation.class,construct, query,
				params, this.navigationDao);
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
	
	public boolean update(Navigation navigation) {
		try {
			this.navigationDao.update( navigation);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
	public List<Navigation> query(String query, Map params, int begin, int max){
		return this.navigationDao.query(query, params, begin, max);
		
	}
}
