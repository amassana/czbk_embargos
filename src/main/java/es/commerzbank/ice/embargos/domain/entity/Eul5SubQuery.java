package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_SUB_QUERIES database table.
 * 
 */
@Entity
@Table(name="EUL5_SUB_QUERIES")
@NamedQuery(name="Eul5SubQuery.findAll", query="SELECT e FROM Eul5SubQuery e")
public class Eul5SubQuery implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SQ_ID", unique=true, nullable=false, precision=10)
	private long sqId;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="SQ_CREATED_BY", nullable=false, length=64)
	private String sqCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SQ_CREATED_DATE", nullable=false)
	private Date sqCreatedDate;

	@Column(name="SQ_DESCRIPTION", length=240)
	private String sqDescription;

	@Column(name="SQ_DEVELOPER_KEY", nullable=false, length=100)
	private String sqDeveloperKey;

	@Column(name="SQ_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal sqElementState;

	@Column(name="SQ_NAME", nullable=false, length=100)
	private String sqName;

	@Column(name="SQ_UPDATED_BY", length=64)
	private String sqUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SQ_UPDATED_DATE")
	private Date sqUpdatedDate;

	@Column(name="SQ_USER_PROP1", length=100)
	private String sqUserProp1;

	@Column(name="SQ_USER_PROP2", length=100)
	private String sqUserProp2;

	//bi-directional many-to-one association to Eul5ExpDep
	@OneToMany(mappedBy="eul5SubQuery")
	private List<Eul5ExpDep> eul5ExpDeps;

	//bi-directional many-to-one association to Eul5SqCrrltn
	@OneToMany(mappedBy="eul5SubQuery")
	private List<Eul5SqCrrltn> eul5SqCrrltns;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SQ_IT_ID", nullable=false)
	private Eul5Expression eul5Expression1;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SQ_FIL_ID", nullable=false)
	private Eul5Expression eul5Expression2;

	//bi-directional many-to-one association to Eul5Obj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SQ_OBJ_ID", nullable=false)
	private Eul5Obj eul5Obj;

	public Eul5SubQuery() {
	}

	public long getSqId() {
		return this.sqId;
	}

	public void setSqId(long sqId) {
		this.sqId = sqId;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public String getSqCreatedBy() {
		return this.sqCreatedBy;
	}

	public void setSqCreatedBy(String sqCreatedBy) {
		this.sqCreatedBy = sqCreatedBy;
	}

	public Date getSqCreatedDate() {
		return this.sqCreatedDate;
	}

	public void setSqCreatedDate(Date sqCreatedDate) {
		this.sqCreatedDate = sqCreatedDate;
	}

	public String getSqDescription() {
		return this.sqDescription;
	}

	public void setSqDescription(String sqDescription) {
		this.sqDescription = sqDescription;
	}

	public String getSqDeveloperKey() {
		return this.sqDeveloperKey;
	}

	public void setSqDeveloperKey(String sqDeveloperKey) {
		this.sqDeveloperKey = sqDeveloperKey;
	}

	public BigDecimal getSqElementState() {
		return this.sqElementState;
	}

	public void setSqElementState(BigDecimal sqElementState) {
		this.sqElementState = sqElementState;
	}

	public String getSqName() {
		return this.sqName;
	}

	public void setSqName(String sqName) {
		this.sqName = sqName;
	}

	public String getSqUpdatedBy() {
		return this.sqUpdatedBy;
	}

	public void setSqUpdatedBy(String sqUpdatedBy) {
		this.sqUpdatedBy = sqUpdatedBy;
	}

	public Date getSqUpdatedDate() {
		return this.sqUpdatedDate;
	}

	public void setSqUpdatedDate(Date sqUpdatedDate) {
		this.sqUpdatedDate = sqUpdatedDate;
	}

	public String getSqUserProp1() {
		return this.sqUserProp1;
	}

	public void setSqUserProp1(String sqUserProp1) {
		this.sqUserProp1 = sqUserProp1;
	}

	public String getSqUserProp2() {
		return this.sqUserProp2;
	}

	public void setSqUserProp2(String sqUserProp2) {
		this.sqUserProp2 = sqUserProp2;
	}

	public List<Eul5ExpDep> getEul5ExpDeps() {
		return this.eul5ExpDeps;
	}

	public void setEul5ExpDeps(List<Eul5ExpDep> eul5ExpDeps) {
		this.eul5ExpDeps = eul5ExpDeps;
	}

	public Eul5ExpDep addEul5ExpDep(Eul5ExpDep eul5ExpDep) {
		getEul5ExpDeps().add(eul5ExpDep);
		eul5ExpDep.setEul5SubQuery(this);

		return eul5ExpDep;
	}

	public Eul5ExpDep removeEul5ExpDep(Eul5ExpDep eul5ExpDep) {
		getEul5ExpDeps().remove(eul5ExpDep);
		eul5ExpDep.setEul5SubQuery(null);

		return eul5ExpDep;
	}

	public List<Eul5SqCrrltn> getEul5SqCrrltns() {
		return this.eul5SqCrrltns;
	}

	public void setEul5SqCrrltns(List<Eul5SqCrrltn> eul5SqCrrltns) {
		this.eul5SqCrrltns = eul5SqCrrltns;
	}

	public Eul5SqCrrltn addEul5SqCrrltn(Eul5SqCrrltn eul5SqCrrltn) {
		getEul5SqCrrltns().add(eul5SqCrrltn);
		eul5SqCrrltn.setEul5SubQuery(this);

		return eul5SqCrrltn;
	}

	public Eul5SqCrrltn removeEul5SqCrrltn(Eul5SqCrrltn eul5SqCrrltn) {
		getEul5SqCrrltns().remove(eul5SqCrrltn);
		eul5SqCrrltn.setEul5SubQuery(null);

		return eul5SqCrrltn;
	}

	public Eul5Expression getEul5Expression1() {
		return this.eul5Expression1;
	}

	public void setEul5Expression1(Eul5Expression eul5Expression1) {
		this.eul5Expression1 = eul5Expression1;
	}

	public Eul5Expression getEul5Expression2() {
		return this.eul5Expression2;
	}

	public void setEul5Expression2(Eul5Expression eul5Expression2) {
		this.eul5Expression2 = eul5Expression2;
	}

	public Eul5Obj getEul5Obj() {
		return this.eul5Obj;
	}

	public void setEul5Obj(Eul5Obj eul5Obj) {
		this.eul5Obj = eul5Obj;
	}

}