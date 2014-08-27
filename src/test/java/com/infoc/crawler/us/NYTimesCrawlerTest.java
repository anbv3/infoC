package com.infoc.crawler.us;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infoc.crawler.NewsCrawler;
import com.infoc.service.USCollectionService;
import com.infoc.service.USContentsAnalysisService;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

public class NYTimesCrawlerTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(NYTimesCrawlerTest.class);

	@Test
	public void test2() {
		String t = "Party’s New Leader Agrees to Form Government in Italy";

		LOG.debug("{}", t.replaceAll("[^\\p{L}\\p{Z}]", ""));

		Set<String> titleList = Sets
				.newHashSet(USContentsAnalysisService.TITLE_SPLITTER
						.omitEmptyStrings().trimResults()
						.split(t.replaceAll("[^\\p{L}\\p{Z}]", " ")));
		LOG.debug("{}", titleList);

		for (String word : titleList) {
			if (word.length() > 2 && StringUtils.isAlphanumeric(word)) {
				LOG.debug("{}", word);
			}
		}
	}

	@Test
	public void test1() {
		NewsCrawler d = new WashingtonTimesCrawler();
		d.createArticleList();

		LOG.debug("{}, {}", USCollectionService.TODAY_CACHE.isEmpty(),
				USCollectionService.TODAY_CACHE.size());
	}

	@Test
	public void testGetImg() throws IOException {
		Document doc = Jsoup
				.connect(
						"http://www.nytimes.com/2014/02/18/nyregion/in-brooklyn-rejecting-the-monotony-of-the-glass-and-steel-look.html?partner=rss&emc=rss")
				.timeout(6000).get();
		Elements contentsArea = doc.select("#story");

		Elements bodyArea = contentsArea.select(".story-body-text");
		StringBuilder sb = new StringBuilder();
		for (Element element : bodyArea) {
			sb.append(element.ownText()).append(" ");
		}
		
		LOG.debug("{}", sb.toString());

		// parse img url
		LOG.debug("{}", contentsArea.select(".image > img").attr("src"));
	}

	@Test
	public void testPreposition() throws IOException {
		String word = "unlike";
		if (word.matches("the|aboard|about|above|across|after|against"
				+ "|along|amid|among|anti|around|before|behind|below|beneath"
				+ "|beside|besides|between|beyond|but|despite|down|during|except"
				+ "|excepting|excluding|following|for|from|inside|into|like|near"
				+ "|off|onto|opposite|outside|over|past|per|plus|regarding|round"
				+ "|since|than|through|toward|under|underneath|until|up|upon|unlike"
				+ "|versus|via|with|within|without")) {

			LOG.debug("haha");
		}
	}

	@Test
	public void testBostonNews() throws IOException {
		Document doc = Jsoup
				.connect(
						"http://www.boston.com/sports/other-sports/olympics/2014/02/17/costas-returns-olympic-coverage/j56d3jKddYFMgQmsRIycIN/story.html?rss_id=Top+Stories")
				.timeout(6000).get();
		Elements contentsArea = doc.select(".blogText");

		// parse img url
		LOG.debug("{}", Strings.isNullOrEmpty(contentsArea.text()));
	}

	@Test
	public void testSpliteSentence() throws UnsupportedEncodingException {
		String kr = "2014-08-27 18:16:50.634 [DEBUG] (RSSCrawlerTest.java:101)(testBasicParsing()) Pete Carroll is entering his fifth season as Seattle's head coach. (Photo: Ted S. Warren AP) 2942 CONNECT 132 TWEET 2 LINKEDIN 32 COMMENTEMAILMORE The NFL has stripped the Super Bowl champion Seattle Seahawks of two days of minicamp practice next year as punishment for violations of the collectively bargained rules on contact in offseason practices, league spokesman Greg Aiello told USA TODAY Sports on Tuesday. The team and coach Pete Carroll were also fined for the violation, which occurred during their June minicamp, a person with knowledge of the decision said, adding that the team was considered a repeat offender over multiple years. The person spoke on condition of anonymity because the league did not confirm the fine, first reported by ESPN, which said the Seahawks and Carroll were collectively docked more than $300,000. USATODAY Buccaneers acquire Logan Mankins from Patriots \"(Seattle) will forfeit its first two days of on-field double practices during its 2015 mandatory minicamp for veteran players and will be permitted a single 2½-hour on-field practice session on the final day of its minicamp,\" Aiello told USA TODAY Sports by e-mail. \"Seahawks players will be paid for the cancelled practice sessions. The violation was for permitting the club's players to engage in excessive levels of on-field physical contact during the team's 2014 mandatory minicamp for veteran players.\" The Seahawks were also fined and lost two OTA practices for a violation of contact rules in 2012. The latest infraction came to light because of publicly available video of the incident, the person with knowledge of the decision said. USATODAY Despite shakeup, Titans and Ken Whisenhunt aiming for playoffs Neither the person nor the league said on which day(s) the violation was considered to have occurred. ESPN reported it was June 16. On June 18, cornerback Richard Sherman and receiver Phil Bates got into a fight that was caught by local TV cameras. At the NFL Players Association's annual meetings in March, the union adopted a zero-tolerance policy toward violations of CBA contact rules, meaning it would pursue discipline in concert with the league even if the team's union representative didn't want to. NFLPA president Eric Winston is a member of the Seahawks, but he didn't sign until July 29. *** Follow Tom Pelissero on Twitter @TomPelissero PHOTOS: NFL player suspensions in 2014 FacebookTwitterGoogle+LinkedIn 2014 NFL player suspensions  Fullscreen Post to Facebook Posted! A link has been posted to your Facebook feed. DB Brandon Meriweather, Redskins: Suspended 2 games - illegal hit (repeat violation).  Alex Brandon, AP Fullscreen K Matt Prater, Broncos: Suspended 4 games - violating substance-abuse policy.  Kyle Terada, USA TODAY Sports Fullscreen OT Donald Stephenson, Chiefs: Suspended 4 games, violating performance-enhancing drugs policy  Ed Zurga, AP Fullscreen WR Dwayne Bowe, Chiefs: Suspended 1 game - violating substance-abuse policy.  Andrew Weber, USA TODAY Sports Fullscreen CB Orlando Scandrick, Cowboys: Suspended 4 games - violating performance-enhancing drugs policy.  Tim Sharp, AP Fullscreen SS Reshad Jones, Dolphins: Suspended four games - violating performance-enhancing drug policy.  Steve Mitchell, USA TODAY Sports Fullscreen RB Ray Rice, Ravens: Suspended 2 games - personal conduct violation.  Tommy Gilligan, USA TODAY Sports Fullscreen DB Reshad Jones, Dolphins: Suspended 4 games - violating performance-enhancing drug policy.  Robert Mayer, USA TODAY Sports Fullscreen DE Dion Jordan, Dolphins: Suspended four games - violating performance-enhancing drug policy.  Steve Mitchell, USA TODAY Sports Fullscreen LB Daryl Washington, Cardinals: Suspended at least one year – violation of substance-abuse policy.  Matt Kartozian-USA TODAY Sports Fullscreen WR LaVon Brazill, Colts: Suspended one year - violation of substance-abuse policy.  Andrew Weber, USA TODAY Sports Fullscreen OL Lane Johnson, Eagles: Suspended 4 games -violation of performance-enhancing drugs policy.  Matt Rourke, AP Fullscreen WR Justin Blackmon, Jaguars: Indefinite suspension – violation of substance-abuse policy.  Don McPeak-USA TODAY Sports Fullscreen Nigel Bradham, LB, Bills: Suspended one game for violation of substance-abuse policy.  Kevin Hoffman, USA TODAY Sports Fullscreen WR Stedman Bailey, Rams: Suspended 4 games – violation of performance-enhancing drugs policy.  Jeff Curry-USA TODAY Sports Fullscreen OLB Robert Mathis, Colts: Suspended 4 games – violation of substance-abuse policy.  Brian Spurlock Brian Spurlock-USA TODAY Sports Fullscreen DB Tanard Jackson, Redskins: Suspended indefinitely - violation of substance-abuse policy.  Rob Grabowski, USA TODAY Sports Fullscreen S Will Hill, formerly of Giants: Suspended 6 games – violation of substance-abuse policy.  Seth Wenig, AP Fullscreen DE Frank Alexander, Panthers: Suspended 4 games – violation of substance-abuse policy.  Brad Mills-US PRESSWIRE Fullscreen CB Jayron Hosley, Giants: Suspended 4 games – violation of substance-abuse policy. Not pictured: DE Brandon Moore, Redskins: Suspended 4 games – violation of substance-abuse policy, OL Rokevious Watkins, Rams: Suspended 4 games - violation on substance-abuse policy and LB Jake Knott, Eagles: Suspended 4 games – violation of performance-enhancing drugs policy.  Robert Deutsch, USA TODAY Sports Fullscreen Like this topic? You may also like these photo galleries: Replay Autoplay Show Thumbnails Show CaptionsLast SlideNext Slide 2942 CONNECT 132 TWEET 2 LINKEDIN 32 COMMENTEMAILMORE";

		List<String> sList = Lists.newArrayList(Splitter
				.onPattern("(?<=\\.\\s)|(?<=\\?\\s)|(?<=\\. )")
				.trimResults().omitEmptyStrings().split(kr));

		for (String s : sList) {
			LOG.debug("{}", s);

		}
	}

}
