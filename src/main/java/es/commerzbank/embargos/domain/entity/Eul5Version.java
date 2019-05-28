package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the EUL5_VERSIONS database table.
 * 
 */
@Entity
@Table(name="EUL5_VERSIONS")
@NamedQuery(name="Eul5Version.findAll", query="SELECT e FROM Eul5Version e")
public class Eul5Version implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="VER_DESCRIPTION", length=240)
	private String verDescription;

	@Column(name="VER_EUL_TIMESTAMP", nullable=false, length=30)
	private String verEulTimestamp;

	@Column(name="VER_MIN_CODE_VER", nullable=false, length=30)
	private String verMinCodeVer;

	@Column(name="VER_NAME", length=100)
	private String verName;

	@Column(name="VER_RELEASE", nullable=false, length=30)
	private String verRelease;

	@Column(name="VER_SA", precision=22)
	private BigDecimal verSa;

	public Eul5Version() {
	}

	public String getVerDescription() {
		return this.verDescription;
	}

	public void setVerDescription(String verDescription) {
		this.verDescription = verDescription;
	}

	public String getVerEulTimestamp() {
		return this.verEulTimestamp;
	}

	public void setVerEulTimestamp(String verEulTimestamp) {
		this.verEulTimestamp = verEulTimestamp;
	}

	public String getVerMinCodeVer() {
		return this.verMinCodeVer;
	}

	public void setVerMinCodeVer(String verMinCodeVer) {
		this.verMinCodeVer = verMinCodeVer;
	}

	public String getVerName() {
		return this.verName;
	}

	public void setVerName(String verName) {
		this.verName = verName;
	}

	public String getVerRelease() {
		return this.verRelease;
	}

	public void setVerRelease(String verRelease) {
		this.verRelease = verRelease;
	}

	public BigDecimal getVerSa() {
		return this.verSa;
	}

	public void setVerSa(BigDecimal verSa) {
		this.verSa = verSa;
	}

}