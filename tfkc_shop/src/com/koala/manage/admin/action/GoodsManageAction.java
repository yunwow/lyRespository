package com.koala.manage.admin.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.koala.core.annotation.SecurityMapping;
import com.koala.core.beans.BeanUtils;
import com.koala.core.beans.BeanWrapper;
import com.koala.core.domain.virtual.SysMap;
import com.koala.core.mv.JModelAndView;
import com.koala.core.qrcode.QRCodeUtil;
import com.koala.core.query.support.IPageList;
import com.koala.core.tools.CommUtil;
import com.koala.core.tools.WebForm;
import com.koala.core.tools.database.DatabaseTools;
import com.koala.foundation.domain.Evaluate;
import com.koala.foundation.domain.Goods;
import com.koala.foundation.domain.GoodsBrand;
import com.koala.foundation.domain.GoodsClass;
import com.koala.foundation.domain.Message;
import com.koala.foundation.domain.Template;
import com.koala.foundation.domain.User;
import com.koala.foundation.domain.ZTCGoldLog;
import com.koala.foundation.domain.query.GoodsQueryObject;
import com.koala.foundation.service.IAccessoryService;
import com.koala.foundation.service.IAlbumService;
import com.koala.foundation.service.IEvaluateService;
import com.koala.foundation.service.IGoodsBrandService;
import com.koala.foundation.service.IGoodsCartService;
import com.koala.foundation.service.IGoodsClassService;
import com.koala.foundation.service.IGoodsService;
import com.koala.foundation.service.IGoodsSpecPropertyService;
import com.koala.foundation.service.IGoodsTypePropertyService;
import com.koala.foundation.service.IMessageService;
import com.koala.foundation.service.IOrderFormLogService;
import com.koala.foundation.service.IOrderFormService;
import com.koala.foundation.service.IPaymentService;
import com.koala.foundation.service.ISysConfigService;
import com.koala.foundation.service.ITemplateService;
import com.koala.foundation.service.ITransportService;
import com.koala.foundation.service.IUserConfigService;
import com.koala.foundation.service.IUserGoodsClassService;
import com.koala.foundation.service.IUserService;
import com.koala.foundation.service.IWaterMarkService;
import com.koala.foundation.service.IZTCGoldLogService;
import com.koala.lucene.LuceneUtil;
import com.koala.lucene.LuceneVo;
import com.koala.manage.admin.tools.StoreTools;
import com.koala.manage.seller.tools.TransportTools;
import com.koala.msg.MsgTools;
import com.koala.msg.SpelTemplate;
import com.koala.view.web.tools.GoodsViewTools;
import com.koala.view.web.tools.StoreViewTools;

/**
 * 
 * <p>
 * Title: GoodsManageAction.java
 * </p>
 * 
 * <p>
 * Description: 商品管理类
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
 * @date 2014年5月27日
 * 
 * @version koala_b2b2c 2.0
 */
@Controller
public class GoodsManageAction {
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IGoodsBrandService goodsBrandService;
	@Autowired
	private IGoodsClassService goodsClassService;
	@Autowired
	private ITemplateService templateService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IMessageService messageService;
	@Autowired
	private MsgTools msgTools;
	@Autowired
	private DatabaseTools databaseTools;
	@Autowired
	private IEvaluateService evaluateService;
	@Autowired
	private IGoodsCartService goodsCartService;
	@Autowired
	private IOrderFormService orderFormService;
	@Autowired
	private IOrderFormLogService orderFormLogService;
	@Autowired
	private IAccessoryService accessoryService;
	@Autowired
	private IUserGoodsClassService userGoodsClassService;
	@Autowired
	private IGoodsSpecPropertyService specPropertyService;
	@Autowired
	private IGoodsTypePropertyService goodsTypePropertyService;
	@Autowired
	private IWaterMarkService waterMarkService;
	@Autowired
	private IAlbumService albumService;
	@Autowired
	private ITransportService transportService;
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private TransportTools transportTools;
	@Autowired
	private StoreTools storeTools;
	@Autowired
	private StoreViewTools storeViewTools;
	@Autowired
	private GoodsViewTools goodsViewTools;
	@Autowired
	private IZTCGoldLogService ztcglService;

