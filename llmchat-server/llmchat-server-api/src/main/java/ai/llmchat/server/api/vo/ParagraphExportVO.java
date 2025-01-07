package ai.llmchat.server.api.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ParagraphExportVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ExcelProperty("分段标题")
	private String title;

	@ExcelProperty("分段内容")
	private String content;

}
