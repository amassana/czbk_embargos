package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_EXPRESSIONS database table.
 * 
 */
@Entity
@Table(name="EUL5_EXPRESSIONS")
@NamedQuery(name="Eul5Expression.findAll", query="SELECT e FROM Eul5Expression e")
public class Eul5Expression implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EXP_ID", unique=true, nullable=false, precision=10)
	private long expId;

	@Column(name="CI_RUNTIME_ITEM", precision=1)
	private BigDecimal ciRuntimeItem;

	@Column(name="CO_NULLABLE", precision=1)
	private BigDecimal coNullable;

	@Column(name="EXP_CREATED_BY", nullable=false, length=64)
	private String expCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="EXP_CREATED_DATE", nullable=false)
	private Date expCreatedDate;

	@Column(name="EXP_DATA_TYPE", nullable=false, precision=2)
	private BigDecimal expDataType;

	@Column(name="EXP_DESCRIPTION", length=240)
	private String expDescription;

	@Column(name="EXP_DEVELOPER_KEY", nullable=false, length=100)
	private String expDeveloperKey;

	@Column(name="EXP_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal expElementState;

	@Column(name="EXP_FORMULA1", length=250)
	private String expFormula1;

	@Column(name="EXP_NAME", nullable=false, length=100)
	private String expName;

	@Column(name="EXP_SEQUENCE", precision=22)
	private BigDecimal expSequence;

	@Column(name="EXP_TYPE", nullable=false, length=10)
	private String expType;

	@Column(name="EXP_UPDATED_BY", length=64)
	private String expUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="EXP_UPDATED_DATE")
	private Date expUpdatedDate;

	@Column(name="EXP_USER_PROP1", length=100)
	private String expUserProp1;

	@Column(name="EXP_USER_PROP2", length=100)
	private String expUserProp2;

	@Column(name="FIL_APP_TYPE", precision=2)
	private BigDecimal filAppType;

	@Column(name="FIL_EXT_FILTER", length=64)
	private String filExtFilter;

	@Column(name="FIL_RUNTIME_FILTER", precision=1)
	private BigDecimal filRuntimeFilter;

	@Column(name="IT_ALIGNMENT", precision=2)
	private BigDecimal itAlignment;

	@Column(name="IT_CASE_DISPLAY", precision=2)
	private BigDecimal itCaseDisplay;

	@Column(name="IT_CASE_STORAGE", precision=2)
	private BigDecimal itCaseStorage;

	@Column(name="IT_DISP_NULL_VAL", length=100)
	private String itDispNullVal;

	@Column(name="IT_EXT_COLUMN", length=64)
	private String itExtColumn;

	@Column(name="IT_FORMAT_MASK", length=100)
	private String itFormatMask;

	@Column(name="IT_HEADING", length=240)
	private String itHeading;

	@Column(name="IT_HIDDEN", precision=1)
	private BigDecimal itHidden;

	@Column(name="IT_MAX_DATA_WIDTH")
	private BigDecimal itMaxDataWidth;

	@Column(name="IT_MAX_DISP_WIDTH")
	private BigDecimal itMaxDispWidth;

	@Column(name="IT_PLACEMENT", precision=2)
	private BigDecimal itPlacement;

	@Column(name="IT_USER_DEF_FMT", length=100)
	private String itUserDefFmt;

	@Column(name="IT_WORD_WRAP", precision=1)
	private BigDecimal itWordWrap;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="P_CASE_SENSITIVE", precision=1)
	private BigDecimal pCaseSensitive;

	@Column(name="PAR_MULTIPLE_VALS", precision=1)
	private BigDecimal parMultipleVals;

	//bi-directional many-to-one association to Eul5BqDep
	@OneToMany(mappedBy="eul5Expression1")
	private List<Eul5BqDep> eul5BqDeps1;

	//bi-directional many-to-one association to Eul5BqDep
	@OneToMany(mappedBy="eul5Expression2")
	private List<Eul5BqDep> eul5BqDeps2;

	//bi-directional many-to-one association to Eul5Domain
	@OneToMany(mappedBy="eul5Expression1")
	private List<Eul5Domain> eul5Domains1;

	//bi-directional many-to-one association to Eul5Domain
	@OneToMany(mappedBy="eul5Expression2")
	private List<Eul5Domain> eul5Domains2;

	//bi-directional many-to-one association to Eul5Document
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FIL_DOC_ID")
	private Eul5Document eul5Document1;

	//bi-directional many-to-one association to Eul5Document
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IT_DOC_ID")
	private Eul5Document eul5Document2;

	//bi-directional many-to-one association to Eul5Domain
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IT_DOM_ID")
	private Eul5Domain eul5Domain;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CI_IT_ID")
	private Eul5Expression eul5Expression;

	//bi-directional many-to-one association to Eul5Expression
	@OneToMany(mappedBy="eul5Expression")
	private List<Eul5Expression> eul5Expressions;

	//bi-directional many-to-one association to Eul5Function
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IT_FUN_ID")
	private Eul5Function eul5Function;

	//bi-directional many-to-one association to Eul5KeyCon
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="JP_KEY_ID")
	private Eul5KeyCon eul5KeyCon;

	//bi-directional many-to-one association to Eul5Obj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IT_OBJ_ID")
	private Eul5Obj eul5Obj1;

	//bi-directional many-to-one association to Eul5Obj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FIL_OBJ_ID")
	private Eul5Obj eul5Obj2;

	//bi-directional many-to-one association to Eul5ExpDep
	@OneToMany(mappedBy="eul5Expression1")
	private List<Eul5ExpDep> eul5ExpDeps1;

	//bi-directional many-to-one association to Eul5ExpDep
	@OneToMany(mappedBy="eul5Expression2")
	private List<Eul5ExpDep> eul5ExpDeps2;

	//bi-directional many-to-one association to Eul5ExpDep
	@OneToMany(mappedBy="eul5Expression3")
	private List<Eul5ExpDep> eul5ExpDeps3;

	//bi-directional many-to-one association to Eul5ExpDep
	@OneToMany(mappedBy="eul5Expression4")
	private List<Eul5ExpDep> eul5ExpDeps4;

	//bi-directional many-to-one association to Eul5IgExpLink
	@OneToMany(mappedBy="eul5Expression1")
	private List<Eul5IgExpLink> eul5IgExpLinks1;

	//bi-directional many-to-one association to Eul5IgExpLink
	@OneToMany(mappedBy="eul5Expression2")
	private List<Eul5IgExpLink> eul5IgExpLinks2;

	//bi-directional many-to-one association to Eul5Segment
	@OneToMany(mappedBy="eul5Expression")
	private List<Eul5Segment> eul5Segments;

	//bi-directional many-to-one association to Eul5SqCrrltn
	@OneToMany(mappedBy="eul5Expression1")
	private List<Eul5SqCrrltn> eul5SqCrrltns1;

	//bi-directional many-to-one association to Eul5SqCrrltn
	@OneToMany(mappedBy="eul5Expression2")
	private List<Eul5SqCrrltn> eul5SqCrrltns2;

	//bi-directional many-to-one association to Eul5SubQuery
	@OneToMany(mappedBy="eul5Expression1")
	private List<Eul5SubQuery> eul5SubQueries1;

	//bi-directional many-to-one association to Eul5SubQuery
	@OneToMany(mappedBy="eul5Expression2")
	private List<Eul5SubQuery> eul5SubQueries2;

	//bi-directional many-to-one association to Eul5SumoExpUsg
	@OneToMany(mappedBy="eul5Expression")
	private List<Eul5SumoExpUsg> eul5SumoExpUsgs;

	//bi-directional many-to-one association to Eul5SumBitmap
	@OneToMany(mappedBy="eul5Expression")
	private List<Eul5SumBitmap> eul5SumBitmaps;

	public Eul5Expression() {
	}

	public long getExpId() {
		return this.expId;
	}

	public void setExpId(long expId) {
		this.expId = expId;
	}

	public BigDecimal getCiRuntimeItem() {
		return this.ciRuntimeItem;
	}

	public void setCiRuntimeItem(BigDecimal ciRuntimeItem) {
		this.ciRuntimeItem = ciRuntimeItem;
	}

	public BigDecimal getCoNullable() {
		return this.coNullable;
	}

	public void setCoNullable(BigDecimal coNullable) {
		this.coNullable = coNullable;
	}

	public String getExpCreatedBy() {
		return this.expCreatedBy;
	}

	public void setExpCreatedBy(String expCreatedBy) {
		this.expCreatedBy = expCreatedBy;
	}

	public Date getExpCreatedDate() {
		return this.expCreatedDate;
	}

	public void setExpCreatedDate(Date expCreatedDate) {
		this.expCreatedDate = expCreatedDate;
	}

	public BigDecimal getExpDataType() {
		return this.expDataType;
	}

	public void setExpDataType(BigDecimal expDataType) {
		this.expDataType = expDataType;
	}

	public String getExpDescription() {
		return this.expDescription;
	}

	public void setExpDescription(String expDescription) {
		this.expDescription = expDescription;
	}

	public String getExpDeveloperKey() {
		return this.expDeveloperKey;
	}

	public void setExpDeveloperKey(String expDeveloperKey) {
		this.expDeveloperKey = expDeveloperKey;
	}

	public BigDecimal getExpElementState() {
		return this.expElementState;
	}

	public void setExpElementState(BigDecimal expElementState) {
		this.expElementState = expElementState;
	}

	public String getExpFormula1() {
		return this.expFormula1;
	}

	public void setExpFormula1(String expFormula1) {
		this.expFormula1 = expFormula1;
	}

	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	public BigDecimal getExpSequence() {
		return this.expSequence;
	}

	public void setExpSequence(BigDecimal expSequence) {
		this.expSequence = expSequence;
	}

	public String getExpType() {
		return this.expType;
	}

	public void setExpType(String expType) {
		this.expType = expType;
	}

	public String getExpUpdatedBy() {
		return this.expUpdatedBy;
	}

	public void setExpUpdatedBy(String expUpdatedBy) {
		this.expUpdatedBy = expUpdatedBy;
	}

	public Date getExpUpdatedDate() {
		return this.expUpdatedDate;
	}

	public void setExpUpdatedDate(Date expUpdatedDate) {
		this.expUpdatedDate = expUpdatedDate;
	}

	public String getExpUserProp1() {
		return this.expUserProp1;
	}

	public void setExpUserProp1(String expUserProp1) {
		this.expUserProp1 = expUserProp1;
	}

	public String getExpUserProp2() {
		return this.expUserProp2;
	}

	public void setExpUserProp2(String expUserProp2) {
		this.expUserProp2 = expUserProp2;
	}

	public BigDecimal getFilAppType() {
		return this.filAppType;
	}

	public void setFilAppType(BigDecimal filAppType) {
		this.filAppType = filAppType;
	}

	public String getFilExtFilter() {
		return this.filExtFilter;
	}

	public void setFilExtFilter(String filExtFilter) {
		this.filExtFilter = filExtFilter;
	}

	public BigDecimal getFilRuntimeFilter() {
		return this.filRuntimeFilter;
	}

	public void setFilRuntimeFilter(BigDecimal filRuntimeFilter) {
		this.filRuntimeFilter = filRuntimeFilter;
	}

	public BigDecimal getItAlignment() {
		return this.itAlignment;
	}

	public void setItAlignment(BigDecimal itAlignment) {
		this.itAlignment = itAlignment;
	}

	public BigDecimal getItCaseDisplay() {
		return this.itCaseDisplay;
	}

	public void setItCaseDisplay(BigDecimal itCaseDisplay) {
		this.itCaseDisplay = itCaseDisplay;
	}

	public BigDecimal getItCaseStorage() {
		return this.itCaseStorage;
	}

	public void setItCaseStorage(BigDecimal itCaseStorage) {
		this.itCaseStorage = itCaseStorage;
	}

	public String getItDispNullVal() {
		return this.itDispNullVal;
	}

	public void setItDispNullVal(String itDispNullVal) {
		this.itDispNullVal = itDispNullVal;
	}

	public String getItExtColumn() {
		return this.itExtColumn;
	}

	public void setItExtColumn(String itExtColumn) {
		this.itExtColumn = itExtColumn;
	}

	public String getItFormatMask() {
		return this.itFormatMask;
	}

	public void setItFormatMask(String itFormatMask) {
		this.itFormatMask = itFormatMask;
	}

	public String getItHeading() {
		return this.itHeading;
	}

	public void setItHeading(String itHeading) {
		this.itHeading = itHeading;
	}

	public BigDecimal getItHidden() {
		return this.itHidden;
	}

	public void setItHidden(BigDecimal itHidden) {
		this.itHidden = itHidden;
	}

	public BigDecimal getItMaxDataWidth() {
		return this.itMaxDataWidth;
	}

	public void setItMaxDataWidth(BigDecimal itMaxDataWidth) {
		this.itMaxDataWidth = itMaxDataWidth;
	}

	public BigDecimal getItMaxDispWidth() {
		return this.itMaxDispWidth;
	}

	public void setItMaxDispWidth(BigDecimal itMaxDispWidth) {
		this.itMaxDispWidth = itMaxDispWidth;
	}

	public BigDecimal getItPlacement() {
		return this.itPlacement;
	}

	public void setItPlacement(BigDecimal itPlacement) {
		this.itPlacement = itPlacement;
	}

	public String getItUserDefFmt() {
		return this.itUserDefFmt;
	}

	public void setItUserDefFmt(String itUserDefFmt) {
		this.itUserDefFmt = itUserDefFmt;
	}

	public BigDecimal getItWordWrap() {
		return this.itWordWrap;
	}

	public void setItWordWrap(BigDecimal itWordWrap) {
		this.itWordWrap = itWordWrap;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public BigDecimal getPCaseSensitive() {
		return this.pCaseSensitive;
	}

	public void setPCaseSensitive(BigDecimal pCaseSensitive) {
		this.pCaseSensitive = pCaseSensitive;
	}

	public BigDecimal getParMultipleVals() {
		return this.parMultipleVals;
	}

	public void setParMultipleVals(BigDecimal parMultipleVals) {
		this.parMultipleVals = parMultipleVals;
	}

	public List<Eul5BqDep> getEul5BqDeps1() {
		return this.eul5BqDeps1;
	}

	public void setEul5BqDeps1(List<Eul5BqDep> eul5BqDeps1) {
		this.eul5BqDeps1 = eul5BqDeps1;
	}

	public Eul5BqDep addEul5BqDeps1(Eul5BqDep eul5BqDeps1) {
		getEul5BqDeps1().add(eul5BqDeps1);
		eul5BqDeps1.setEul5Expression1(this);

		return eul5BqDeps1;
	}

	public Eul5BqDep removeEul5BqDeps1(Eul5BqDep eul5BqDeps1) {
		getEul5BqDeps1().remove(eul5BqDeps1);
		eul5BqDeps1.setEul5Expression1(null);

		return eul5BqDeps1;
	}

	public List<Eul5BqDep> getEul5BqDeps2() {
		return this.eul5BqDeps2;
	}

	public void setEul5BqDeps2(List<Eul5BqDep> eul5BqDeps2) {
		this.eul5BqDeps2 = eul5BqDeps2;
	}

	public Eul5BqDep addEul5BqDeps2(Eul5BqDep eul5BqDeps2) {
		getEul5BqDeps2().add(eul5BqDeps2);
		eul5BqDeps2.setEul5Expression2(this);

		return eul5BqDeps2;
	}

	public Eul5BqDep removeEul5BqDeps2(Eul5BqDep eul5BqDeps2) {
		getEul5BqDeps2().remove(eul5BqDeps2);
		eul5BqDeps2.setEul5Expression2(null);

		return eul5BqDeps2;
	}

	public List<Eul5Domain> getEul5Domains1() {
		return this.eul5Domains1;
	}

	public void setEul5Domains1(List<Eul5Domain> eul5Domains1) {
		this.eul5Domains1 = eul5Domains1;
	}

	public Eul5Domain addEul5Domains1(Eul5Domain eul5Domains1) {
		getEul5Domains1().add(eul5Domains1);
		eul5Domains1.setEul5Expression1(this);

		return eul5Domains1;
	}

	public Eul5Domain removeEul5Domains1(Eul5Domain eul5Domains1) {
		getEul5Domains1().remove(eul5Domains1);
		eul5Domains1.setEul5Expression1(null);

		return eul5Domains1;
	}

	public List<Eul5Domain> getEul5Domains2() {
		return this.eul5Domains2;
	}

	public void setEul5Domains2(List<Eul5Domain> eul5Domains2) {
		this.eul5Domains2 = eul5Domains2;
	}

	public Eul5Domain addEul5Domains2(Eul5Domain eul5Domains2) {
		getEul5Domains2().add(eul5Domains2);
		eul5Domains2.setEul5Expression2(this);

		return eul5Domains2;
	}

	public Eul5Domain removeEul5Domains2(Eul5Domain eul5Domains2) {
		getEul5Domains2().remove(eul5Domains2);
		eul5Domains2.setEul5Expression2(null);

		return eul5Domains2;
	}

	public Eul5Document getEul5Document1() {
		return this.eul5Document1;
	}

	public void setEul5Document1(Eul5Document eul5Document1) {
		this.eul5Document1 = eul5Document1;
	}

	public Eul5Document getEul5Document2() {
		return this.eul5Document2;
	}

	public void setEul5Document2(Eul5Document eul5Document2) {
		this.eul5Document2 = eul5Document2;
	}

	public Eul5Domain getEul5Domain() {
		return this.eul5Domain;
	}

	public void setEul5Domain(Eul5Domain eul5Domain) {
		this.eul5Domain = eul5Domain;
	}

	public Eul5Expression getEul5Expression() {
		return this.eul5Expression;
	}

	public void setEul5Expression(Eul5Expression eul5Expression) {
		this.eul5Expression = eul5Expression;
	}

	public List<Eul5Expression> getEul5Expressions() {
		return this.eul5Expressions;
	}

	public void setEul5Expressions(List<Eul5Expression> eul5Expressions) {
		this.eul5Expressions = eul5Expressions;
	}

	public Eul5Expression addEul5Expression(Eul5Expression eul5Expression) {
		getEul5Expressions().add(eul5Expression);
		eul5Expression.setEul5Expression(this);

		return eul5Expression;
	}

	public Eul5Expression removeEul5Expression(Eul5Expression eul5Expression) {
		getEul5Expressions().remove(eul5Expression);
		eul5Expression.setEul5Expression(null);

		return eul5Expression;
	}

	public Eul5Function getEul5Function() {
		return this.eul5Function;
	}

	public void setEul5Function(Eul5Function eul5Function) {
		this.eul5Function = eul5Function;
	}

	public Eul5KeyCon getEul5KeyCon() {
		return this.eul5KeyCon;
	}

	public void setEul5KeyCon(Eul5KeyCon eul5KeyCon) {
		this.eul5KeyCon = eul5KeyCon;
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

	public List<Eul5ExpDep> getEul5ExpDeps1() {
		return this.eul5ExpDeps1;
	}

	public void setEul5ExpDeps1(List<Eul5ExpDep> eul5ExpDeps1) {
		this.eul5ExpDeps1 = eul5ExpDeps1;
	}

	public Eul5ExpDep addEul5ExpDeps1(Eul5ExpDep eul5ExpDeps1) {
		getEul5ExpDeps1().add(eul5ExpDeps1);
		eul5ExpDeps1.setEul5Expression1(this);

		return eul5ExpDeps1;
	}

	public Eul5ExpDep removeEul5ExpDeps1(Eul5ExpDep eul5ExpDeps1) {
		getEul5ExpDeps1().remove(eul5ExpDeps1);
		eul5ExpDeps1.setEul5Expression1(null);

		return eul5ExpDeps1;
	}

	public List<Eul5ExpDep> getEul5ExpDeps2() {
		return this.eul5ExpDeps2;
	}

	public void setEul5ExpDeps2(List<Eul5ExpDep> eul5ExpDeps2) {
		this.eul5ExpDeps2 = eul5ExpDeps2;
	}

	public Eul5ExpDep addEul5ExpDeps2(Eul5ExpDep eul5ExpDeps2) {
		getEul5ExpDeps2().add(eul5ExpDeps2);
		eul5ExpDeps2.setEul5Expression2(this);

		return eul5ExpDeps2;
	}

	public Eul5ExpDep removeEul5ExpDeps2(Eul5ExpDep eul5ExpDeps2) {
		getEul5ExpDeps2().remove(eul5ExpDeps2);
		eul5ExpDeps2.setEul5Expression2(null);

		return eul5ExpDeps2;
	}

	public List<Eul5ExpDep> getEul5ExpDeps3() {
		return this.eul5ExpDeps3;
	}

	public void setEul5ExpDeps3(List<Eul5ExpDep> eul5ExpDeps3) {
		this.eul5ExpDeps3 = eul5ExpDeps3;
	}

	public Eul5ExpDep addEul5ExpDeps3(Eul5ExpDep eul5ExpDeps3) {
		getEul5ExpDeps3().add(eul5ExpDeps3);
		eul5ExpDeps3.setEul5Expression3(this);

		return eul5ExpDeps3;
	}

	public Eul5ExpDep removeEul5ExpDeps3(Eul5ExpDep eul5ExpDeps3) {
		getEul5ExpDeps3().remove(eul5ExpDeps3);
		eul5ExpDeps3.setEul5Expression3(null);

		return eul5ExpDeps3;
	}

	public List<Eul5ExpDep> getEul5ExpDeps4() {
		return this.eul5ExpDeps4;
	}

	public void setEul5ExpDeps4(List<Eul5ExpDep> eul5ExpDeps4) {
		this.eul5ExpDeps4 = eul5ExpDeps4;
	}

	public Eul5ExpDep addEul5ExpDeps4(Eul5ExpDep eul5ExpDeps4) {
		getEul5ExpDeps4().add(eul5ExpDeps4);
		eul5ExpDeps4.setEul5Expression4(this);

		return eul5ExpDeps4;
	}

	public Eul5ExpDep removeEul5ExpDeps4(Eul5ExpDep eul5ExpDeps4) {
		getEul5ExpDeps4().remove(eul5ExpDeps4);
		eul5ExpDeps4.setEul5Expression4(null);

		return eul5ExpDeps4;
	}

	public List<Eul5IgExpLink> getEul5IgExpLinks1() {
		return this.eul5IgExpLinks1;
	}

	public void setEul5IgExpLinks1(List<Eul5IgExpLink> eul5IgExpLinks1) {
		this.eul5IgExpLinks1 = eul5IgExpLinks1;
	}

	public Eul5IgExpLink addEul5IgExpLinks1(Eul5IgExpLink eul5IgExpLinks1) {
		getEul5IgExpLinks1().add(eul5IgExpLinks1);
		eul5IgExpLinks1.setEul5Expression1(this);

		return eul5IgExpLinks1;
	}

	public Eul5IgExpLink removeEul5IgExpLinks1(Eul5IgExpLink eul5IgExpLinks1) {
		getEul5IgExpLinks1().remove(eul5IgExpLinks1);
		eul5IgExpLinks1.setEul5Expression1(null);

		return eul5IgExpLinks1;
	}

	public List<Eul5IgExpLink> getEul5IgExpLinks2() {
		return this.eul5IgExpLinks2;
	}

	public void setEul5IgExpLinks2(List<Eul5IgExpLink> eul5IgExpLinks2) {
		this.eul5IgExpLinks2 = eul5IgExpLinks2;
	}

	public Eul5IgExpLink addEul5IgExpLinks2(Eul5IgExpLink eul5IgExpLinks2) {
		getEul5IgExpLinks2().add(eul5IgExpLinks2);
		eul5IgExpLinks2.setEul5Expression2(this);

		return eul5IgExpLinks2;
	}

	public Eul5IgExpLink removeEul5IgExpLinks2(Eul5IgExpLink eul5IgExpLinks2) {
		getEul5IgExpLinks2().remove(eul5IgExpLinks2);
		eul5IgExpLinks2.setEul5Expression2(null);

		return eul5IgExpLinks2;
	}

	public List<Eul5Segment> getEul5Segments() {
		return this.eul5Segments;
	}

	public void setEul5Segments(List<Eul5Segment> eul5Segments) {
		this.eul5Segments = eul5Segments;
	}

	public Eul5Segment addEul5Segment(Eul5Segment eul5Segment) {
		getEul5Segments().add(eul5Segment);
		eul5Segment.setEul5Expression(this);

		return eul5Segment;
	}

	public Eul5Segment removeEul5Segment(Eul5Segment eul5Segment) {
		getEul5Segments().remove(eul5Segment);
		eul5Segment.setEul5Expression(null);

		return eul5Segment;
	}

	public List<Eul5SqCrrltn> getEul5SqCrrltns1() {
		return this.eul5SqCrrltns1;
	}

	public void setEul5SqCrrltns1(List<Eul5SqCrrltn> eul5SqCrrltns1) {
		this.eul5SqCrrltns1 = eul5SqCrrltns1;
	}

	public Eul5SqCrrltn addEul5SqCrrltns1(Eul5SqCrrltn eul5SqCrrltns1) {
		getEul5SqCrrltns1().add(eul5SqCrrltns1);
		eul5SqCrrltns1.setEul5Expression1(this);

		return eul5SqCrrltns1;
	}

	public Eul5SqCrrltn removeEul5SqCrrltns1(Eul5SqCrrltn eul5SqCrrltns1) {
		getEul5SqCrrltns1().remove(eul5SqCrrltns1);
		eul5SqCrrltns1.setEul5Expression1(null);

		return eul5SqCrrltns1;
	}

	public List<Eul5SqCrrltn> getEul5SqCrrltns2() {
		return this.eul5SqCrrltns2;
	}

	public void setEul5SqCrrltns2(List<Eul5SqCrrltn> eul5SqCrrltns2) {
		this.eul5SqCrrltns2 = eul5SqCrrltns2;
	}

	public Eul5SqCrrltn addEul5SqCrrltns2(Eul5SqCrrltn eul5SqCrrltns2) {
		getEul5SqCrrltns2().add(eul5SqCrrltns2);
		eul5SqCrrltns2.setEul5Expression2(this);

		return eul5SqCrrltns2;
	}

	public Eul5SqCrrltn removeEul5SqCrrltns2(Eul5SqCrrltn eul5SqCrrltns2) {
		getEul5SqCrrltns2().remove(eul5SqCrrltns2);
		eul5SqCrrltns2.setEul5Expression2(null);

		return eul5SqCrrltns2;
	}

	public List<Eul5SubQuery> getEul5SubQueries1() {
		return this.eul5SubQueries1;
	}

	public void setEul5SubQueries1(List<Eul5SubQuery> eul5SubQueries1) {
		this.eul5SubQueries1 = eul5SubQueries1;
	}

	public Eul5SubQuery addEul5SubQueries1(Eul5SubQuery eul5SubQueries1) {
		getEul5SubQueries1().add(eul5SubQueries1);
		eul5SubQueries1.setEul5Expression1(this);

		return eul5SubQueries1;
	}

	public Eul5SubQuery removeEul5SubQueries1(Eul5SubQuery eul5SubQueries1) {
		getEul5SubQueries1().remove(eul5SubQueries1);
		eul5SubQueries1.setEul5Expression1(null);

		return eul5SubQueries1;
	}

	public List<Eul5SubQuery> getEul5SubQueries2() {
		return this.eul5SubQueries2;
	}

	public void setEul5SubQueries2(List<Eul5SubQuery> eul5SubQueries2) {
		this.eul5SubQueries2 = eul5SubQueries2;
	}

	public Eul5SubQuery addEul5SubQueries2(Eul5SubQuery eul5SubQueries2) {
		getEul5SubQueries2().add(eul5SubQueries2);
		eul5SubQueries2.setEul5Expression2(this);

		return eul5SubQueries2;
	}

	public Eul5SubQuery removeEul5SubQueries2(Eul5SubQuery eul5SubQueries2) {
		getEul5SubQueries2().remove(eul5SubQueries2);
		eul5SubQueries2.setEul5Expression2(null);

		return eul5SubQueries2;
	}

	public List<Eul5SumoExpUsg> getEul5SumoExpUsgs() {
		return this.eul5SumoExpUsgs;
	}

	public void setEul5SumoExpUsgs(List<Eul5SumoExpUsg> eul5SumoExpUsgs) {
		this.eul5SumoExpUsgs = eul5SumoExpUsgs;
	}

	public Eul5SumoExpUsg addEul5SumoExpUsg(Eul5SumoExpUsg eul5SumoExpUsg) {
		getEul5SumoExpUsgs().add(eul5SumoExpUsg);
		eul5SumoExpUsg.setEul5Expression(this);

		return eul5SumoExpUsg;
	}

	public Eul5SumoExpUsg removeEul5SumoExpUsg(Eul5SumoExpUsg eul5SumoExpUsg) {
		getEul5SumoExpUsgs().remove(eul5SumoExpUsg);
		eul5SumoExpUsg.setEul5Expression(null);

		return eul5SumoExpUsg;
	}

	public List<Eul5SumBitmap> getEul5SumBitmaps() {
		return this.eul5SumBitmaps;
	}

	public void setEul5SumBitmaps(List<Eul5SumBitmap> eul5SumBitmaps) {
		this.eul5SumBitmaps = eul5SumBitmaps;
	}

	public Eul5SumBitmap addEul5SumBitmap(Eul5SumBitmap eul5SumBitmap) {
		getEul5SumBitmaps().add(eul5SumBitmap);
		eul5SumBitmap.setEul5Expression(this);

		return eul5SumBitmap;
	}

	public Eul5SumBitmap removeEul5SumBitmap(Eul5SumBitmap eul5SumBitmap) {
		getEul5SumBitmaps().remove(eul5SumBitmap);
		eul5SumBitmap.setEul5Expression(null);

		return eul5SumBitmap;
	}

}