package com.adem.DTOresponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageSavedResponse extends Response {
	private String imageId;

	// constructor , message ve success bilgisini VRResponse constructorını
	// kullanarak set edeceğiz
	public ImageSavedResponse(String imageId, String message, boolean success) {
		super(message, success);
		this.imageId = imageId;

	}
}