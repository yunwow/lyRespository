package com.koala.manage.buyer.action;

import java.util.Map;

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
import com.koala.core.security.support.SecurityUserHolder;
import com.koala.core.tools.CommUtil;
import com.koala.core.tools.WebForm;
import com.koala.foundation.domain.GroupInfo;
import com.koala.foundation.domain.GroupLifeGoods;
import com.koala.foundation.domain.OrderForm;
import com.koala.foundation.domain.User;
import com.koala.foundation.domain.query.GroupInfoQueryObject;
import com.koala.foundation.domain.query.OrderFormQueryObject;
import com.koala.foundation.service.IGroupInfoService;
import com.koala.foundation.service.IGroupLifeGoodsService;
import com.koala.foundation.service.IOrderFormService;
import com.koala.foundation.service.ISysConfigService;
import com.koala.foundation.service.IUserConfigService;
import com.koala.foundation.service.IUserService;
import com.koala.manage.admin.tools.OrderFormTools;
import com.koala.manage.admin.tools.PaymentTools;

/**
 * 
 * <p>
 * Title: GroupBuyerAction.java
 * </p>
 * 
 * <p>
 * Description: 买家生活购控制器，查看列表，使用过的状况
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
 * @author jinxinzhe
 * 
 * @date 2014-5-23
 * 
 * @version koala_b2b2c v2.0 2015版
 */
@Controller
public class GroupBuyerAction {
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IGroupInfoService groupinfoService;
	@Autowired
	private IUserService userService;
	@Autowired
	private OrderFormTools orderFormTools;
	@Autowired
	private IOrderFormService orderFormService;
	@Autowired
	private PaymentTools paymentTools;
	@Autowired
	private IGroupLifeGoodsService groupLifeGoodsService;

