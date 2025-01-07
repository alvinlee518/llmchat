package ai.llmchat.common.core.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExcelUtils {

	public static void export2File(String path, String excelName, String sheetName, Class<?> clazz, List<?> data) {
		String fileName = path.concat(excelName).concat(ExcelTypeEnum.XLSX.getValue());
		EasyExcel.write(fileName, clazz)
			.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
			.sheet(sheetName)
			.doWrite(data);
	}

	public static void export2Web(HttpServletResponse response, String excelName, String sheetName, Class<?> clazz,
			List<?> list) throws Exception {
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		// 这里URLEncoder.encode可以防止中文乱码
		excelName = URLEncoder.encode(excelName, StandardCharsets.UTF_8);
		response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
		EasyExcel.write(response.getOutputStream(), clazz)
			.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
			.sheet(sheetName)
			.doWrite(list);
	}

}
