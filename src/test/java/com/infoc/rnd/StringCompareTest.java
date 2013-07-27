package com.infoc.rnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.CharMatcher;
import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringCompareTest {
    private final Logger LOG = LoggerFactory.getLogger(StringCompareTest.class);

	@Test
	public void parse() {
		String s1 = "전두환 측근 47명 증권계좌 조사";
		String s2 = "전두환 일가 은행 대여금고 7개 압수";
		Splitter splitter = Splitter.onPattern(" ").trimResults().omitEmptyStrings();

        Set<String> intersection = Sets.intersection(
		        Sets.newHashSet(splitter.split(s1)),
		        Sets.newHashSet(splitter.split(s2)));
		
		System.out.println(intersection);		
	}



    @Test
    public void parse2() {
        String s1 = "[종합]KB금융, 2분기 순익 1652억원…전년比 70.1% ↓";
        String s2 = "KB금융 2Q 순익 1635억원···전기比 60.3%↓";
        Splitter splitter = Splitter.onPattern("\\s|\\,|\\[|\\]|\\;|\\'|\\·|\\…|\\!|\\\"|\\“|\\”").trimResults().omitEmptyStrings();

        for(String ss : Sets.newHashSet(splitter.split(s1))) {
            LOG.debug("{}",ss);
        }

        for(String ss : Sets.newHashSet(splitter.split(s2))) {
            LOG.debug("{}",ss);
        }

        Set<String> intersection = Sets.intersection(
                Sets.newHashSet(splitter.split(s1)),
                Sets.newHashSet(splitter.split(s2)));

        System.out.println(intersection);
    }

    @Test
    public void parse3() {
        String s1 = "클라라 민낯, 섹시 벗고 화장 벗기니 \"딴사람\"";
        String s2 = "클라라 민낯, “진한 화장보다 예뻐… 지우고 다녀!”";
        Splitter splitter = Splitter.onPattern("\\s|\\,|\\[|\\]|\\;|\\'|\\·|\\…|\\!|\\\"|\\“|\\”").trimResults().omitEmptyStrings();


//        for(String ss : Sets.newHashSet(splitter.split(s1))) {
//            LOG.debug("{}",ss);
//        }
//
//        for(String ss : Sets.newHashSet(splitter.split(s2))) {
//            LOG.debug("{}",ss);
//        }
        
        
        List<String> dupWorkList = new ArrayList<>();
        for(String oriWord : Sets.newHashSet(splitter.split(s1))) {
        	for(String tarWord : Sets.newHashSet(splitter.split(s2))) {
            	
        		if(oriWord.contains(tarWord) || tarWord.contains(oriWord) ) {
        			String keyWord = oriWord.length() < tarWord.length() ? oriWord : tarWord;
        			dupWorkList.add(keyWord);
        		}
        		
            }	
        }
        System.out.println(dupWorkList);
    }
}
