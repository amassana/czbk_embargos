package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_SUMMARY_OBJS database table.
 * 
 */
@Entity
@Table(name="EUL5_SUMMARY_OBJS")
@NamedQuery(name="Eul5SummaryObj.findAll", query="SELECT e FROM Eul5SummaryObj e")
public class Eul5SummaryObj implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SUMO_ID", unique=true, nullable=false, precision=10)
	private long sumoId;

	@Column(name="EMS_COMMIT_SIZE", precision=22)
	private BigDecimal emsCommitSize;

	@Column(name="EMS_STATE", precision=2)
	private BigDecimal emsState;

	@Column(name="MSDO_REFRESH_REQD", precision=1)
	private BigDecimal msdoRefreshReqd;

	@Column(name="MSDO_SVR_ERR_CODE", precision=22)
	private BigDecimal msdoSvrErrCode;

	@Column(name="MSDO_SVR_ERR_TEXT", length=240)
	private String msdoSvrErrText;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="SBO_MAX_ITEM_COMB", precision=22)
	private BigDecimal sboMaxItemComb;

	@Column(name="SDO_BITMAP_POS", precision=22)
	private BigDecimal sdoBitmapPos;

	@Column(name="SDO_DATABASE_LINK", length=64)
	private String sdoDatabaseLink;

	@Temporal(TemporalType.DATE)
	@Column(name="SDO_LAST_REFRESH")
	private Date sdoLastRefresh;

	@Column(name="SDO_NUM_AXIS_ITEMS", precision=22)
	private BigDecimal sdoNumAxisItems;

	@Column(name="SDO_NUM_JOINS", precision=22)
	private BigDecimal sdoNumJoins;

	@Column(name="SDO_NUM_ROWS", precision=22)
	private BigDecimal sdoNumRows;

	@Column(name="SDO_NUM_USGS", precision=22)
	private BigDecimal sdoNumUsgs;

	@Column(name="SDO_OBJECT_SQL1", length=250)
	private String sdoObjectSql1;

	@Column(name="SDO_OBJECT_SQL2", length=250)
	private String sdoObjectSql2;

	@Column(name="SDO_OBJECT_SQL3", length=250)
	private String sdoObjectSql3;

	@Column(name="SDO_TABLE_NAME", length=64)
	private String sdoTableName;

	@Column(name="SDO_TABLE_OWNER", length=64)
	private String sdoTableOwner;

	@Column(name="SUMO_ACTIVE", nullable=false, precision=1)
	private BigDecimal sumoActive;

	@Column(name="SUMO_CREATED_BY", nullable=false, length=64)
	private String sumoCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SUMO_CREATED_DATE", nullable=false)
	private Date sumoCreatedDate;

	@Column(name="SUMO_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal sumoElementState;

	@Column(name="SUMO_EXT_DB_LINK", length=64)
	private String sumoExtDbLink;

	@Column(name="SUMO_EXT_OBJECT", length=64)
	private String sumoExtObject;

	@Column(name="SUMO_EXT_OWNER", length=64)
	private String sumoExtOwner;

	@Column(name="SUMO_INTERNAL", nullable=false, precision=1)
	private BigDecimal sumoInternal;

	@Column(name="SUMO_ITEM_DELETED", nullable=false, precision=1)
	private BigDecimal sumoItemDeleted;

	@Column(name="SUMO_ITEM_MODIFIED", nullable=false, precision=1)
	private BigDecimal sumoItemModified;

	@Column(name="SUMO_JOIN_STATE", nullable=false, precision=1)
	private BigDecimal sumoJoinState;

	@Column(name="SUMO_TYPE", nullable=false, length=10)
	private String sumoType;

	@Column(name="SUMO_UPDATED_BY", length=64)
	private String sumoUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SUMO_UPDATED_DATE")
	private Date sumoUpdatedDate;

	@Column(name="SUMO_VALIDITY", nullable=false, precision=1)
	private BigDecimal sumoValidity;

	//bi-directional many-to-one association to Eul5AsmpCon
	@OneToMany(mappedBy="eul5SummaryObj")
	private List<Eul5AsmpCon> eul5AsmpCons;

	//bi-directional many-to-one association to Eul5AsmpLog
	@OneToMany(mappedBy="eul5SummaryObj")
	private List<Eul5AsmpLog> eul5AsmpLogs;

	//bi-directional many-to-one association to Eul5ObjJoinUsg
	@OneToMany(mappedBy="eul5SummaryObj")
	private List<Eul5ObjJoinUsg> eul5ObjJoinUsgs;

	//bi-directional many-to-one association to Eul5Segment
	@OneToMany(mappedBy="eul5SummaryObj1")
	private List<Eul5Segment> eul5Segments1;

	//bi-directional many-to-one association to Eul5Segment
	@OneToMany(mappedBy="eul5SummaryObj2")
	private List<Eul5Segment> eul5Segments2;

	//bi-directional many-to-one association to Eul5AsmPolicy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUMO_ASMP_ID")
	private Eul5AsmPolicy eul5AsmPolicy;

	//bi-directional many-to-one association to Eul5SummaryObj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SDO_SBO_ID")
	private Eul5SummaryObj eul5SummaryObj;

	//bi-directional many-to-one association to Eul5SummaryObj
	@OneToMany(mappedBy="eul5SummaryObj")
	private List<Eul5SummaryObj> eul5SummaryObjs;

	//bi-directional many-to-one association to Eul5SumRfshSet
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SBO_SRS_ID")
	private Eul5SumRfshSet eul5SumRfshSet;

	//bi-directional many-to-one association to Eul5SumoExpUsg
	@OneToMany(mappedBy="eul5SummaryObj")
	private List<Eul5SumoExpUsg> eul5SumoExpUsgs;

	public Eul5SummaryObj() {
	}

	public long getSumoId() {
		return this.sumoId;
	}

	public void setSumoId(long sumoId) {
		this.sumoId = sumoId;
	}

	public BigDecimal getEmsCommitSize() {
		return this.emsCommitSize;
	}

	public void setEmsCommitSize(BigDecimal emsCommitSize) {
		this.emsCommitSize = emsCommitSize;
	}

	public BigDecimal getEmsState() {
		return this.emsState;
	}

	public void setEmsState(BigDecimal emsState) {
		this.emsState = emsState;
	}

	public BigDecimal getMsdoRefreshReqd() {
		return this.msdoRefreshReqd;
	}

	public void setMsdoRefreshReqd(BigDecimal msdoRefreshReqd) {
		this.msdoRefreshReqd = msdoRefreshReqd;
	}

	public BigDecimal getMsdoSvrErrCode() {
		return this.msdoSvrErrCode;
	}

	public void setMsdoSvrErrCode(BigDecimal msdoSvrErrCode) {
		this.msdoSvrErrCode = msdoSvrErrCode;
	}

	public String getMsdoSvrErrText() {
		return this.msdoSvrErrText;
	}

	public void setMsdoSvrErrText(String msdoSvrErrText) {
		this.msdoSvrErrText = msdoSvrErrText;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public BigDecimal getSboMaxItemComb() {
		return this.sboMaxItemComb;
	}

	public void setSboMaxItemComb(BigDecimal sboMaxItemComb) {
		this.sboMaxItemComb = sboMaxItemComb;
	}

	public BigDecimal getSdoBitmapPos() {
		return this.sdoBitmapPos;
	}

	public void setSdoBitmapPos(BigDecimal sdoBitmapPos) {
		this.sdoBitmapPos = sdoBitmapPos;
	}

	public String getSdoDatabaseLink() {
		return this.sdoDatabaseLink;
	}

	public void setSdoDatabaseLink(String sdoDatabaseLink) {
		this.sdoDatabaseLink = sdoDatabaseLink;
	}

	public Date getSdoLastRefresh() {
		return this.sdoLastRefresh;
	}

	public void setSdoLastRefresh(Date sdoLastRefresh) {
		this.sdoLastRefresh = sdoLastRefresh;
	}

	public BigDecimal getSdoNumAxisItems() {
		return this.sdoNumAxisItems;
	}

	public void setSdoNumAxisItems(BigDecimal sdoNumAxisItems) {
		this.sdoNumAxisItems = sdoNumAxisItems;
	}

	public BigDecimal getSdoNumJoins() {
		return this.sdoNumJoins;
	}

	public void setSdoNumJoins(BigDecimal sdoNumJoins) {
		this.sdoNumJoins = sdoNumJoins;
	}

	public BigDecimal getSdoNumRows() {
		return this.sdoNumRows;
	}

	public void setSdoNumRows(BigDecimal sdoNumRows) {
		this.sdoNumRows = sdoNumRows;
	}

	public BigDecimal getSdoNumUsgs() {
		return this.sdoNumUsgs;
	}

	public void setSdoNumUsgs(BigDecimal sdoNumUsgs) {
		this.sdoNumUsgs = sdoNumUsgs;
	}

	public String getSdoObjectSql1() {
		return this.sdoObjectSql1;
	}

	public void setSdoObjectSql1(String sdoObjectSql1) {
		this.sdoObjectSql1 = sdoObjectSql1;
	}

	public String getSdoObjectSql2() {
		return this.sdoObjectSql2;
	}

	public void setSdoObjectSql2(String sdoObjectSql2) {
		this.sdoObjectSql2 = sdoObjectSql2;
	}

	public String getSdoObjectSql3() {
		return this.sdoObjectSql3;
	}

	public void setSdoObjectSql3(String sdoObjectSql3) {
		this.sdoObjectSql3 = sdoObjectSql3;
	}

	public String getSdoTableName() {
		return this.sdoTableName;
	}

	public void setSdoTableName(String sdoTableName) {
		this.sdoTableName = sdoTableName;
	}

	public String getSdoTableOwner() {
		return this.sdoTableOwner;
	}

	public void setSdoTableOwner(String sdoTableOwner) {
		this.sdoTableOwner = sdoTableOwner;
	}

	public BigDecimal getSumoActive() {
		return this.sumoActive;
	}

	public void setSumoActive(BigDecimal sumoActive) {
		this.sumoActive = sumoActive;
	}

	public String getSumoCreatedBy() {
		return this.sumoCreatedBy;
	}

	public void setSumoCreatedBy(String sumoCreatedBy) {
		this.sumoCreatedBy = sumoCreatedBy;
	}

	public Date getSumoCreatedDate() {
		return this.sumoCreatedDate;
	}

	public void setSumoCreatedDate(Date sumoCreatedDate) {
		this.sumoCreatedDate = sumoCreatedDate;
	}

	public BigDecimal getSumoElementState() {
		return this.sumoElementState;
	}

	public void setSumoElementState(BigDecimal sumoElementState) {
		this.sumoElementState = sumoElementState;
	}

	public String getSumoExtDbLink() {
		return this.sumoExtDbLink;
	}

	public void setSumoExtDbLink(String sumoExtDbLink) {
		this.sumoExtDbLink = sumoExtDbLink;
	}

	public String getSumoExtObject() {
		return this.sumoExtObject;
	}

	public void setSumoExtObject(String sumoExtObject) {
		this.sumoExtObject = sumoExtObject;
	}

	public String getSumoExtOwner() {
		return this.sumoExtOwner;
	}

	public void setSumoExtOwner(String sumoExtOwner) {
		this.sumoExtOwner = sumoExtOwner;
	}

	public BigDecimal getSumoInternal() {
		return this.sumoInternal;
	}

	public void setSumoInternal(BigDecimal sumoInternal) {
		this.sumoInternal = sumoInternal;
	}

	public BigDecimal getSumoItemDeleted() {
		return this.sumoItemDeleted;
	}

	public void setSumoItemDeleted(BigDecimal sumoItemDeleted) {
		this.sumoItemDeleted = sumoItemDeleted;
	}

	public BigDecimal getSumoItemModified() {
		return this.sumoItemModified;
	}

	public void setSumoItemModified(BigDecimal sumoItemModified) {
		this.sumoItemModified = sumoItemModified;
	}

	public BigDecimal getSumoJoinState() {
		return this.sumoJoinState;
	}

	public void setSumoJoinState(BigDecimal sumoJoinState) {
		this.sumoJoinState = sumoJoinState;
	}

	public String getSumoType() {
		return this.sumoType;
	}

	public void setSumoType(String sumoType) {
		this.sumoType = sumoType;
	}

	public String getSumoUpdatedBy() {
		return this.sumoUpdatedBy;
	}

	public void setSumoUpdatedBy(String sumoUpdatedBy) {
		this.sumoUpdatedBy = sumoUpdatedBy;
	}

	public Date getSumoUpdatedDate() {
		return this.sumoUpdatedDate;
	}

	public void setSumoUpdatedDate(Date sumoUpdatedDate) {
		this.sumoUpdatedDate = sumoUpdatedDate;
	}

	public BigDecimal getSumoValidity() {
		return this.sumoValidity;
	}

	public void setSumoValidity(BigDecimal sumoValidity) {
		this.sumoValidity = sumoValidity;
	}

	public List<Eul5AsmpCon> getEul5AsmpCons() {
		return this.eul5AsmpCons;
	}

	public void setEul5AsmpCons(List<Eul5AsmpCon> eul5AsmpCons) {
		this.eul5AsmpCons = eul5AsmpCons;
	}

	public Eul5AsmpCon addEul5AsmpCon(Eul5AsmpCon eul5AsmpCon) {
		getEul5AsmpCons().add(eul5AsmpCon);
		eul5AsmpCon.setEul5SummaryObj(this);

		return eul5AsmpCon;
	}

	public Eul5AsmpCon removeEul5AsmpCon(Eul5AsmpCon eul5AsmpCon) {
		getEul5AsmpCons().remove(eul5AsmpCon);
		eul5AsmpCon.setEul5SummaryObj(null);

		return eul5AsmpCon;
	}

	public List<Eul5AsmpLog> getEul5AsmpLogs() {
		return this.eul5AsmpLogs;
	}

	public void setEul5AsmpLogs(List<Eul5AsmpLog> eul5AsmpLogs) {
		this.eul5AsmpLogs = eul5AsmpLogs;
	}

	public Eul5AsmpLog addEul5AsmpLog(Eul5AsmpLog eul5AsmpLog) {
		getEul5AsmpLogs().add(eul5AsmpLog);
		eul5AsmpLog.setEul5SummaryObj(this);

		return eul5AsmpLog;
	}

	public Eul5AsmpLog removeEul5AsmpLog(Eul5AsmpLog eul5AsmpLog) {
		getEul5AsmpLogs().remove(eul5AsmpLog);
		eul5AsmpLog.setEul5SummaryObj(null);

		return eul5AsmpLog;
	}

	public List<Eul5ObjJoinUsg> getEul5ObjJoinUsgs() {
		return this.eul5ObjJoinUsgs;
	}

	public void setEul5ObjJoinUsgs(List<Eul5ObjJoinUsg> eul5ObjJoinUsgs) {
		this.eul5ObjJoinUsgs = eul5ObjJoinUsgs;
	}

	public Eul5ObjJoinUsg addEul5ObjJoinUsg(Eul5ObjJoinUsg eul5ObjJoinUsg) {
		getEul5ObjJoinUsgs().add(eul5ObjJoinUsg);
		eul5ObjJoinUsg.setEul5SummaryObj(this);

		return eul5ObjJoinUsg;
	}

	public Eul5ObjJoinUsg removeEul5ObjJoinUsg(Eul5ObjJoinUsg eul5ObjJoinUsg) {
		getEul5ObjJoinUsgs().remove(eul5ObjJoinUsg);
		eul5ObjJoinUsg.setEul5SummaryObj(null);

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
		eul5Segments1.setEul5SummaryObj1(this);

		return eul5Segments1;
	}

	public Eul5Segment removeEul5Segments1(Eul5Segment eul5Segments1) {
		getEul5Segments1().remove(eul5Segments1);
		eul5Segments1.setEul5SummaryObj1(null);

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
		eul5Segments2.setEul5SummaryObj2(this);

		return eul5Segments2;
	}

	public Eul5Segment removeEul5Segments2(Eul5Segment eul5Segments2) {
		getEul5Segments2().remove(eul5Segments2);
		eul5Segments2.setEul5SummaryObj2(null);

		return eul5Segments2;
	}

	public Eul5AsmPolicy getEul5AsmPolicy() {
		return this.eul5AsmPolicy;
	}

	public void setEul5AsmPolicy(Eul5AsmPolicy eul5AsmPolicy) {
		this.eul5AsmPolicy = eul5AsmPolicy;
	}

	public Eul5SummaryObj getEul5SummaryObj() {
		return this.eul5SummaryObj;
	}

	public void setEul5SummaryObj(Eul5SummaryObj eul5SummaryObj) {
		this.eul5SummaryObj = eul5SummaryObj;
	}

	public List<Eul5SummaryObj> getEul5SummaryObjs() {
		return this.eul5SummaryObjs;
	}

	public void setEul5SummaryObjs(List<Eul5SummaryObj> eul5SummaryObjs) {
		this.eul5SummaryObjs = eul5SummaryObjs;
	}

	public Eul5SummaryObj addEul5SummaryObj(Eul5SummaryObj eul5SummaryObj) {
		getEul5SummaryObjs().add(eul5SummaryObj);
		eul5SummaryObj.setEul5SummaryObj(this);

		return eul5SummaryObj;
	}

	public Eul5SummaryObj removeEul5SummaryObj(Eul5SummaryObj eul5SummaryObj) {
		getEul5SummaryObjs().remove(eul5SummaryObj);
		eul5SummaryObj.setEul5SummaryObj(null);

		return eul5SummaryObj;
	}

	public Eul5SumRfshSet getEul5SumRfshSet() {
		return this.eul5SumRfshSet;
	}

	public void setEul5SumRfshSet(Eul5SumRfshSet eul5SumRfshSet) {
		this.eul5SumRfshSet = eul5SumRfshSet;
	}

	public List<Eul5SumoExpUsg> getEul5SumoExpUsgs() {
		return this.eul5SumoExpUsgs;
	}

	public void setEul5SumoExpUsgs(List<Eul5SumoExpUsg> eul5SumoExpUsgs) {
		this.eul5SumoExpUsgs = eul5SumoExpUsgs;
	}

	public Eul5SumoExpUsg addEul5SumoExpUsg(Eul5SumoExpUsg eul5SumoExpUsg) {
		getEul5SumoExpUsgs().add(eul5SumoExpUsg);
		eul5SumoExpUsg.setEul5SummaryObj(this);

		return eul5SumoExpUsg;
	}

	public Eul5SumoExpUsg removeEul5SumoExpUsg(Eul5SumoExpUsg eul5SumoExpUsg) {
		getEul5SumoExpUsgs().remove(eul5SumoExpUsg);
		eul5SumoExpUsg.setEul5SummaryObj(null);

		return eul5SumoExpUsg;
	}

}