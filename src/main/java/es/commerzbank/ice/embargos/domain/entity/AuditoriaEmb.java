package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="AUDITORIA")
@NamedQuery(name="AuditoriaEmb.findAll", query="SELECT a FROM AuditoriaEmb a")
public class AuditoriaEmb implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "auditoria_seq_gen", sequenceName = "AUDITORIA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auditoria_seq_gen")
	@Column(name="COD_AUDITORIA", unique=true, nullable=false)
	private Long codAuditoria;
	
	@Column(name="VALOR_PK")
	private String valorPk;
	
	@Column(name="PK_LOGICO")
	private String pkLogico;
	
	@Column(name="TABLA")
	private String tabla;

	@Column(name="FECHA")
	private LocalDateTime fecha;
	
	@Column(name="USUARIO")
	private String usuario;
	
	@Column(name="NEW_VALUE")
	private String newValue;

	public Long getCodAuditoria() {
		return codAuditoria;
	}

	public void setCodAuditoria(Long codAuditoria) {
		this.codAuditoria = codAuditoria;
	}

	public String getValorPk() {
		return valorPk;
	}

	public void setValorPk(String valorPk) {
		this.valorPk = valorPk;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getPkLogico() {
		return pkLogico;
	}

	public void setPkLogico(String pkLogico) {
		this.pkLogico = pkLogico;
	}
	
}
