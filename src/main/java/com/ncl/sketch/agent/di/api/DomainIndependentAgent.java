package com.ncl.sketch.agent.di.api;

import com.ncl.sketch.agent.api.SketchRecognitionAgent;

/**
 * A domain-independent {@link SketchRecognitionAgent}. This agent makes no assumption about the context in which
 * the sketch was drawn.
 * 
 * @see <a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.89.3800&amp;rep=rep1&amp;type=pdf">A
 *      Domain-Independent System for Sketch Recognition</a>
 */
public interface DomainIndependentAgent extends SketchRecognitionAgent, DomainIndependentAgentParameters {
    // empty.
}
