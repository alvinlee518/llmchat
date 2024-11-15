package ai.llmchat.common.langchain.event;

import com.lmax.disruptor.RingBuffer;

public class DisruptorProducer {
    private final RingBuffer<DocumentEvent> documentEventRingBuffer;

    public DisruptorProducer(RingBuffer<DocumentEvent> documentEventRingBuffer) {
        this.documentEventRingBuffer = documentEventRingBuffer;
    }

    public void sendDocumentEvent(Long docId) {
        documentEventRingBuffer.publishEvent((event, sequence) -> {
            event.setDocId(docId);
        });
    }
}
