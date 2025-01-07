package ai.llmchat.common.core.wrapper;

import ai.llmchat.common.core.wrapper.data.PageData;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class PageResult<T> extends Result<Collection<T>> {

	private Integer total;

	private Integer page;

	private Integer size;

	public PageResult(Integer code, String message, Collection<T> data) {
		super(code, message, data);
	}

	public Integer getPage() {
		if (this.page < 1) {
			this.page = 1;
		}
		return page;
	}

	public Integer getSize() {
		if (this.size < 1) {
			this.size = 10;
		}
		return size;
	}

	@Override
	public Collection<T> getData() {
		Collection<T> data = super.getData();
		return Objects.isNull(data) ? List.of() : data;
	}

	public static <T> PageResult<T> of(Long total, Integer page, Integer size, Collection<T> list) {
		PageResult<T> result = new PageResult<>(200, "success", list);
		result.setTotal(total.intValue());
		result.setPage(page);
		result.setSize(size);
		return result;
	}

	public static <T> PageResult<T> of(PageData<T> pageData) {
		PageResult<T> result = new PageResult<>(200, "success", pageData.getList());
		result.setTotal(pageData.getTotal().intValue());
		result.setPage(pageData.getPage());
		result.setSize(pageData.getSize());
		return result;
	}

}
