package com.cursodsouza.libraryapi.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cursodsouza.libraryapi.model.entity.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	BookRepository repository;

	@Test
	@DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
	public void returnTrueWhenIsbnExists() {
		// cenario
		String isbn = "123";
		Book book = Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
		entityManager.persist(book);

		// execucao
		boolean exists = repository.existsByIsbn(isbn);

		// verificação
		assertThat(exists).isTrue();

	}

	@Test
	@DisplayName("Deve retornar falso quando existir um livro na base com o isbn informado")
	public void returnFalseWhenIsbnExists() {
		// cenario
		String isbn = "123";

		// execucao
		boolean exists = repository.existsByIsbn(isbn);

		// verificação
		assertThat(exists).isFalse();

	}
}
