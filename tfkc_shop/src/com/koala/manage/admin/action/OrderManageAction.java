package com.koala.manage.admin.action;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.koala.core.annotation.SecurityMapping;
import com.koala.core.domain.virtual.SysMap;
import com.koala.core.mv.JModelAndView;
import com.koala.core.query.support.IPageList;
import com.koala.core.tools.CommUtil;
import com.koala.core.tools.WebForm;
import com.koala.foundation.domain.OrderForm;
import com.koala.foundation.domain.Store;
import com.koala.foundation.domain.SysConfig;
import com.koala.foundation.domain.query.OrderFormQueryObject;
import com.koala.foundation.service.IExpressCompanyService;
import com.koala.foundation.service.IOrderFormService;
import com.koala.foundation.service.IStoreService;
import com.koala.foundation.service.ISysConfigService;
import com.koala.foundation.service.IUserConfigService;
import com.koala.manage.admin.tools.OrderFormTools;

/**
 * 
 * <p>
 * Title: OrderManageAction.java
 * </p>
 * 
 * <p>
 * Description:商城后台订单管理器
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * 
 * <p>
 * Company: 沈阳网之商科技有限公司 www.koala.com
 * </p>
 * 
 * @author erikzhang
 * 
 * @date 2014-5-21
 * 
 * @version koala_b2b2c v2.0 2015版
 */
@Controller
public class OrderManageAction {
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IOrderFormService orderFormService;
	@Autowired
	private IExpressCompanyService ecService;
	@Autowired
	private OrderFormTools orderFormTools;
	@Autowired
	private IStoreService storeService;

