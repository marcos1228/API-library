package com.cursodsouza.libraryapi.api.resource;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cursodsouza.libraryapi.api.dto.BooKDTO;
import com.cursodsouza.libraryapi.api.exception.ApiErrors;
import com.cursodsouza.libraryapi.model.entity.Book;
import com.cursodsouza.libraryapi.service.BookService;

import antlr.collections.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private BookService service;
	private ModelMapper modelMapper;

	public BookController(BookService service, ModelMapper mapper) {
		this.service = service;
		this.modelMapper = mapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BooKDTO create(@RequestBody  @Valid BooKDTO dto) {
		Book entity = modelMapper.map(dto, Book.class);
		entity = service.save(entity);
		return modelMapper.map(entity, BooKDTO.class);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handlerValidationExceptions(MethodArgumentNotValidException ex) {
	BindingResult bindingResult =	ex.getBindingResult();
	return new ApiErrors(bindingResult);
		
	}
}
