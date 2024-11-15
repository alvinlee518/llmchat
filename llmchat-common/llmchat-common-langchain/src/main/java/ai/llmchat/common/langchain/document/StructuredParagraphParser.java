package ai.llmchat.common.langchain.document;

import dev.langchain4j.data.document.source.UrlSource;
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

public class StructuredParagraphParser implements ParagraphParser {
    private final String url;

    public StructuredParagraphParser(String url) {
        this.url = url;
    }

    @Override
    public List<Paragraph> parse() {
        String fileExtension = FilenameUtils.getExtension(url);
        try {
            UrlSource source = UrlSource.from(url);
            if (StringUtils.equalsIgnoreCase("csv", fileExtension)) {
                return parseCSV(source);
            } else {
                return parseXLSAndXLSX(source, fileExtension);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load document", e);
        }
    }

    private List<Paragraph> parseCSV(UrlSource source) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(new InputStreamReader(source.inputStream(), StandardCharsets.UTF_8));
        List<Paragraph> paragraphs = new ArrayList<>();
        int index = -1;
        for (CSVRecord record : records) {
            index = index + 1;
            if (index == 0) {
                continue;
            }
            paragraphs.add(new Paragraph(record.get(0), record.get(1), index));
        }
        return paragraphs;
    }

    private List<Paragraph> parseXLSAndXLSX(UrlSource source, String fileExtension) throws IOException {
        List<Paragraph> paragraphs = new ArrayList<>();
        try (Workbook workbook = StringUtils.equalsIgnoreCase("xlsx", fileExtension) ? new XSSFWorkbook(source.inputStream()) : new HSSFWorkbook(source.inputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowStart = sheet.getFirstRowNum() + 1;
            int rowEnd = sheet.getPhysicalNumberOfRows();
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (null == row) {
                    continue;
                }
                paragraphs.add(new Paragraph(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue(), rowNum));
            }
        }
        return paragraphs;
    }
}
