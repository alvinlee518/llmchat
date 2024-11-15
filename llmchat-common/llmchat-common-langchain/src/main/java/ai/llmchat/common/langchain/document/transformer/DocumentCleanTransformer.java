package ai.llmchat.common.langchain.document.transformer;

import ai.llmchat.common.langchain.enums.CleanPatternEnum;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentTransformer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import java.util.Objects;

public class DocumentCleanTransformer implements DocumentTransformer {
    private final Integer[] cleanRules;

    public DocumentCleanTransformer(Integer[] cleanRules) {
        this.cleanRules = cleanRules;
    }

    @Override
    public Document transform(Document document) {
        String text = document.text();
        // remove invalid symbol
        text = RegExUtils.replaceAll(text, "<\\|", "<");
        text = RegExUtils.replaceAll(text, "\\|>", ">");
        text = RegExUtils.replaceAll(text, "[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\xEF\\xBF\\xBE]", StringUtils.EMPTY);
        // Remove Unicode U+FFFE character
        text = RegExUtils.replaceAll(text, "\ufffe", StringUtils.EMPTY);
        if (ArrayUtils.isNotEmpty(cleanRules)) {
            for (Integer ruleId : cleanRules) {
                // 替换掉连续的空格、换行符和制表符
                if (Objects.equals(CleanPatternEnum.REMOVE_EXTRA_SPACES.getCode(), ruleId)) {
                    text = RegExUtils.replaceAll(text, "\\n{3,}", "\n\n");
                    text = RegExUtils.replaceAll(text, "[\\t\\f\\r\\x20\\u00a0\\u1680\\u180e\\u2000-\\u200a\\u202f\\u205f\\u3000]{2,}", StringUtils.SPACE);
                } else if (Objects.equals(CleanPatternEnum.REMOVE_URLS_EMAILS.getCode(), ruleId)) {
                    text = RegExUtils.replaceAll(text, "([a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+)", StringUtils.EMPTY);
                    text = RegExUtils.replaceAll(text, "([A-Za-z][A-Za-z0-9+.-]{1,120}:" + "[A-Za-z0-9/](([A-Za-z0-9$_.+!*,;/?:@&~=-])|%[A-Fa-f0-9]{2}){1,333}" + "(#([a-zA-Z0-9][a-zA-Z0-9$_.+!*,;/?:@&~=%-]{0,1000}))?)", StringUtils.EMPTY);
                } else if (Objects.equals(CleanPatternEnum.REMOVE_HTML_TAGS.getCode(), ruleId)) {
                    text = Jsoup.parse(text).text();
                }
            }
        }
        return Document.document(text);
    }
}
