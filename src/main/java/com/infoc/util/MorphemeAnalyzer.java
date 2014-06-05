package com.infoc.util;

import java.io.File;
import java.net.URL;
import java.util.List;

import kr.co.shineware.nlp.komoran.core.MorphologyAnalyzer;
import kr.co.shineware.util.common.model.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MorphemeAnalyzer {
	private static final Logger LOG = LoggerFactory
			.getLogger(MorphemeAnalyzer.class);
	private static MorphemeAnalyzer ourInstance = new MorphemeAnalyzer();

	public static MorphemeAnalyzer getInstance() {
		return ourInstance;
	}

	private MorphemeAnalyzer() {
	}

	public String extractNouns(String contents) {
		StringBuilder sb = new StringBuilder();

		try {
			LOG.debug("------------------------------------------------------");
			URL url = this.getClass().getClassLoader().getResource("ko/");
			File ko = new File(url.toURI());
			LOG.debug("{}", ko.getAbsolutePath());

			MorphologyAnalyzer analyzer = new MorphologyAnalyzer(ko.getAbsolutePath());
			LOG.debug("------------------------------------------------------");
			
			List<List<Pair<String, String>>> result = analyzer.analyze(contents);

			for (List<Pair<String, String>> eojeolResult : result) {
				for (Pair<String, String> wordMorph : eojeolResult) {
					if (wordMorph.getSecond().equals("NNG")
							|| wordMorph.getSecond().equals("NNP")) {
						sb.append(wordMorph.getFirst()).append(" ");
					}
				}
			}
			LOG.debug("{}", sb.toString());
		} catch (Exception e) {
			LOG.error("", e);
		}

		return sb.toString();
	}

}
