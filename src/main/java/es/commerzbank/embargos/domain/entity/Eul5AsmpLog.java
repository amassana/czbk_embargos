package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_ASMP_LOGS database table.
 * 
 */
@Entity
@Table(name="EUL5_ASMP_LOGS")
@NamedQuery(name="Eul5AsmpLog.findAll", query="SELECT e FROM Eul5AsmpLog e")
public class Eul5AsmpLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="APL_ID", unique=true, nullable=false, precision=10)
	private long aplId;

	@Column(name="APL_CREATED_BY", nullable=false, length=64)
	private String aplCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="APL_CREATED_DATE", nullable=false)
	private Date aplCreatedDate;

	@Column(name="APL_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal aplElementState;

	@Column(name="APL_EVENT_TYPE", nullable=false, precision=2)
	private BigDecimal aplEventType;

	@Temporal(TemporalType.DATE)
	@Column(name="APL_TIMESTAMP", nullable=false)
	private Date aplTimestamp;

	@Column(name="APL_UPDATED_BY", length=64)
	private String aplUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="APL_UPDATED_DATE")
	private Date aplUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5AsmPolicy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APL_ASMP_ID", nullable=false)
	private Eul5AsmPolicy eul5AsmPolicy;

	//bi-directional many-to-one association to Eul5SummaryObj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APL_SUMO_ID")
	private Eul5SummaryObj eul5SummaryObj;

	public Eul5AsmpLog() {
	}

	public long getAplId() {
		return this.aplId;
	}

	public void setAplId(long aplId) {
		this.aplId = aplId;
	}

	public String getAplCreatedBy() {
		return this.aplCreatedBy;
	}

	public void setAplCreatedBy(String aplCreatedBy) {
		this.aplCreatedBy = aplCreatedBy;
	}

	public Date getAplCreatedDate() {
		return this.aplCreatedDate;
	}

	public void setAplCreatedDate(Date aplCreatedDate) {
		this.aplCreatedDate = aplCreatedDate;
	}

	public BigDecimal getAplElementState() {
		return this.aplElementState;
	}

	public void setAplElementState(BigDecimal aplElementState) {
		this.aplElementState = aplElementState;
	}

	public BigDecimal getAplEventType() {
		return this.aplEventType;
	}

	public void setAplEventType(BigDecimal aplEventType) {
		this.aplEventType = aplEventType;
	}

	public Date getAplTimestamp() {
		return this.aplTimestamp;
	}

	public void setAplTimestamp(Date aplTimestamp) {
		this.aplTimestamp = aplTimestamp;
	}

	public String getAplUpdatedBy() {
		return this.aplUpdatedBy;
	}

	public void setAplUpdatedBy(String aplUpdatedBy) {
		this.aplUpdatedBy = aplUpdatedBy;
	}

	public Date getAplUpdatedDate() {
		return this.aplUpdatedDate;
	}

	public void setAplUpdatedDate(Date aplUpdatedDate) {
		this.aplUpdatedDate = aplUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5AsmPolicy getEul5AsmPolicy() {
		return this.eul5AsmPolicy;
	}

	public void setEul5AsmPolicy(Eul5AsmPolicy eul5AsmPolicy) {
		this.eul5AsmPolicy = eul5AsmPolicy;
	}

	public Eul5SummaryObj getEul5SummaryObj() {
		return this.eul5SummaryObj;
	}

	public void setEul5SummaryObj(Eul5SummaryObj eul5SummaryObj) {
		this.eul5SummaryObj = eul5SummaryObj;
	}

}