package com.security.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersControllers {
	
	@GetMapping("/")
	public ResponseEntity<?> rutaRaiz() {

		return ResponseEntity.ok("ruta raiz");
	}

	@GetMapping("/index")
	public ResponseEntity<?> raiz() {

		return ResponseEntity.ok("ruta despues de loguearme correctamente");
	}
	
	@GetMapping("/names")
	public ResponseEntity<?> getUsers() {

		return ResponseEntity.ok("url asegurada");
	}
	
	
	@GetMapping("/dir")
	public ResponseEntity<?> usersDireccion() {

		return ResponseEntity.ok("url libre");
	}
}
