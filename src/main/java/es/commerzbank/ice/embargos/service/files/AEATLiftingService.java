package es.commerzbank.ice.embargos.service.files;


import java.io.File;

public interface AEATLiftingService
{
	public void tratarFicheroLevantamientos(File file, String originalName, File processedFile) throws Exception;
}
