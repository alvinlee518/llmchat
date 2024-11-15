package ai.llmchat.server.api.enums;

import ai.llmchat.common.core.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum MenuTypeEnum implements BaseEnum<Integer> {
    DIR(0, "目录"), MENU(1, "菜单"), BUTTON(2, "按钮");
    private final Integer code;
    private final String message;

    MenuTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
