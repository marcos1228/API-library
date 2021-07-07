package com.cursodsouza.libraryapi.api.dto;

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
	private String title;
	private String author;
	private String isbn;
}