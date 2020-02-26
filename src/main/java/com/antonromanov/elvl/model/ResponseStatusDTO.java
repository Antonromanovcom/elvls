package com.antonromanov.elvl.model;

import lombok.Builder;

@Builder
public class ResponseStatusDTO {
	private String status;
	private String errorMessage;
	private String okMessage;
}
