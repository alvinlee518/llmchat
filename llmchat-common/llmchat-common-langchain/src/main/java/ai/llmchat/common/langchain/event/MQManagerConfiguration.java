package ai.llmchat.common.langchain.event;

import cn.hutool.core.thread.NamedThreadFactory;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Configuration
public class MQManagerConfiguration {

    @Bean
    public RingBuffer<DocumentEvent> documentEventRingBuffer(List<DisruptorConsumer> eventHandlerList) {
        int bufferSize = 1024 * 256;
        Disruptor<DocumentEvent> disruptor = new Disruptor<>(new DocumentEventFactory(),
                bufferSize,
                new NamedThreadFactory("LangChain-", false),
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );
        List<DisruptorConsumer> list = eventHandlerList.stream().sorted(Comparator.comparing(Ordered::getOrder)).toList();
        EventHandlerGroup<DocumentEvent> eventHandlerGroup = null;
        for (DisruptorConsumer disruptorConsumer : list) {
            if (Objects.isNull(eventHandlerGroup)) {
                eventHandlerGroup = disruptor.handleEventsWith(disruptorConsumer);
            } else {
                eventHandlerGroup = eventHandlerGroup.then(disruptorConsumer);
            }
        }
        disruptor.start();
        //获取ringbuffer环，用于接取生产者生产的事件
        return disruptor.getRingBuffer();
    }

    @Bean
    public DisruptorProducer disruptorProducer(RingBuffer<DocumentEvent> ringBuffer) {
        return new DisruptorProducer(ringBuffer);
    }
}
