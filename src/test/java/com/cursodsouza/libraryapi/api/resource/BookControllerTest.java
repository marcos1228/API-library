package com.cursodsouza.libraryapi.api.resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.cursodsouza.libraryapi.api.dto.BooKDTO;
import com.cursodsouza.libraryapi.api.exception.BusinessException;
import com.cursodsouza.libraryapi.model.entity.Book;
import com.cursodsouza.libraryapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

/*@RunWith(SpringRunner.class) -versão do junit 4*/
@ExtendWith(SpringExtension.class) // versaão do junit 5
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

	static String BOOK_API = "/api/books";

	@Autowired
	MockMvc mvc;

	@MockBean // mock especializado e colocar dentro do contexto de depêndencia
	BookService service;

	@Test
	@DisplayName("Deve criar um livro com sucesso.")
	public void createBookTest() throws Exception {

		BooKDTO dto = createNewBook();

		Book saveBook = Book.builder().id(10l).author("Artur").title("As aventuras").isbn("001").build();

		BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(saveBook);

		String json = new ObjectMapper().writeValueAsString(dto);

		// criando a requisição
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		// fazendo a requisição
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("title").value(dto.getTitle()))
				.andExpect(MockMvcResultMatchers.jsonPath("author").value(dto.getAuthor()))
				.andExpect(MockMvcResultMatchers.jsonPath("isbn").value(dto.getIsbn()))

		;
	}

	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficiente para a criação do livro.")
	public void createInvalidBookTest() throws Exception {

		String json = new ObjectMapper().writeValueAsString(new BooKDTO());
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(3)));

	}

	@Test
	@DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn já utilizado por outro.")
	public void createBookWithDuplicatedIsbn() throws Exception {

		BooKDTO dto = createNewBook();

		String json = new ObjectMapper().writeValueAsString(dto);
		String mensagemErro = "isbn já cadastrado.";
		BDDMockito.given(service.save(Mockito.any(Book.class))).willThrow(new BusinessException(mensagemErro));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(mensagemErro));

	}

	@Test
	@DisplayName("Deve obter informaçõ")
	public void getBookDetailsTest() throws Exception {
		// cenario (given dado BDD)
		Long id = 1l;

		Book book = Book.builder().id(id).title(createNewBook().getTitle()).author(createNewBook().getAuthor())
				.isbn(createNewBook().getIsbn()).build();
		BDDMockito.given(service.getById(id)).willReturn(Optional.of(book));

		// execucao (when, criar requisição)
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat("/" + id))
				.accept(MediaType.APPLICATION_JSON);

		// verificação
		mvc.perform(request).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("title").value(createNewBook().getTitle()))
				.andExpect(MockMvcResultMatchers.jsonPath("author").value(createNewBook().getAuthor()))
				.andExpect(MockMvcResultMatchers.jsonPath("isbn").value(createNewBook().getIsbn()));

	}

	@Test
	@DisplayName("Deve retornar resource not found quando o livro procurado não existir")
	public void bookNotFoundTest() throws Exception {
		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat("/" + 1))
				.accept(MediaType.APPLICATION_JSON);

		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());

	}

	@Test
	@DisplayName("Deve deletar  um livro")
	public void deleteBookTest() throws Exception {
		// cenario
		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.of(Book.builder().id(1l).build()));

		// execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(BOOK_API.concat("/" + 1));

		// Verificação

		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNoContent());

	}

	@Test
	@DisplayName("Deve retornar resource not found quando não encontrar o livro para deletar")
	public void deleteInexistentBookTest() throws Exception {
		// cenario
		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());

		// execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(BOOK_API.concat("/" + 1));

		// Verificação

		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());

	}

	@Test
	@DisplayName("Deve atualizar um livro ")
	public void updateBookTest() throws Exception {
		// Cenario (given)
		Long id = 1l;
		String json = new ObjectMapper().writeValueAsString(createNewBook());
		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.of(Book.builder().id(1l).build()));

		// execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(BOOK_API.concat("/" + 1)
				.);

		// Verificação

		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNoContent());

	}

	@Test
	@DisplayName("Deve retornar 404 ao tentar atualizar um livro  inexistente")
	public void updateInexistentBookTest() throws Exception {
		

	}

	public BooKDTO createNewBook() {
		return BooKDTO.builder().author("Artur").title("As aventuras").isbn("001").build();
	}
}
