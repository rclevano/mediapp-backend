package com.mitocode.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mitocode.dto.ConsultaResumenDTO;
import com.mitocode.dto.EspecialidadDTO;
import com.mitocode.dto.SignoVitalDTO;
import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Especialidad;
import com.mitocode.model.SignoVital;
import com.mitocode.service.IEspecialidadService;
import com.mitocode.service.ISignoVitalService;

@RestController
@RequestMapping("/signosvitales")
public class SignosVitalesController {
	
	@Autowired
	private ISignoVitalService service;
	
	@Autowired
	private ModelMapper mapper;

	@GetMapping
	public ResponseEntity<List<SignoVitalDTO>> listar() throws Exception{
		List<SignoVitalDTO> lista = service.listar().stream().map(p -> mapper.map(p, SignoVitalDTO.class)).collect(Collectors.toList());
		return new ResponseEntity<>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	//@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<SignoVitalDTO> listarPorId(@PathVariable("id") Integer id) throws Exception{
		SignoVitalDTO dtoResponse;
		SignoVital obj = service.listarPorId(id);
		if(obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO " + id);
		}else {
			dtoResponse = mapper.map(obj, SignoVitalDTO.class);
		}
		return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
	}
	
	/*@PostMapping
	public ResponseEntity<EspecialidadDTO> registrar(@Valid @RequestBody EspecialidadDTO dtoRequest) throws Exception {
		Especialidad p = mapper.map(dtoRequest, Especialidad.class);
		Especialidad obj = service.registrar(p);
		EspecialidadDTO dtoResponse = mapper.map(obj, EspecialidadDTO.class);
		return new ResponseEntity<>(dtoResponse, HttpStatus.CREATED);
	}*/
	
	@PostMapping
	public ResponseEntity<Void> registrar(@Valid @RequestBody SignoVitalDTO dtoRequest) throws Exception {
		SignoVital p = mapper.map(dtoRequest, SignoVital.class);
		SignoVital obj = service.registrar(p);
		SignoVitalDTO dtoResponse = mapper.map(obj, SignoVitalDTO.class);
		//localhost:8080/Especialidads/9
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dtoResponse.getIdSignoVital()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<SignoVitalDTO> modificar(@Valid @RequestBody SignoVitalDTO dtoRequest) throws Exception {
		SignoVital pac = service.listarPorId(dtoRequest.getIdSignoVital());
		
		if(pac == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO " + dtoRequest.getIdSignoVital());	
		}		
		
		SignoVital p = mapper.map(dtoRequest, SignoVital.class);		 
		SignoVital obj = service.modificar(p);		
		SignoVitalDTO dtoResponse = mapper.map(obj, SignoVitalDTO.class);
		
		return new ResponseEntity<>(dtoResponse, HttpStatus.OK);		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception {
		SignoVital sig = service.listarPorId(id);
		
		if(sig == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO " + id);
		}
		
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<SignoVitalDTO> listarHateoas (@PathVariable("id") Integer id) throws Exception{
		SignoVital obj = service.listarPorId(id);
		
		if(obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO " + id);
		}
		
		SignoVitalDTO dto = mapper.map(obj, SignoVitalDTO.class);
		
		EntityModel<SignoVitalDTO> recurso = EntityModel.of(dto);
		//localhost:8080/Especialidads/1
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarPorId(id));
		
		recurso.add(link1.withRel("signovital-link"));
		return recurso;
	}
	
}
