package com.ncl.sketch.agent.di;

/**
 * The Domain independent agent parameters.
 */
public interface DomainIndependantAgentParameters {

    void k(final int k);

    int k();

    DefaultPatternRecognizerParameters linePatternRecognizerParameters();

    CirclePatternRecognizerParameters circlePatternRecognizerParameters();
}
