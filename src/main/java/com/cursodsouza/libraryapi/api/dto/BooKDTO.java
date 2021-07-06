package com.cursodsouza.libraryapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BooKDTO {

	private Long id;
	private String title;
	private String author;
	private String isbn;

}
