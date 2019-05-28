package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_BQ_TABLES database table.
 * 
 */
@Entity
@Table(name="EUL5_BQ_TABLES")
@NamedQuery(name="Eul5BqTable.findAll", query="SELECT e FROM Eul5BqTable e")
public class Eul5BqTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BQT_ID", unique=true, nullable=false, precision=10)
	private long bqtId;

	@Column(name="BQT_CREATED_BY", nullable=false, length=64)
	private String bqtCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BQT_CREATED_DATE", nullable=false)
	private Date bqtCreatedDate;

	@Column(name="BQT_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal bqtElementState;

	@Column(name="BQT_TABLE_NAME", nullable=false, length=64)
	private String bqtTableName;

	@Column(name="BQT_UPDATED_BY", length=64)
	private String bqtUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BQT_UPDATED_DATE")
	private Date bqtUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5BatchQuery
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BQT_BQ_ID", nullable=false)
	private Eul5BatchQuery eul5BatchQuery;

	//bi-directional many-to-one association to Eul5BrRun
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BQT_BRR_ID", nullable=false)
	private Eul5BrRun eul5BrRun;

	public Eul5BqTable() {
	}

	public long getBqtId() {
		return this.bqtId;
	}

	public void setBqtId(long bqtId) {
		this.bqtId = bqtId;
	}

	public String getBqtCreatedBy() {
		return this.bqtCreatedBy;
	}

	public void setBqtCreatedBy(String bqtCreatedBy) {
		this.bqtCreatedBy = bqtCreatedBy;
	}

	public Date getBqtCreatedDate() {
		return this.bqtCreatedDate;
	}

	public void setBqtCreatedDate(Date bqtCreatedDate) {
		this.bqtCreatedDate = bqtCreatedDate;
	}

	public BigDecimal getBqtElementState() {
		return this.bqtElementState;
	}

	public void setBqtElementState(BigDecimal bqtElementState) {
		this.bqtElementState = bqtElementState;
	}

	public String getBqtTableName() {
		return this.bqtTableName;
	}

	public void setBqtTableName(String bqtTableName) {
		this.bqtTableName = bqtTableName;
	}

	public String getBqtUpdatedBy() {
		return this.bqtUpdatedBy;
	}

	public void setBqtUpdatedBy(String bqtUpdatedBy) {
		this.bqtUpdatedBy = bqtUpdatedBy;
	}

	public Date getBqtUpdatedDate() {
		return this.bqtUpdatedDate;
	}

	public void setBqtUpdatedDate(Date bqtUpdatedDate) {
		this.bqtUpdatedDate = bqtUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5BatchQuery getEul5BatchQuery() {
		return this.eul5BatchQuery;
	}

	public void setEul5BatchQuery(Eul5BatchQuery eul5BatchQuery) {
		this.eul5BatchQuery = eul5BatchQuery;
	}

	public Eul5BrRun getEul5BrRun() {
		return this.eul5BrRun;
	}

	public void setEul5BrRun(Eul5BrRun eul5BrRun) {
		this.eul5BrRun = eul5BrRun;
	}

}