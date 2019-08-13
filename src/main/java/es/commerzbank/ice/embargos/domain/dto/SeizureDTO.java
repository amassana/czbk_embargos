package es.commerzbank.ice.embargos.domain.dto;

import java.math.BigDecimal;

public class SeizureDTO
{
    private String idSeizure;
    private String idSeizureRequest;
    private String NIF;
    private String name;
    private String nameInternal;
    private SeizureStatusDTO status;
    private BigDecimal requestedAmount;

    public String getIdSeizure() {
        return idSeizure;
    }

    public void setIdSeizure(String idSeizure) {
        this.idSeizure = idSeizure;
    }

    public String getIdSeizureRequest() {
        return idSeizureRequest;
    }

    public void setIdSeizureRequest(String idSeizureRequest) {
        this.idSeizureRequest = idSeizureRequest;
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameInternal() {
        return nameInternal;
    }

    public void setNameInternal(String nameInternal) {
        this.nameInternal = nameInternal;
    }

    public SeizureStatusDTO getStatus() {
        return status;
    }

    public void setStatus(SeizureStatusDTO status) {
        this.status = status;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
}
