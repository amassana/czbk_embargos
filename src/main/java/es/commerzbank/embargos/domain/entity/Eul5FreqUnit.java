package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_FREQ_UNITS database table.
 * 
 */
@Entity
@Table(name="EUL5_FREQ_UNITS")
@NamedQuery(name="Eul5FreqUnit.findAll", query="SELECT e FROM Eul5FreqUnit e")
public class Eul5FreqUnit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="RFU_ID", unique=true, nullable=false, precision=10)
	private long rfuId;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="RFU_CREATED_BY", nullable=false, length=64)
	private String rfuCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="RFU_CREATED_DATE", nullable=false)
	private Date rfuCreatedDate;

	@Column(name="RFU_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal rfuElementState;

	@Column(name="RFU_NAME_MN", nullable=false, precision=10)
	private BigDecimal rfuNameMn;

	@Column(name="RFU_SEQUENCE", precision=22)
	private BigDecimal rfuSequence;

	@Column(name="RFU_SQL_EXPRESSION", nullable=false, length=240)
	private String rfuSqlExpression;

	@Column(name="RFU_UPDATED_BY", length=64)
	private String rfuUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="RFU_UPDATED_DATE")
	private Date rfuUpdatedDate;

	//bi-directional many-to-one association to Eul5BatchReport
	@OneToMany(mappedBy="eul5FreqUnit")
	private List<Eul5BatchReport> eul5BatchReports;

	//bi-directional many-to-one association to Eul5SumRfshSet
	@OneToMany(mappedBy="eul5FreqUnit")
	private List<Eul5SumRfshSet> eul5SumRfshSets;

	public Eul5FreqUnit() {
	}

	public long getRfuId() {
		return this.rfuId;
	}

	public void setRfuId(long rfuId) {
		this.rfuId = rfuId;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public String getRfuCreatedBy() {
		return this.rfuCreatedBy;
	}

	public void setRfuCreatedBy(String rfuCreatedBy) {
		this.rfuCreatedBy = rfuCreatedBy;
	}

	public Date getRfuCreatedDate() {
		return this.rfuCreatedDate;
	}

	public void setRfuCreatedDate(Date rfuCreatedDate) {
		this.rfuCreatedDate = rfuCreatedDate;
	}

	public BigDecimal getRfuElementState() {
		return this.rfuElementState;
	}

	public void setRfuElementState(BigDecimal rfuElementState) {
		this.rfuElementState = rfuElementState;
	}

	public BigDecimal getRfuNameMn() {
		return this.rfuNameMn;
	}

	public void setRfuNameMn(BigDecimal rfuNameMn) {
		this.rfuNameMn = rfuNameMn;
	}

	public BigDecimal getRfuSequence() {
		return this.rfuSequence;
	}

	public void setRfuSequence(BigDecimal rfuSequence) {
		this.rfuSequence = rfuSequence;
	}

	public String getRfuSqlExpression() {
		return this.rfuSqlExpression;
	}

	public void setRfuSqlExpression(String rfuSqlExpression) {
		this.rfuSqlExpression = rfuSqlExpression;
	}

	public String getRfuUpdatedBy() {
		return this.rfuUpdatedBy;
	}

	public void setRfuUpdatedBy(String rfuUpdatedBy) {
		this.rfuUpdatedBy = rfuUpdatedBy;
	}

	public Date getRfuUpdatedDate() {
		return this.rfuUpdatedDate;
	}

	public void setRfuUpdatedDate(Date rfuUpdatedDate) {
		this.rfuUpdatedDate = rfuUpdatedDate;
	}

	public List<Eul5BatchReport> getEul5BatchReports() {
		return this.eul5BatchReports;
	}

	public void setEul5BatchReports(List<Eul5BatchReport> eul5BatchReports) {
		this.eul5BatchReports = eul5BatchReports;
	}

	public Eul5BatchReport addEul5BatchReport(Eul5BatchReport eul5BatchReport) {
		getEul5BatchReports().add(eul5BatchReport);
		eul5BatchReport.setEul5FreqUnit(this);

		return eul5BatchReport;
	}

	public Eul5BatchReport removeEul5BatchReport(Eul5BatchReport eul5BatchReport) {
		getEul5BatchReports().remove(eul5BatchReport);
		eul5BatchReport.setEul5FreqUnit(null);

		return eul5BatchReport;
	}

	public List<Eul5SumRfshSet> getEul5SumRfshSets() {
		return this.eul5SumRfshSets;
	}

	public void setEul5SumRfshSets(List<Eul5SumRfshSet> eul5SumRfshSets) {
		this.eul5SumRfshSets = eul5SumRfshSets;
	}

	public Eul5SumRfshSet addEul5SumRfshSet(Eul5SumRfshSet eul5SumRfshSet) {
		getEul5SumRfshSets().add(eul5SumRfshSet);
		eul5SumRfshSet.setEul5FreqUnit(this);

		return eul5SumRfshSet;
	}

	public Eul5SumRfshSet removeEul5SumRfshSet(Eul5SumRfshSet eul5SumRfshSet) {
		getEul5SumRfshSets().remove(eul5SumRfshSet);
		eul5SumRfshSet.setEul5FreqUnit(null);

		return eul5SumRfshSet;
	}

}