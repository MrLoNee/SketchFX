package com.ncl.sketch.agent.di.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.ncl.sketch.agent.api.Stroke;
import com.ncl.sketch.agent.di.impl.PatternRecognizer;
import com.ncl.sketch.agent.di.impl.PatternRecognizerChain;
import com.ncl.sketch.agent.di.impl.StrokeRecognitionResult;

public final class PatternRecognizerChainTest {

    @Test
    public final void recognize() {
        final PatternRecognizer r1 = mock(PatternRecognizer.class);
        final PatternRecognizer r2 = mock(PatternRecognizer.class);
        final PatternRecognizerChain chain = new PatternRecognizerChain();
        chain.add(r1).add(r2);
        final Stroke stroke = new Stroke(0);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();
        chain.recognize(stroke, result);
        verify(r1).recognize(stroke, result);
        verify(r2).recognize(stroke, result);
    }

    @Test
    public final void stopEarly() {
        final PatternRecognizer r1 = mock(PatternRecognizer.class);
        final PatternRecognizer r2 = mock(PatternRecognizer.class);
        final PatternRecognizerChain chain = new PatternRecognizerChain();
        chain.add(r1).add(r2);
        final Stroke stroke = new Stroke(0);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();
        when(r1.recognize(stroke, result)).thenReturn(true);
        chain.recognize(stroke, result);
        verify(r1).recognize(stroke, result);
        verifyZeroInteractions(r2);
    }

}
