package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_BATCH_QUERIES database table.
 * 
 */
@Entity
@Table(name="EUL5_BATCH_QUERIES")
@NamedQuery(name="Eul5BatchQuery.findAll", query="SELECT e FROM Eul5BatchQuery e")
public class Eul5BatchQuery implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BQ_ID", unique=true, nullable=false, precision=10)
	private long bqId;

	@Column(name="BQ_CREATED_BY", nullable=false, length=64)
	private String bqCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BQ_CREATED_DATE", nullable=false)
	private Date bqCreatedDate;

	@Column(name="BQ_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal bqElementState;

	@Column(name="BQ_QUERY_ID", nullable=false, length=240)
	private String bqQueryId;

	@Column(name="BQ_RESULT_SQL_1", length=250)
	private String bqResultSql1;

	@Column(name="BQ_RESULT_SQL_2", length=250)
	private String bqResultSql2;

	@Column(name="BQ_RESULT_SQL_3", length=250)
	private String bqResultSql3;

	@Column(name="BQ_RESULT_SQL_4", length=250)
	private String bqResultSql4;

	@Column(name="BQ_UPDATED_BY", length=64)
	private String bqUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BQ_UPDATED_DATE")
	private Date bqUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5BatchSheet
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BQ_BS_ID", nullable=false)
	private Eul5BatchSheet eul5BatchSheet;

	//bi-directional many-to-one association to Eul5BqDep
	@OneToMany(mappedBy="eul5BatchQuery")
	private List<Eul5BqDep> eul5BqDeps;

	//bi-directional many-to-one association to Eul5BqTable
	@OneToMany(mappedBy="eul5BatchQuery")
	private List<Eul5BqTable> eul5BqTables;

	//bi-directional many-to-one association to Eul5Segment
	@OneToMany(mappedBy="eul5BatchQuery")
	private List<Eul5Segment> eul5Segments;

	public Eul5BatchQuery() {
	}

	public long getBqId() {
		return this.bqId;
	}

	public void setBqId(long bqId) {
		this.bqId = bqId;
	}

	public String getBqCreatedBy() {
		return this.bqCreatedBy;
	}

	public void setBqCreatedBy(String bqCreatedBy) {
		this.bqCreatedBy = bqCreatedBy;
	}

	public Date getBqCreatedDate() {
		return this.bqCreatedDate;
	}

	public void setBqCreatedDate(Date bqCreatedDate) {
		this.bqCreatedDate = bqCreatedDate;
	}

	public BigDecimal getBqElementState() {
		return this.bqElementState;
	}

	public void setBqElementState(BigDecimal bqElementState) {
		this.bqElementState = bqElementState;
	}

	public String getBqQueryId() {
		return this.bqQueryId;
	}

	public void setBqQueryId(String bqQueryId) {
		this.bqQueryId = bqQueryId;
	}

	public String getBqResultSql1() {
		return this.bqResultSql1;
	}

	public void setBqResultSql1(String bqResultSql1) {
		this.bqResultSql1 = bqResultSql1;
	}

	public String getBqResultSql2() {
		return this.bqResultSql2;
	}

	public void setBqResultSql2(String bqResultSql2) {
		this.bqResultSql2 = bqResultSql2;
	}

	public String getBqResultSql3() {
		return this.bqResultSql3;
	}

	public void setBqResultSql3(String bqResultSql3) {
		this.bqResultSql3 = bqResultSql3;
	}

	public String getBqResultSql4() {
		return this.bqResultSql4;
	}

	public void setBqResultSql4(String bqResultSql4) {
		this.bqResultSql4 = bqResultSql4;
	}

	public String getBqUpdatedBy() {
		return this.bqUpdatedBy;
	}

	public void setBqUpdatedBy(String bqUpdatedBy) {
		this.bqUpdatedBy = bqUpdatedBy;
	}

	public Date getBqUpdatedDate() {
		return this.bqUpdatedDate;
	}

	public void setBqUpdatedDate(Date bqUpdatedDate) {
		this.bqUpdatedDate = bqUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5BatchSheet getEul5BatchSheet() {
		return this.eul5BatchSheet;
	}

	public void setEul5BatchSheet(Eul5BatchSheet eul5BatchSheet) {
		this.eul5BatchSheet = eul5BatchSheet;
	}

	public List<Eul5BqDep> getEul5BqDeps() {
		return this.eul5BqDeps;
	}

	public void setEul5BqDeps(List<Eul5BqDep> eul5BqDeps) {
		this.eul5BqDeps = eul5BqDeps;
	}

	public Eul5BqDep addEul5BqDep(Eul5BqDep eul5BqDep) {
		getEul5BqDeps().add(eul5BqDep);
		eul5BqDep.setEul5BatchQuery(this);

		return eul5BqDep;
	}

	public Eul5BqDep removeEul5BqDep(Eul5BqDep eul5BqDep) {
		getEul5BqDeps().remove(eul5BqDep);
		eul5BqDep.setEul5BatchQuery(null);

		return eul5BqDep;
	}

	public List<Eul5BqTable> getEul5BqTables() {
		return this.eul5BqTables;
	}

	public void setEul5BqTables(List<Eul5BqTable> eul5BqTables) {
		this.eul5BqTables = eul5BqTables;
	}

	public Eul5BqTable addEul5BqTable(Eul5BqTable eul5BqTable) {
		getEul5BqTables().add(eul5BqTable);
		eul5BqTable.setEul5BatchQuery(this);

		return eul5BqTable;
	}

	public Eul5BqTable removeEul5BqTable(Eul5BqTable eul5BqTable) {
		getEul5BqTables().remove(eul5BqTable);
		eul5BqTable.setEul5BatchQuery(null);

		return eul5BqTable;
	}

	public List<Eul5Segment> getEul5Segments() {
		return this.eul5Segments;
	}

	public void setEul5Segments(List<Eul5Segment> eul5Segments) {
		this.eul5Segments = eul5Segments;
	}

	public Eul5Segment addEul5Segment(Eul5Segment eul5Segment) {
		getEul5Segments().add(eul5Segment);
		eul5Segment.setEul5BatchQuery(this);

		return eul5Segment;
	}

	public Eul5Segment removeEul5Segment(Eul5Segment eul5Segment) {
		getEul5Segments().remove(eul5Segment);
		eul5Segment.setEul5BatchQuery(null);

		return eul5Segment;
	}

}