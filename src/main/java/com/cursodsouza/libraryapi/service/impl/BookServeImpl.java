package com.cursodsouza.libraryapi.service.impl;

import org.springframework.stereotype.Service;

import com.cursodsouza.libraryapi.model.entity.Book;
import com.cursodsouza.libraryapi.model.repository.BookRepository;
import com.cursodsouza.libraryapi.service.BookService;

@Service
public class BookServeImpl implements BookService {

	private BookRepository repository;

	public BookServeImpl(BookRepository repository) {

		this.repository = repository;
	}

	@Override
	public Book save(Book book) {

		return repository.save(book);
	}

}
