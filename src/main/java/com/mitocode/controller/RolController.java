package com.mitocode.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitocode.dto.RolDTO;
import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Rol;
import com.mitocode.service.IRolService;

@RestController
@RequestMapping("/roles")
public class RolController {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private IRolService service;
	
	
	@GetMapping("/{usuario}")
	public ResponseEntity<RolDTO> rolPorUsuario(@PathVariable("usuario") String usuario) throws Exception{
		RolDTO dtoResponse;
		Rol obj = service.rolPorUsuario(usuario);
		if(obj == null) {
			throw new ModeloNotFoundException("NOMBRE NO ENCONTRADO " + usuario);
		}else {
			dtoResponse = modelMapper.map(obj, RolDTO.class);
		}
		return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
	}

}
