package ai.llmchat.common.core.util;

import ai.llmchat.common.core.wrapper.data.TreeNode;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeNodeUtils {
    public static List<TreeNode> toTree(List<TreeNode> nodes) {
        return findChildren(0L, nodes);
    }

    public static List<TreeNode> findChildren(Long parentId, List<TreeNode> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return List.of();
        }
        List<TreeNode> result = new ArrayList<>();
        for (TreeNode node : nodes) {
            if (Objects.equals(parentId, node.getPid())) {
                List<TreeNode> children = findChildren(node.getValue(), nodes);
                node.setChildren(children);
                result.add(node);
            }
        }
        return CollectionUtils.isEmpty(result) ? null : result;
    }
}
