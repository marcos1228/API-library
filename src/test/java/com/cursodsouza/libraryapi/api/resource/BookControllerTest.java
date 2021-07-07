package com.cursodsouza.libraryapi.api.resource;

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
	@DisplayName("Deve criar um livro com sucesso")
	public void createBookTest() throws Exception {

		BooKDTO dto = BooKDTO.builder().author("Artur").title("As aventuras").isbn("001").build();

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
	@DisplayName("Deve lançar erro de validação quando não houver dados suficiente para a criação do livro")
	public void createInvalidBookTest() throws Exception{

		String json = new ObjectMapper().writeValueAsString(new BooKDTO());
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(3)))
		;
		
		
	}

}
