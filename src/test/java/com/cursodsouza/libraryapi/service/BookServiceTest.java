package com.cursodsouza.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cursodsouza.libraryapi.api.exception.BusinessException;
import com.cursodsouza.libraryapi.model.entity.Book;
import com.cursodsouza.libraryapi.model.repository.BookRepository;
import com.cursodsouza.libraryapi.service.impl.BookServeImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

	BookService service;

	@MockBean
	BookRepository repositpory;

	@BeforeEach
	public void setUp() {
		this.service = new BookServeImpl(repositpory);
	}

	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBookTest() {
		// Cenario
		Book book = createValidBook();
		Mockito.when(repositpory.existsByIsbn(Mockito.anyString())).thenReturn(false);
		Mockito.when(repositpory.save(book))
				.thenReturn(Book.builder().id(1l).isbn("123").author("Fulano").title("As aventuras").build());

		// execao
		Book savedBook = service.save(book);

		// verificação
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getIsbn()).isEqualTo("123");
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
	}

	@Test
	@DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
	public void shouldNotSaveABookWithDuplicatedISBN() {
		// cenario
		Book book = createValidBook();

		Mockito.when(repositpory.existsByIsbn(Mockito.anyString())).thenReturn(true);

		// execução
		Throwable excption = Assertions.catchThrowable(() -> service.save(book));

		// verificação
		assertThat(excption).isInstanceOf(BusinessException.class).hasMessage("Isbn já cadastrado");

		Mockito.verify(repositpory, Mockito.never()).save(book);

	}

	public Book createValidBook() {
		return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
	}

}
