package ai.llmchat.common.langchain.model.impl;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.ModelDisabledException;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.scoring.ScoringModel;

import java.util.List;

public class DisabledScoringModel implements ScoringModel {
    @Override
    public Response<List<Double>> scoreAll(List<TextSegment> segments, String query) {
        throw new ModelDisabledException("RerankModel is disabled");
    }
}
