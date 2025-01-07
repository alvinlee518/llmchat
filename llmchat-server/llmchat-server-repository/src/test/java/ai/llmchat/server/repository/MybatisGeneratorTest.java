package ai.llmchat.server.repository;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.keywords.PostgreSqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.type.ITypeConvertHandler;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = { DynamicDataSourceAutoConfiguration.class })
public class MybatisGeneratorTest {

	@Autowired
	private DynamicDataSourceProperties dynamicDataSourceProperties;

	@Test
	public void codeGenerator() throws IOException {
		DataSourceProperty dataSourceProperty = dynamicDataSourceProperties.getDatasource()
			.get(dynamicDataSourceProperties.getPrimary());
		final Path OUTPUT_DIR = Paths.get(System.getProperty("user.dir"), "code");
		if (!Files.exists(OUTPUT_DIR)) {
			Files.createDirectories(OUTPUT_DIR);
		}
		final String table = """
				dict_data
				oauth_dept
				oauth_menu
				oauth_post
				oauth_role
				oauth_role_menu
				oauth_role_dept
				oauth_user
				oauth_user_role
				oauth_user_post
				file_detail
				ai_model
				ai_dataset
				ai_document
				ai_paragraph
				ai_app
				ai_app_dataset
				ai_chat
				ai_chat_message""";
		final List<String> TABLE_LIST = Arrays.stream(StringUtils.split(table, System.lineSeparator())).toList();

		FastAutoGenerator
			.create(dataSourceProperty.getUrl(), dataSourceProperty.getUsername(), dataSourceProperty.getPassword())
			.dataSourceConfig(builder -> builder.typeConvert(new PostgreSqlTypeConvert())
				.keyWordsHandler(new PostgreSqlKeyWordsHandler())
				.typeConvertHandler(new ITypeConvertHandler() {
					@Override
					public IColumnType convert(GlobalConfig globalConfig, TypeRegistry typeRegistry,
							TableField.MetaInfo metaInfo) {
						return typeRegistry.getColumnType(metaInfo);
					}
				}))
			.globalConfig(builder -> builder.author("lixw").outputDir(OUTPUT_DIR.toString()))
			.packageConfig(builder -> builder.parent("ai.llmchat.server")
				.entity("repository.entity")
				.mapper("repository.mapper")
				.service("service")
				.serviceImpl("service.impl")
				.controller("controller")
				.pathInfo(Map.of(OutputFile.xml, OUTPUT_DIR + "/mapper")))
			.strategyConfig(builder -> builder.addInclude(TABLE_LIST)
				.entityBuilder()
				// .formatFileName("%sDO")
				.enableFileOverride()
				.enableLombok()
				.enableChainModel()
				.idType(IdType.ASSIGN_ID)
				// .logicDeleteColumnName("status")
				.enableTableFieldAnnotation()
				.addTableFills(new Column("status", FieldFill.INSERT), new Column("create_at", FieldFill.INSERT),
						new Column("create_by", FieldFill.INSERT), new Column("update_at", FieldFill.INSERT_UPDATE),
						new Column("update_by", FieldFill.INSERT_UPDATE))
				.mapperBuilder()
				.enableFileOverride()
				.enableBaseResultMap()
				.enableBaseColumnList()
				.enableMapperAnnotation()
				.serviceBuilder()
				.enableFileOverride()
				.formatServiceFileName("%sService")
				.formatServiceImplFileName("%sServiceImpl")
				.controllerBuilder()
				.enableFileOverride()
				.enableRestStyle())
			.templateEngine(new FreemarkerTemplateEngine())
			.execute();
	}

}
