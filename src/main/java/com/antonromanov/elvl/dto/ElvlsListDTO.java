package com.antonromanov.elvl.dto;

import com.antonromanov.elvl.model.Elvls;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ElvlsListDTO implements ResponseDTO{
	private List<Elvls> list;
}
