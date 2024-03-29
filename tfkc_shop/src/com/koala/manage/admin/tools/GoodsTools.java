package com.koala.manage.admin.tools;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.koala.core.qrcode.QRCodeUtil;
import com.koala.core.tools.CommUtil;
import com.koala.foundation.domain.Accessory;
import com.koala.foundation.domain.Goods;
import com.koala.foundation.service.IAccessoryService;
import com.koala.foundation.service.IGoodsService;
import com.koala.foundation.service.ISysConfigService;
import com.koala.foundation.service.IUserConfigService;

/**
 * 
 * <p>
 * Title: GoodsTools.java
 * </p>
 * 
 * <p>
 * Description:商品管理工具
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
 * @author hezeng
 * 
 * @date 2014-12-10
 * 
 * @version koala_b2b2c v2.0 2015版
 */
@Component
public class GoodsTools {
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IAccessoryService accessoryService;

	/**
	 * 异步生成商品二维码,使用@Async，有时session、商品主图片获取不到
	 * 
	 * @param request
	 * @param goods_id
	 */
	@Async
	public void createGoodsQR(HttpServletRequest request, String goods_id,
			String uploadFilePath, Long goods_main_id) {
		try {
			String destPath = System.getProperty("koalab2b2c.root")
					+ uploadFilePath + File.separator + "goods_qr";
			if (!CommUtil.fileExist(destPath)) {
				CommUtil.createFolder(destPath);
			}
			Goods obj = this.goodsService.getObjById(CommUtil
					.null2Long(goods_id));
			Accessory main_img = this.accessoryService.getObjById(CommUtil
					.null2Long(goods_main_id));
			destPath = destPath + File.separator + obj.getId() + "_qr.jpg";

			String logoPath = "";
			if (main_img != null) {
				logoPath = System.getProperty("koalab2b2c.root")
						+ main_img.getPath() + File.separator
						+ main_img.getName();
			} else {
				logoPath = System.getProperty("koalab2b2c.root")
						+ this.configService.getSysConfig().getGoodsImage()
								.getPath()
						+ File.separator
						+ File.separator
						+ this.configService.getSysConfig().getGoodsImage()
								.getName();
			}
			QRCodeUtil.encode(CommUtil.getURL(request) + "/goods_" + goods_id
					+ ".htm", logoPath, destPath, true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
