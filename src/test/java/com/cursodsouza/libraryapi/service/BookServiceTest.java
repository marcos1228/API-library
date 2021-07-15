package com.cursodsouza.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

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

	@Test
	@DisplayName("Deve obter um livro por ID")
	public void getByIdTest() {
		// Cenario
		Long id = 1l;
		Book book = createValidBook();
		book.setId(id);
		;
		Mockito.when(repositpory.findById(id)).thenReturn(Optional.of(book));

		// execucao

		Optional<Book> foundBook = service.getById(id);

		// verificações
		assertThat(foundBook.isPresent()).isTrue();
		assertThat(foundBook.get().getId()).isEqualTo(id);
		assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
		assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
		assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());

	}

	@Test
	@DisplayName("Deve retornar vazio ao obter um livro por id quando ele não existe na base.")
	public void bookNotFoundByIdTest() {
		// Cenario
		Long id = 1l;
		Mockito.when(repositpory.findById(id)).thenReturn(Optional.empty());
		// execucao
		Optional<Book> book = service.getById(id);
		// verificações
		assertThat(book.isPresent()).isFalse();

	}

	@Test
	@DisplayName("Deve deletar um livro.")
	public void deleteBookTest() {
		// cenario
		Book book = Book.builder().id(1L).build();
		// execução
		org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(book));
		// verifuicação
		Mockito.verify(repositpory, Mockito.times(1)).delete(book);

	}

	@Test
	@DisplayName("Deve ocorrer erro ao tentar deletar um livro inexistente.")
	public void deleteInvalidBookTest() {
		// cenario
		Book book = new Book();
		// execução
		org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));
		// verificação
		Mockito.verify(repositpory, Mockito.never()).delete(book);
	}

	@Test
	@DisplayName("Deve atualizar um livro.")
	public void updateBookTest() {
		// cenario
		Long id = 1l;

		// livro a atualizar
		Book updatingBook = Book.builder().id(1L).build();
		// simulação
		Book updatedBook = createValidBook();
		updatedBook.setId(id);
		Mockito.when(repositpory.save(updatingBook)).thenReturn(updatedBook);
		// execução
		Book book = service.update(updatingBook);

		// verificações
		assertThat(book.getId()).isEqualTo(updatedBook.getId());
		assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
		assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
		assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
	}

	@Test
	@DisplayName("Deve ocorrer erro ao tentar atualizar  um livro inexistente.")
	public void updateInvalidBookTest() {
		// cenario
		Book book = new Book();
		// execução
		org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));
		// verificação
		Mockito.verify(repositpory, Mockito.never()).save(book);
	}

	public Book createValidBook() {
		return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
	}

}
