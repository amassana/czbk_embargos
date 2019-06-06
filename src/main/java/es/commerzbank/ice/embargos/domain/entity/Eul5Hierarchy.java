package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_HIERARCHIES database table.
 * 
 */
@Entity
@Table(name="EUL5_HIERARCHIES")
@NamedQuery(name="Eul5Hierarchy.findAll", query="SELECT e FROM Eul5Hierarchy e")
public class Eul5Hierarchy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="HI_ID", unique=true, nullable=false, precision=10)
	private long hiId;

	@Column(name="DBH_DEFAULT", precision=1)
	private BigDecimal dbhDefault;

	@Column(name="HI_CREATED_BY", nullable=false, length=64)
	private String hiCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="HI_CREATED_DATE", nullable=false)
	private Date hiCreatedDate;

	@Column(name="HI_DESCRIPTION", length=240)
	private String hiDescription;

	@Column(name="HI_DEVELOPER_KEY", nullable=false, length=100)
	private String hiDeveloperKey;

	@Column(name="HI_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal hiElementState;

	@Column(name="HI_EXT_HIERARCHY", length=64)
	private String hiExtHierarchy;

	@Column(name="HI_NAME", nullable=false, length=100)
	private String hiName;

	@Column(name="HI_SYS_GENERATED", nullable=false, precision=1)
	private BigDecimal hiSysGenerated;

	@Column(name="HI_TYPE", nullable=false, length=10)
	private String hiType;

	@Column(name="HI_UPDATED_BY", length=64)
	private String hiUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="HI_UPDATED_DATE")
	private Date hiUpdatedDate;

	@Column(name="HI_USER_PROP1", length=100)
	private String hiUserProp1;

	@Column(name="HI_USER_PROP2", length=100)
	private String hiUserProp2;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5DbhNode
	@OneToMany(mappedBy="eul5Hierarchy")
	private List<Eul5DbhNode> eul5DbhNodes;

	//bi-directional many-to-one association to Eul5Hierarchy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IBH_DBH_ID")
	private Eul5Hierarchy eul5Hierarchy;

	//bi-directional many-to-one association to Eul5Hierarchy
	@OneToMany(mappedBy="eul5Hierarchy")
	private List<Eul5Hierarchy> eul5Hierarchies;

	//bi-directional many-to-one association to Eul5HiNode
	@OneToMany(mappedBy="eul5Hierarchy")
	private List<Eul5HiNode> eul5HiNodes;

	//bi-directional many-to-one association to Eul5HiSegment
	@OneToMany(mappedBy="eul5Hierarchy1")
	private List<Eul5HiSegment> eul5HiSegments1;

	//bi-directional many-to-one association to Eul5HiSegment
	@OneToMany(mappedBy="eul5Hierarchy2")
	private List<Eul5HiSegment> eul5HiSegments2;

	public Eul5Hierarchy() {
	}

	public long getHiId() {
		return this.hiId;
	}

	public void setHiId(long hiId) {
		this.hiId = hiId;
	}

	public BigDecimal getDbhDefault() {
		return this.dbhDefault;
	}

	public void setDbhDefault(BigDecimal dbhDefault) {
		this.dbhDefault = dbhDefault;
	}

	public String getHiCreatedBy() {
		return this.hiCreatedBy;
	}

	public void setHiCreatedBy(String hiCreatedBy) {
		this.hiCreatedBy = hiCreatedBy;
	}

	public Date getHiCreatedDate() {
		return this.hiCreatedDate;
	}

	public void setHiCreatedDate(Date hiCreatedDate) {
		this.hiCreatedDate = hiCreatedDate;
	}

	public String getHiDescription() {
		return this.hiDescription;
	}

	public void setHiDescription(String hiDescription) {
		this.hiDescription = hiDescription;
	}

	public String getHiDeveloperKey() {
		return this.hiDeveloperKey;
	}

	public void setHiDeveloperKey(String hiDeveloperKey) {
		this.hiDeveloperKey = hiDeveloperKey;
	}

	public BigDecimal getHiElementState() {
		return this.hiElementState;
	}

	public void setHiElementState(BigDecimal hiElementState) {
		this.hiElementState = hiElementState;
	}

	public String getHiExtHierarchy() {
		return this.hiExtHierarchy;
	}

	public void setHiExtHierarchy(String hiExtHierarchy) {
		this.hiExtHierarchy = hiExtHierarchy;
	}

	public String getHiName() {
		return this.hiName;
	}

	public void setHiName(String hiName) {
		this.hiName = hiName;
	}

	public BigDecimal getHiSysGenerated() {
		return this.hiSysGenerated;
	}

	public void setHiSysGenerated(BigDecimal hiSysGenerated) {
		this.hiSysGenerated = hiSysGenerated;
	}

	public String getHiType() {
		return this.hiType;
	}

	public void setHiType(String hiType) {
		this.hiType = hiType;
	}

	public String getHiUpdatedBy() {
		return this.hiUpdatedBy;
	}

	public void setHiUpdatedBy(String hiUpdatedBy) {
		this.hiUpdatedBy = hiUpdatedBy;
	}

	public Date getHiUpdatedDate() {
		return this.hiUpdatedDate;
	}

	public void setHiUpdatedDate(Date hiUpdatedDate) {
		this.hiUpdatedDate = hiUpdatedDate;
	}

	public String getHiUserProp1() {
		return this.hiUserProp1;
	}

	public void setHiUserProp1(String hiUserProp1) {
		this.hiUserProp1 = hiUserProp1;
	}

	public String getHiUserProp2() {
		return this.hiUserProp2;
	}

	public void setHiUserProp2(String hiUserProp2) {
		this.hiUserProp2 = hiUserProp2;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public List<Eul5DbhNode> getEul5DbhNodes() {
		return this.eul5DbhNodes;
	}

	public void setEul5DbhNodes(List<Eul5DbhNode> eul5DbhNodes) {
		this.eul5DbhNodes = eul5DbhNodes;
	}

	public Eul5DbhNode addEul5DbhNode(Eul5DbhNode eul5DbhNode) {
		getEul5DbhNodes().add(eul5DbhNode);
		eul5DbhNode.setEul5Hierarchy(this);

		return eul5DbhNode;
	}

	public Eul5DbhNode removeEul5DbhNode(Eul5DbhNode eul5DbhNode) {
		getEul5DbhNodes().remove(eul5DbhNode);
		eul5DbhNode.setEul5Hierarchy(null);

		return eul5DbhNode;
	}

	public Eul5Hierarchy getEul5Hierarchy() {
		return this.eul5Hierarchy;
	}

	public void setEul5Hierarchy(Eul5Hierarchy eul5Hierarchy) {
		this.eul5Hierarchy = eul5Hierarchy;
	}

	public List<Eul5Hierarchy> getEul5Hierarchies() {
		return this.eul5Hierarchies;
	}

	public void setEul5Hierarchies(List<Eul5Hierarchy> eul5Hierarchies) {
		this.eul5Hierarchies = eul5Hierarchies;
	}

	public Eul5Hierarchy addEul5Hierarchy(Eul5Hierarchy eul5Hierarchy) {
		getEul5Hierarchies().add(eul5Hierarchy);
		eul5Hierarchy.setEul5Hierarchy(this);

		return eul5Hierarchy;
	}

	public Eul5Hierarchy removeEul5Hierarchy(Eul5Hierarchy eul5Hierarchy) {
		getEul5Hierarchies().remove(eul5Hierarchy);
		eul5Hierarchy.setEul5Hierarchy(null);

		return eul5Hierarchy;
	}

	public List<Eul5HiNode> getEul5HiNodes() {
		return this.eul5HiNodes;
	}

	public void setEul5HiNodes(List<Eul5HiNode> eul5HiNodes) {
		this.eul5HiNodes = eul5HiNodes;
	}

	public Eul5HiNode addEul5HiNode(Eul5HiNode eul5HiNode) {
		getEul5HiNodes().add(eul5HiNode);
		eul5HiNode.setEul5Hierarchy(this);

		return eul5HiNode;
	}

	public Eul5HiNode removeEul5HiNode(Eul5HiNode eul5HiNode) {
		getEul5HiNodes().remove(eul5HiNode);
		eul5HiNode.setEul5Hierarchy(null);

		return eul5HiNode;
	}

	public List<Eul5HiSegment> getEul5HiSegments1() {
		return this.eul5HiSegments1;
	}

	public void setEul5HiSegments1(List<Eul5HiSegment> eul5HiSegments1) {
		this.eul5HiSegments1 = eul5HiSegments1;
	}

	public Eul5HiSegment addEul5HiSegments1(Eul5HiSegment eul5HiSegments1) {
		getEul5HiSegments1().add(eul5HiSegments1);
		eul5HiSegments1.setEul5Hierarchy1(this);

		return eul5HiSegments1;
	}

	public Eul5HiSegment removeEul5HiSegments1(Eul5HiSegment eul5HiSegments1) {
		getEul5HiSegments1().remove(eul5HiSegments1);
		eul5HiSegments1.setEul5Hierarchy1(null);

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
		eul5HiSegments2.setEul5Hierarchy2(this);

		return eul5HiSegments2;
	}

	public Eul5HiSegment removeEul5HiSegments2(Eul5HiSegment eul5HiSegments2) {
		getEul5HiSegments2().remove(eul5HiSegments2);
		eul5HiSegments2.setEul5Hierarchy2(null);

		return eul5HiSegments2;
	}

}