package com.jaiaxn.adaptation.utils.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页对象
 */
public class Page<T> implements Serializable {
	private static final long serialVersionUID = 6027531115910874448L;
	private List<T> objects;
	private int pageSize;
	private long totalNumber;
	private long totalPage;
	private long currentPage;
	private boolean hasPrevious = false;

	private boolean hasNext = false;
	private long startNum;
	private long endNum;

	public Page(List<T> objects, int pageSize, long totalNum, long currentPage) {
		if (objects.equals(Collections.EMPTY_LIST)) {
			this.objects = objects;
		} else {
			this.objects = objects;
			this.totalNumber = totalNum;
			this.currentPage = currentPage;
			this.pageSize = pageSize;
			this.totalPage = ((this.totalNumber - 1) / pageSize + 1);
			this.hasPrevious = (currentPage > 1);
			this.hasNext = (currentPage < this.totalPage);
			this.startNum = ((currentPage - 1) * pageSize + 1);
			this.endNum = (this.startNum + objects.size() - 1);
		}
	}

	public Page() {
		this.objects = Collections.emptyList();
		this.totalNumber = 0;
		this.currentPage = 0;
		this.pageSize = 0;
		this.totalPage = 0;
		this.hasPrevious = false;
		this.hasNext = false;
		this.startNum = 0;
		this.endNum = 0;
	}
	public List<T> setList(List<T> list ) {
		return this.objects =list;
	}
	
	public List<T> getList() {
		return this.objects;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public long getTotalNumber() {
		return this.totalNumber;
	}

	public long getTotalPage() {
		return this.totalPage;
	}

	public long getCurrentPage() {
		if (getTotalNumber() > 0) {
			this.currentPage = (this.currentPage < 1 ? 1 : this.currentPage);
		}
		return this.currentPage;
	}

	public long getNextPageIndex() {
		this.totalPage = (this.totalPage < 1 ? 1 : this.totalPage);
		this.currentPage = (this.currentPage < 1 ? 1 : this.currentPage);
		if (this.currentPage < this.totalPage) {
			return this.currentPage + 1;
		}
		return this.currentPage;
	}

	public long getPrePageIndex() {
		this.currentPage = (this.currentPage < 1 ? 1 : this.currentPage);
		if (this.currentPage > 1) {
			return this.currentPage - 1;
		}
		return this.currentPage;
	}

	public boolean hasPrevious() {
		return this.hasPrevious;
	}

	public boolean hasNext() {
		return this.hasNext;
	}

	public long getStartNum() {
		return this.startNum;
	}

	public long getEndNum() {
		return this.endNum;
	}
}
