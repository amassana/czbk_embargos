package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_SUM_BITMAPS database table.
 * 
 */
@Entity
@Table(name="EUL5_SUM_BITMAPS")
@NamedQuery(name="Eul5SumBitmap.findAll", query="SELECT e FROM Eul5SumBitmap e")
public class Eul5SumBitmap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SB_ID", unique=true, nullable=false, precision=10)
	private long sbId;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="SB_BITMAP", nullable=false)
	private byte[] sbBitmap;

	@Column(name="SB_CREATED_BY", nullable=false, length=64)
	private String sbCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SB_CREATED_DATE", nullable=false)
	private Date sbCreatedDate;

	@Column(name="SB_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal sbElementState;

	@Column(name="SB_SEQUENCE", nullable=false, precision=22)
	private BigDecimal sbSequence;

	@Column(name="SB_UPDATED_BY", length=64)
	private String sbUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SB_UPDATED_DATE")
	private Date sbUpdatedDate;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SB_EXP_ID")
	private Eul5Expression eul5Expression;

	//bi-directional many-to-one association to Eul5Function
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SB_FUN_ID")
	private Eul5Function eul5Function;

	//bi-directional many-to-one association to Eul5KeyCon
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SB_KEY_ID")
	private Eul5KeyCon eul5KeyCon;

	public Eul5SumBitmap() {
	}

	public long getSbId() {
		return this.sbId;
	}

	public void setSbId(long sbId) {
		this.sbId = sbId;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public byte[] getSbBitmap() {
		return this.sbBitmap;
	}

	public void setSbBitmap(byte[] sbBitmap) {
		this.sbBitmap = sbBitmap;
	}

	public String getSbCreatedBy() {
		return this.sbCreatedBy;
	}

	public void setSbCreatedBy(String sbCreatedBy) {
		this.sbCreatedBy = sbCreatedBy;
	}

	public Date getSbCreatedDate() {
		return this.sbCreatedDate;
	}

	public void setSbCreatedDate(Date sbCreatedDate) {
		this.sbCreatedDate = sbCreatedDate;
	}

	public BigDecimal getSbElementState() {
		return this.sbElementState;
	}

	public void setSbElementState(BigDecimal sbElementState) {
		this.sbElementState = sbElementState;
	}

	public BigDecimal getSbSequence() {
		return this.sbSequence;
	}

	public void setSbSequence(BigDecimal sbSequence) {
		this.sbSequence = sbSequence;
	}

	public String getSbUpdatedBy() {
		return this.sbUpdatedBy;
	}

	public void setSbUpdatedBy(String sbUpdatedBy) {
		this.sbUpdatedBy = sbUpdatedBy;
	}

	public Date getSbUpdatedDate() {
		return this.sbUpdatedDate;
	}

	public void setSbUpdatedDate(Date sbUpdatedDate) {
		this.sbUpdatedDate = sbUpdatedDate;
	}

	public Eul5Expression getEul5Expression() {
		return this.eul5Expression;
	}

	public void setEul5Expression(Eul5Expression eul5Expression) {
		this.eul5Expression = eul5Expression;
	}

	public Eul5Function getEul5Function() {
		return this.eul5Function;
	}

	public void setEul5Function(Eul5Function eul5Function) {
		this.eul5Function = eul5Function;
	}

	public Eul5KeyCon getEul5KeyCon() {
		return this.eul5KeyCon;
	}

	public void setEul5KeyCon(Eul5KeyCon eul5KeyCon) {
		this.eul5KeyCon = eul5KeyCon;
	}

}