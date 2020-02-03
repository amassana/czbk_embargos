package es.commerzbank.ice.embargos.domain.dto;

import java.time.LocalDate;

public class AuditoriaEmbFilter {

	private String pkLogico;
	
	private String tabla;

	private String tipo;
	
	private String usuario;
	
	private LocalDate inicio;

	private LocalDate fin;

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public LocalDate getInicio() {
		return inicio;
	}

	public void setInicio(LocalDate inicio) {
		this.inicio = inicio;
	}

	public LocalDate getFin() {
		return fin;
	}

	public void setFin(LocalDate fin) {
		this.fin = fin;
	}

	public String getPkLogico() {
		return pkLogico;
	}

	public void setPkLogico(String pkLogico) {
		this.pkLogico = pkLogico;
	}
}
