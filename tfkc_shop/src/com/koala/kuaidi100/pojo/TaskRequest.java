package com.koala.kuaidi100.pojo;

import java.util.HashMap;
import java.util.Map.Entry;

import com.koala.kuaidi100.post.JacksonHelper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.DefaultMapper;

/**
 * 
 * <p>
 * Title: TaskRequest.java
 * </p>
 * 
 * <p>
 * Description: 该实体类来自快递100提供的接口 
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
 * @date 2014-12-9
 * 
 * @version koala_b2b2c 2015
 */
public class TaskRequest {
	private static XStream xstream;
	private String company;
	private String number;
	private String from;
	private String to;
	private String key;
	private String src;

	private HashMap<String, String> parameters = new HashMap<String, String>();

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public HashMap<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		return JacksonHelper.toJSON(this);
	}

	private static XStream getXStream() {
		if (xstream == null) {
			xstream = new XStream();
			xstream.registerConverter(new MapCustomConverter(new DefaultMapper(
					XStream.class.getClassLoader())));
			xstream.autodetectAnnotations(true);
			xstream.alias("orderRequest", TaskRequest.class);
			xstream.alias("property", Entry.class);
		}
		return xstream;
	}

	public String toXml() {
		return "<?xml version='1.0' encoding='UTF-8'?>\r\n"
				+ getXStream().toXML(this);
	}

	public static TaskRequest fromXml(String sXml) {
		return (TaskRequest) getXStream().fromXML(sXml);
	}

}
