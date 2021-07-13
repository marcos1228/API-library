package com.cursodsouza.libraryapi.service;

import java.util.Optional;

import com.cursodsouza.libraryapi.model.entity.Book;

public interface BookService {

	Book save(Book any);

	Optional<Book> getById(Long id);

	void delete(Book book);

}
