package com.cursodsouza.libraryapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursodsouza.libraryapi.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

	boolean existsByIsbn(String isbn);

}
