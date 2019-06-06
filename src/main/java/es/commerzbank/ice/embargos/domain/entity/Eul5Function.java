package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_FUNCTIONS database table.
 * 
 */
@Entity
@Table(name="EUL5_FUNCTIONS")
@NamedQuery(name="Eul5Function.findAll", query="SELECT e FROM Eul5Function e")
public class Eul5Function implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FUN_ID", unique=true, nullable=false, precision=10)
	private long funId;

	@Column(name="FUN_AVAILABLE", nullable=false, precision=1)
	private BigDecimal funAvailable;

	@Column(name="FUN_BUILT_IN", nullable=false, precision=1)
	private BigDecimal funBuiltIn;

	@Column(name="FUN_CREATED_BY", nullable=false, length=64)
	private String funCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="FUN_CREATED_DATE", nullable=false)
	private Date funCreatedDate;

	@Column(name="FUN_DATA_TYPE", nullable=false, precision=2)
	private BigDecimal funDataType;

	@Column(name="FUN_DESCRIPTION_MN", precision=10)
	private BigDecimal funDescriptionMn;

	@Column(name="FUN_DESCRIPTION_S", length=240)
	private String funDescriptionS;

	@Column(name="FUN_DEVELOPER_KEY", nullable=false, length=100)
	private String funDeveloperKey;

	@Column(name="FUN_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal funElementState;

	@Column(name="FUN_EXT_DB_LINK", length=64)
	private String funExtDbLink;

	@Column(name="FUN_EXT_NAME", nullable=false, length=64)
	private String funExtName;

	@Column(name="FUN_EXT_OWNER", length=64)
	private String funExtOwner;

	@Column(name="FUN_EXT_PACKAGE", length=64)
	private String funExtPackage;

	@Column(name="FUN_FUNCTION_TYPE", nullable=false, precision=2)
	private BigDecimal funFunctionType;

	@Column(name="FUN_HIDDEN", nullable=false, precision=1)
	private BigDecimal funHidden;

	@Column(name="FUN_MAXIMUM_ARGS", precision=22)
	private BigDecimal funMaximumArgs;

	@Column(name="FUN_MINIMUM_ARGS", nullable=false, precision=22)
	private BigDecimal funMinimumArgs;

	@Column(name="FUN_NAME", nullable=false, length=100)
	private String funName;

	@Column(name="FUN_UPDATED_BY", length=64)
	private String funUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="FUN_UPDATED_DATE")
	private Date funUpdatedDate;

	@Column(name="FUN_USER_PROP1", length=100)
	private String funUserProp1;

	@Column(name="FUN_USER_PROP2", length=100)
	private String funUserProp2;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5BqDep
	@OneToMany(mappedBy="eul5Function")
	private List<Eul5BqDep> eul5BqDeps;

	//bi-directional many-to-one association to Eul5Expression
	@OneToMany(mappedBy="eul5Function")
	private List<Eul5Expression> eul5Expressions;

	//bi-directional many-to-one association to Eul5ExpDep
	@OneToMany(mappedBy="eul5Function1")
	private List<Eul5ExpDep> eul5ExpDeps1;

	//bi-directional many-to-one association to Eul5ExpDep
	@OneToMany(mappedBy="eul5Function2")
	private List<Eul5ExpDep> eul5ExpDeps2;

	//bi-directional many-to-one association to Eul5FunArgument
	@OneToMany(mappedBy="eul5Function")
	private List<Eul5FunArgument> eul5FunArguments;

	//bi-directional many-to-one association to Eul5FunFcLink
	@OneToMany(mappedBy="eul5Function")
	private List<Eul5FunFcLink> eul5FunFcLinks;

	//bi-directional many-to-one association to Eul5SumoExpUsg
	@OneToMany(mappedBy="eul5Function1")
	private List<Eul5SumoExpUsg> eul5SumoExpUsgs1;

	//bi-directional many-to-one association to Eul5SumoExpUsg
	@OneToMany(mappedBy="eul5Function2")
	private List<Eul5SumoExpUsg> eul5SumoExpUsgs2;

	//bi-directional many-to-one association to Eul5SumBitmap
	@OneToMany(mappedBy="eul5Function")
	private List<Eul5SumBitmap> eul5SumBitmaps;

	public Eul5Function() {
	}

	public long getFunId() {
		return this.funId;
	}

	public void setFunId(long funId) {
		this.funId = funId;
	}

	public BigDecimal getFunAvailable() {
		return this.funAvailable;
	}

	public void setFunAvailable(BigDecimal funAvailable) {
		this.funAvailable = funAvailable;
	}

	public BigDecimal getFunBuiltIn() {
		return this.funBuiltIn;
	}

	public void setFunBuiltIn(BigDecimal funBuiltIn) {
		this.funBuiltIn = funBuiltIn;
	}

	public String getFunCreatedBy() {
		return this.funCreatedBy;
	}

	public void setFunCreatedBy(String funCreatedBy) {
		this.funCreatedBy = funCreatedBy;
	}

	public Date getFunCreatedDate() {
		return this.funCreatedDate;
	}

	public void setFunCreatedDate(Date funCreatedDate) {
		this.funCreatedDate = funCreatedDate;
	}

	public BigDecimal getFunDataType() {
		return this.funDataType;
	}

	public void setFunDataType(BigDecimal funDataType) {
		this.funDataType = funDataType;
	}

	public BigDecimal getFunDescriptionMn() {
		return this.funDescriptionMn;
	}

	public void setFunDescriptionMn(BigDecimal funDescriptionMn) {
		this.funDescriptionMn = funDescriptionMn;
	}

	public String getFunDescriptionS() {
		return this.funDescriptionS;
	}

	public void setFunDescriptionS(String funDescriptionS) {
		this.funDescriptionS = funDescriptionS;
	}

	public String getFunDeveloperKey() {
		return this.funDeveloperKey;
	}

	public void setFunDeveloperKey(String funDeveloperKey) {
		this.funDeveloperKey = funDeveloperKey;
	}

	public BigDecimal getFunElementState() {
		return this.funElementState;
	}

	public void setFunElementState(BigDecimal funElementState) {
		this.funElementState = funElementState;
	}

	public String getFunExtDbLink() {
		return this.funExtDbLink;
	}

	public void setFunExtDbLink(String funExtDbLink) {
		this.funExtDbLink = funExtDbLink;
	}

	public String getFunExtName() {
		return this.funExtName;
	}

	public void setFunExtName(String funExtName) {
		this.funExtName = funExtName;
	}

	public String getFunExtOwner() {
		return this.funExtOwner;
	}

	public void setFunExtOwner(String funExtOwner) {
		this.funExtOwner = funExtOwner;
	}

	public String getFunExtPackage() {
		return this.funExtPackage;
	}

	public void setFunExtPackage(String funExtPackage) {
		this.funExtPackage = funExtPackage;
	}

	public BigDecimal getFunFunctionType() {
		return this.funFunctionType;
	}

	public void setFunFunctionType(BigDecimal funFunctionType) {
		this.funFunctionType = funFunctionType;
	}

	public BigDecimal getFunHidden() {
		return this.funHidden;
	}

	public void setFunHidden(BigDecimal funHidden) {
		this.funHidden = funHidden;
	}

	public BigDecimal getFunMaximumArgs() {
		return this.funMaximumArgs;
	}

	public void setFunMaximumArgs(BigDecimal funMaximumArgs) {
		this.funMaximumArgs = funMaximumArgs;
	}

	public BigDecimal getFunMinimumArgs() {
		return this.funMinimumArgs;
	}

	public void setFunMinimumArgs(BigDecimal funMinimumArgs) {
		this.funMinimumArgs = funMinimumArgs;
	}

	public String getFunName() {
		return this.funName;
	}

	public void setFunName(String funName) {
		this.funName = funName;
	}

	public String getFunUpdatedBy() {
		return this.funUpdatedBy;
	}

	public void setFunUpdatedBy(String funUpdatedBy) {
		this.funUpdatedBy = funUpdatedBy;
	}

	public Date getFunUpdatedDate() {
		return this.funUpdatedDate;
	}

	public void setFunUpdatedDate(Date funUpdatedDate) {
		this.funUpdatedDate = funUpdatedDate;
	}

	public String getFunUserProp1() {
		return this.funUserProp1;
	}

	public void setFunUserProp1(String funUserProp1) {
		this.funUserProp1 = funUserProp1;
	}

	public String getFunUserProp2() {
		return this.funUserProp2;
	}

	public void setFunUserProp2(String funUserProp2) {
		this.funUserProp2 = funUserProp2;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public List<Eul5BqDep> getEul5BqDeps() {
		return this.eul5BqDeps;
	}

	public void setEul5BqDeps(List<Eul5BqDep> eul5BqDeps) {
		this.eul5BqDeps = eul5BqDeps;
	}

	public Eul5BqDep addEul5BqDep(Eul5BqDep eul5BqDep) {
		getEul5BqDeps().add(eul5BqDep);
		eul5BqDep.setEul5Function(this);

		return eul5BqDep;
	}

	public Eul5BqDep removeEul5BqDep(Eul5BqDep eul5BqDep) {
		getEul5BqDeps().remove(eul5BqDep);
		eul5BqDep.setEul5Function(null);

		return eul5BqDep;
	}

	public List<Eul5Expression> getEul5Expressions() {
		return this.eul5Expressions;
	}

	public void setEul5Expressions(List<Eul5Expression> eul5Expressions) {
		this.eul5Expressions = eul5Expressions;
	}

	public Eul5Expression addEul5Expression(Eul5Expression eul5Expression) {
		getEul5Expressions().add(eul5Expression);
		eul5Expression.setEul5Function(this);

		return eul5Expression;
	}

	public Eul5Expression removeEul5Expression(Eul5Expression eul5Expression) {
		getEul5Expressions().remove(eul5Expression);
		eul5Expression.setEul5Function(null);

		return eul5Expression;
	}

	public List<Eul5ExpDep> getEul5ExpDeps1() {
		return this.eul5ExpDeps1;
	}

	public void setEul5ExpDeps1(List<Eul5ExpDep> eul5ExpDeps1) {
		this.eul5ExpDeps1 = eul5ExpDeps1;
	}

	public Eul5ExpDep addEul5ExpDeps1(Eul5ExpDep eul5ExpDeps1) {
		getEul5ExpDeps1().add(eul5ExpDeps1);
		eul5ExpDeps1.setEul5Function1(this);

		return eul5ExpDeps1;
	}

	public Eul5ExpDep removeEul5ExpDeps1(Eul5ExpDep eul5ExpDeps1) {
		getEul5ExpDeps1().remove(eul5ExpDeps1);
		eul5ExpDeps1.setEul5Function1(null);

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
		eul5ExpDeps2.setEul5Function2(this);

		return eul5ExpDeps2;
	}

	public Eul5ExpDep removeEul5ExpDeps2(Eul5ExpDep eul5ExpDeps2) {
		getEul5ExpDeps2().remove(eul5ExpDeps2);
		eul5ExpDeps2.setEul5Function2(null);

		return eul5ExpDeps2;
	}

	public List<Eul5FunArgument> getEul5FunArguments() {
		return this.eul5FunArguments;
	}

	public void setEul5FunArguments(List<Eul5FunArgument> eul5FunArguments) {
		this.eul5FunArguments = eul5FunArguments;
	}

	public Eul5FunArgument addEul5FunArgument(Eul5FunArgument eul5FunArgument) {
		getEul5FunArguments().add(eul5FunArgument);
		eul5FunArgument.setEul5Function(this);

		return eul5FunArgument;
	}

	public Eul5FunArgument removeEul5FunArgument(Eul5FunArgument eul5FunArgument) {
		getEul5FunArguments().remove(eul5FunArgument);
		eul5FunArgument.setEul5Function(null);

		return eul5FunArgument;
	}

	public List<Eul5FunFcLink> getEul5FunFcLinks() {
		return this.eul5FunFcLinks;
	}

	public void setEul5FunFcLinks(List<Eul5FunFcLink> eul5FunFcLinks) {
		this.eul5FunFcLinks = eul5FunFcLinks;
	}

	public Eul5FunFcLink addEul5FunFcLink(Eul5FunFcLink eul5FunFcLink) {
		getEul5FunFcLinks().add(eul5FunFcLink);
		eul5FunFcLink.setEul5Function(this);

		return eul5FunFcLink;
	}

	public Eul5FunFcLink removeEul5FunFcLink(Eul5FunFcLink eul5FunFcLink) {
		getEul5FunFcLinks().remove(eul5FunFcLink);
		eul5FunFcLink.setEul5Function(null);

		return eul5FunFcLink;
	}

	public List<Eul5SumoExpUsg> getEul5SumoExpUsgs1() {
		return this.eul5SumoExpUsgs1;
	}

	public void setEul5SumoExpUsgs1(List<Eul5SumoExpUsg> eul5SumoExpUsgs1) {
		this.eul5SumoExpUsgs1 = eul5SumoExpUsgs1;
	}

	public Eul5SumoExpUsg addEul5SumoExpUsgs1(Eul5SumoExpUsg eul5SumoExpUsgs1) {
		getEul5SumoExpUsgs1().add(eul5SumoExpUsgs1);
		eul5SumoExpUsgs1.setEul5Function1(this);

		return eul5SumoExpUsgs1;
	}

	public Eul5SumoExpUsg removeEul5SumoExpUsgs1(Eul5SumoExpUsg eul5SumoExpUsgs1) {
		getEul5SumoExpUsgs1().remove(eul5SumoExpUsgs1);
		eul5SumoExpUsgs1.setEul5Function1(null);

		return eul5SumoExpUsgs1;
	}

	public List<Eul5SumoExpUsg> getEul5SumoExpUsgs2() {
		return this.eul5SumoExpUsgs2;
	}

	public void setEul5SumoExpUsgs2(List<Eul5SumoExpUsg> eul5SumoExpUsgs2) {
		this.eul5SumoExpUsgs2 = eul5SumoExpUsgs2;
	}

	public Eul5SumoExpUsg addEul5SumoExpUsgs2(Eul5SumoExpUsg eul5SumoExpUsgs2) {
		getEul5SumoExpUsgs2().add(eul5SumoExpUsgs2);
		eul5SumoExpUsgs2.setEul5Function2(this);

		return eul5SumoExpUsgs2;
	}

	public Eul5SumoExpUsg removeEul5SumoExpUsgs2(Eul5SumoExpUsg eul5SumoExpUsgs2) {
		getEul5SumoExpUsgs2().remove(eul5SumoExpUsgs2);
		eul5SumoExpUsgs2.setEul5Function2(null);

		return eul5SumoExpUsgs2;
	}

	public List<Eul5SumBitmap> getEul5SumBitmaps() {
		return this.eul5SumBitmaps;
	}

	public void setEul5SumBitmaps(List<Eul5SumBitmap> eul5SumBitmaps) {
		this.eul5SumBitmaps = eul5SumBitmaps;
	}

	public Eul5SumBitmap addEul5SumBitmap(Eul5SumBitmap eul5SumBitmap) {
		getEul5SumBitmaps().add(eul5SumBitmap);
		eul5SumBitmap.setEul5Function(this);

		return eul5SumBitmap;
	}

	public Eul5SumBitmap removeEul5SumBitmap(Eul5SumBitmap eul5SumBitmap) {
		getEul5SumBitmaps().remove(eul5SumBitmap);
		eul5SumBitmap.setEul5Function(null);

		return eul5SumBitmap;
	}

}