package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_HI_NODES database table.
 * 
 */
@Entity
@Table(name="EUL5_HI_NODES")
@NamedQuery(name="Eul5HiNode.findAll", query="SELECT e FROM Eul5HiNode e")
public class Eul5HiNode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="HN_ID", unique=true, nullable=false, precision=10)
	private long hnId;

	@Column(name="HN_CREATED_BY", nullable=false, length=64)
	private String hnCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="HN_CREATED_DATE", nullable=false)
	private Date hnCreatedDate;

	@Column(name="HN_DESCRIPTION", length=240)
	private String hnDescription;

	@Column(name="HN_DEVELOPER_KEY", nullable=false, length=100)
	private String hnDeveloperKey;

	@Column(name="HN_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal hnElementState;

	@Column(name="HN_EXT_NODE", length=64)
	private String hnExtNode;

	@Column(name="HN_NAME", nullable=false, length=100)
	private String hnName;

	@Column(name="HN_UPDATED_BY", length=64)
	private String hnUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="HN_UPDATED_DATE")
	private Date hnUpdatedDate;

	@Column(name="HN_USER_PROP1", length=100)
	private String hnUserProp1;

	@Column(name="HN_USER_PROP2", length=100)
	private String hnUserProp2;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5Hierarchy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HN_HI_ID", nullable=false)
	private Eul5Hierarchy eul5Hierarchy;

	//bi-directional many-to-one association to Eul5HiSegment
	@OneToMany(mappedBy="eul5HiNode1")
	private List<Eul5HiSegment> eul5HiSegments1;

	//bi-directional many-to-one association to Eul5HiSegment
	@OneToMany(mappedBy="eul5HiNode2")
	private List<Eul5HiSegment> eul5HiSegments2;

	//bi-directional many-to-one association to Eul5IgExpLink
	@OneToMany(mappedBy="eul5HiNode")
	private List<Eul5IgExpLink> eul5IgExpLinks;

	public Eul5HiNode() {
	}

	public long getHnId() {
		return this.hnId;
	}

	public void setHnId(long hnId) {
		this.hnId = hnId;
	}

	public String getHnCreatedBy() {
		return this.hnCreatedBy;
	}

	public void setHnCreatedBy(String hnCreatedBy) {
		this.hnCreatedBy = hnCreatedBy;
	}

	public Date getHnCreatedDate() {
		return this.hnCreatedDate;
	}

	public void setHnCreatedDate(Date hnCreatedDate) {
		this.hnCreatedDate = hnCreatedDate;
	}

	public String getHnDescription() {
		return this.hnDescription;
	}

	public void setHnDescription(String hnDescription) {
		this.hnDescription = hnDescription;
	}

	public String getHnDeveloperKey() {
		return this.hnDeveloperKey;
	}

	public void setHnDeveloperKey(String hnDeveloperKey) {
		this.hnDeveloperKey = hnDeveloperKey;
	}

	public BigDecimal getHnElementState() {
		return this.hnElementState;
	}

	public void setHnElementState(BigDecimal hnElementState) {
		this.hnElementState = hnElementState;
	}

	public String getHnExtNode() {
		return this.hnExtNode;
	}

	public void setHnExtNode(String hnExtNode) {
		this.hnExtNode = hnExtNode;
	}

	public String getHnName() {
		return this.hnName;
	}

	public void setHnName(String hnName) {
		this.hnName = hnName;
	}

	public String getHnUpdatedBy() {
		return this.hnUpdatedBy;
	}

	public void setHnUpdatedBy(String hnUpdatedBy) {
		this.hnUpdatedBy = hnUpdatedBy;
	}

	public Date getHnUpdatedDate() {
		return this.hnUpdatedDate;
	}

	public void setHnUpdatedDate(Date hnUpdatedDate) {
		this.hnUpdatedDate = hnUpdatedDate;
	}

	public String getHnUserProp1() {
		return this.hnUserProp1;
	}

	public void setHnUserProp1(String hnUserProp1) {
		this.hnUserProp1 = hnUserProp1;
	}

	public String getHnUserProp2() {
		return this.hnUserProp2;
	}

	public void setHnUserProp2(String hnUserProp2) {
		this.hnUserProp2 = hnUserProp2;
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
		eul5HiSegments1.setEul5HiNode1(this);

		return eul5HiSegments1;
	}

	public Eul5HiSegment removeEul5HiSegments1(Eul5HiSegment eul5HiSegments1) {
		getEul5HiSegments1().remove(eul5HiSegments1);
		eul5HiSegments1.setEul5HiNode1(null);

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
		eul5HiSegments2.setEul5HiNode2(this);

		return eul5HiSegments2;
	}

	public Eul5HiSegment removeEul5HiSegments2(Eul5HiSegment eul5HiSegments2) {
		getEul5HiSegments2().remove(eul5HiSegments2);
		eul5HiSegments2.setEul5HiNode2(null);

		return eul5HiSegments2;
	}

	public List<Eul5IgExpLink> getEul5IgExpLinks() {
		return this.eul5IgExpLinks;
	}

	public void setEul5IgExpLinks(List<Eul5IgExpLink> eul5IgExpLinks) {
		this.eul5IgExpLinks = eul5IgExpLinks;
	}

	public Eul5IgExpLink addEul5IgExpLink(Eul5IgExpLink eul5IgExpLink) {
		getEul5IgExpLinks().add(eul5IgExpLink);
		eul5IgExpLink.setEul5HiNode(this);

		return eul5IgExpLink;
	}

	public Eul5IgExpLink removeEul5IgExpLink(Eul5IgExpLink eul5IgExpLink) {
		getEul5IgExpLinks().remove(eul5IgExpLink);
		eul5IgExpLink.setEul5HiNode(null);

		return eul5IgExpLink;
	}

}