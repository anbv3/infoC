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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class MALLET_Test {
    private static final Logger LOG = LoggerFactory.getLogger(MALLET_Test.class);

    static String inData = "새누리당 신의진 의원이 '중독 예방 및 관리 및 치료를 위한 법률안'(이하 중독법)에서 인터넷 게임을 제외할 수 도 있다고 밝힌 것으로 알려져 2일 게임업계가 주목하고 있다.\n" +
            "게임업계에 따르면 신 의원이 중독법에서 게임을 빼기로 한 것은 게임업계와 유저들 반발로 중독법의 입법이 쉽지 않기 때문인 것으로 알려졌다. 중독법은 2013년 보건복지 위원회 심사부터 '게임을 제외해야 한다'는 문화체육관광부의 반대에 부딪혔고 이후에 '게임을 마약과 동일시한다', '정확하게 검증되지 않은 게임에 중독낙인을 찍고 있다' 등 반발을 샀다. " +
            "게임 때문에 마약과 알코올, 도박 등 다른 중독물질에 대한 법안마저 통과에 어려움을 겪자 중독법 통과를 위해 게임을 중독법에서 제외하기로 결정한 셈이다.\n" +
            "신 의원이 발의한 '중독법'은 알코올, 마약, 도박 같은 중독물질에 대해 국가 차원에서 중독 예방 및 치료 체계를 갖추고, 이를 위해 국무총리 소속 국가중독관리위원회와 중독관리센터를 설치하는 내용을 골자로 하고 있다.\n" +
            "이 법안은 게임을 물리적, 정신적 중독 요소인 도박과 마약 등을 동급인 중독 물질로 취급해 게이머들과 많은 전문가에게 거센 반발을 불러오며 논란이 된 바 있다. 현재 해당 법안은 지난해 4월 발의 이후 국회 보건복지위원회 법안심사소위원회에 계류돼 있는 상태다.\n" +
            "새누리당의 손인춘 의원이 발의한 '인터넷 게임중독 예방 및 치유지원에 관한 법률(이른바 손인춘법)도 아직 국회에 계류된 상태이며 신 의원 또한 '게임 및 미디어 콘텐츠를 따로 떼어내 관리하는 방안을 검토 중'이라고 밝힌 바 있다.\n" +
            "신 의원은 지난 1일 머나투데이와 가진 인터뷰에서 \"중독법에서 인터넷 게임과 스마트폰 게임 등의 미디어 콘텐츠를 제외하고 이를 특별히 다루는 새로운 방안을 보건복지부와 협의 중\"이라고 밝혔다. 알코올, 마약, 도박 등 이미 그 사례가 입증된 중독 요소들을 이번 법안에 포함하고, 게임 및 미디어 콘텐츠에 대해서는 다음에 논의하겠다는 의미다.\n" +
            "신의원은 \"이번 법안에 반대하는 사람들은 게임과 마약이 같이 분류되어 화가 나는 것으로 생각한다. 이런 식으로 논란이 되면 법안이 입법되기 힘들다\"고 전했다.\n" +
            "이에 대한 논란이 이어지자 신 의원은 2일 \"중독법에서 게임 등 미디어 콘텐츠만을 분리시켜 별도 법안을 마련하는 방안을 논의 중\"이라며 \"입법화를 위한 과정으로 게임 중독에 대한 기본 입장에 변화가 없다\"는 입장을 밝혔다.\n" +
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

        Reader fileReader = new InputStreamReader(new ByteArrayInputStream(inData.getBytes()));


        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // data, label, name fields

        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is the parameter for a single dimension of the Dirichlet prior.
        int numTopics = 3;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, 0.5, 0.01);

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
        Set<String> keywordList = new HashSet<>();
        // Show top 5 words in topics with proportions for the first document
        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

            out = new Formatter(new StringBuilder(), Locale.US);
            out.format("%d\t%.3f\t", topic, topicDistribution[topic]);

            int rank = 0;
            while (iterator.hasNext() && rank < 2) {
                IDSorter idCountPair = iterator.next();
                out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
                rank++;

                String topicWord = (String) dataAlphabet.lookupObject(idCountPair.getID());
                keywordList.add(topicWord);
            }

            LOG.debug("{}", out);
        }
        LOG.debug("{}", keywordList);

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
        //String nouns = MorphemeAnalyzer.getInstance().extractNouns(inData);
    	String krData = "[OSEN=김태우 기자] 류현진(27, LA 다저스)이 투수들의 무덤으로 불리는 쿠어스필드에서도 흔들리지 않으며 호투했다. 쿠어스필드에서 가장 위험한 장타 위협을 줄인 땅볼유도가 가장 큰 원동력이었다."
