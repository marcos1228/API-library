package com.cursodsouza.libraryapi.api.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BooKDTO {

	
	private Long id;
	@NotEmpty
	private String title;
	@NotEmpty
	private String author;
	@NotEmpty
	private String isbn;
}