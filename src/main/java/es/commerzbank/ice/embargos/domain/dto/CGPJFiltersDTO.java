package es.commerzbank.ice.embargos.domain.dto;

import java.time.Instant;

public class CGPJFiltersDTO
{
	private IntegradorRequestStatusDTO status;
	private Instant startDate;
	private Instant endDate;

	public IntegradorRequestStatusDTO getStatus() {
		return status;
	}
	public void setStatus(IntegradorRequestStatusDTO status) {
		this.status = status;
	}

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
}
