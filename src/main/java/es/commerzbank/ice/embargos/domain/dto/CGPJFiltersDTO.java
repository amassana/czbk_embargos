package es.commerzbank.ice.embargos.domain.dto;

import java.time.Instant;

public class CGPJFiltersDTO
{
	public Long [] statuses;
	private Instant startDate;
	private Instant endDate;
	private Instant fileLoadTimestampMax;

	public Instant getStartDate() {
		return startDate;
	}
	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}
	public Instant getEndDate() {
		return endDate;
	}
	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
	}

	public Long[] getStatuses() {
		return statuses;
	}

	public void setStatuses(Long[] statuses) {
		this.statuses = statuses;
	}

	public Instant getFileLoadTimestampMax() {
		return fileLoadTimestampMax;
	}

	public void setFileLoadTimestampMax(Instant fileLoadTimestampMax) {
		this.fileLoadTimestampMax = fileLoadTimestampMax;
	}
}
