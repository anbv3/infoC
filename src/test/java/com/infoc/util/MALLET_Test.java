package com.infoc.util;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;
import kr.ac.kaist.swrc.jhannanum.comm.Eojeol;
import kr.ac.kaist.swrc.jhannanum.comm.Sentence;
import kr.ac.kaist.swrc.jhannanum.hannanum.Workflow;
import kr.ac.kaist.swrc.jhannanum.plugin.MajorPlugin.MorphAnalyzer.ChartMorphAnalyzer.ChartMorphAnalyzer;
import kr.ac.kaist.swrc.jhannanum.plugin.MajorPlugin.PosTagger.HmmPosTagger.HMMTagger;
import kr.ac.kaist.swrc.jhannanum.plugin.SupplementPlugin.MorphemeProcessor.UnknownMorphProcessor.UnknownProcessor;
import kr.ac.kaist.swrc.jhannanum.plugin.SupplementPlugin.PlainTextProcessor.InformalSentenceFilter.InformalSentenceFilter;
import kr.ac.kaist.swrc.jhannanum.plugin.SupplementPlugin.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor;
import kr.ac.kaist.swrc.jhannanum.plugin.SupplementPlugin.PosProcessor.NounExtractor.NounExtractor;
import kr.co.shineware.nlp.komoran.core.MorphologyAnalyzer;
import kr.co.shineware.util.common.model.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * @author Naver
 * @date 2014-06-02
 * Copyright 2007 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class MALLET_Test {
    private static final Logger LOG = LoggerFactory.getLogger(MALLET_Test.class);

    static String inData = "새누리당 신의진 의원이 '중독 예방 및 관리 및 치료를 위한 법률안'(이하 중독법)에서 인터넷 게임을 제외할 수 도 있다고 밝힌 것으로 알려져 2일 게임업계가 주목하고 있다.\n" +
            "\n" +
            "게임업계에 따르면 신 의원이 중독법에서 게임을 빼기로 한 것은 게임업계와 유저들 반발로 중독법의 입법이 쉽지 않기 때문인 것으로 알려졌다. 중독법은 2013년 보건복지 위원회 심사부터 '게임을 제외해야 한다'는 문화체육관광부의 반대에 부딪혔고 이후에 '게임을 마약과 동일시한다', '정확하게 검증되지 않은 게임에 중독낙인을 찍고 있다' 등 반발을 샀다. 게임 때문에 마약과 알코올, 도박 등 다른 중독물질에 대한 법안마저 통과에 어려움을 겪자 중독법 통과를 위해 게임을 중독법에서 제외하기로 결정한 셈이다.\n" +
            "\n" +
            "신 의원이 발의한 '중독법'은 알코올, 마약, 도박 같은 중독물질에 대해 국가 차원에서 중독 예방 및 치료 체계를 갖추고, 이를 위해 국무총리 소속 국가중독관리위원회와 중독관리센터를 설치하는 내용을 골자로 하고 있다.\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "이 법안은 게임을 물리적, 정신적 중독 요소인 도박과 마약 등을 동급인 중독 물질로 취급해 게이머들과 많은 전문가에게 거센 반발을 불러오며 논란이 된 바 있다. 현재 해당 법안은 지난해 4월 발의 이후 국회 보건복지위원회 법안심사소위원회에 계류돼 있는 상태다.\n" +
            "\n" +
            "새누리당의 손인춘 의원이 발의한 '인터넷 게임중독 예방 및 치유지원에 관한 법률(이른바 손인춘법)도 아직 국회에 계류된 상태이며 신 의원 또한 '게임 및 미디어 콘텐츠를 따로 떼어내 관리하는 방안을 검토 중'이라고 밝힌 바 있다.\n" +
            "\n" +
            "신 의원은 지난 1일 머나투데이와 가진 인터뷰에서 \"중독법에서 인터넷 게임과 스마트폰 게임 등의 미디어 콘텐츠를 제외하고 이를 특별히 다루는 새로운 방안을 보건복지부와 협의 중\"이라고 밝혔다. 알코올, 마약, 도박 등 이미 그 사례가 입증된 중독 요소들을 이번 법안에 포함하고, 게임 및 미디어 콘텐츠에 대해서는 다음에 논의하겠다는 의미다.\n" +
            "\n" +
            "신의원은 \"이번 법안에 반대하는 사람들은 게임과 마약이 같이 분류되어 화가 나는 것으로 생각한다. 이런 식으로 논란이 되면 법안이 입법되기 힘들다\"고 전했다.\n" +
            "\n" +
            "이에 대한 논란이 이어지자 신 의원은 2일 \"중독법에서 게임 등 미디어 콘텐츠만을 분리시켜 별도 법안을 마련하는 방안을 논의 중\"이라며 \"입법화를 위한 과정으로 게임 중독에 대한 기본 입장에 변화가 없다\"는 입장을 밝혔다.\n" +
            "\n" +
            "게임업계와 ICT산업에선일단 게임을 제외한 상태에서 중독법을 통과시킨 후 향후 게임에 대한 별도의 법안을 발의하는 것이 아니냐는 추측도 나오고 있다.";

    @Test
    public void testLucene() throws Exception {

    }

    @Test
    public void testWorkflowNounExtractor() throws IOException, URISyntaxException {

        String baseDir = new ClassPathResource("han/").getFile().getAbsolutePath();
        Workflow workflow = new Workflow(baseDir);

        workflow.appendPlainTextProcessor(new SentenceSegmentor(), null);
        workflow.appendPlainTextProcessor(new InformalSentenceFilter(), null);

        workflow.setMorphAnalyzer(new ChartMorphAnalyzer(), "/plugin/MajorPlugin/MorphAnalyzer/ChartMorphAnalyzer.json");
        workflow.appendMorphemeProcessor(new UnknownProcessor(), null);

        workflow.setPosTagger(new HMMTagger(), "/plugin/MajorPlugin/PosTagger/HmmPosTagger.json");
        workflow.appendPosProcessor(new NounExtractor(), null);


        try {
            /* Activate the work flow in the thread mode */
            workflow.activateWorkflow(true);

			/* Analysis using the work flow */
            String document = "롯데마트가 판매하고 있는 흑마늘 양념 치킨이 논란이 되고 있다.\n";
            workflow.analyze(inData);

            LinkedList<Sentence> resultList = workflow.getResultOfDocument(new Sentence(0, 0, false));
            for (Sentence s : resultList) {
                Eojeol[] eojeolArray = s.getEojeols();
                for (int i = 0; i < eojeolArray.length; i++) {
                    if (eojeolArray[i].length > 0) {
                        String[] morphemes = eojeolArray[i].getMorphemes();


                        for (int j = 0; j < morphemes.length; j++) {
                            if (morphemes[j].length() < 2) {
                                continue;
                            }

                            System.out.print(morphemes[j]);
                        }
                        System.out.print(", ");
                    }
                }
            }

            workflow.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

		/* Shutdown the work flow */
        workflow.close();
    }

    @Test
    public void testKO() throws IOException, URISyntaxException {
        StringBuilder sb = new StringBuilder();

        File file = new ClassPathResource("ko/").getFile();
        MorphologyAnalyzer analyzer = new MorphologyAnalyzer(file.getAbsolutePath());

        List<List<Pair<String, String>>> result = analyzer.analyze(inData);

        for (List<Pair<String, String>> eojeolResult : result) {
            for (Pair<String, String> wordMorph : eojeolResult) {
                LOG.debug("{}", wordMorph);

                if (wordMorph.getSecond().equals("NNG") || wordMorph.getSecond().equals("NNP")) {
                    sb.append(wordMorph.getFirst()).append(" ");
                }
            }
        }

        LOG.debug("{}", sb.toString());
    }


    @Test
    public void testMALLET() throws IOException, URISyntaxException {
        StringBuilder sb = new StringBuilder();

        String baseDir = new ClassPathResource("han/").getFile().getAbsolutePath();
        Workflow workflow = new Workflow(baseDir);
        workflow.appendPlainTextProcessor(new SentenceSegmentor(), null);
        workflow.appendPlainTextProcessor(new InformalSentenceFilter(), null);
        workflow.setMorphAnalyzer(new ChartMorphAnalyzer(), "/plugin/MajorPlugin/MorphAnalyzer/ChartMorphAnalyzer.json");
        workflow.appendMorphemeProcessor(new UnknownProcessor(), null);
        workflow.setPosTagger(new HMMTagger(), "/plugin/MajorPlugin/PosTagger/HmmPosTagger.json");
        workflow.appendPosProcessor(new NounExtractor(), null);

        try {
            /* Activate the work flow in the thread mode */
            workflow.activateWorkflow(true);

			/* Analysis using the work flow */
            workflow.analyze(inData);

            LinkedList<Sentence> resultList = workflow.getResultOfDocument(new Sentence(0, 0, false));
            for (Sentence s : resultList) {
                Eojeol[] eojeolArray = s.getEojeols();
                for (int i = 0; i < eojeolArray.length; i++) {
                    if (eojeolArray[i].length > 0) {
                        String[] morphemes = eojeolArray[i].getMorphemes();
                        for (int j = 0; j < morphemes.length; j++) {
//                            if (morphemes[j].length() < 2) {
//                                continue;
//                            }
                            sb.append(morphemes[j]);
                        }
                        sb.append(" ");
                    }
                }
            }

            workflow.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

		/* Shutdown the work flow */
        workflow.close();

        LOG.debug("{}", sb.toString());
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add(new CharSequenceLowercase());
        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));

        java.net.URL url = this.getClass().getResource("stoplists/en.txt");
        //File stopListFile = java.nio.file.Paths.get(url.toURI()).toFile();

        File file = new ClassPathResource("stoplists/en.txt").getFile();

        pipeList.add(new TokenSequenceRemoveStopwords(file, "UTF-8", false, false, false));
//        pipeList.add(new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false));
        pipeList.add(new TokenSequence2FeatureSequence());

        InstanceList instances = new InstanceList(new SerialPipes(pipeList));

        String fileName = "";
//        Reader fileReader = new InputStreamReader(new FileInputStream(new File(fileName)), "UTF-8");
        Reader fileReader = new InputStreamReader(new ByteArrayInputStream(sb.toString().getBytes()));


        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // data, label, name fields

        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is the parameter for a single dimension of the Dirichlet prior.
        int numTopics = 100;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

        model.addInstances(instances);

        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(2);

        // Run the model for 50 iterations and stop (this is for testing only,
        //  for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(300);
        model.estimate();

        // Show the words and topics in the first instance

        // The data alphabet maps word IDs to strings
        Alphabet dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;

        Formatter out = new Formatter(new StringBuilder(), Locale.KOREA);
        for (int position = 0; position < tokens.getLength(); position++) {
            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }
        LOG.info("{}", out);

        // Estimate the topic distribution of the first instance,
        //  given the current Gibbs state.
        double[] topicDistribution = model.getTopicProbabilities(0);

        // Get an array of sorted sets of word ID/count pairs
        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

        // Show top 5 words in topics with proportions for the first document
        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

            out = new Formatter(new StringBuilder(), Locale.US);
            out.format("%d\t%.3f\t", topic, topicDistribution[topic]);

            int rank = 0;
            while (iterator.hasNext() && rank < 5) {
                IDSorter idCountPair = iterator.next();
                out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
                rank++;
            }

            if (out.toString().contains("0.00")) {
                continue;
            }
            LOG.debug("{}", out);
        }

        // Create a new instance with high probability of topic 0
        StringBuilder topicZeroText = new StringBuilder();
        Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

        int rank = 0;
        while (iterator.hasNext() && rank < 5) {
            IDSorter idCountPair = iterator.next();
            topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
            rank++;
        }

        // Create a new instance named "test instance" with empty target and source fields.
        InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

        TopicInferencer inferencer = model.getInferencer();
        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
        LOG.info("0\t{}", testProbabilities[0]);
    }


    @Test
    public void testMALLET2() throws IOException, URISyntaxException {
        StringBuilder sb = new StringBuilder();

        MorphologyAnalyzer analyzer = new MorphologyAnalyzer(new ClassPathResource("ko/").getFile().getAbsolutePath());

        List<List<Pair<String, String>>> result = analyzer.analyze(inData);


        for (List<Pair<String, String>> eojeolResult : result) {
            for (Pair<String, String> wordMorph : eojeolResult) {
                LOG.debug("{}", wordMorph);
                if (wordMorph.getSecond().equals("NNG") || wordMorph.getSecond().equals("NNP")) {
                    sb.append(wordMorph.getFirst()).append(" ");
                }

            }
//            System.out.println();
        }
        LOG.debug("{}", sb.toString());


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add(new CharSequenceLowercase());
        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));

        java.net.URL url = this.getClass().getResource("stoplists/en.txt");
        //File stopListFile = java.nio.file.Paths.get(url.toURI()).toFile();

        File file = new ClassPathResource("stoplists/en.txt").getFile();

        pipeList.add(new TokenSequenceRemoveStopwords(file, "UTF-8", false, false, false));
