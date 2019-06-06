package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_SEGMENTS database table.
 * 
 */
@Entity
@Table(name="EUL5_SEGMENTS")
@NamedQuery(name="Eul5Segment.findAll", query="SELECT e FROM Eul5Segment e")
public class Eul5Segment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SEG_ID", unique=true, nullable=false, precision=10)
	private long segId;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="SEG_CHUNK1", length=250)
	private String segChunk1;

	@Column(name="SEG_CHUNK2", length=250)
	private String segChunk2;

	@Column(name="SEG_CHUNK3", length=250)
	private String segChunk3;

	@Column(name="SEG_CHUNK4", length=250)
	private String segChunk4;

	@Column(name="SEG_CREATED_BY", nullable=false, length=64)
	private String segCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SEG_CREATED_DATE", nullable=false)
	private Date segCreatedDate;

	@Column(name="SEG_EL_ID", precision=10)
	private BigDecimal segElId;

	@Column(name="SEG_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal segElementState;

	@Column(name="SEG_SEG_TYPE", nullable=false, precision=1)
	private BigDecimal segSegType;

	@Column(name="SEG_SEQUENCE", nullable=false, precision=22)
	private BigDecimal segSequence;

	@Column(name="SEG_UPDATED_BY", length=64)
	private String segUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SEG_UPDATED_DATE")
	private Date segUpdatedDate;

	//bi-directional many-to-one association to Eul5BatchQuery
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEG_BQ_ID")
	private Eul5BatchQuery eul5BatchQuery;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEG_EXP_ID")
	private Eul5Expression eul5Expression;

	//bi-directional many-to-one association to Eul5Obj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEG_OBJ_ID")
	private Eul5Obj eul5Obj1;

	//bi-directional many-to-one association to Eul5Obj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEG_CUO_ID")
	private Eul5Obj eul5Obj2;

	//bi-directional many-to-one association to Eul5SummaryObj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEG_SMS_ID")
	private Eul5SummaryObj eul5SummaryObj1;

	//bi-directional many-to-one association to Eul5SummaryObj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEG_SUMO_ID")
	private Eul5SummaryObj eul5SummaryObj2;

	public Eul5Segment() {
	}

	public long getSegId() {
		return this.segId;
	}

	public void setSegId(long segId) {
		this.segId = segId;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public String getSegChunk1() {
		return this.segChunk1;
	}

	public void setSegChunk1(String segChunk1) {
		this.segChunk1 = segChunk1;
	}

	public String getSegChunk2() {
		return this.segChunk2;
	}

	public void setSegChunk2(String segChunk2) {
		this.segChunk2 = segChunk2;
	}

	public String getSegChunk3() {
		return this.segChunk3;
	}

	public void setSegChunk3(String segChunk3) {
		this.segChunk3 = segChunk3;
	}

	public String getSegChunk4() {
		return this.segChunk4;
	}

	public void setSegChunk4(String segChunk4) {
		this.segChunk4 = segChunk4;
	}

	public String getSegCreatedBy() {
		return this.segCreatedBy;
	}

	public void setSegCreatedBy(String segCreatedBy) {
		this.segCreatedBy = segCreatedBy;
	}

	public Date getSegCreatedDate() {
		return this.segCreatedDate;
	}

	public void setSegCreatedDate(Date segCreatedDate) {
		this.segCreatedDate = segCreatedDate;
	}

	public BigDecimal getSegElId() {
		return this.segElId;
	}

	public void setSegElId(BigDecimal segElId) {
		this.segElId = segElId;
	}

	public BigDecimal getSegElementState() {
		return this.segElementState;
	}

	public void setSegElementState(BigDecimal segElementState) {
		this.segElementState = segElementState;
	}

	public BigDecimal getSegSegType() {
		return this.segSegType;
	}

	public void setSegSegType(BigDecimal segSegType) {
		this.segSegType = segSegType;
	}

	public BigDecimal getSegSequence() {
		return this.segSequence;
	}

	public void setSegSequence(BigDecimal segSequence) {
		this.segSequence = segSequence;
	}

	public String getSegUpdatedBy() {
		return this.segUpdatedBy;
	}

	public void setSegUpdatedBy(String segUpdatedBy) {
		this.segUpdatedBy = segUpdatedBy;
	}

	public Date getSegUpdatedDate() {
		return this.segUpdatedDate;
	}

	public void setSegUpdatedDate(Date segUpdatedDate) {
		this.segUpdatedDate = segUpdatedDate;
	}

	public Eul5BatchQuery getEul5BatchQuery() {
		return this.eul5BatchQuery;
	}

	public void setEul5BatchQuery(Eul5BatchQuery eul5BatchQuery) {
		this.eul5BatchQuery = eul5BatchQuery;
	}

	public Eul5Expression getEul5Expression() {
		return this.eul5Expression;
	}

	public void setEul5Expression(Eul5Expression eul5Expression) {
		this.eul5Expression = eul5Expression;
	}

	public Eul5Obj getEul5Obj1() {
		return this.eul5Obj1;
	}

	public void setEul5Obj1(Eul5Obj eul5Obj1) {
		this.eul5Obj1 = eul5Obj1;
	}

	public Eul5Obj getEul5Obj2() {
		return this.eul5Obj2;
	}

	public void setEul5Obj2(Eul5Obj eul5Obj2) {
		this.eul5Obj2 = eul5Obj2;
	}

	public Eul5SummaryObj getEul5SummaryObj1() {
		return this.eul5SummaryObj1;
	}

	public void setEul5SummaryObj1(Eul5SummaryObj eul5SummaryObj1) {
		this.eul5SummaryObj1 = eul5SummaryObj1;
	}

	public Eul5SummaryObj getEul5SummaryObj2() {
		return this.eul5SummaryObj2;
	}

	public void setEul5SummaryObj2(Eul5SummaryObj eul5SummaryObj2) {
		this.eul5SummaryObj2 = eul5SummaryObj2;
	}

}