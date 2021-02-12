package es.commerzbank.ice.embargos.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ResourcesUtil {

	public static Resource getImageLogoCommerceResource() {
		return getFromJasperFolder("images/commerce_bank_logo.png");
	}

	public static Resource getReportHeaderResource() {
		return getFromJasperFolder("header_sucursal.jasper");
	}
	
//	public static Resource getSimpleHeader() {
//		return getFromJasperFolder("SimpleHeader.jasper");
//	}
	
	public static Resource getFromJasperFolder(String path) {
		return new ClassPathResource("reports/" + path);
	}
	
	public static Resource getTemplateStyleResource() {
		return getFromJasperFolder("CommerzBankStyle.jrtx");
	}

}
