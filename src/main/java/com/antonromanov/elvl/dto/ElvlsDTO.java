package com.antonromanov.elvl.dto;

import com.antonromanov.elvl.model.Elvls;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ElvlsDTO implements ResponseDTO{
	private Elvls elvls;
}
