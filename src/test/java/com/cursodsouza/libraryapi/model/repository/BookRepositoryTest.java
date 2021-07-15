package com.cursodsouza.libraryapi.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import javax.validation.constraints.AssertTrue;

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
		Book book = createNewBook(isbn);
		entityManager.persist(book);

		// execucao
		boolean exists = repository.existsByIsbn(isbn);

		// verificação
		assertThat(exists).isTrue();

	}

	public Book createNewBook(String isbn) {
		return Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
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

	@Test
	@DisplayName("Deve obter um livro por id.")
	public void findByIdTest() {
		// cenario
		Book book = createNewBook("123");
		entityManager.persist(book);
//	execução
		Optional<Book> foundBook = repository.findById(book.getId());
		// verificação
		assertThat(foundBook.isPresent()).isTrue();

	}

	@Test
	@DisplayName("Deve salvar um livro.")
	public void saveBookTest() {
		// cenario
		Book book = createNewBook("123");
		// execução
		Book savedBook = repository.save(book);

		// verificação
		assertThat(savedBook.getId()).isNotNull();

	}

	@Test
	@DisplayName("Deve deletar um livro")
	public void deleteBookTest() {
		// cenario
		Book book = createNewBook("123");
		entityManager.persist(book);

		// Execução
		Book foundBook = entityManager.find(Book.class, book.getId());
		repository.delete(foundBook);

		Book deletedBook = entityManager.find(Book.class, book.getId());
		// verificação		
		assertThat(deletedBook).isNull();

	}

}
