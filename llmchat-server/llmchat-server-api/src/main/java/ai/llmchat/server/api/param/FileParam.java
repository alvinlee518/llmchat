package ai.llmchat.server.api.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long fileId;
    private String fileName;
}
