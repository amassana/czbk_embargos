package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_HI_SEGMENTS database table.
 * 
 */
@Entity
@Table(name="EUL5_HI_SEGMENTS")
@NamedQuery(name="Eul5HiSegment.findAll", query="SELECT e FROM Eul5HiSegment e")
public class Eul5HiSegment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="HS_ID", unique=true, nullable=false, precision=10)
	private long hsId;

	@Column(name="HS_CREATED_BY", nullable=false, length=64)
	private String hsCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="HS_CREATED_DATE", nullable=false)
	private Date hsCreatedDate;

	@Column(name="HS_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal hsElementState;

	@Column(name="HS_TYPE", nullable=false, length=10)
	private String hsType;

	@Column(name="HS_UPDATED_BY", length=64)
	private String hsUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="HS_UPDATED_DATE")
	private Date hsUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5DbhNode
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DHS_DHN_ID_PARENT")
	private Eul5DbhNode eul5DbhNode1;

	//bi-directional many-to-one association to Eul5DbhNode
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DHS_DHN_ID_CHILD")
	private Eul5DbhNode eul5DbhNode2;

	//bi-directional many-to-one association to Eul5Hierarchy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IHS_HI_ID")
	private Eul5Hierarchy eul5Hierarchy1;

	//bi-directional many-to-one association to Eul5Hierarchy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DHS_HI_ID")
	private Eul5Hierarchy eul5Hierarchy2;

	//bi-directional many-to-one association to Eul5HiNode
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IHS_HN_ID_CHILD")
	private Eul5HiNode eul5HiNode1;

	//bi-directional many-to-one association to Eul5HiNode
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IHS_HN_ID_PARENT")
	private Eul5HiNode eul5HiNode2;

	//bi-directional many-to-one association to Eul5IhsFkLink
	@OneToMany(mappedBy="eul5HiSegment")
	private List<Eul5IhsFkLink> eul5IhsFkLinks;

	public Eul5HiSegment() {
	}

	public long getHsId() {
		return this.hsId;
	}

	public void setHsId(long hsId) {
		this.hsId = hsId;
	}

	public String getHsCreatedBy() {
		return this.hsCreatedBy;
	}

	public void setHsCreatedBy(String hsCreatedBy) {
		this.hsCreatedBy = hsCreatedBy;
	}

	public Date getHsCreatedDate() {
		return this.hsCreatedDate;
	}

	public void setHsCreatedDate(Date hsCreatedDate) {
		this.hsCreatedDate = hsCreatedDate;
	}

	public BigDecimal getHsElementState() {
		return this.hsElementState;
	}

	public void setHsElementState(BigDecimal hsElementState) {
		this.hsElementState = hsElementState;
	}

	public String getHsType() {
		return this.hsType;
	}

	public void setHsType(String hsType) {
		this.hsType = hsType;
	}

	public String getHsUpdatedBy() {
		return this.hsUpdatedBy;
	}

	public void setHsUpdatedBy(String hsUpdatedBy) {
		this.hsUpdatedBy = hsUpdatedBy;
	}

	public Date getHsUpdatedDate() {
		return this.hsUpdatedDate;
	}

	public void setHsUpdatedDate(Date hsUpdatedDate) {
		this.hsUpdatedDate = hsUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5DbhNode getEul5DbhNode1() {
		return this.eul5DbhNode1;
	}

	public void setEul5DbhNode1(Eul5DbhNode eul5DbhNode1) {
		this.eul5DbhNode1 = eul5DbhNode1;
	}

	public Eul5DbhNode getEul5DbhNode2() {
		return this.eul5DbhNode2;
	}

	public void setEul5DbhNode2(Eul5DbhNode eul5DbhNode2) {
		this.eul5DbhNode2 = eul5DbhNode2;
	}

	public Eul5Hierarchy getEul5Hierarchy1() {
		return this.eul5Hierarchy1;
	}

	public void setEul5Hierarchy1(Eul5Hierarchy eul5Hierarchy1) {
		this.eul5Hierarchy1 = eul5Hierarchy1;
	}

	public Eul5Hierarchy getEul5Hierarchy2() {
		return this.eul5Hierarchy2;
	}

	public void setEul5Hierarchy2(Eul5Hierarchy eul5Hierarchy2) {
		this.eul5Hierarchy2 = eul5Hierarchy2;
	}

	public Eul5HiNode getEul5HiNode1() {
		return this.eul5HiNode1;
	}

	public void setEul5HiNode1(Eul5HiNode eul5HiNode1) {
		this.eul5HiNode1 = eul5HiNode1;
	}

	public Eul5HiNode getEul5HiNode2() {
		return this.eul5HiNode2;
	}

	public void setEul5HiNode2(Eul5HiNode eul5HiNode2) {
		this.eul5HiNode2 = eul5HiNode2;
	}

	public List<Eul5IhsFkLink> getEul5IhsFkLinks() {
		return this.eul5IhsFkLinks;
	}

	public void setEul5IhsFkLinks(List<Eul5IhsFkLink> eul5IhsFkLinks) {
		this.eul5IhsFkLinks = eul5IhsFkLinks;
	}

	public Eul5IhsFkLink addEul5IhsFkLink(Eul5IhsFkLink eul5IhsFkLink) {
		getEul5IhsFkLinks().add(eul5IhsFkLink);
		eul5IhsFkLink.setEul5HiSegment(this);

		return eul5IhsFkLink;
	}

	public Eul5IhsFkLink removeEul5IhsFkLink(Eul5IhsFkLink eul5IhsFkLink) {
		getEul5IhsFkLinks().remove(eul5IhsFkLink);
		eul5IhsFkLink.setEul5HiSegment(null);

		return eul5IhsFkLink;
	}

}