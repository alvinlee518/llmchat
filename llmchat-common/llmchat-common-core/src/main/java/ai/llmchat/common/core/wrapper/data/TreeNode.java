package ai.llmchat.common.core.wrapper.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TreeNode implements Serializable {
    private static final long serialVersionUID = 1L;
    private String label;
    private Long value;
    private Long pid;
    private List<TreeNode> children;

    public TreeNode(String label, Long value, Long pid) {
        this.label = label;
        this.value = value;
        this.pid = pid;
    }
}
