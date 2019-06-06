package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_BAS database table.
 * 
 */
@Entity
@Table(name="EUL5_BAS")
@NamedQuery(name="Eul5Ba.findAll", query="SELECT e FROM Eul5Ba e")
public class Eul5Ba implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BA_ID", unique=true, nullable=false, precision=10)
	private long baId;

	@Column(name="BA_CREATED_BY", nullable=false, length=64)
	private String baCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BA_CREATED_DATE", nullable=false)
	private Date baCreatedDate;

	@Column(name="BA_DESCRIPTION", length=240)
	private String baDescription;

	@Column(name="BA_DEVELOPER_KEY", nullable=false, length=100)
	private String baDeveloperKey;

	@Column(name="BA_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal baElementState;

	@Column(name="BA_EXT_NAME", length=64)
	private String baExtName;

	@Column(name="BA_NAME", nullable=false, length=100)
	private String baName;

	@Column(name="BA_UPDATED_BY", length=64)
	private String baUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BA_UPDATED_DATE")
	private Date baUpdatedDate;

	@Column(name="BA_USER_PROP1", length=100)
	private String baUserProp1;

	@Column(name="BA_USER_PROP2", length=100)
	private String baUserProp2;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5AccessPriv
	@OneToMany(mappedBy="eul5Ba")
	private List<Eul5AccessPriv> eul5AccessPrivs;

	//bi-directional many-to-one association to Eul5BaObjLink
	@OneToMany(mappedBy="eul5Ba")
	private List<Eul5BaObjLink> eul5BaObjLinks;

	//bi-directional many-to-one association to Eul5Obj
	@OneToMany(mappedBy="eul5Ba")
	private List<Eul5Obj> eul5Objs;

	public Eul5Ba() {
	}

	public long getBaId() {
		return this.baId;
	}

	public void setBaId(long baId) {
		this.baId = baId;
	}

	public String getBaCreatedBy() {
		return this.baCreatedBy;
	}

	public void setBaCreatedBy(String baCreatedBy) {
		this.baCreatedBy = baCreatedBy;
	}

	public Date getBaCreatedDate() {
		return this.baCreatedDate;
	}

	public void setBaCreatedDate(Date baCreatedDate) {
		this.baCreatedDate = baCreatedDate;
	}

	public String getBaDescription() {
		return this.baDescription;
	}

	public void setBaDescription(String baDescription) {
		this.baDescription = baDescription;
	}

	public String getBaDeveloperKey() {
		return this.baDeveloperKey;
	}

	public void setBaDeveloperKey(String baDeveloperKey) {
		this.baDeveloperKey = baDeveloperKey;
	}

	public BigDecimal getBaElementState() {
		return this.baElementState;
	}

	public void setBaElementState(BigDecimal baElementState) {
		this.baElementState = baElementState;
	}

	public String getBaExtName() {
		return this.baExtName;
	}

	public void setBaExtName(String baExtName) {
		this.baExtName = baExtName;
	}

	public String getBaName() {
		return this.baName;
	}

	public void setBaName(String baName) {
		this.baName = baName;
	}

	public String getBaUpdatedBy() {
		return this.baUpdatedBy;
	}

	public void setBaUpdatedBy(String baUpdatedBy) {
		this.baUpdatedBy = baUpdatedBy;
	}

	public Date getBaUpdatedDate() {
		return this.baUpdatedDate;
	}

	public void setBaUpdatedDate(Date baUpdatedDate) {
		this.baUpdatedDate = baUpdatedDate;
	}

	public String getBaUserProp1() {
		return this.baUserProp1;
	}

	public void setBaUserProp1(String baUserProp1) {
		this.baUserProp1 = baUserProp1;
	}

	public String getBaUserProp2() {
		return this.baUserProp2;
	}

	public void setBaUserProp2(String baUserProp2) {
		this.baUserProp2 = baUserProp2;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public List<Eul5AccessPriv> getEul5AccessPrivs() {
		return this.eul5AccessPrivs;
	}

	public void setEul5AccessPrivs(List<Eul5AccessPriv> eul5AccessPrivs) {
		this.eul5AccessPrivs = eul5AccessPrivs;
	}

	public Eul5AccessPriv addEul5AccessPriv(Eul5AccessPriv eul5AccessPriv) {
		getEul5AccessPrivs().add(eul5AccessPriv);
		eul5AccessPriv.setEul5Ba(this);

		return eul5AccessPriv;
	}

	public Eul5AccessPriv removeEul5AccessPriv(Eul5AccessPriv eul5AccessPriv) {
		getEul5AccessPrivs().remove(eul5AccessPriv);
		eul5AccessPriv.setEul5Ba(null);

		return eul5AccessPriv;
	}

	public List<Eul5BaObjLink> getEul5BaObjLinks() {
		return this.eul5BaObjLinks;
	}

	public void setEul5BaObjLinks(List<Eul5BaObjLink> eul5BaObjLinks) {
		this.eul5BaObjLinks = eul5BaObjLinks;
	}

	public Eul5BaObjLink addEul5BaObjLink(Eul5BaObjLink eul5BaObjLink) {
		getEul5BaObjLinks().add(eul5BaObjLink);
		eul5BaObjLink.setEul5Ba(this);

		return eul5BaObjLink;
	}

	public Eul5BaObjLink removeEul5BaObjLink(Eul5BaObjLink eul5BaObjLink) {
		getEul5BaObjLinks().remove(eul5BaObjLink);
		eul5BaObjLink.setEul5Ba(null);

		return eul5BaObjLink;
	}

	public List<Eul5Obj> getEul5Objs() {
		return this.eul5Objs;
	}

	public void setEul5Objs(List<Eul5Obj> eul5Objs) {
		this.eul5Objs = eul5Objs;
	}

	public Eul5Obj addEul5Obj(Eul5Obj eul5Obj) {
		getEul5Objs().add(eul5Obj);
		eul5Obj.setEul5Ba(this);

		return eul5Obj;
	}

	public Eul5Obj removeEul5Obj(Eul5Obj eul5Obj) {
		getEul5Objs().remove(eul5Obj);
		eul5Obj.setEul5Ba(null);

		return eul5Obj;
	}

}