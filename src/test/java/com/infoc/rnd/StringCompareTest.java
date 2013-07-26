package com.infoc.rnd;

import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

public class StringCompareTest {
	
	@Test
	public void parse() {
		String s1 = "전두환 측근 47명 증권계좌 조사";
		String s2 = "전두환 일가 은행 대여금고 7개 압수";
		Splitter splitter = Splitter.onPattern(" ").trimResults().omitEmptyStrings();
		Set<String> intersection = Sets.intersection(//
		        Sets.newHashSet(splitter.split(s1)), //
		        Sets.newHashSet(splitter.split(s2)));
		
		System.out.println(intersection);		
	}
	
}
