package com.j2kb.codev21.domains.gisucategory.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.j2kb.codev21.domains.gisucategory.domain.GisuCategory;
import com.j2kb.codev21.domains.gisucategory.dto.GisuCategoryDto;

@Mapper(componentModel = "spring")
public interface GisuCategoryMapper {

	GisuCategory reqToGisuCategory(GisuCategoryDto.Req req);
	
	@Mapping(target = "gisuId", source = "id")
	GisuCategoryDto.Res gisuCategoryToRes(GisuCategory gisuCategory);
}
