/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.kathrynhuxtable.heroes.service.persistence.filter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class UIFilterServiceImpl implements UIFilterService {

	Map<Class<?>, Map<String, FieldDescriptor>> classMap = new HashMap<>();

	/**
	 * Get the map of FieldDescriptor objects by filter field name.
	 *
	 * @param clazz the domain class to get
	 * @return a Map mapping filter field names to FieldDescriptor objects.
	 */
	@Override
	public Map<String, FieldDescriptor> getDescriptorMap(Class<?> clazz) {
		if (!classMap.containsKey(clazz)) {
			classMap.put(clazz, registerClass(clazz));
		}
		return classMap.get(clazz);
	}

	private Map<String, FieldDescriptor> registerClass(Class<?> clazz) {
		Map<String, FieldDescriptor> map = new HashMap<>();

		for (Field field : clazz.getDeclaredFields()) {
			UIFilterDescriptor fieldDescriptor = field.getAnnotation(UIFilterDescriptor.class);
			if (fieldDescriptor != null) {
				String name = fieldDescriptor.name().isEmpty() ? field.getName() : fieldDescriptor.name();
				map.put(name,
						new FieldDescriptor(
								field.getName(),
								DataType.getDataType(field.getType()),
								fieldDescriptor.global()));
			}
		}

		return map;
	}
}
