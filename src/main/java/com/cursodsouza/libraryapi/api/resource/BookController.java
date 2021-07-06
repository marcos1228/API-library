package com.cursodsouza.libraryapi.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cursodsouza.libraryapi.api.dto.BooKDTO;

@RestController
@RequestMapping("/api/books")
public class BookController {

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BooKDTO create(@RequestBody BooKDTO dto) {
		return dto;

	}
}
