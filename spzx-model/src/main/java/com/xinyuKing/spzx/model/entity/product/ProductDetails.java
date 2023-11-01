package com.xinyuKing.spzx.model.entity.product;

import com.xinyuKing.spzx.model.entity.base.BaseEntity;
import lombok.Data;

@Data
public class ProductDetails extends BaseEntity {

	private Long productId;
	private String imageUrls;

}