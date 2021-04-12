package es.commerzbank.ice.embargos.service;

public interface FinalResponseGenerationService
{
    void calcFinalResult(Long codeFileControlFase3, String user) throws Exception;
}