	@SecurityMapping(title = "订单设置", value = "/admin/set_order_confirm.htm*", rtype = "admin", rname = "订单设置", rcode = "set_order_confirm", rgroup = "交易")
	@RequestMapping("/admin/set_order_confirm.htm")
	public ModelAndView set_order_confirm(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("admin/blue/set_order_confirm.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		return mv;
	}

	@SecurityMapping(title = "订单设置保存", value = "/admin/set_order_confirm_save.htm*", rtype = "admin", rname = "订单设置", rcode = "set_order_confirm", rgroup = "交易")
	@RequestMapping("/admin/set_order_confirm_save.htm")
	public ModelAndView set_order_confirm_save(HttpServletRequest request, HttpServletResponse response, String id,
			String auto_order_confirm, String auto_order_notice, String auto_order_return, String auto_order_evaluate,
			String grouplife_order_return, String evaluate_edit_deadline, String evaluate_add_deadline) {
		SysConfig obj = this.configService.getSysConfig();
		WebForm wf = new WebForm();
		SysConfig config = null;
		if (id.equals("")) {
			config = wf.toPo(request, SysConfig.class);
			config.setAddTime(new Date());
		} else {
			config = (SysConfig) wf.toPo(request, obj);
		}
		config.setAuto_order_confirm(CommUtil.null2Int(auto_order_confirm));
		config.setAuto_order_notice(CommUtil.null2Int(auto_order_notice));
		config.setAuto_order_return(CommUtil.null2Int(auto_order_return));
		config.setAuto_order_evaluate(CommUtil.null2Int(auto_order_evaluate));
		config.setGrouplife_order_return(CommUtil.null2Int(grouplife_order_return));
		config.setEvaluate_edit_deadline(CommUtil.null2Int(evaluate_edit_deadline));
		config.setEvaluate_add_deadline(CommUtil.null2Int(evaluate_add_deadline));
		if (id.equals("")) {
			this.configService.save(config);
		} else {
			this.configService.update(config);
		}
		ModelAndView mv = new JModelAndView("admin/blue/success.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		mv.addObject("op_title", "订单设置成功");
		mv.addObject("list_url", CommUtil.getURL(request) + "/admin/set_order_confirm.htm");
		return mv;
	}

	@SecurityMapping(title = "订单列表", value = "/admin/order_list.htm*", rtype = "admin", rname = "订单管理", rcode = "order_admin", rgroup = "交易")
	@RequestMapping("/admin/order_list.htm")
	public ModelAndView order_list(HttpServletRequest request, HttpServletResponse response, String order_status,
			String type, String type_data, String payment, String beginTime, String endTime, String begin_price,
			String end_price, String currentPage) {
		ModelAndView mv = new JModelAndView("admin/blue/order_list.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		OrderFormQueryObject ofqo = new OrderFormQueryObject(currentPage, mv, "addTime", "desc");
		ofqo.addQuery("obj.order_cat", new SysMap("order_cat", 0), "=");// 这里只查询商品订单，手机充值订单独立出来
		
		 /* if (!CommUtil.null2String(order_status).equals("")) {
		  ofqo.addQuery("obj.order_status", new SysMap("order_status",
		  CommUtil.null2Int(order_status)), "=");
		  }
		 
		
		  if (!CommUtil.null2String(type_data).equals("")) {
			  if(type.equals("store")) {
				  ofqo.addQuery("obj.store_name", new SysMap("store_name", type_data), "="); }
			  if (type.equals("buyer")) {
		 ofqo.addQuery("obj.user_name", new SysMap("userName", type_data),"="); }
			  if (type.equals("order")) { ofqo.addQuery("obj.order_id", new
		  SysMap("order_id", type_data), "="); } }
		 
		
		  if (CommUtil.null2String(payment).equals("alipay")) {
		  ofqo.addQuery("obj.payment.mark", new SysMap("mark", payment), "=");
		 ofqo.addQuery("obj.payment.mark", new SysMap("mark", "alipay_app"),
		  "=", "or"); ofqo.addQuery("obj.payment.mark", new SysMap("mark",
		 "alipay_wap"), "=", "or");
		  } else if(CommUtil.null2String(payment).equals("apyafter")) {
		 ofqo.addQuery("obj.payType", new SysMap("mark", "payafter"), "="); }
		 else if (CommUtil.null2String(payment).equals("wx_app")) {
		  ofqo.addQuery("obj.payment.mark", new SysMap("mark", "wx_app"), "=");
		  ofqo.addQuery("obj.payment.mark", new SysMap("mark", "wx_pay"), "=",
		  "or"); } else if (!CommUtil.null2String(payment).equals("")) {
		 ofqo.addQuery("obj.payment.mark", new SysMap("mark", payment), "=");
		 } if (!CommUtil.null2String(beginTime).equals("")) {
		 ofqo.addQuery("obj.addTime", new SysMap("beginTime",
		 CommUtil.formatDate(beginTime)), ">="); } if
		 (!CommUtil.null2String(endTime).equals("")) {
		 ofqo.addQuery("obj.addTime", new SysMap("endTime",
		 CommUtil.formatDate(endTime)), "<="); } if
		 (!CommUtil.null2String(begin_price).equals("")) {
		 ofqo.addQuery("obj.totalPrice", new SysMap("begin_price",
		 BigDecimal.valueOf(CommUtil.null2Double(begin_price))), ">="); } if
		 (!CommUtil.null2String(end_price).equals("")) { ofqo.addQuery(
		 "obj.totalPrice", new SysMap("end_price", BigDecimal.valueOf(CommUtil
		 .null2Double(end_price))), "<="); }*/
		 
		IPageList pList = this.orderFormService.list(ofqo);
		CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
		mv.addObject("order_status", order_status);
		mv.addObject("type", type);
		mv.addObject("type_data", type_data);
		mv.addObject("payment", payment);
		mv.addObject("beginTime", beginTime);
		mv.addObject("endTime", endTime);
		mv.addObject("begin_price", begin_price);
		mv.addObject("end_price", end_price);
		return mv;
	}

	@SecurityMapping(title = "手机充值订单列表", value = "/admin/order_recharge.htm*", rtype = "admin", rname = "充值列表", rcode = "ofcard_list", rgroup = "交易")
	@RequestMapping("/admin/order_recharge.htm")
	public ModelAndView order_recharge(HttpServletRequest request, HttpServletResponse response, String order_status,
			String beginTime, String endTime, String begin_price, String end_price, String currentPage) {
		ModelAndView mv = new JModelAndView("admin/blue/order_recharge.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		OrderFormQueryObject ofqo = new OrderFormQueryObject(currentPage, mv, "addTime", "desc");
		ofqo.addQuery("obj.order_cat", new SysMap("order_cat", 1), "=");// 这里只查手机充值订单
		if (!CommUtil.null2String(order_status).equals("")) {
			ofqo.addQuery("obj.order_status", new SysMap("order_status", CommUtil.null2Int(order_status)), "=");
		}
		if (!CommUtil.null2String(beginTime).equals("")) {
			ofqo.addQuery("obj.addTime", new SysMap("beginTime", CommUtil.formatDate(beginTime)), ">=");
		}
		if (!CommUtil.null2String(endTime).equals("")) {
			ofqo.addQuery("obj.addTime", new SysMap("endTime", CommUtil.formatDate(endTime)), "<=");
		}
		if (!CommUtil.null2String(begin_price).equals("")) {
			ofqo.addQuery("obj.totalPrice",
					new SysMap("begin_price", BigDecimal.valueOf(CommUtil.null2Double(begin_price))), ">=");
		}
		if (!CommUtil.null2String(end_price).equals("")) {
			ofqo.addQuery("obj.totalPrice",
					new SysMap("end_price", BigDecimal.valueOf(CommUtil.null2Double(end_price))), "<=");
		}
		IPageList pList = this.orderFormService.list(ofqo);
		CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
		mv.addObject("order_status", order_status);
		mv.addObject("beginTime", beginTime);
		mv.addObject("endTime", endTime);
		mv.addObject("begin_price", begin_price);
		mv.addObject("end_price", end_price);
		return mv;
	}

	@SecurityMapping(title = "订单详情", value = "/admin/order_view.htm*", rtype = "admin", rname = "订单管理", rcode = "order_admin", rgroup = "交易")
	@RequestMapping("/admin/order_view.htm")
	public ModelAndView order_view(HttpServletRequest request, HttpServletResponse response, String id,
			String view_type) {
		ModelAndView mv = new JModelAndView("admin/blue/order_view.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		OrderForm obj = this.orderFormService.getObjById(CommUtil.null2Long(id));
		if (obj.getOrder_cat() == 1) {
			mv = new JModelAndView("admin/blue/order_recharge_view.html", configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 0, request, response);
		} else {
			Store store = this.storeService.getObjById(CommUtil.null2Long(obj.getStore_id()));
			mv.addObject("store", store);
			mv.addObject("obj", obj);
		}
		mv.addObject("express_company_name",
				this.orderFormTools.queryExInfo(obj.getExpress_info(), "express_company_name"));
		mv.addObject("orderFormTools", orderFormTools);
		mv.addObject("obj", obj);
		mv.addObject("view_type", view_type);
		return mv;
	}

}
