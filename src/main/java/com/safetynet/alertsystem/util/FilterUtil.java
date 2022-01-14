package com.safetynet.alertsystem.util;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Component
public class FilterUtil {
	
	public static MappingJacksonValue applyPersonDetailsExcludeFilter(Object object, String... fieldExcluded) {
	     if(object == null) {
	    	 return null;
	     }
	     
		 SimpleBeanPropertyFilter personInfoDetailsFilter = SimpleBeanPropertyFilter.serializeAllExcept(fieldExcluded);
	     
		 FilterProvider filteredList = new SimpleFilterProvider().addFilter("myFilterPersonDetails", personInfoDetailsFilter);
	     
	     MappingJacksonValue objectFiltered = new MappingJacksonValue(object);
	     objectFiltered.setFilters(filteredList);
	     
	     return objectFiltered;
	}

}