	/**
	 * Goods列表页
	 * 
	 * @param currentPage
	 * @param orderBy
	 * @param orderType
	 * @param request
	 * @return
	 */
	@SecurityMapping(title = "商品列表", value = "/admin/goods_list.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping("/admin/goods_list.htm")
	public ModelAndView goods_list(HttpServletRequest request,
			HttpServletResponse response, String currentPage, String orderBy,
			String orderType, String store_name, String brand_id, String gc_id,
			String goods_type, String goods_name, String store_recommend,
			String status) {
		ModelAndView mv = new JModelAndView("admin/blue/goods_list.html",
				configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		GoodsQueryObject qo = new GoodsQueryObject(currentPage, mv, orderBy,
				orderType);
		qo.setOrderBy("addTime");
		qo.setOrderType("desc");
		WebForm wf = new WebForm();
		wf.toQueryPo(request, qo, Goods.class, mv);
		if (store_name != null && !store_name.equals("")) {
			qo.addQuery("obj.goods_store.store_name", new SysMap("store_name",
					"%" + CommUtil.null2String(store_name) + "%"), "like");
			mv.addObject("store_name", store_name);
		}
		if (brand_id != null && !brand_id.equals("")) {
			qo.addQuery("obj.goods_brand.id", new SysMap("goods_brand_id",
					CommUtil.null2Long(brand_id)), "=");
			mv.addObject("brand_id", brand_id);
		}
		if (gc_id != null && !gc_id.equals("")) {
			qo.addQuery("obj.gc.id",
					new SysMap("goods_gc_id", CommUtil.null2Long(gc_id)), "=");
			mv.addObject("gc_id", gc_id);
		}
		if (goods_type != null && !goods_type.equals("")) {
			qo.addQuery("obj.goods_type", new SysMap("goods_goods_type",
					CommUtil.null2Int(goods_type)), "=");
			mv.addObject("goods_type", goods_type);
		}
		if (goods_name != null && !goods_name.equals("")) {
			qo.addQuery("obj.goods_name", new SysMap("goods_goods_name", "%"
					+ goods_name + "%"), "like");
			mv.addObject("goods_name", goods_name);
		}
		if (store_recommend != null && !store_recommend.equals("")) {
			qo.addQuery(
					"obj.store_recommend",
					new SysMap("goods_store_recommend", CommUtil
							.null2Boolean(store_recommend)), "=");
			mv.addObject("store_recommend", store_recommend);
		}
		if (status != null && !status.equals("")) {
			qo.addQuery("obj.goods_status",
					new SysMap("goods_status", CommUtil.null2Int(status)), "=");
			mv.addObject("status", status);
		} else {
			qo.addQuery("obj.goods_status", new SysMap("goods_status", -2), ">");
		}
		IPageList pList = this.goodsService.list(qo);
		CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
		List<GoodsBrand> gbs = this.goodsBrandService
				.query("select new GoodsBrand(id,addTime,name) from GoodsBrand obj order by obj.sequence asc",
						null, -1, -1);
		List<GoodsClass> gcs = this.goodsClassService
				.query("select new GoodsClass(id,className) from GoodsClass obj where obj.parent.id is null order by obj.sequence asc",
						null, -1, -1);
		mv.addObject("gcs", gcs);
		mv.addObject("gbs", gbs);
		return mv;
	}

	@SecurityMapping(title = "违规商品列表", value = "/admin/goods_outline.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping("/admin/goods_outline.htm")
	public ModelAndView goods_outline(HttpServletRequest request,
			HttpServletResponse response, String currentPage, String orderBy,
			String orderType,String goods_name,String gb_id ,String gc_id) {
		ModelAndView mv = new JModelAndView("admin/blue/goods_outline.html",
				configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		String url = this.configService.getSysConfig().getAddress();
		if (url == null || url.equals("")) {
			url = CommUtil.getURL(request);
		}
		String params = "";
		GoodsQueryObject qo = new GoodsQueryObject(currentPage, mv, orderBy,
				orderType);
		if (goods_name != null && !goods_name.equals("")) {
			qo.addQuery("obj.goods_name", new SysMap("goods_goods_name", "%"
					+ goods_name + "%"), "like");
			mv.addObject("goods_name", goods_name);
		}
		if (gb_id != null && !gb_id.equals("")) {
			qo.addQuery("obj.goods_brand.id", new SysMap("goods_brand_id",
					CommUtil.null2Long(gb_id)), "=");
			mv.addObject("gb_id", gb_id);
		}
		if (gb_id != null && !gc_id.equals("")) {
			qo.addQuery("obj.gc.id", new SysMap("goods_class_id",
					CommUtil.null2Long(gc_id)), "=");
			mv.addObject("gc_id", gc_id);
		}
		qo.addQuery("obj.goods_status", new SysMap("goods_status", -2), "=");
		IPageList pList = this.goodsService.list(qo);
		CommUtil.saveIPageList2ModelAndView(url + "/admin/goods_list.htm", "",
				params, pList, mv);
		List<GoodsBrand> gbs = this.goodsBrandService
				.query("select new GoodsBrand(id,addTime,name) from GoodsBrand obj order by obj.sequence asc",
						null, -1, -1);
		List<GoodsClass> gcs = this.goodsClassService
				.query("select obj from GoodsClass obj where obj.level=1 order by obj.sequence asc",
						null, -1, -1);
		mv.addObject("gcs", gcs);
		mv.addObject("gbs", gbs);
		return mv;
	}

	@SecurityMapping(title = "商品删除", value = "/admin/goods_del.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping("/admin/goods_del.htm")
	public String goods_del(HttpServletRequest request, String mulitId)
			throws Exception {
		String[] ids = mulitId.split(",");
		for (String id : ids) {
			if (!id.equals("")) {
				Goods goods = this.goodsService.getObjById(CommUtil
						.null2Long(id));
				List<Evaluate> evaluates = goods.getEvaluates();
				for (Evaluate e : evaluates) {
					this.evaluateService.delete(e.getId());
				}
				goods.getGoods_photos().clear();
				goods.getGoods_ugcs().clear();
				goods.getGoods_specs().clear();
				Map params = new HashMap();
				params.clear();// 直通车商品记录
				params.put("gid", goods.getId());
				List<ZTCGoldLog> ztcgls = this.ztcglService
						.query("select obj from ZTCGoldLog obj where obj.zgl_goods_id=:gid",
								params, -1, -1);
				for (ZTCGoldLog ztc : ztcgls) {
					this.ztcglService.delete(ztc.getId());
				}
				goods.setGoods_main_photo(null);
				this.goodsService.delete(goods.getId());
				// 删除索引
				String goods_lucene_path = System.getProperty("koalab2b2c.root")
						+ File.separator + "luence" + File.separator + "goods";
				File file = new File(goods_lucene_path);
				if (!file.exists()) {
					CommUtil.createFolder(goods_lucene_path);
				}
				LuceneUtil lucene = LuceneUtil.instance();
				lucene.setIndex_path(goods_lucene_path);
				lucene.delete_index(CommUtil.null2String(id));
				// 发送站内短信提醒卖家
				if (goods.getGoods_type() == 1) {
					this.send_site_msg(request,
							"msg_toseller_goods_delete_by_admin_notify", goods
									.getGoods_store().getUser(), goods,
							"商城存在违规");
				}
			}
		}
		return "redirect:goods_list.htm";
	}

	private void send_site_msg(HttpServletRequest request, String mark,
			User user, Goods goods, String reason) throws Exception {
		Template template = this.templateService.getObjByProperty(null, "mark",
				mark);
		if (template.isOpen()) {
			ExpressionParser exp = new SpelExpressionParser();
			EvaluationContext context = new StandardEvaluationContext();
			context.setVariable("reason", reason);
			context.setVariable("user", user);
			context.setVariable("config", this.configService.getSysConfig());
			context.setVariable("send_time",
					CommUtil.formatLongDate(new Date()));
			Expression ex = exp.parseExpression(template.getContent(),
					new SpelTemplate());
			String content = ex.getValue(context, String.class);
			User fromUser = this.userService.getObjByProperty(null, "userName",
					"admin");
			Message msg = new Message();
			msg.setAddTime(new Date());
			msg.setContent(content);
			msg.setFromUser(fromUser);
			msg.setTitle(template.getTitle());
			msg.setToUser(user);
			msg.setType(0);
			this.messageService.save(msg);
		}
	}

	@SecurityMapping(title = "商品AJAX更新", value = "/admin/goods_ajax.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping("/admin/goods_ajax.htm")
	public void goods_ajax(HttpServletRequest request,
			HttpServletResponse response, String id, String fieldName,
			String value) throws ClassNotFoundException {
		Goods obj = this.goodsService.getObjById(Long.parseLong(id));
		Field[] fields = Goods.class.getDeclaredFields();
		BeanWrapper wrapper = new BeanWrapper(obj);
		Object val = null;
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				Class clz = Class.forName("java.lang.String");
				if (field.getType().getName().equals("int")) {
					clz = Class.forName("java.lang.Integer");
				}
				if (field.getType().getName().equals("boolean")) {
					clz = Class.forName("java.lang.Boolean");
				}
				if (!value.equals("")) {
					val = BeanUtils.convertType(value, clz);
				} else {
					val = !CommUtil.null2Boolean(wrapper
							.getPropertyValue(fieldName));
				}
				wrapper.setPropertyValue(fieldName, val);
			}
		}
		if (fieldName.equals("store_recommend")) {
			if (obj.isStore_recommend()) {
				obj.setStore_recommend_time(new Date());
			} else
				obj.setStore_recommend_time(null);
		}
		this.goodsService.update(obj);
		if (obj.getGoods_status() == 0) {
			// 更新lucene索引
			String goods_lucene_path = System.getProperty("koalab2b2c.root")
					+ File.separator + "luence" + File.separator + "goods";
			File file = new File(goods_lucene_path);
			if (!file.exists()) {
				CommUtil.createFolder(goods_lucene_path);
			}
			LuceneVo vo = new LuceneVo();
			vo.setVo_id(obj.getId());
			vo.setVo_title(obj.getGoods_name());
			vo.setVo_content(obj.getGoods_details());
			vo.setVo_type("goods");
			vo.setVo_store_price(CommUtil.null2Double(obj
					.getGoods_current_price()));
			vo.setVo_add_time(obj.getAddTime().getTime());
			vo.setVo_goods_salenum(obj.getGoods_salenum());
			LuceneUtil lucene = LuceneUtil.instance();
			lucene.setConfig(this.configService.getSysConfig());
			lucene.setIndex_path(goods_lucene_path);
			lucene.update(CommUtil.null2String(obj.getId()), vo);
		} else {
			String goods_lucene_path = System.getProperty("koalab2b2c.root")
					+ File.separator + "luence" + File.separator + "goods";
			File file = new File(goods_lucene_path);
			if (!file.exists()) {
				CommUtil.createFolder(goods_lucene_path);
			}
			LuceneUtil lucene = LuceneUtil.instance();
			lucene.setConfig(this.configService.getSysConfig());
			lucene.setIndex_path(goods_lucene_path);
			lucene.delete_index(CommUtil.null2String(id));
		}
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.print(val.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SecurityMapping(title = "商品审核", value = "/admin/goods_audit.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping("/admin/goods_audit.htm")
	public String goods_audit(HttpServletRequest request,
			HttpServletResponse response, String mulitId, String status)
			throws ClassNotFoundException {
		String ids[] = mulitId.split(",");
		for (String id : ids) {
			if (id != null) {
				Goods obj = this.goodsService
						.getObjById(CommUtil.null2Long(id));
				obj.setGoods_status(obj.getPublish_goods_status());// 设置商品发布审核后状态
				goodsService.update(obj);
			}
		}
		return "redirect:goods_list.htm?status=" + status;
	}

	@SecurityMapping(title = "商品二维码生成", value = "/admin/goods_qr.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping("/admin/goods_qr.htm")
	public String goods_self_qr(HttpServletRequest request,
			HttpServletResponse response, String mulitId, String currentPage)
			throws ClassNotFoundException {
		String ids[] = mulitId.split(",");
		for (String id : ids) {
			if (id != null) {
				Goods obj = this.goodsService
						.getObjById(CommUtil.null2Long(id));
				String uploadFilePath = this.configService.getSysConfig()
						.getUploadFilePath();
				String destPath = request.getSession().getServletContext()
						.getRealPath("/")
						+ uploadFilePath + File.separator + "goods_qr";
				if (!CommUtil.fileExist(destPath)) {
					CommUtil.createFolder(destPath);
				}
				destPath = destPath + File.separator + obj.getId() + "_qr.jpg";
				// Map goods_qr = new HashMap();
				// goods_qr.put("id", CommUtil.null2String(obj.getId()));
				// goods_qr.put("type", "goods");
				String logoPath = "";
				if (obj.getGoods_main_photo() != null) {
					logoPath = request.getSession().getServletContext()
							.getRealPath("/")
							+ File.separator
							+ obj.getGoods_main_photo().getPath()
							+ File.separator
							+ obj.getGoods_main_photo().getName();
				} else {
					logoPath = request.getSession().getServletContext()
							.getRealPath("/")
							+ File.separator
							+ this.configService.getSysConfig().getGoodsImage()
									.getPath()
							+ File.separator
							+ File.separator
							+ this.configService.getSysConfig().getGoodsImage()
									.getName();
				}
				String goods_url = CommUtil.getURL(request) + "/goods_" + id
						+ ".htm";
				if (obj.getGoods_type() == 1) {// 商家商品
					if (this.configService.getSysConfig()
							.isSecond_domain_open()
							&& !CommUtil.null2String(
									obj.getGoods_store()
											.getStore_second_domain()).equals(
									"")) {
						goods_url = "http://"
								+ obj.getGoods_store().getStore_second_domain()
								+ "." + CommUtil.generic_domain(request)
								+ "/goods_" + id + ".htm";
					}
				}
				QRCodeUtil.encode(goods_url, logoPath, destPath, true);
				obj.setQr_img_path(CommUtil.getURL(request) + "/"
						+ uploadFilePath + "/" + "goods_qr" + "/" + obj.getId()
						+ "_qr.jpg");

				this.goodsService.update(obj);
			}
		}
		return "redirect:goods_list.htm?currentPage=" + currentPage;
	}
}