	@SecurityMapping(title = "买家生活购订单列表", value = "/buyer/group.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping("/buyer/group.htm")
	public ModelAndView buyer_group(HttpServletRequest request,
			HttpServletResponse response, String currentPage, String order_id) {
		ModelAndView mv = new JModelAndView(
				"user/default/usercenter/buyer_group.html",
				configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		OrderFormQueryObject ofqo = new OrderFormQueryObject(currentPage, mv,
				"addTime", "desc");
		User user = this.userService.getObjById(SecurityUserHolder
				.getCurrentUser().getId());
		ofqo.addQuery("obj.user_id", new SysMap("user_id", user.getId()
				.toString()), "=");
		ofqo.addQuery("obj.order_main", new SysMap("order_main", 0), "=");// 无需查询主订单
		ofqo.addQuery("obj.order_cat", new SysMap("order_cat", 2), "=");
		if (!CommUtil.null2String(order_id).equals("")) {
			ofqo.addQuery("obj.order_id", new SysMap("order_id", "%" + order_id
					+ "%"), "like");
			mv.addObject("order_id", order_id);
		}
		mv.addObject("orderFormTools", orderFormTools);
		IPageList pList = this.orderFormService.list(ofqo);
		CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
		mv.addObject("orderFormTools", this.orderFormTools);
		return mv;
	}

	/**
	 * 买家中心消费码列表页
	 * 
	 * @param currentPage
	 * @param orderBy
	 * @param orderType
	 * @param request
	 * @return
	 */
	@SecurityMapping(title = "买家生活购消费码列表", value = "/buyer/groupinfo_list.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping("/buyer/groupinfo_list.htm")
	public ModelAndView groupinfo_list(HttpServletRequest request,
			HttpServletResponse response, String currentPage, String orderBy,
			String orderType, String info_id) {
		ModelAndView mv = new JModelAndView(
				"user/default/usercenter/groupinfo_list.html",
				configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		User user = this.userService.getObjById(SecurityUserHolder
				.getCurrentUser().getId());
		String url = this.configService.getSysConfig().getAddress();
		if (url == null || url.equals("")) {
			url = CommUtil.getURL(request);
		}
		String params = "";
		GroupInfoQueryObject qo = new GroupInfoQueryObject(currentPage, mv,
				orderBy, orderType);
		qo.addQuery("obj.user_id", new SysMap("user_id", user.getId()), "=");
		if (!CommUtil.null2String(info_id).equals("")) {
			qo.addQuery("obj.group_sn", new SysMap("group_sn", "%" + info_id
					+ "%"), "like");
			mv.addObject("info_id", info_id);
		}
		WebForm wf = new WebForm();
		wf.toQueryPo(request, qo, GroupInfo.class, mv);
		IPageList pList = this.groupinfoService.list(qo);
		CommUtil.saveIPageList2ModelAndView(url + "/buyer/groupinfo_list.htm",
				"", params, pList, mv);
		return mv;
	}

	/**
	 * 支付生活购订单
	 * 
	 * @param currentPage
	 * @param orderBy
	 * @param orderType
	 * @param request
	 * @return
	 */
	@SecurityMapping(title = "支付生活购订单", value = "/buyer/pay_lifeorder.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping("/buyer/pay_lifeorder.htm")
	public ModelAndView pay_lifeorder(HttpServletRequest request,
			HttpServletResponse response, String id) {
		ModelAndView mv = new JModelAndView("life_order_pay.html",
				configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		OrderForm obj = this.orderFormService
				.getObjById(CommUtil.null2Long(id));
		User user = this.userService.getObjById(SecurityUserHolder
				.getCurrentUser().getId());
		if (obj != null
				&& obj.getUser_id().equals(
						SecurityUserHolder.getCurrentUser().getId().toString())
				&& obj.getOrder_status() == 10) {
			mv.addObject("order", obj);
			mv.addObject("all_of_price", obj.getTotalPrice());
			mv.addObject("paymentTools", paymentTools);
			mv.addObject("group", true);
		} else {
			mv = new JModelAndView("error.html", configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request,
					response);
			mv.addObject("op_title", "订单编号错误");
			mv.addObject("url", CommUtil.getURL(request) + "/buyer/group.htm");
		}
		return mv;
	}

	/**
	 * 生活购订单详情
	 * 
	 * @param currentPage
	 * @param orderBy
	 * @param orderType
	 * @param request
	 * @return
	 */
	@SecurityMapping(title = "生活购订单详情", value = "/buyer/lifeorder_view.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping("/buyer/lifeorder_view.htm")
	public ModelAndView lifeorder_view(HttpServletRequest request,
			HttpServletResponse response, String id, String currentPage) {
		ModelAndView mv = new JModelAndView(
				"user/default/usercenter/lifeorder_view.html",
				configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		OrderForm obj = this.orderFormService
				.getObjById(CommUtil.null2Long(id));
		User user = this.userService.getObjById(SecurityUserHolder
				.getCurrentUser().getId());
		if (obj != null
				&& obj.getUser_id().equals(
						SecurityUserHolder.getCurrentUser().getId().toString())
				&& obj.getOrder_status() == 20) {
			Map json = this.orderFormTools.queryGroupInfo(obj.getGroup_info());
			String url = this.configService.getSysConfig().getAddress();
			if (url == null || url.equals("")) {
				url = CommUtil.getURL(request);
			}
			String params = "";
			GroupInfoQueryObject qo = new GroupInfoQueryObject(currentPage, mv,
					"", "");
			qo.addQuery("obj.user_id", new SysMap("user_id", user.getId()), "=");
			qo.addQuery("obj.order_id", new SysMap("order_id", obj.getId()),
					"=");
			qo.addQuery(
					"obj.lifeGoods.id",
					new SysMap("goods_id", CommUtil.null2Long(json.get(
							"goods_id").toString())), "=");
			WebForm wf = new WebForm();
			wf.toQueryPo(request, qo, GroupInfo.class, mv);
			IPageList pList = this.groupinfoService.list(qo);
			CommUtil.saveIPageList2ModelAndView(url
					+ "/buyer/groupinfo_list.htm", "", params, pList, mv);
			GroupLifeGoods goods = this.groupLifeGoodsService
					.getObjById(CommUtil.null2Long(json.get("goods_id")));
			mv.addObject("infos", pList.getResult());
			mv.addObject("order", obj);
			mv.addObject("goods", goods);
		} else {
			mv = new JModelAndView("error.html", configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request,
					response);
			mv.addObject("op_title", "订单编号错误");
			mv.addObject("url", CommUtil.getURL(request) + "/buyer/group.htm");
		}
		return mv;
	}

	@SecurityMapping(title = "订单取消", value = "/buyer/lifeorder_cancel.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping("/buyer/lifeorder_cancel.htm")
	public String lifeorder_cancel(HttpServletRequest request,
			HttpServletResponse response, String currentPage, String id) {
		OrderForm of = this.orderFormService.getObjById(CommUtil.null2Long(id));
		if (of.getUser_id().equals(
				SecurityUserHolder.getCurrentUser().getId().toString())) {
			of.setOrder_status(0);
			this.orderFormService.update(of);
		}
		return "redirect:" + "/buyer/group.htm?currentPage" + currentPage;
	}
}
