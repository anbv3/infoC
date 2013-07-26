package com.infoc.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

public class Collector {
	public static ConcurrentSkipListMap<Integer, List<Article>> CACHE = 
			new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());
	
	static {
		for(int i=0 ; i<24 ;i++) {
			CACHE.put(i, new ArrayList<Article>());
		}
	}
}
