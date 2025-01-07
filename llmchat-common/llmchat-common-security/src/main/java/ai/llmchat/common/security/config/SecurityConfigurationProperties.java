package ai.llmchat.common.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = SecurityConfigurationProperties.PREFIX)
public class SecurityConfigurationProperties {

	static final String PREFIX = "security";

	private List<String> includePatterns;

	private List<String> excludePatterns;

	public List<String> getIncludePatterns() {
		if (CollectionUtils.isEmpty(includePatterns)) {
			includePatterns = new ArrayList<>();
		}
		this.includePatterns.add("/**");
		return this.includePatterns;
	}

	public List<String> getExcludePatterns() {
		if (CollectionUtils.isEmpty(excludePatterns)) {
			this.excludePatterns = new ArrayList<>();
		}
		this.excludePatterns.add("/favicon.ico");
		this.excludePatterns.add("/icons/**");
		this.excludePatterns.add("/security/**");
		return this.excludePatterns.stream().distinct().toList();
	}

}
