package com.koala.manage.admin.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.koala.core.tools.CommUtil;
import com.koala.foundation.domain.Accessory;
import com.koala.foundation.domain.Album;
import com.koala.foundation.service.IAccessoryService;
import com.koala.foundation.service.IAlbumService;

/**
 * 
 * <p>
 * Title: ImageTools.java
 * </p>
 * 
 * <p>
 * Description: 图片工具类
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
 * @author lixiaoyang
 * 
 * @date 2014-10-28
 * 
 * @version koala_b2b2c v2.0 2015版
 */
@Component
public class ImageTools {
	@Autowired
	private IAccessoryService accessoryService;
	@Autowired
	private IAlbumService albumService;

	public Accessory queryImg(Object id) {
		if (id != null && !id.equals("")) {
			Accessory img = this.accessoryService.getObjById(CommUtil
					.null2Long(id));
			return img;
		}
		return null;
	}

	public List<Accessory> queryImgs(String ids) {
		List<Accessory> list = new ArrayList<Accessory>();
		if (ids != null && !ids.equals("")) {
			for (String str : ids.split(",")) {
				if (str != null && !str.equals("")) {
					Accessory img = this.accessoryService.getObjById(CommUtil
							.null2Long(str));
					list.add(img);
				}
			}
		}
		return list;
	}

	public List<Accessory> queryAlbumPhotos(String album_id) {
		List<Accessory> list = new ArrayList<Accessory>();
		Album album = this.albumService
				.getObjById(CommUtil.null2Long(album_id));
		if (album != null) {
			Map params = new HashMap();
			params.put("album_id", CommUtil.null2Long(album_id));
			list = this.accessoryService
					.query("select obj from Accessory obj where obj.album.id=:album_id",
							params, -1, -1);
		}
		return list;
	}

	public int queryAlbumPhotosSize(String album_id) {
		int size = 0;
		List<Accessory> list = new ArrayList<Accessory>();
		Album album = this.albumService
				.getObjById(CommUtil.null2Long(album_id));
		if (album != null) {
			Map params = new HashMap();
			params.put("album_id", CommUtil.null2Long(album_id));
			list = this.accessoryService
					.query("select obj.id from Accessory obj where obj.album.id=:album_id",
							params, -1, -1);
			size = list.size();
		}
		return size;
	}
}
