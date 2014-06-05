package com.infoc.util;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author NBP
 */
public class TranslationParserTest {
	private static final Logger LOG = LoggerFactory.getLogger(TranslationParserTest.class);
	
	@Test
	public void transContentsParsing() {
		String t = ", \"he said";
		
		LOG.debug(t.replaceAll("\\\"", "\""));
	}
	
	@Test
	public void testGetTranslation() throws UnsupportedEncodingException {
		String kr = "북한은 핵물질을 일시에 압축해 핵폭발을 유도하는 내폭형 장치를 집중적으로 개발해 1980년대 후반부터 100여 차례 이상의" +
				" 고폭실험을 했고 실제 핵실험을 통해 위력을 높인 것으로 알려졌다. 특히 북한은 2011년 12월 장거리 로켓 발사 성공으로 사거리 1만㎞ 이상의 " +
				"장거리 미사일 능력을 거의 확보했기 때문에 핵탄두의 소형화ㆍ경량화를 달성하면 미국 본토를 위협할 수 있는 대륙간탄도미사일 개발에 바짝 다가설 " +
				"수 있기 때문이다. 국내 한 북핵 전문가는 \"ICBM에 탑재하는 수준은 아니더라도 북한도 어느 정도 소형화 기술을 가지고 있다고 판단한다\"며 \"파키스탄이 " +
				"500~11000㎏으로 소형화했다는 점에 비추어  북한도 1000㎏ 정도의 소형화는 가능하다고 봐야 한다\"고 말했다.";
		
		LOG.debug(TranslationParser.krToEn(kr));
	}
	
	@Test
	public void testGetTranslationKR2EN() throws UnsupportedEncodingException {
		String kr = "Europe Party’s New Leader Agrees to Form Government in Italy By ELISABETTA POVOLEDOFEB. "
				+ "But his first challenge is to form a coalition that will back his party, "
				+ "which does not have a majority in either house of Parliament. Renzi said that "
				+ "forming a government would “take a few days,” given the scope of the changes he hoped to enact..";
		
		LOG.debug(TranslationParser.enToKr(kr));
		LOG.debug("{}", kr.length());
	}
	
}
