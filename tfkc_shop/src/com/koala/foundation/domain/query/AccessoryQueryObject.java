package com.koala.foundation.domain.query;

import org.springframework.web.servlet.ModelAndView;

import com.koala.core.query.QueryObject;

public class AccessoryQueryObject extends QueryObject {

	public AccessoryQueryObject(String currentPage, ModelAndView mv,
			String orderBy, String orderType) {
		super(currentPage, mv, orderBy, orderType);
		// TODO Auto-generated constructor stub
	}

	public AccessoryQueryObject(String construct, String currentPage,
			ModelAndView mv, String orderBy, String orderType) {
		super(construct, currentPage, mv, orderBy, orderType);
		// TODO Auto-generated constructor stub
	}

	public AccessoryQueryObject() {
		super();
	}

}
