package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_FUN_CTGS database table.
 * 
 */
@Entity
@Table(name="EUL5_FUN_CTGS")
@NamedQuery(name="Eul5FunCtg.findAll", query="SELECT e FROM Eul5FunCtg e")
public class Eul5FunCtg implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FC_ID", unique=true, nullable=false, precision=10)
	private long fcId;

	@Column(name="FC_CREATED_BY", nullable=false, length=64)
	private String fcCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="FC_CREATED_DATE", nullable=false)
	private Date fcCreatedDate;

	@Column(name="FC_DESCRIPTION_MN", precision=10)
	private BigDecimal fcDescriptionMn;

	@Column(name="FC_DESCRIPTION_S", length=240)
	private String fcDescriptionS;

	@Column(name="FC_DEVELOPER_KEY", nullable=false, length=100)
	private String fcDeveloperKey;

	@Column(name="FC_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal fcElementState;

	@Column(name="FC_NAME_MN", precision=10)
	private BigDecimal fcNameMn;

	@Column(name="FC_NAME_S", length=100)
	private String fcNameS;

	@Column(name="FC_UPDATED_BY", length=64)
	private String fcUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="FC_UPDATED_DATE")
	private Date fcUpdatedDate;

	@Column(name="FC_USER_PROP1", length=100)
	private String fcUserProp1;

	@Column(name="FC_USER_PROP2", length=100)
	private String fcUserProp2;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5FunFcLink
	@OneToMany(mappedBy="eul5FunCtg")
	private List<Eul5FunFcLink> eul5FunFcLinks;

	public Eul5FunCtg() {
	}

	public long getFcId() {
		return this.fcId;
	}

	public void setFcId(long fcId) {
		this.fcId = fcId;
	}

	public String getFcCreatedBy() {
		return this.fcCreatedBy;
	}

	public void setFcCreatedBy(String fcCreatedBy) {
		this.fcCreatedBy = fcCreatedBy;
	}

	public Date getFcCreatedDate() {
		return this.fcCreatedDate;
	}

	public void setFcCreatedDate(Date fcCreatedDate) {
		this.fcCreatedDate = fcCreatedDate;
	}

	public BigDecimal getFcDescriptionMn() {
		return this.fcDescriptionMn;
	}

	public void setFcDescriptionMn(BigDecimal fcDescriptionMn) {
		this.fcDescriptionMn = fcDescriptionMn;
	}

	public String getFcDescriptionS() {
		return this.fcDescriptionS;
	}

	public void setFcDescriptionS(String fcDescriptionS) {
		this.fcDescriptionS = fcDescriptionS;
	}

	public String getFcDeveloperKey() {
		return this.fcDeveloperKey;
	}

	public void setFcDeveloperKey(String fcDeveloperKey) {
		this.fcDeveloperKey = fcDeveloperKey;
	}

	public BigDecimal getFcElementState() {
		return this.fcElementState;
	}

	public void setFcElementState(BigDecimal fcElementState) {
		this.fcElementState = fcElementState;
	}

	public BigDecimal getFcNameMn() {
		return this.fcNameMn;
	}

	public void setFcNameMn(BigDecimal fcNameMn) {
		this.fcNameMn = fcNameMn;
	}

	public String getFcNameS() {
		return this.fcNameS;
	}

	public void setFcNameS(String fcNameS) {
		this.fcNameS = fcNameS;
	}

	public String getFcUpdatedBy() {
		return this.fcUpdatedBy;
	}

	public void setFcUpdatedBy(String fcUpdatedBy) {
		this.fcUpdatedBy = fcUpdatedBy;
	}

	public Date getFcUpdatedDate() {
		return this.fcUpdatedDate;
	}

	public void setFcUpdatedDate(Date fcUpdatedDate) {
		this.fcUpdatedDate = fcUpdatedDate;
	}

	public String getFcUserProp1() {
		return this.fcUserProp1;
	}

	public void setFcUserProp1(String fcUserProp1) {
		this.fcUserProp1 = fcUserProp1;
	}

	public String getFcUserProp2() {
		return this.fcUserProp2;
	}

	public void setFcUserProp2(String fcUserProp2) {
		this.fcUserProp2 = fcUserProp2;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public List<Eul5FunFcLink> getEul5FunFcLinks() {
		return this.eul5FunFcLinks;
	}

	public void setEul5FunFcLinks(List<Eul5FunFcLink> eul5FunFcLinks) {
		this.eul5FunFcLinks = eul5FunFcLinks;
	}

	public Eul5FunFcLink addEul5FunFcLink(Eul5FunFcLink eul5FunFcLink) {
		getEul5FunFcLinks().add(eul5FunFcLink);
		eul5FunFcLink.setEul5FunCtg(this);

		return eul5FunFcLink;
	}

	public Eul5FunFcLink removeEul5FunFcLink(Eul5FunFcLink eul5FunFcLink) {
		getEul5FunFcLinks().remove(eul5FunFcLink);
		eul5FunFcLink.setEul5FunCtg(null);

		return eul5FunFcLink;
	}

}