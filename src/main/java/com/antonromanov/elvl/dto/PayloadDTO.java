package com.antonromanov.elvl.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayloadDTO implements ResponseDTO{
	private Object body;
}
