package es.commerzbank.ice.embargos.controller;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import es.commerzbank.ice.comun.lib.security.Permissions;
import es.commerzbank.ice.embargos.domain.dto.OrderingEntity;
import es.commerzbank.ice.embargos.domain.dto.OrderingEntityCsv;
import es.commerzbank.ice.embargos.domain.mapper.OrderingEntityMapper;
import es.commerzbank.ice.embargos.service.OrderingEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/orderingEntity")
public class OrderingEntityController {
	private static final Logger logger = LoggerFactory.getLogger(OrderingEntityController.class);
	
	@Autowired
	private OrderingEntityService orderingEntityService;
	
	@Autowired
	private OrderingEntityMapper orderingEntityMapper;
	
	
	@PostMapping(value = "",
	        produces = { "application/json" }, 
	        consumes = { "application/json" })
	public ResponseEntity<Void> create(Authentication authentication, @RequestBody OrderingEntity orderingEntity) {
		logger.info("OrderingEntityController - create - start");
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("OrderingEntityController - create - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (orderingEntity != null) {
			result = orderingEntityService.createUpdateOrderingEntity(orderingEntity, authentication.getName());
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		logger.info("OrderingEntityController - create - end");
		return response;
	}
	
	@PutMapping(value = "",
	        produces = { "application/json" }, 
	        consumes = { "application/json" })
	public ResponseEntity<Void> update(Authentication authentication,  @RequestBody OrderingEntity orderingEntity) {
		logger.info("OrderingEntityController - update - start");
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("OrderingEntityController - update - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (orderingEntity != null) {
			result = orderingEntityService.createUpdateOrderingEntity(orderingEntity, authentication.getName());
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		logger.info("OrderingEntityController - update - end");
		return response;
	}
	
	@DeleteMapping(value = "/{idOrderingEntity}",
	        produces = { "application/json" })
	public ResponseEntity<Void> delete(Authentication authentication, @PathVariable("idOrderingEntity") Long idOrderingEntity) {
		logger.info("OrderingEntityController - delete - start");
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("OrderingEntityController - delete - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (idOrderingEntity != null && idOrderingEntity > 0) {
			result = orderingEntityService.deleteOrderingEntity(idOrderingEntity, authentication.getName());
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		logger.info("OrderingEntityController - delete - end");
		return response;
	}
	
	@GetMapping(value = "/{idOrderingEntity}",
	        produces = { "application/json" })
	public ResponseEntity<OrderingEntity> view(Authentication authentication, @PathVariable("idOrderingEntity") Long idOrderingEntity) {
		ResponseEntity<OrderingEntity> response = null;
		OrderingEntity result = null;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("OrderingEntityController - view - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (idOrderingEntity != null && idOrderingEntity > 0) {
			result = orderingEntityService.viewOrderingEntity(idOrderingEntity);
			
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}

		return response;
	}
	
	@PostMapping(value = "/export")
	public void exportCSV(Authentication authentication, HttpServletResponse response) throws Exception {
		try {
	        //set file name and content type
	        response.setContentType("text/csv");
	        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
	                "attachment; filename=\"" + "orderingEntity.csv" + "\"");

			response.getWriter().println("sep=,");
	
	        //create a csv writer
	        StatefulBeanToCsv<OrderingEntityCsv> writer = new StatefulBeanToCsvBuilder<OrderingEntityCsv>(response.getWriter())
					.withApplyQuotesToAll(true)
					.withEscapechar('"')
					.withQuotechar('"')
					.withSeparator(',')
					.withThrowExceptions(true)
					.withOrderedResults(false)
	                .build();
	
	        //write all to csv file
			Page<OrderingEntity> list = orderingEntityService.filter(Pageable.unpaged());
			
			if (list!=null) {
				List<OrderingEntityCsv> listOrderingEntityCsv = orderingEntityMapper.toOrderingEntityCsv(list.getContent());
		        writer.write(listOrderingEntityCsv);
			}
	        
		} catch(Exception e) {
			logger.error("Error - OrderingEntityController - export", e);
		}
	}
	
	@PostMapping(value = "/filter",
			produces = {"application/json"})
	public ResponseEntity<Page<OrderingEntity>> filter(Authentication authentication, Pageable dataPage) {
		ResponseEntity<Page<OrderingEntity>> response = null;
		Page<OrderingEntity> list = null;
		
		list = orderingEntityService.filter(dataPage);
		
		if (list != null) {
			if (list.getContent().size() > 0) {
				response = new ResponseEntity<>(list, HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
			}
		} else {
			response = new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
		}

		return response;
	}
	
	@GetMapping(value = "/listAll",
			produces = {"application/json"})
	public ResponseEntity<List<OrderingEntity>> listAll(Authentication authentication) {
		ResponseEntity<List<OrderingEntity>> response = null;
		List<OrderingEntity> list = null;
		
		list = orderingEntityService.listAll();
		
		if (list != null) {
			if (list.size() > 0) {
				response = new ResponseEntity<>(list, HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
			}
		} else {
			response = new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
		}

		return response;
	}
}
