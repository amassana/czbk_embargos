package es.commerzbank.ice.embargos.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.commerzbank.ice.embargos.domain.dto.Representative;

public interface RepresentativeService {

	boolean createUpdateRepresentative(Representative representative, String user);

	boolean deleteRepresentative(Long idRepresentative);

	Representative viewRepresentative(Long idRepresentative);

	Page<Representative> filter(Map<String, Object> parametros, Pageable dataPage);

}
