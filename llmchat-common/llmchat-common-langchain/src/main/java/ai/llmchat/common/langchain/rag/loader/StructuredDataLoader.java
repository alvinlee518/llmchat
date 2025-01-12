package ai.llmchat.common.langchain.rag.loader;

import ai.llmchat.common.langchain.document.Paragraph;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.source.UrlSource;
import dev.langchain4j.data.segment.TextSegment;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StructuredDataLoader {

	public static List<TextSegment> load(String url) {
		String fileExtension = FilenameUtils.getExtension(url);
		try {
			UrlSource source = UrlSource.from(url);
			if (StringUtils.equalsIgnoreCase("csv", fileExtension)) {
				return parseCSV(source);
			}
			else {
				return parseXLSAndXLSX(source, fileExtension);
			}
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to load document", e);
		}
	}

	private static List<TextSegment> parseCSV(UrlSource source) throws IOException {
		Iterable<CSVRecord> records = CSVFormat.DEFAULT
			.parse(new InputStreamReader(source.inputStream(), StandardCharsets.UTF_8));
		List<TextSegment> segmentList = new ArrayList<>();
		int index = -1;
		for (CSVRecord record : records) {
			index = index + 1;
			if (index == 0) {
				continue;
			}
			String prompt = record.get(0);
			String completion = record.get(1);
			TextSegment segment = TextSegment.from(completion,
					Metadata.from(Map.of(TextSegmentLoader.PROMPT, prompt, TextSegmentLoader.INDEX, index)));
			segmentList.add(segment);
		}
		return segmentList;
	}

	private static List<TextSegment> parseXLSAndXLSX(UrlSource source, String fileExtension) throws IOException {
		List<TextSegment> segmentList = new ArrayList<>();
		try (Workbook workbook = StringUtils.equalsIgnoreCase("xlsx", fileExtension)
				? new XSSFWorkbook(source.inputStream()) : new HSSFWorkbook(source.inputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			int rowStart = sheet.getFirstRowNum() + 1;
			int rowEnd = sheet.getPhysicalNumberOfRows();
			for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (null == row) {
					continue;
				}
				String prompt = row.getCell(0).getStringCellValue();
				String completion = row.getCell(1).getStringCellValue();
				TextSegment segment = TextSegment.from(completion,
						Metadata.from(Map.of(TextSegmentLoader.PROMPT, prompt, TextSegmentLoader.INDEX, rowNum)));
				segmentList.add(segment);
			}
		}
		return segmentList;
	}

}
