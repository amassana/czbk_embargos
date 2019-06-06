package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_SUMO_EXP_USGS database table.
 * 
 */
@Entity
@Table(name="EUL5_SUMO_EXP_USGS")
@NamedQuery(name="Eul5SumoExpUsg.findAll", query="SELECT e FROM Eul5SumoExpUsg e")
public class Eul5SumoExpUsg implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SEU_ID", unique=true, nullable=false, precision=10)
	private long seuId;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="SEU_CREATED_BY", nullable=false, length=64)
	private String seuCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SEU_CREATED_DATE", nullable=false)
	private Date seuCreatedDate;

	@Column(name="SEU_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal seuElementState;

	@Column(name="SEU_EXT_COLUMN", length=64)
	private String seuExtColumn;

	@Column(name="SEU_TYPE", nullable=false, length=10)
	private String seuType;

	@Column(name="SEU_UPDATED_BY", length=64)
	private String seuUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SEU_UPDATED_DATE")
	private Date seuUpdatedDate;

	@Column(name="SEU_VISIBLE", nullable=false, precision=1)
	private BigDecimal seuVisible;

	@Column(name="SIU_ITEM_MODIFIED", precision=1)
	private BigDecimal siuItemModified;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SIU_EXP_ID")
	private Eul5Expression eul5Expression;

	//bi-directional many-to-one association to Eul5Function
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SFU_FUN_ID")
	private Eul5Function eul5Function1;

	//bi-directional many-to-one association to Eul5Function
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SMIU_FUN_ID")
	private Eul5Function eul5Function2;

	//bi-directional many-to-one association to Eul5SummaryObj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEU_SUMO_ID", nullable=false)
	private Eul5SummaryObj eul5SummaryObj;

	public Eul5SumoExpUsg() {
	}

	public long getSeuId() {
		return this.seuId;
	}

	public void setSeuId(long seuId) {
		this.seuId = seuId;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public String getSeuCreatedBy() {
		return this.seuCreatedBy;
	}

	public void setSeuCreatedBy(String seuCreatedBy) {
		this.seuCreatedBy = seuCreatedBy;
	}

	public Date getSeuCreatedDate() {
		return this.seuCreatedDate;
	}

	public void setSeuCreatedDate(Date seuCreatedDate) {
		this.seuCreatedDate = seuCreatedDate;
	}

	public BigDecimal getSeuElementState() {
		return this.seuElementState;
	}

	public void setSeuElementState(BigDecimal seuElementState) {
		this.seuElementState = seuElementState;
	}

	public String getSeuExtColumn() {
		return this.seuExtColumn;
	}

	public void setSeuExtColumn(String seuExtColumn) {
		this.seuExtColumn = seuExtColumn;
	}

	public String getSeuType() {
		return this.seuType;
	}

	public void setSeuType(String seuType) {
		this.seuType = seuType;
	}

	public String getSeuUpdatedBy() {
		return this.seuUpdatedBy;
	}

	public void setSeuUpdatedBy(String seuUpdatedBy) {
		this.seuUpdatedBy = seuUpdatedBy;
	}

	public Date getSeuUpdatedDate() {
		return this.seuUpdatedDate;
	}

	public void setSeuUpdatedDate(Date seuUpdatedDate) {
		this.seuUpdatedDate = seuUpdatedDate;
	}

	public BigDecimal getSeuVisible() {
		return this.seuVisible;
	}

	public void setSeuVisible(BigDecimal seuVisible) {
		this.seuVisible = seuVisible;
	}

	public BigDecimal getSiuItemModified() {
		return this.siuItemModified;
	}

	public void setSiuItemModified(BigDecimal siuItemModified) {
		this.siuItemModified = siuItemModified;
	}

	public Eul5Expression getEul5Expression() {
		return this.eul5Expression;
	}

	public void setEul5Expression(Eul5Expression eul5Expression) {
		this.eul5Expression = eul5Expression;
	}

	public Eul5Function getEul5Function1() {
		return this.eul5Function1;
	}

	public void setEul5Function1(Eul5Function eul5Function1) {
		this.eul5Function1 = eul5Function1;
	}

	public Eul5Function getEul5Function2() {
		return this.eul5Function2;
	}

	public void setEul5Function2(Eul5Function eul5Function2) {
		this.eul5Function2 = eul5Function2;
	}

	public Eul5SummaryObj getEul5SummaryObj() {
		return this.eul5SummaryObj;
	}

	public void setEul5SummaryObj(Eul5SummaryObj eul5SummaryObj) {
		this.eul5SummaryObj = eul5SummaryObj;
	}

}