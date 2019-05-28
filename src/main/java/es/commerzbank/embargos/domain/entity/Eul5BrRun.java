package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_BR_RUNS database table.
 * 
 */
@Entity
@Table(name="EUL5_BR_RUNS")
@NamedQuery(name="Eul5BrRun.findAll", query="SELECT e FROM Eul5BrRun e")
public class Eul5BrRun implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BRR_ID", unique=true, nullable=false, precision=10)
	private long brrId;

	@Column(name="BRR_ACT_ELAP_TIME")
	private BigDecimal brrActElapTime;

	@Column(name="BRR_CREATED_BY", nullable=false, length=64)
	private String brrCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BRR_CREATED_DATE", nullable=false)
	private Date brrCreatedDate;

	@Column(name="BRR_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal brrElementState;

	@Temporal(TemporalType.DATE)
	@Column(name="BRR_RUN_DATE")
	private Date brrRunDate;

	@Column(name="BRR_RUN_NUMBER", nullable=false, precision=22)
	private BigDecimal brrRunNumber;

	@Column(name="BRR_STATE", nullable=false, precision=2)
	private BigDecimal brrState;

	@Column(name="BRR_SVR_ERR_CODE")
	private BigDecimal brrSvrErrCode;

	@Column(name="BRR_SVR_ERR_TEXT", length=240)
	private String brrSvrErrText;

	@Column(name="BRR_UPDATED_BY", length=64)
	private String brrUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BRR_UPDATED_DATE")
	private Date brrUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5BqTable
	@OneToMany(mappedBy="eul5BrRun")
	private List<Eul5BqTable> eul5BqTables;

	//bi-directional many-to-one association to Eul5BatchReport
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BRR_BR_ID", nullable=false)
	private Eul5BatchReport eul5BatchReport;

	public Eul5BrRun() {
	}

	public long getBrrId() {
		return this.brrId;
	}

	public void setBrrId(long brrId) {
		this.brrId = brrId;
	}

	public BigDecimal getBrrActElapTime() {
		return this.brrActElapTime;
	}

	public void setBrrActElapTime(BigDecimal brrActElapTime) {
		this.brrActElapTime = brrActElapTime;
	}

	public String getBrrCreatedBy() {
		return this.brrCreatedBy;
	}

	public void setBrrCreatedBy(String brrCreatedBy) {
		this.brrCreatedBy = brrCreatedBy;
	}

	public Date getBrrCreatedDate() {
		return this.brrCreatedDate;
	}

	public void setBrrCreatedDate(Date brrCreatedDate) {
		this.brrCreatedDate = brrCreatedDate;
	}

	public BigDecimal getBrrElementState() {
		return this.brrElementState;
	}

	public void setBrrElementState(BigDecimal brrElementState) {
		this.brrElementState = brrElementState;
	}

	public Date getBrrRunDate() {
		return this.brrRunDate;
	}

	public void setBrrRunDate(Date brrRunDate) {
		this.brrRunDate = brrRunDate;
	}

	public BigDecimal getBrrRunNumber() {
		return this.brrRunNumber;
	}

	public void setBrrRunNumber(BigDecimal brrRunNumber) {
		this.brrRunNumber = brrRunNumber;
	}

	public BigDecimal getBrrState() {
		return this.brrState;
	}

	public void setBrrState(BigDecimal brrState) {
		this.brrState = brrState;
	}

	public BigDecimal getBrrSvrErrCode() {
		return this.brrSvrErrCode;
	}

	public void setBrrSvrErrCode(BigDecimal brrSvrErrCode) {
		this.brrSvrErrCode = brrSvrErrCode;
	}

	public String getBrrSvrErrText() {
		return this.brrSvrErrText;
	}

	public void setBrrSvrErrText(String brrSvrErrText) {
		this.brrSvrErrText = brrSvrErrText;
	}

	public String getBrrUpdatedBy() {
		return this.brrUpdatedBy;
	}

	public void setBrrUpdatedBy(String brrUpdatedBy) {
		this.brrUpdatedBy = brrUpdatedBy;
	}

	public Date getBrrUpdatedDate() {
		return this.brrUpdatedDate;
	}

	public void setBrrUpdatedDate(Date brrUpdatedDate) {
		this.brrUpdatedDate = brrUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public List<Eul5BqTable> getEul5BqTables() {
		return this.eul5BqTables;
	}

	public void setEul5BqTables(List<Eul5BqTable> eul5BqTables) {
		this.eul5BqTables = eul5BqTables;
	}

	public Eul5BqTable addEul5BqTable(Eul5BqTable eul5BqTable) {
		getEul5BqTables().add(eul5BqTable);
		eul5BqTable.setEul5BrRun(this);

		return eul5BqTable;
	}

	public Eul5BqTable removeEul5BqTable(Eul5BqTable eul5BqTable) {
		getEul5BqTables().remove(eul5BqTable);
		eul5BqTable.setEul5BrRun(null);

		return eul5BqTable;
	}

	public Eul5BatchReport getEul5BatchReport() {
		return this.eul5BatchReport;
	}

	public void setEul5BatchReport(Eul5BatchReport eul5BatchReport) {
		this.eul5BatchReport = eul5BatchReport;
	}

}