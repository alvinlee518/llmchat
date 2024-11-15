package ai.llmchat.common.langchain.event;

import com.lmax.disruptor.EventFactory;

public class DocumentEventFactory implements EventFactory<DocumentEvent> {
    @Override
    public DocumentEvent newInstance() {
        return new DocumentEvent();
    }
}
