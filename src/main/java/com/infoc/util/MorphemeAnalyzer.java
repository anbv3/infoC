package com.infoc.util;

import kr.ac.kaist.swrc.jhannanum.comm.Eojeol;
import kr.ac.kaist.swrc.jhannanum.comm.Sentence;
import kr.ac.kaist.swrc.jhannanum.hannanum.Workflow;
import kr.ac.kaist.swrc.jhannanum.plugin.MajorPlugin.MorphAnalyzer.ChartMorphAnalyzer.ChartMorphAnalyzer;
import kr.ac.kaist.swrc.jhannanum.plugin.MajorPlugin.PosTagger.HmmPosTagger.HMMTagger;
import kr.ac.kaist.swrc.jhannanum.plugin.SupplementPlugin.MorphemeProcessor.UnknownMorphProcessor.UnknownProcessor;
import kr.ac.kaist.swrc.jhannanum.plugin.SupplementPlugin.PlainTextProcessor.InformalSentenceFilter.InformalSentenceFilter;
import kr.ac.kaist.swrc.jhannanum.plugin.SupplementPlugin.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor;
import kr.ac.kaist.swrc.jhannanum.plugin.SupplementPlugin.PosProcessor.NounExtractor.NounExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.util.LinkedList;

/**
 * @author Naver
 * @date 2014-06-03
 * Copyright 2007 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class MorphemeAnalyzer {
    private static final Logger LOG = LoggerFactory.getLogger(MorphemeAnalyzer.class);
    private static MorphemeAnalyzer ourInstance = new MorphemeAnalyzer();
    public static MorphemeAnalyzer getInstance() {
        return ourInstance;
    }

    private Workflow workflow;

    private MorphemeAnalyzer() {
        try {
            this.workflow = new Workflow(new ClassPathResource("han/").getFile().getAbsolutePath());
            this.workflow.appendPlainTextProcessor(new SentenceSegmentor(), null);
            this.workflow.appendPlainTextProcessor(new InformalSentenceFilter(), null);
            this.workflow.setMorphAnalyzer(new ChartMorphAnalyzer(), "/plugin/MajorPlugin/MorphAnalyzer/ChartMorphAnalyzer.json");
            this.workflow.appendMorphemeProcessor(new UnknownProcessor(), null);
            this.workflow.setPosTagger(new HMMTagger(), "/plugin/MajorPlugin/PosTagger/HmmPosTagger.json");
            this.workflow.appendPosProcessor(new NounExtractor(), null);
            this.workflow.activateWorkflow(true);
        } catch (Exception e) {
            LOG.error("{}", e);
        }
    }


    public String extractNouns(String contents) {
        StringBuilder sb = new StringBuilder();
        try {
			/* Analysis using the work flow */
            this.workflow.analyze(contents);

            LinkedList<Sentence> resultList = this.workflow.getResultOfDocument(new Sentence(0, 0, false));
            for (Sentence s : resultList) {
                Eojeol[] eojeolArray = s.getEojeols();
                for (int i = 0; i < eojeolArray.length; i++) {
                    if (eojeolArray[i].length > 0) {
                        String[] morphemes = eojeolArray[i].getMorphemes();
                        for (int j = 0; j < morphemes.length; j++) {
                            sb.append(morphemes[j]);
                        }
                        sb.append(" ");
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }


        return sb.toString();
    }

    public void cleanup() {
        this.workflow.close();
        this.workflow.clear();
    }

}
