package es.commerzbank.ice.embargos.service.files;


import java.io.File;
import java.io.IOException;

public interface AEATLiftingService
{
	public void tratarFicheroLevantamientos(File file, String originalName, File processedFile) throws IOException;
}