//        pipeList.add(new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false));
        pipeList.add(new TokenSequence2FeatureSequence());

        InstanceList instances = new InstanceList(new SerialPipes(pipeList));

        String fileName = "";
//        Reader fileReader = new InputStreamReader(new FileInputStream(new File(fileName)), "UTF-8");
        Reader fileReader = new InputStreamReader(new ByteArrayInputStream(sb.toString().getBytes()));


        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // data, label, name fields

        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is the parameter for a single dimension of the Dirichlet prior.
        int numTopics = 100;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

        model.addInstances(instances);

        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(2);

        // Run the model for 50 iterations and stop (this is for testing only,
        //  for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(300);
        model.estimate();

        // Show the words and topics in the first instance

        // The data alphabet maps word IDs to strings
        Alphabet dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;

        Formatter out = new Formatter(new StringBuilder(), Locale.KOREA);
        for (int position = 0; position < tokens.getLength(); position++) {
            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }
        LOG.info("{}", out);

        // Estimate the topic distribution of the first instance,
        //  given the current Gibbs state.
        double[] topicDistribution = model.getTopicProbabilities(0);

        // Get an array of sorted sets of word ID/count pairs
        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

        // Show top 5 words in topics with proportions for the first document
        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

            out = new Formatter(new StringBuilder(), Locale.US);
            out.format("%d\t%.3f\t", topic, topicDistribution[topic]);

            int rank = 0;
            while (iterator.hasNext() && rank < 5) {
                IDSorter idCountPair = iterator.next();
                out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
                rank++;
            }

            if (out.toString().contains("0.00")) {
                continue;
            }
            LOG.debug("{}", out);
        }

        // Create a new instance with high probability of topic 0
        StringBuilder topicZeroText = new StringBuilder();
        Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

        int rank = 0;
        while (iterator.hasNext() && rank < 5) {
            IDSorter idCountPair = iterator.next();
            topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
            rank++;
        }

        // Create a new instance named "test instance" with empty target and source fields.
        InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

        TopicInferencer inferencer = model.getInferencer();
        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
        LOG.info("0\t{}", testProbabilities[0]);
    }


    @Test
    public void testMALLET3() throws IOException, URISyntaxException {
        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add(new CharSequenceLowercase());
        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));

        java.net.URL url = this.getClass().getResource("stoplists/en.txt");
        //File stopListFile = java.nio.file.Paths.get(url.toURI()).toFile();

        File file = new ClassPathResource("stoplists/en.txt").getFile();

        pipeList.add(new TokenSequenceRemoveStopwords(file, "UTF-8", false, false, false));
        pipeList.add(new TokenSequence2FeatureSequence());

        InstanceList instances = new InstanceList(new SerialPipes(pipeList));

        String fileName = "";
        Reader fileReader = new InputStreamReader(new ByteArrayInputStream(inData.getBytes()));


        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // data, label, name fields

        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is the parameter for a single dimension of the Dirichlet prior.
        int numTopics = 100;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

        model.addInstances(instances);

        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(2);

        // Run the model for 50 iterations and stop (this is for testing only,
        //  for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(500);
        model.estimate();

        // Show the words and topics in the first instance

        // The data alphabet maps word IDs to strings
        Alphabet dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;

        Formatter out = new Formatter(new StringBuilder(), Locale.KOREA);
        for (int position = 0; position < tokens.getLength(); position++) {
            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }
        LOG.info("{}", out);

        // Estimate the topic distribution of the first instance,
        //  given the current Gibbs state.
        double[] topicDistribution = model.getTopicProbabilities(0);

        // Get an array of sorted sets of word ID/count pairs
        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

        // Show top 5 words in topics with proportions for the first document
        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

            out = new Formatter(new StringBuilder(), Locale.US);
            out.format("%d\t%.3f\t", topic, topicDistribution[topic]);

            int rank = 0;
            while (iterator.hasNext() && rank < 5) {
                IDSorter idCountPair = iterator.next();
                out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
                rank++;
            }

            if (out.toString().contains("0.00")) {
                continue;
            }
            LOG.debug("{}", out);
        }

        // Create a new instance with high probability of topic 0
        StringBuilder topicZeroText = new StringBuilder();
        Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

        int rank = 0;
        while (iterator.hasNext() && rank < 5) {
            IDSorter idCountPair = iterator.next();
            topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
            rank++;
        }

        // Create a new instance named "test instance" with empty target and source fields.
        InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

        TopicInferencer inferencer = model.getInferencer();
        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
        LOG.info("0\t{}", testProbabilities[0]);
    }

    @Test
    public void test() throws IOException {
        String nouns = MorphemeAnalyzer.getInstance().extractNouns(inData);
        Set<String> tList = TopicModeler.getInstance().getMainTopics(nouns);

        LOG.debug("{}", tList);
    }
}
