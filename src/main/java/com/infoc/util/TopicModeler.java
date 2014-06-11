package com.infoc.util;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
import cc.mallet.types.InstanceList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class TopicModeler {
    private static final Logger LOG = LoggerFactory.getLogger(TopicModeler.class);

    private static final int NUM_ITERATIONS = 50;
    private static final int NUM_TOPIC = 1;
    private static final int NUM_TOPIC_WORDS = 6;

    // Begin by importing documents from text to feature sequences
    private static ArrayList<Pipe> PIPE_LIST = new ArrayList<>();

    static {
        // Pipes: lowercase, tokenize, remove stopwords, map to features
        PIPE_LIST.add(new CharSequenceLowercase());
        PIPE_LIST.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));

        File file = null;
        try {
            file = new ClassPathResource("stoplists/en.txt").getFile();
        } catch (IOException e) {
        }
        PIPE_LIST.add(new TokenSequenceRemoveStopwords(file, "UTF-8", false, false, false));
        PIPE_LIST.add(new TokenSequence2FeatureSequence());
    }


    public static synchronized Set<String> getMainTopics(String contents) throws IOException {
        Reader fileReader = new InputStreamReader(new ByteArrayInputStream(contents.getBytes()));

        InstanceList instances = new InstanceList(new SerialPipes(PIPE_LIST));
        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1));

        ParallelTopicModel model = new ParallelTopicModel(NUM_TOPIC, 1.0, 0.01);
        model.addInstances(instances);

        model.setNumThreads(2);
        model.setNumIterations(NUM_ITERATIONS);

        model.estimate();

        // The data alphabet maps word IDs to strings
        Alphabet dataAlphabet = instances.getDataAlphabet();

        // Get an array of sorted sets of word ID/count pairs
        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

        Set<String> keywordList = new HashSet<>();

        // Show top 5 words in topics with proportions for the first document
        for (int topic = 0; topic < NUM_TOPIC; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

            int rank = 0;
            while (iterator.hasNext() && rank < NUM_TOPIC_WORDS) {
                IDSorter idCountPair = iterator.next();
                String topicWord = (String) dataAlphabet.lookupObject(idCountPair.getID());
                if (topicWord.isEmpty()) {
                    continue;
                }
                keywordList.add(topicWord);

                rank++;
            }
        }

        fileReader.close();
        instances.removeSources();
        instances = null;
        return keywordList;
    }
}
