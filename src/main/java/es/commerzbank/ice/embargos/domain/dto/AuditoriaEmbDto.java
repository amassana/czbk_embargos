package es.commerzbank.ice.embargos.domain.dto;

import java.time.LocalDateTime;

public class AuditoriaEmbDto {

	private Long codAuditoria;
	
	private String valorPk;
	
	private String pkLogico;
	
	private String tabla;

	private LocalDateTime fecha;
	
	private String usuario;
	
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
