package com.koala.kuaidi100.pojo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * 
 * <p>
 * Title: MapCustomConverter.java
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
public class MapCustomConverter extends AbstractCollectionConverter {

	public MapCustomConverter(Mapper mapper) {
		super(mapper);
	}

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		// 这里只列了HashMap一种情况
		return type.equals(HashMap.class);
	}

	@SuppressWarnings("rawtypes")
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Map map = (Map) source;
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			ExtendedHierarchicalStreamWriterHelper.startNode(writer, entry
					.getKey().toString(), Entry.class);

			writer.setValue(entry.getValue().toString());
			writer.endNode();
		}
	}

	@SuppressWarnings("rawtypes")
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		Map map = (Map) createCollection(context.getRequiredType());
		populateMap(reader, context, map);
		return map;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void populateMap(HierarchicalStreamReader reader,
			UnmarshallingContext context, Map map) {
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			Object key = reader.getNodeName();
			Object value = reader.getValue();
			map.put(key, value);
			reader.moveUp();
		}
	}

}