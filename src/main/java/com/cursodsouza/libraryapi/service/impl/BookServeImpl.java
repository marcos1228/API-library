package com.cursodsouza.libraryapi.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cursodsouza.libraryapi.api.exception.BusinessException;
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
		if (repository.existsByIsbn(book.getIsbn())) {
			throw new BusinessException("Isbn já cadastrado");
		}
		return repository.save(book);
	}

	@Override
	public Optional<Book> getById(Long id) {
		return this.repository.findById(id);
	}

	@Override
	public void delete(Book book) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Book update(Book book) {
		// TODO Auto-generated method stub
		return null;
	}

}
