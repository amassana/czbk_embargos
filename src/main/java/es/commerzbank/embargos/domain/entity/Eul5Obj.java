package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_OBJS database table.
 * 
 */
@Entity
@Table(name="EUL5_OBJS")
@NamedQuery(name="Eul5Obj.findAll", query="SELECT e FROM Eul5Obj e")
public class Eul5Obj implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OBJ_ID", unique=true, nullable=false, precision=10)
	private long objId;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="OBJ_CBO_HINT", length=100)
	private String objCboHint;

	@Column(name="OBJ_CREATED_BY", nullable=false, length=64)
	private String objCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="OBJ_CREATED_DATE", nullable=false)
	private Date objCreatedDate;

	@Column(name="OBJ_DESCRIPTION", length=240)
	private String objDescription;

	@Column(name="OBJ_DEVELOPER_KEY", nullable=false, length=100)
	private String objDeveloperKey;

	@Column(name="OBJ_DISTINCT_FLAG", nullable=false, precision=1)
	private BigDecimal objDistinctFlag;

	@Column(name="OBJ_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal objElementState;

	@Column(name="OBJ_EXT_DB_LINK", length=64)
	private String objExtDbLink;

	@Column(name="OBJ_EXT_OBJECT", length=64)
	private String objExtObject;

	@Column(name="OBJ_EXT_OWNER", length=64)
	private String objExtOwner;

	@Column(name="OBJ_HIDDEN", nullable=false, precision=1)
	private BigDecimal objHidden;

	@Column(name="OBJ_NAME", nullable=false, length=100)
	private String objName;

	@Column(name="OBJ_NDETERMINISTIC", nullable=false, precision=1)
	private BigDecimal objNdeterministic;

	@Column(name="OBJ_OBJECT_SQL1", length=250)
	private String objObjectSql1;

	@Column(name="OBJ_OBJECT_SQL2", length=250)
	private String objObjectSql2;

	@Column(name="OBJ_OBJECT_SQL3", length=250)
	private String objObjectSql3;

	@Column(name="OBJ_TYPE", nullable=false, length=10)
	private String objType;

	@Column(name="OBJ_UPDATED_BY", length=64)
	private String objUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="OBJ_UPDATED_DATE")
	private Date objUpdatedDate;

	@Column(name="OBJ_USER_PROP1", length=100)
	private String objUserProp1;

	@Column(name="OBJ_USER_PROP2", length=100)
	private String objUserProp2;

	@Column(name="SOBJ_EXT_TABLE", length=64)
	private String sobjExtTable;

	//bi-directional many-to-one association to Eul5AsmpCon
	@OneToMany(mappedBy="eul5Obj")
	private List<Eul5AsmpCon> eul5AsmpCons;

	//bi-directional many-to-one association to Eul5BaObjLink
	@OneToMany(mappedBy="eul5Obj")
	private List<Eul5BaObjLink> eul5BaObjLinks;

	//bi-directional many-to-one association to Eul5Expression
	@OneToMany(mappedBy="eul5Obj1")
	private List<Eul5Expression> eul5Expressions1;

	//bi-directional many-to-one association to Eul5Expression
	@OneToMany(mappedBy="eul5Obj2")
	private List<Eul5Expression> eul5Expressions2;

	//bi-directional many-to-one association to Eul5KeyCon
	@OneToMany(mappedBy="eul5Obj1")
	private List<Eul5KeyCon> eul5KeyCons1;

	//bi-directional many-to-one association to Eul5KeyCon
	@OneToMany(mappedBy="eul5Obj2")
	private List<Eul5KeyCon> eul5KeyCons2;

	//bi-directional many-to-one association to Eul5Ba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OBJ_BA_ID")
	private Eul5Ba eul5Ba;

	//bi-directional many-to-one association to Eul5ObjDep
	@OneToMany(mappedBy="eul5Obj1")
	private List<Eul5ObjDep> eul5ObjDeps1;

	//bi-directional many-to-one association to Eul5ObjDep
	@OneToMany(mappedBy="eul5Obj2")
	private List<Eul5ObjDep> eul5ObjDeps2;

	//bi-directional many-to-one association to Eul5ObjJoinUsg
	@OneToMany(mappedBy="eul5Obj")
	private List<Eul5ObjJoinUsg> eul5ObjJoinUsgs;

	//bi-directional many-to-one association to Eul5Segment
	@OneToMany(mappedBy="eul5Obj1")
	private List<Eul5Segment> eul5Segments1;

	//bi-directional many-to-one association to Eul5Segment
	@OneToMany(mappedBy="eul5Obj2")
	private List<Eul5Segment> eul5Segments2;

	//bi-directional many-to-one association to Eul5SubQuery
	@OneToMany(mappedBy="eul5Obj")
	private List<Eul5SubQuery> eul5SubQueries;

	public Eul5Obj() {
	}

	public long getObjId() {
		return this.objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public String getObjCboHint() {
		return this.objCboHint;
	}

	public void setObjCboHint(String objCboHint) {
		this.objCboHint = objCboHint;
	}

	public String getObjCreatedBy() {
		return this.objCreatedBy;
	}

	public void setObjCreatedBy(String objCreatedBy) {
		this.objCreatedBy = objCreatedBy;
	}

	public Date getObjCreatedDate() {
		return this.objCreatedDate;
	}

	public void setObjCreatedDate(Date objCreatedDate) {
		this.objCreatedDate = objCreatedDate;
	}

	public String getObjDescription() {
		return this.objDescription;
	}

	public void setObjDescription(String objDescription) {
		this.objDescription = objDescription;
	}

	public String getObjDeveloperKey() {
		return this.objDeveloperKey;
	}

	public void setObjDeveloperKey(String objDeveloperKey) {
		this.objDeveloperKey = objDeveloperKey;
	}

	public BigDecimal getObjDistinctFlag() {
		return this.objDistinctFlag;
	}

	public void setObjDistinctFlag(BigDecimal objDistinctFlag) {
		this.objDistinctFlag = objDistinctFlag;
	}

	public BigDecimal getObjElementState() {
		return this.objElementState;
	}

	public void setObjElementState(BigDecimal objElementState) {
		this.objElementState = objElementState;
	}

	public String getObjExtDbLink() {
		return this.objExtDbLink;
	}

	public void setObjExtDbLink(String objExtDbLink) {
		this.objExtDbLink = objExtDbLink;
	}

	public String getObjExtObject() {
		return this.objExtObject;
	}

	public void setObjExtObject(String objExtObject) {
		this.objExtObject = objExtObject;
	}

	public String getObjExtOwner() {
		return this.objExtOwner;
	}

	public void setObjExtOwner(String objExtOwner) {
		this.objExtOwner = objExtOwner;
	}

	public BigDecimal getObjHidden() {
		return this.objHidden;
	}

	public void setObjHidden(BigDecimal objHidden) {
		this.objHidden = objHidden;
	}

	public String getObjName() {
		return this.objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public BigDecimal getObjNdeterministic() {
		return this.objNdeterministic;
	}

	public void setObjNdeterministic(BigDecimal objNdeterministic) {
		this.objNdeterministic = objNdeterministic;
	}

	public String getObjObjectSql1() {
		return this.objObjectSql1;
	}

	public void setObjObjectSql1(String objObjectSql1) {
		this.objObjectSql1 = objObjectSql1;
	}

	public String getObjObjectSql2() {
		return this.objObjectSql2;
	}

	public void setObjObjectSql2(String objObjectSql2) {
		this.objObjectSql2 = objObjectSql2;
	}

	public String getObjObjectSql3() {
		return this.objObjectSql3;
	}

	public void setObjObjectSql3(String objObjectSql3) {
		this.objObjectSql3 = objObjectSql3;
	}

	public String getObjType() {
		return this.objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public String getObjUpdatedBy() {
		return this.objUpdatedBy;
	}

	public void setObjUpdatedBy(String objUpdatedBy) {
		this.objUpdatedBy = objUpdatedBy;
	}

	public Date getObjUpdatedDate() {
		return this.objUpdatedDate;
	}

	public void setObjUpdatedDate(Date objUpdatedDate) {
		this.objUpdatedDate = objUpdatedDate;
	}

	public String getObjUserProp1() {
		return this.objUserProp1;
	}

	public void setObjUserProp1(String objUserProp1) {
		this.objUserProp1 = objUserProp1;
	}

	public String getObjUserProp2() {
		return this.objUserProp2;
	}

	public void setObjUserProp2(String objUserProp2) {
		this.objUserProp2 = objUserProp2;
	}

	public String getSobjExtTable() {
		return this.sobjExtTable;
	}

	public void setSobjExtTable(String sobjExtTable) {
		this.sobjExtTable = sobjExtTable;
	}

	public List<Eul5AsmpCon> getEul5AsmpCons() {
		return this.eul5AsmpCons;
	}

	public void setEul5AsmpCons(List<Eul5AsmpCon> eul5AsmpCons) {
		this.eul5AsmpCons = eul5AsmpCons;
	}

	public Eul5AsmpCon addEul5AsmpCon(Eul5AsmpCon eul5AsmpCon) {
		getEul5AsmpCons().add(eul5AsmpCon);
		eul5AsmpCon.setEul5Obj(this);

		return eul5AsmpCon;
	}

	public Eul5AsmpCon removeEul5AsmpCon(Eul5AsmpCon eul5AsmpCon) {
		getEul5AsmpCons().remove(eul5AsmpCon);
		eul5AsmpCon.setEul5Obj(null);

		return eul5AsmpCon;
	}

	public List<Eul5BaObjLink> getEul5BaObjLinks() {
		return this.eul5BaObjLinks;
	}

	public void setEul5BaObjLinks(List<Eul5BaObjLink> eul5BaObjLinks) {
		this.eul5BaObjLinks = eul5BaObjLinks;
	}

	public Eul5BaObjLink addEul5BaObjLink(Eul5BaObjLink eul5BaObjLink) {
		getEul5BaObjLinks().add(eul5BaObjLink);
		eul5BaObjLink.setEul5Obj(this);

		return eul5BaObjLink;
	}

	public Eul5BaObjLink removeEul5BaObjLink(Eul5BaObjLink eul5BaObjLink) {
		getEul5BaObjLinks().remove(eul5BaObjLink);
		eul5BaObjLink.setEul5Obj(null);

		return eul5BaObjLink;
	}

	public List<Eul5Expression> getEul5Expressions1() {
		return this.eul5Expressions1;
	}

	public void setEul5Expressions1(List<Eul5Expression> eul5Expressions1) {
		this.eul5Expressions1 = eul5Expressions1;
	}

	public Eul5Expression addEul5Expressions1(Eul5Expression eul5Expressions1) {
		getEul5Expressions1().add(eul5Expressions1);
		eul5Expressions1.setEul5Obj1(this);

		return eul5Expressions1;
	}

	public Eul5Expression removeEul5Expressions1(Eul5Expression eul5Expressions1) {
		getEul5Expressions1().remove(eul5Expressions1);
		eul5Expressions1.setEul5Obj1(null);

		return eul5Expressions1;
	}

	public List<Eul5Expression> getEul5Expressions2() {
		return this.eul5Expressions2;
	}

	public void setEul5Expressions2(List<Eul5Expression> eul5Expressions2) {
		this.eul5Expressions2 = eul5Expressions2;
	}

	public Eul5Expression addEul5Expressions2(Eul5Expression eul5Expressions2) {
		getEul5Expressions2().add(eul5Expressions2);
		eul5Expressions2.setEul5Obj2(this);

		return eul5Expressions2;
	}

	public Eul5Expression removeEul5Expressions2(Eul5Expression eul5Expressions2) {
		getEul5Expressions2().remove(eul5Expressions2);
		eul5Expressions2.setEul5Obj2(null);

		return eul5Expressions2;
	}

	public List<Eul5KeyCon> getEul5KeyCons1() {
		return this.eul5KeyCons1;
	}

	public void setEul5KeyCons1(List<Eul5KeyCon> eul5KeyCons1) {
		this.eul5KeyCons1 = eul5KeyCons1;
	}

	public Eul5KeyCon addEul5KeyCons1(Eul5KeyCon eul5KeyCons1) {
		getEul5KeyCons1().add(eul5KeyCons1);
		eul5KeyCons1.setEul5Obj1(this);

		return eul5KeyCons1;
	}

	public Eul5KeyCon removeEul5KeyCons1(Eul5KeyCon eul5KeyCons1) {
		getEul5KeyCons1().remove(eul5KeyCons1);
		eul5KeyCons1.setEul5Obj1(null);

		return eul5KeyCons1;
	}

	public List<Eul5KeyCon> getEul5KeyCons2() {
		return this.eul5KeyCons2;
	}

	public void setEul5KeyCons2(List<Eul5KeyCon> eul5KeyCons2) {
		this.eul5KeyCons2 = eul5KeyCons2;
	}

	public Eul5KeyCon addEul5KeyCons2(Eul5KeyCon eul5KeyCons2) {
		getEul5KeyCons2().add(eul5KeyCons2);
		eul5KeyCons2.setEul5Obj2(this);

		return eul5KeyCons2;
	}

	public Eul5KeyCon removeEul5KeyCons2(Eul5KeyCon eul5KeyCons2) {
		getEul5KeyCons2().remove(eul5KeyCons2);
		eul5KeyCons2.setEul5Obj2(null);

		return eul5KeyCons2;
	}

	public Eul5Ba getEul5Ba() {
		return this.eul5Ba;
	}

	public void setEul5Ba(Eul5Ba eul5Ba) {
		this.eul5Ba = eul5Ba;
	}

	public List<Eul5ObjDep> getEul5ObjDeps1() {
		return this.eul5ObjDeps1;
	}

	public void setEul5ObjDeps1(List<Eul5ObjDep> eul5ObjDeps1) {
		this.eul5ObjDeps1 = eul5ObjDeps1;
	}

	public Eul5ObjDep addEul5ObjDeps1(Eul5ObjDep eul5ObjDeps1) {
		getEul5ObjDeps1().add(eul5ObjDeps1);
		eul5ObjDeps1.setEul5Obj1(this);

		return eul5ObjDeps1;
	}

	public Eul5ObjDep removeEul5ObjDeps1(Eul5ObjDep eul5ObjDeps1) {
		getEul5ObjDeps1().remove(eul5ObjDeps1);
		eul5ObjDeps1.setEul5Obj1(null);

		return eul5ObjDeps1;
	}

	public List<Eul5ObjDep> getEul5ObjDeps2() {
		return this.eul5ObjDeps2;
	}

	public void setEul5ObjDeps2(List<Eul5ObjDep> eul5ObjDeps2) {
		this.eul5ObjDeps2 = eul5ObjDeps2;
	}

	public Eul5ObjDep addEul5ObjDeps2(Eul5ObjDep eul5ObjDeps2) {
		getEul5ObjDeps2().add(eul5ObjDeps2);
		eul5ObjDeps2.setEul5Obj2(this);

		return eul5ObjDeps2;
	}

	public Eul5ObjDep removeEul5ObjDeps2(Eul5ObjDep eul5ObjDeps2) {
		getEul5ObjDeps2().remove(eul5ObjDeps2);
		eul5ObjDeps2.setEul5Obj2(null);

		return eul5ObjDeps2;
	}

	public List<Eul5ObjJoinUsg> getEul5ObjJoinUsgs() {
		return this.eul5ObjJoinUsgs;
	}

	public void setEul5ObjJoinUsgs(List<Eul5ObjJoinUsg> eul5ObjJoinUsgs) {
		this.eul5ObjJoinUsgs = eul5ObjJoinUsgs;
	}

	public Eul5ObjJoinUsg addEul5ObjJoinUsg(Eul5ObjJoinUsg eul5ObjJoinUsg) {
		getEul5ObjJoinUsgs().add(eul5ObjJoinUsg);
		eul5ObjJoinUsg.setEul5Obj(this);

		return eul5ObjJoinUsg;
	}

	public Eul5ObjJoinUsg removeEul5ObjJoinUsg(Eul5ObjJoinUsg eul5ObjJoinUsg) {
		getEul5ObjJoinUsgs().remove(eul5ObjJoinUsg);
		eul5ObjJoinUsg.setEul5Obj(null);

		return eul5ObjJoinUsg;
	}

	public List<Eul5Segment> getEul5Segments1() {
		return this.eul5Segments1;
	}

	public void setEul5Segments1(List<Eul5Segment> eul5Segments1) {
		this.eul5Segments1 = eul5Segments1;
	}

	public Eul5Segment addEul5Segments1(Eul5Segment eul5Segments1) {
		getEul5Segments1().add(eul5Segments1);
		eul5Segments1.setEul5Obj1(this);

		return eul5Segments1;
	}

	public Eul5Segment removeEul5Segments1(Eul5Segment eul5Segments1) {
		getEul5Segments1().remove(eul5Segments1);
		eul5Segments1.setEul5Obj1(null);

		return eul5Segments1;
	}

	public List<Eul5Segment> getEul5Segments2() {
		return this.eul5Segments2;
	}

	public void setEul5Segments2(List<Eul5Segment> eul5Segments2) {
		this.eul5Segments2 = eul5Segments2;
	}

	public Eul5Segment addEul5Segments2(Eul5Segment eul5Segments2) {
		getEul5Segments2().add(eul5Segments2);
		eul5Segments2.setEul5Obj2(this);

		return eul5Segments2;
	}

	public Eul5Segment removeEul5Segments2(Eul5Segment eul5Segments2) {
		getEul5Segments2().remove(eul5Segments2);
		eul5Segments2.setEul5Obj2(null);

		return eul5Segments2;
	}

	public List<Eul5SubQuery> getEul5SubQueries() {
		return this.eul5SubQueries;
	}

	public void setEul5SubQueries(List<Eul5SubQuery> eul5SubQueries) {
		this.eul5SubQueries = eul5SubQueries;
	}

	public Eul5SubQuery addEul5SubQuery(Eul5SubQuery eul5SubQuery) {
		getEul5SubQueries().add(eul5SubQuery);
		eul5SubQuery.setEul5Obj(this);

		return eul5SubQuery;
	}

	public Eul5SubQuery removeEul5SubQuery(Eul5SubQuery eul5SubQuery) {
		getEul5SubQueries().remove(eul5SubQuery);
		eul5SubQuery.setEul5Obj(null);

		return eul5SubQuery;
	}

}