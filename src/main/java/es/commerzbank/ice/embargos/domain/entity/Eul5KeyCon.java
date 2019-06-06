package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_KEY_CONS database table.
 * 
 */
@Entity
@Table(name="EUL5_KEY_CONS")
@NamedQuery(name="Eul5KeyCon.findAll", query="SELECT e FROM Eul5KeyCon e")
public class Eul5KeyCon implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="KEY_ID", unique=true, nullable=false, precision=10)
	private long keyId;

	@Column(name="FK_DTL_NO_MASTER", precision=1)
	private BigDecimal fkDtlNoMaster;

	@Column(name="FK_MANDATORY", precision=1)
	private BigDecimal fkMandatory;

	@Column(name="FK_MSTR_NO_DETAIL", precision=1)
	private BigDecimal fkMstrNoDetail;

	@Column(name="FK_ONE_TO_ONE", precision=1)
	private BigDecimal fkOneToOne;

	@Column(name="KEY_CREATED_BY", nullable=false, length=64)
	private String keyCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="KEY_CREATED_DATE", nullable=false)
	private Date keyCreatedDate;

	@Column(name="KEY_DESCRIPTION", length=240)
	private String keyDescription;

	@Column(name="KEY_DEVELOPER_KEY", nullable=false, length=100)
	private String keyDeveloperKey;

	@Column(name="KEY_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal keyElementState;

	@Column(name="KEY_EXT_KEY", length=64)
	private String keyExtKey;

	@Column(name="KEY_NAME", nullable=false, length=100)
	private String keyName;

	@Column(name="KEY_TYPE", nullable=false, length=10)
	private String keyType;

	@Column(name="KEY_UPDATED_BY", length=64)
	private String keyUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="KEY_UPDATED_DATE")
	private Date keyUpdatedDate;

	@Column(name="KEY_USER_PROP1", length=100)
	private String keyUserProp1;

	@Column(name="KEY_USER_PROP2", length=100)
	private String keyUserProp2;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="UK_PRIMARY", precision=1)
	private BigDecimal ukPrimary;

	//bi-directional many-to-one association to Eul5Expression
	@OneToMany(mappedBy="eul5KeyCon")
	private List<Eul5Expression> eul5Expressions;

	//bi-directional many-to-one association to Eul5IgExpLink
	@OneToMany(mappedBy="eul5KeyCon")
	private List<Eul5IgExpLink> eul5IgExpLinks;

	//bi-directional many-to-one association to Eul5IhsFkLink
	@OneToMany(mappedBy="eul5KeyCon")
	private List<Eul5IhsFkLink> eul5IhsFkLinks;

	//bi-directional many-to-one association to Eul5KeyCon
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_KEY_ID_REMOTE")
	private Eul5KeyCon eul5KeyCon;

	//bi-directional many-to-one association to Eul5KeyCon
	@OneToMany(mappedBy="eul5KeyCon")
	private List<Eul5KeyCon> eul5KeyCons;

	//bi-directional many-to-one association to Eul5Obj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_OBJ_ID_REMOTE")
	private Eul5Obj eul5Obj1;

	//bi-directional many-to-one association to Eul5Obj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="KEY_OBJ_ID", nullable=false)
	private Eul5Obj eul5Obj2;

	//bi-directional many-to-one association to Eul5ObjJoinUsg
	@OneToMany(mappedBy="eul5KeyCon")
	private List<Eul5ObjJoinUsg> eul5ObjJoinUsgs;

	//bi-directional many-to-one association to Eul5SumBitmap
	@OneToMany(mappedBy="eul5KeyCon")
	private List<Eul5SumBitmap> eul5SumBitmaps;

	public Eul5KeyCon() {
	}

	public long getKeyId() {
		return this.keyId;
	}

	public void setKeyId(long keyId) {
		this.keyId = keyId;
	}

	public BigDecimal getFkDtlNoMaster() {
		return this.fkDtlNoMaster;
	}

	public void setFkDtlNoMaster(BigDecimal fkDtlNoMaster) {
		this.fkDtlNoMaster = fkDtlNoMaster;
	}

	public BigDecimal getFkMandatory() {
		return this.fkMandatory;
	}

	public void setFkMandatory(BigDecimal fkMandatory) {
		this.fkMandatory = fkMandatory;
	}

	public BigDecimal getFkMstrNoDetail() {
		return this.fkMstrNoDetail;
	}

	public void setFkMstrNoDetail(BigDecimal fkMstrNoDetail) {
		this.fkMstrNoDetail = fkMstrNoDetail;
	}

	public BigDecimal getFkOneToOne() {
		return this.fkOneToOne;
	}

	public void setFkOneToOne(BigDecimal fkOneToOne) {
		this.fkOneToOne = fkOneToOne;
	}

	public String getKeyCreatedBy() {
		return this.keyCreatedBy;
	}

	public void setKeyCreatedBy(String keyCreatedBy) {
		this.keyCreatedBy = keyCreatedBy;
	}

	public Date getKeyCreatedDate() {
		return this.keyCreatedDate;
	}

	public void setKeyCreatedDate(Date keyCreatedDate) {
		this.keyCreatedDate = keyCreatedDate;
	}

	public String getKeyDescription() {
		return this.keyDescription;
	}

	public void setKeyDescription(String keyDescription) {
		this.keyDescription = keyDescription;
	}

	public String getKeyDeveloperKey() {
		return this.keyDeveloperKey;
	}

	public void setKeyDeveloperKey(String keyDeveloperKey) {
		this.keyDeveloperKey = keyDeveloperKey;
	}

	public BigDecimal getKeyElementState() {
		return this.keyElementState;
	}

	public void setKeyElementState(BigDecimal keyElementState) {
		this.keyElementState = keyElementState;
	}

	public String getKeyExtKey() {
		return this.keyExtKey;
	}

	public void setKeyExtKey(String keyExtKey) {
		this.keyExtKey = keyExtKey;
	}

	public String getKeyName() {
		return this.keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyType() {
		return this.keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getKeyUpdatedBy() {
		return this.keyUpdatedBy;
	}

	public void setKeyUpdatedBy(String keyUpdatedBy) {
		this.keyUpdatedBy = keyUpdatedBy;
	}

	public Date getKeyUpdatedDate() {
		return this.keyUpdatedDate;
	}

	public void setKeyUpdatedDate(Date keyUpdatedDate) {
		this.keyUpdatedDate = keyUpdatedDate;
	}

	public String getKeyUserProp1() {
		return this.keyUserProp1;
	}

	public void setKeyUserProp1(String keyUserProp1) {
		this.keyUserProp1 = keyUserProp1;
	}

	public String getKeyUserProp2() {
		return this.keyUserProp2;
	}

	public void setKeyUserProp2(String keyUserProp2) {
		this.keyUserProp2 = keyUserProp2;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public BigDecimal getUkPrimary() {
		return this.ukPrimary;
	}

	public void setUkPrimary(BigDecimal ukPrimary) {
		this.ukPrimary = ukPrimary;
	}

	public List<Eul5Expression> getEul5Expressions() {
		return this.eul5Expressions;
	}

	public void setEul5Expressions(List<Eul5Expression> eul5Expressions) {
		this.eul5Expressions = eul5Expressions;
	}

	public Eul5Expression addEul5Expression(Eul5Expression eul5Expression) {
		getEul5Expressions().add(eul5Expression);
		eul5Expression.setEul5KeyCon(this);

		return eul5Expression;
	}

	public Eul5Expression removeEul5Expression(Eul5Expression eul5Expression) {
		getEul5Expressions().remove(eul5Expression);
		eul5Expression.setEul5KeyCon(null);

		return eul5Expression;
	}

	public List<Eul5IgExpLink> getEul5IgExpLinks() {
		return this.eul5IgExpLinks;
	}

	public void setEul5IgExpLinks(List<Eul5IgExpLink> eul5IgExpLinks) {
		this.eul5IgExpLinks = eul5IgExpLinks;
	}

	public Eul5IgExpLink addEul5IgExpLink(Eul5IgExpLink eul5IgExpLink) {
		getEul5IgExpLinks().add(eul5IgExpLink);
		eul5IgExpLink.setEul5KeyCon(this);

		return eul5IgExpLink;
	}

	public Eul5IgExpLink removeEul5IgExpLink(Eul5IgExpLink eul5IgExpLink) {
		getEul5IgExpLinks().remove(eul5IgExpLink);
		eul5IgExpLink.setEul5KeyCon(null);

		return eul5IgExpLink;
	}

	public List<Eul5IhsFkLink> getEul5IhsFkLinks() {
		return this.eul5IhsFkLinks;
	}

	public void setEul5IhsFkLinks(List<Eul5IhsFkLink> eul5IhsFkLinks) {
		this.eul5IhsFkLinks = eul5IhsFkLinks;
	}

	public Eul5IhsFkLink addEul5IhsFkLink(Eul5IhsFkLink eul5IhsFkLink) {
		getEul5IhsFkLinks().add(eul5IhsFkLink);
		eul5IhsFkLink.setEul5KeyCon(this);

		return eul5IhsFkLink;
	}

	public Eul5IhsFkLink removeEul5IhsFkLink(Eul5IhsFkLink eul5IhsFkLink) {
		getEul5IhsFkLinks().remove(eul5IhsFkLink);
		eul5IhsFkLink.setEul5KeyCon(null);

		return eul5IhsFkLink;
	}

	public Eul5KeyCon getEul5KeyCon() {
		return this.eul5KeyCon;
	}

	public void setEul5KeyCon(Eul5KeyCon eul5KeyCon) {
		this.eul5KeyCon = eul5KeyCon;
	}

	public List<Eul5KeyCon> getEul5KeyCons() {
		return this.eul5KeyCons;
	}

	public void setEul5KeyCons(List<Eul5KeyCon> eul5KeyCons) {
		this.eul5KeyCons = eul5KeyCons;
	}

	public Eul5KeyCon addEul5KeyCon(Eul5KeyCon eul5KeyCon) {
		getEul5KeyCons().add(eul5KeyCon);
		eul5KeyCon.setEul5KeyCon(this);

		return eul5KeyCon;
	}

	public Eul5KeyCon removeEul5KeyCon(Eul5KeyCon eul5KeyCon) {
		getEul5KeyCons().remove(eul5KeyCon);
		eul5KeyCon.setEul5KeyCon(null);

		return eul5KeyCon;
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

	public List<Eul5ObjJoinUsg> getEul5ObjJoinUsgs() {
		return this.eul5ObjJoinUsgs;
	}

	public void setEul5ObjJoinUsgs(List<Eul5ObjJoinUsg> eul5ObjJoinUsgs) {
		this.eul5ObjJoinUsgs = eul5ObjJoinUsgs;
	}

	public Eul5ObjJoinUsg addEul5ObjJoinUsg(Eul5ObjJoinUsg eul5ObjJoinUsg) {
		getEul5ObjJoinUsgs().add(eul5ObjJoinUsg);
		eul5ObjJoinUsg.setEul5KeyCon(this);

		return eul5ObjJoinUsg;
	}

	public Eul5ObjJoinUsg removeEul5ObjJoinUsg(Eul5ObjJoinUsg eul5ObjJoinUsg) {
		getEul5ObjJoinUsgs().remove(eul5ObjJoinUsg);
		eul5ObjJoinUsg.setEul5KeyCon(null);

		return eul5ObjJoinUsg;
	}

	public List<Eul5SumBitmap> getEul5SumBitmaps() {
		return this.eul5SumBitmaps;
	}

	public void setEul5SumBitmaps(List<Eul5SumBitmap> eul5SumBitmaps) {
		this.eul5SumBitmaps = eul5SumBitmaps;
	}

	public Eul5SumBitmap addEul5SumBitmap(Eul5SumBitmap eul5SumBitmap) {
		getEul5SumBitmaps().add(eul5SumBitmap);
		eul5SumBitmap.setEul5KeyCon(this);

		return eul5SumBitmap;
	}

	public Eul5SumBitmap removeEul5SumBitmap(Eul5SumBitmap eul5SumBitmap) {
		getEul5SumBitmaps().remove(eul5SumBitmap);
		eul5SumBitmap.setEul5KeyCon(null);

		return eul5SumBitmap;
	}

}