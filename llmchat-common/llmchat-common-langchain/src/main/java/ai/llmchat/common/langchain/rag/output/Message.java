package ai.llmchat.common.langchain.rag.output;

import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.internal.Utils;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Object chatId;
    private ChatMessageType role;
    private String content;
    private Boolean error;
    private Usage usage;
    private List<Citation> citations;
    private LocalDateTime createAt;

    public static Message error(Long id, Object chatId, String content, Long duration) {
        Usage usage = Usage.builder().totalTokens(0).promptTokens(0).completionTokens(0).duration(duration).build();
        return Message.builder()
                .id(id)
                .chatId(chatId)
                .role(ChatMessageType.AI)
                .createAt(LocalDateTime.now())
                .content(content)
                .error(true)
                .usage(usage)
                .build();
    }

    public static Message complete(Long id, Object chatId, String content, Usage usage, List<Citation> citations) {
        return Message.builder()
                .id(id)
                .chatId(chatId)
                .role(ChatMessageType.AI)
                .createAt(LocalDateTime.now())
                .content(content)
                .usage(usage)
                .citations(citations)
                .build();
    }

    public static Message next(Long id, Object chatId, String content) {
        return Message.builder()
                .id(id)
                .chatId(chatId)
                .role(ChatMessageType.AI)
                .createAt(LocalDateTime.now())
                .content(content)
                .build();
    }
}
