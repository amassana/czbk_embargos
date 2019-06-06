package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_BATCH_SHEETS database table.
 * 
 */
@Entity
@Table(name="EUL5_BATCH_SHEETS")
@NamedQuery(name="Eul5BatchSheet.findAll", query="SELECT e FROM Eul5BatchSheet e")
public class Eul5BatchSheet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BS_ID", unique=true, nullable=false, precision=10)
	private long bsId;

	@Column(name="BS_CREATED_BY", nullable=false, length=64)
	private String bsCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BS_CREATED_DATE", nullable=false)
	private Date bsCreatedDate;

	@Column(name="BS_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal bsElementState;

	@Column(name="BS_SHEET_ID", nullable=false, length=240)
	private String bsSheetId;

	@Column(name="BS_SHEET_NAME", nullable=false, length=240)
	private String bsSheetName;

	@Column(name="BS_UPDATED_BY", length=64)
	private String bsUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BS_UPDATED_DATE")
	private Date bsUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5BatchParam
	@OneToMany(mappedBy="eul5BatchSheet")
	private List<Eul5BatchParam> eul5BatchParams;

	//bi-directional many-to-one association to Eul5BatchQuery
	@OneToMany(mappedBy="eul5BatchSheet")
	private List<Eul5BatchQuery> eul5BatchQueries;

	//bi-directional many-to-one association to Eul5BatchReport
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BS_BR_ID", nullable=false)
	private Eul5BatchReport eul5BatchReport;

	public Eul5BatchSheet() {
	}

	public long getBsId() {
		return this.bsId;
	}

	public void setBsId(long bsId) {
		this.bsId = bsId;
	}

	public String getBsCreatedBy() {
		return this.bsCreatedBy;
	}

	public void setBsCreatedBy(String bsCreatedBy) {
		this.bsCreatedBy = bsCreatedBy;
	}

	public Date getBsCreatedDate() {
		return this.bsCreatedDate;
	}

	public void setBsCreatedDate(Date bsCreatedDate) {
		this.bsCreatedDate = bsCreatedDate;
	}

	public BigDecimal getBsElementState() {
		return this.bsElementState;
	}

	public void setBsElementState(BigDecimal bsElementState) {
		this.bsElementState = bsElementState;
	}

	public String getBsSheetId() {
		return this.bsSheetId;
	}

	public void setBsSheetId(String bsSheetId) {
		this.bsSheetId = bsSheetId;
	}

	public String getBsSheetName() {
		return this.bsSheetName;
	}

	public void setBsSheetName(String bsSheetName) {
		this.bsSheetName = bsSheetName;
	}

	public String getBsUpdatedBy() {
		return this.bsUpdatedBy;
	}

	public void setBsUpdatedBy(String bsUpdatedBy) {
		this.bsUpdatedBy = bsUpdatedBy;
	}

	public Date getBsUpdatedDate() {
		return this.bsUpdatedDate;
	}

	public void setBsUpdatedDate(Date bsUpdatedDate) {
		this.bsUpdatedDate = bsUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public List<Eul5BatchParam> getEul5BatchParams() {
		return this.eul5BatchParams;
	}

	public void setEul5BatchParams(List<Eul5BatchParam> eul5BatchParams) {
		this.eul5BatchParams = eul5BatchParams;
	}

	public Eul5BatchParam addEul5BatchParam(Eul5BatchParam eul5BatchParam) {
		getEul5BatchParams().add(eul5BatchParam);
		eul5BatchParam.setEul5BatchSheet(this);

		return eul5BatchParam;
	}

	public Eul5BatchParam removeEul5BatchParam(Eul5BatchParam eul5BatchParam) {
		getEul5BatchParams().remove(eul5BatchParam);
		eul5BatchParam.setEul5BatchSheet(null);

		return eul5BatchParam;
	}

	public List<Eul5BatchQuery> getEul5BatchQueries() {
		return this.eul5BatchQueries;
	}

	public void setEul5BatchQueries(List<Eul5BatchQuery> eul5BatchQueries) {
		this.eul5BatchQueries = eul5BatchQueries;
	}

	public Eul5BatchQuery addEul5BatchQuery(Eul5BatchQuery eul5BatchQuery) {
		getEul5BatchQueries().add(eul5BatchQuery);
		eul5BatchQuery.setEul5BatchSheet(this);

		return eul5BatchQuery;
	}

	public Eul5BatchQuery removeEul5BatchQuery(Eul5BatchQuery eul5BatchQuery) {
		getEul5BatchQueries().remove(eul5BatchQuery);
		eul5BatchQuery.setEul5BatchSheet(null);

		return eul5BatchQuery;
	}

	public Eul5BatchReport getEul5BatchReport() {
		return this.eul5BatchReport;
	}

	public void setEul5BatchReport(Eul5BatchReport eul5BatchReport) {
		this.eul5BatchReport = eul5BatchReport;
	}

}