package com.infoc.domain;

import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentenceInfoTest {
    private static final Logger LOG = LoggerFactory.getLogger(SentenceInfoTest.class);

    @Test
    public void testAddPositionPnt() throws Exception {
        SentenceInfo s = new SentenceInfo();

        s.setMatchedWord(0);
        s.setIndex(1);
        s.addPositionPnt(13);
        LOG.debug("{}", s);
        Assert.assertEquals(1, (int)s.getMatchedWord());

        s.setMatchedWord(0);
        s.setIndex(3);
        s.addPositionPnt(13);
        LOG.debug("{}", s);
        Assert.assertEquals(1, (int)s.getMatchedWord());

        s.setMatchedWord(0);
        s.setIndex(6);
        s.addPositionPnt(13);
        LOG.debug("{}", s);
        Assert.assertEquals(0, (int)s.getMatchedWord());

        s.setMatchedWord(0);
        s.setIndex(10);
        s.addPositionPnt(13);
        LOG.debug("{}", s);
        Assert.assertEquals(2, (int)s.getMatchedWord());
    }
}