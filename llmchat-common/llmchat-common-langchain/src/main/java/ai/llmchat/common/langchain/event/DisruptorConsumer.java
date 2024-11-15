package ai.llmchat.common.langchain.event;

import com.lmax.disruptor.EventHandler;
import org.springframework.core.Ordered;

public interface DisruptorConsumer extends Ordered, EventHandler<DocumentEvent> {

}
