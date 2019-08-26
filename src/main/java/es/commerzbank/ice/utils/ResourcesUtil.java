package es.commerzbank.ice.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ResourcesUtil {

	public static Resource getImageLogoCommerceResource() {
		return getFromJasperFolder("images/commerce_bank_logo.png");
	}

	public static Resource getReportHeaderResource() {
		return getFromJasperFolder("header_sucursal.jasper");
	}
	
	public static Resource getFromJasperFolder(String path) {
		return new ClassPathResource("jasper/" + path);
	}
	
	public static Resource getTemplateStyleResource() {
		return getFromJasperFolder("CommerzBankStyle.jrtx");
	}

}
