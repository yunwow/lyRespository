package com.koala.foundation.domain.query;

import org.springframework.web.servlet.ModelAndView;

import com.koala.core.query.QueryObject;

public class GoodsBrandQueryObject extends QueryObject {

	public GoodsBrandQueryObject(String currentPage, ModelAndView mv,
			String orderBy, String orderType) {
		super(currentPage, mv, orderBy, orderType);
		// TODO Auto-generated constructor stub
	}

	public GoodsBrandQueryObject(String construct, String currentPage,
			ModelAndView mv, String orderBy, String orderType) {
		super(construct, currentPage, mv, orderBy, orderType);
		// TODO Auto-generated constructor stub
	}

	public GoodsBrandQueryObject() {
		super();
		// TODO Auto-generated constructor stub
	}
}
