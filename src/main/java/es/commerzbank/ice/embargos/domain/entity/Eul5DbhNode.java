package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_DBH_NODES database table.
 * 
 */
@Entity
@Table(name="EUL5_DBH_NODES")
@NamedQuery(name="Eul5DbhNode.findAll", query="SELECT e FROM Eul5DbhNode e")
public class Eul5DbhNode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DHN_ID", unique=true, nullable=false, precision=10)
	private long dhnId;

	@Column(name="DHN_CREATED_BY", nullable=false, length=64)
	private String dhnCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="DHN_CREATED_DATE", nullable=false)
	private Date dhnCreatedDate;

	@Column(name="DHN_DATA_FMT_MSK", nullable=false, length=100)
	private String dhnDataFmtMsk;

	@Column(name="DHN_DESCRIPTION", length=240)
	private String dhnDescription;

	@Column(name="DHN_DEVELOPER_KEY", nullable=false, length=100)
	private String dhnDeveloperKey;

	@Column(name="DHN_DISP_FMT_MSK", nullable=false, length=100)
	private String dhnDispFmtMsk;

	@Column(name="DHN_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal dhnElementState;

	@Column(name="DHN_NAME", nullable=false, length=100)
	private String dhnName;

	@Column(name="DHN_UPDATED_BY", length=64)
	private String dhnUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="DHN_UPDATED_DATE")
	private Date dhnUpdatedDate;

	@Column(name="DHN_USER_PROP1", length=100)
	private String dhnUserProp1;

	@Column(name="DHN_USER_PROP2", length=100)
	private String dhnUserProp2;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5Hierarchy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DHN_HI_ID", nullable=false)
	private Eul5Hierarchy eul5Hierarchy;

	//bi-directional many-to-one association to Eul5HiSegment
	@OneToMany(mappedBy="eul5DbhNode1")
	private List<Eul5HiSegment> eul5HiSegments1;

	//bi-directional many-to-one association to Eul5HiSegment
	@OneToMany(mappedBy="eul5DbhNode2")
	private List<Eul5HiSegment> eul5HiSegments2;

	public Eul5DbhNode() {
	}

	public long getDhnId() {
		return this.dhnId;
	}

	public void setDhnId(long dhnId) {
		this.dhnId = dhnId;
	}

	public String getDhnCreatedBy() {
		return this.dhnCreatedBy;
	}

	public void setDhnCreatedBy(String dhnCreatedBy) {
		this.dhnCreatedBy = dhnCreatedBy;
	}

	public Date getDhnCreatedDate() {
		return this.dhnCreatedDate;
	}

	public void setDhnCreatedDate(Date dhnCreatedDate) {
		this.dhnCreatedDate = dhnCreatedDate;
	}

	public String getDhnDataFmtMsk() {
		return this.dhnDataFmtMsk;
	}

	public void setDhnDataFmtMsk(String dhnDataFmtMsk) {
		this.dhnDataFmtMsk = dhnDataFmtMsk;
	}

	public String getDhnDescription() {
		return this.dhnDescription;
	}

	public void setDhnDescription(String dhnDescription) {
		this.dhnDescription = dhnDescription;
	}

	public String getDhnDeveloperKey() {
		return this.dhnDeveloperKey;
	}

	public void setDhnDeveloperKey(String dhnDeveloperKey) {
		this.dhnDeveloperKey = dhnDeveloperKey;
	}

	public String getDhnDispFmtMsk() {
		return this.dhnDispFmtMsk;
	}

	public void setDhnDispFmtMsk(String dhnDispFmtMsk) {
		this.dhnDispFmtMsk = dhnDispFmtMsk;
	}

	public BigDecimal getDhnElementState() {
		return this.dhnElementState;
	}

	public void setDhnElementState(BigDecimal dhnElementState) {
		this.dhnElementState = dhnElementState;
	}

	public String getDhnName() {
		return this.dhnName;
	}

	public void setDhnName(String dhnName) {
		this.dhnName = dhnName;
	}

	public String getDhnUpdatedBy() {
		return this.dhnUpdatedBy;
	}

	public void setDhnUpdatedBy(String dhnUpdatedBy) {
		this.dhnUpdatedBy = dhnUpdatedBy;
	}

	public Date getDhnUpdatedDate() {
		return this.dhnUpdatedDate;
	}

	public void setDhnUpdatedDate(Date dhnUpdatedDate) {
		this.dhnUpdatedDate = dhnUpdatedDate;
	}

	public String getDhnUserProp1() {
		return this.dhnUserProp1;
	}

	public void setDhnUserProp1(String dhnUserProp1) {
		this.dhnUserProp1 = dhnUserProp1;
	}

	public String getDhnUserProp2() {
		return this.dhnUserProp2;
	}

	public void setDhnUserProp2(String dhnUserProp2) {
		this.dhnUserProp2 = dhnUserProp2;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5Hierarchy getEul5Hierarchy() {
		return this.eul5Hierarchy;
	}

	public void setEul5Hierarchy(Eul5Hierarchy eul5Hierarchy) {
		this.eul5Hierarchy = eul5Hierarchy;
	}

	public List<Eul5HiSegment> getEul5HiSegments1() {
		return this.eul5HiSegments1;
	}

	public void setEul5HiSegments1(List<Eul5HiSegment> eul5HiSegments1) {
		this.eul5HiSegments1 = eul5HiSegments1;
	}

	public Eul5HiSegment addEul5HiSegments1(Eul5HiSegment eul5HiSegments1) {
		getEul5HiSegments1().add(eul5HiSegments1);
		eul5HiSegments1.setEul5DbhNode1(this);

		return eul5HiSegments1;
	}

	public Eul5HiSegment removeEul5HiSegments1(Eul5HiSegment eul5HiSegments1) {
		getEul5HiSegments1().remove(eul5HiSegments1);
		eul5HiSegments1.setEul5DbhNode1(null);

		return eul5HiSegments1;
	}

	public List<Eul5HiSegment> getEul5HiSegments2() {
		return this.eul5HiSegments2;
	}

	public void setEul5HiSegments2(List<Eul5HiSegment> eul5HiSegments2) {
		this.eul5HiSegments2 = eul5HiSegments2;
	}

	public Eul5HiSegment addEul5HiSegments2(Eul5HiSegment eul5HiSegments2) {
		getEul5HiSegments2().add(eul5HiSegments2);
		eul5HiSegments2.setEul5DbhNode2(this);

		return eul5HiSegments2;
	}

	public Eul5HiSegment removeEul5HiSegments2(Eul5HiSegment eul5HiSegments2) {
		getEul5HiSegments2().remove(eul5HiSegments2);
		eul5HiSegments2.setEul5DbhNode2(null);

		return eul5HiSegments2;
	}

}