+ "류현진은 7일(이하 한국시간) 미 콜로라도주 덴버 쿠어스필드에서 열린 콜로라도 로키스와의 원정 경기에 선발 등판해 6이닝 동안 8피안타(1피홈런) 2볼넷 2탈삼진 2실점으로 잘 던지며 시즌 7승(2패)을 거뒀다. 6회 실점이 다소 아쉽긴 했지만 5회까지는 뛰어난 위기관리능력을 발휘하며 팀 승리의 발판을 놨다. 왼 어깨 부상에서 돌아온 뒤 4연승 질주다."
+ "이날 등판은 류현진의 쿠어스필드 첫 등판이라는 점에서 큰 화제를 모았다. 쿠어스필드는 해발 1610m의 고지대에 위치하고 있다. 평지보다는 공기 저항이 적어 큰 타구가 많이 나오는 구장으로 악명이 높다. 타자들에게는 기회의 땅이지만, 투수들에게는 악몽의 땅이다. 실제 리그를 대표하는 에이스급 투수들도 쿠어스필드에서 장타에 고전하는 경우가 적지 않았다. 리그를 호령했던 박찬호 역시 쿠어스필드에서의 평균자책점은 6.06이었다."
+ "그러나 류현진은 영리하게 쿠어스필드 격파 비법을 찾았다. 결국 장타를 줄이려면 뜬공보다는 땅볼을 많이 유도해야 했고 류현진은 이 평범한 진리를 쉽게 풀어나갔다. 8개의 안타, 2개의 볼넷을 허용하며 피출루 자체는 많았지만 특유의 위기관리능력으로 좋은 성적을 냈다."
+ "1회 선두 블랙먼에게 안타를 맞았지만 드마이유를 병살타로 잡아내며 땅볼 유도의 감을 찾았다. 2회에도 스텁스와 버틀러를 유격수 땅볼로 잡아내며 역시 득점권 상황을 정리했다. 3회부터는 땅볼 타구 비율을 높여갔다. 블랙먼, 르마이유, 모노를 모두 땅볼로 요리했다. 4회에도 버틀러를, 6회에도 모노를 땅볼로 잡았다. 이날 류현진의 땅볼로 총 8개의 아웃카운트(병살 1개 포함)를 잡았던 반면 뜬공은 4개였다."
+ "사실 류현진은 올 시즌 땅볼 유도가 다소 줄어든 편이었다. 올 시즌 류현진의 땅볼/뜬공 비율은 1.10이었다. 이는 지난해 1.45보다 떨어진 성적이다. 그러나 장타를 가장 조심해야 할 쿠어스필드에서 땅볼유도능력을 발휘하며 고비를 슬기롭게 잘 넘겼다. 류현진의 저력을 엿볼 수 있었던 한 판이었다.";
   
    	String enData = "Mayor Rahm Emanuel put the city's schoolchildren on the hook to read a couple of million"
    			+ " more books this summer, part of a bet on national TV to get late-night host Jimmy Fallon to"
    			+ " visit Chicago again.    The mayor's appearance on 'The Tonight Show' capped a two-day trip "
    			+ "to New York in which Emanuel also pitched financial executives on the city's balance sheet "
    			+ "and held a political meeting.    Emanuel agreed to be a talk show guest after Fallon met "
    			+ "his dare of jumping into an icy Lake Michigan as part of the Polar Plunge, a March event that "
    			+ "benefits Special Olympics. Emanuel joined Fallon in the frigid lake after challenging Chicago "
    			+ "students to read 2 million books.    RELATED  Video: Rahm on 'Tonight Show With Jimmy Fallon'  "
    			+ "Video: Rahm on 'Tonight Show'  Rahm Emanuel visits Jimmy Fallon  Rahm Emanuel visits "
    			+ "Jimmy Fallon  Fallon, Emanuel take the Polar Plunge  Fallon, Emanuel take the Polar Plunge "
    			+ " Jimmy and Rahm: A Twitter 'bromance'  Photos: Fallon, Emanuel take polar plunge  Emanuel to "
    			+ " on Fallon show tonight  Emanuel to Fallon: Forget the sunblock  See more stories ≫    "
    			+ "On Tuesday night, Emanuel offered Fallon another bet.    'Here's our challenge: This year"
    			+ " the kids of the city of Chicago, I challenged them to 2.4 million books to be read, and if "
    			+ "they do it this summer we want you to bring the Tonight Show back to Chicago, it hasn't been "
    			+ "since 1998,' Emanuel said in a rare moment of pleading. 'If they read it, will you do it, for"
    			+ " the kids of Chicago?'    After Fallon tried to bump the number up to 11 million, Emanuel joked"
    			+ " that a local accounting firm would handle the tally before saying, '2.5 million books, "
    			+ "you can bring your show back to Chicago any time.'    Fallon agreed, saying 'I love to go to"
    			+ " Chicago.'    Emanuel and Fallon also exchanged jokes about side-by-side photos of the two "
    			+ "leaving the icy waters of the lake from their respective plunges.    'I went under the water,"
    			+ " and I saw bubbles come out of my mouth and then I froze,' Fallon said. 'I came out and I heard"
    			+ " bagpipes, and I looked around for bagpipers, because when you're Irish that's what you hear"
    			+ " when you die.'    'You know what the difference is?' Emanuel quipped. 'I heard ‘Hava Nagila.'"
    			+ "    That also was the song the show's band The Roots played as Emanuel's four-and-a-half minute"
    			+ " segment went to commercial.    Emanuel was the second guest, sitting in for one segment after "
    			+ "movie star Jonah Hill did two, including one in which he apologized for using a homophobic slur"
    			+ " in an exchange with a papparzo. Reunited '90s grunge band Soundgarden closed the show with "
    			+ "a performance of 'Spoonman.'    Before taping the show, the mayor spent the day 'trying to "
    			+ "recruit two major events to the city,' said Emanuel spokeswoman Kelley Quinn, who declined to "
    			+ "identify them.    Emanuel also met with 'a bond house to discuss city finances' and the Federal "
    			+ "Reserve, Quinn said.    Asked if Emanuel did any campaign fundraising on the trip or met with "
    			+ "any donors, Quinn said the mayor had 'one non-city meeting' Monday but wouldn't say who it was "
    			+ "with. Quinn, however, said Emanuel's travel expenses would be split between funds from the "
    			+ "city and the mayor's political campaign.    The New York trip is not the first time Emanuel "
    			+ "has scheduled city meetings around political events and TV show appearances or speeches."
    			+ " Earlier this year, the Tribune reported that Emanuel had spent city funds on at least nine"
    			+ " trips in which he met with political donors or raised campaign money. Six additional "
    			+ "taxpayer-funded trips involved little or no official city business.";
    	
        Set<String> tList = TopicModeler.getMainTopics(inData);
        LOG.debug("{}", tList);
        tList = TopicModeler.getMainTopics(krData);
        LOG.debug("{}", tList);
        tList = TopicModeler.getMainTopics(enData);
        LOG.debug("{}", tList);

        String aa = "있습니다. 있습니다. 가 있습니다. 나있습니다. 와 있습니다. 디 있습니다.";
        tList = TopicModeler.getMainTopics(aa);
        LOG.debug("{}", tList);
    }
}
