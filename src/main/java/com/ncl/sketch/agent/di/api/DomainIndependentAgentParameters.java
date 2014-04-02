package com.ncl.sketch.agent.di.api;

/**
 * The Domain independent agent parameters.
 */
public interface DomainIndependentAgentParameters {

    void k(final int k);

    int k();

    LineRecognitionParameters lineRecognitionParameters();

    CircleRecognitionParameters circleRecognitionParameters();

}
