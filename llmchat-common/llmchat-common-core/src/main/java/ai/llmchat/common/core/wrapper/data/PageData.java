package ai.llmchat.common.core.wrapper.data;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Data
public class PageData<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long total;
    private Integer page;
    private Integer size;
    private Collection<T> list;

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

    public Collection<T> getList() {
        if (Objects.isNull(this.list)) {
            this.list = Collections.emptyList();
        }
        return list;
    }

    public boolean isEmpty() {
        return (list == null || list.isEmpty());
    }

    public long pages() {
        return total % size == 0 ? (total / size) : (total / size) + 1;
    }

    public static <T> PageData<T> of(Long total, Integer page, Integer size, Collection<T> list) {
        PageData<T> result = new PageData<>();
        result.setTotal(total);
        result.setPage(page);
        result.setSize(size);
        result.setList(list);
        return result;
    }
}
