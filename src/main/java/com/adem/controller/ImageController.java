package com.adem.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.adem.DTO.ImageDTO;
import com.adem.DTOresponse.ImageSavedResponse;
import com.adem.DTOresponse.Response;
import com.adem.DTOresponse.ResponseMessage;
import com.adem.domain.ImageData;
import com.adem.service.ImageService;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {

	private final ImageService imageService;

	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}

	// *********** Upload ********************
	@Transactional
	@PostMapping("/upload")
	public ResponseEntity<ImageSavedResponse> uploadImage(@RequestParam("imageFile") MultipartFile file)
			throws IOException {

		String imageId = imageService.uploadImage(file);

		ImageSavedResponse response = new ImageSavedResponse(imageId, ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE,
				true);

		return ResponseEntity.ok(response);

	}

	// ********************* Download***************
	@Transactional
	@GetMapping("/download/{id}")

	public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {

		ImageData imageFile = imageService.getImageById(id);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + imageFile.getName())
				.body(imageFile.getImageFile().getData());
	}

	// ***********************Image Display*************************
	@Transactional
	@GetMapping("/display/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable String id) throws IOException {
		ImageData image = imageService.getImageById(id);
		// header bilgisine MediaType bilgisini giriyorum
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.IMAGE_PNG);

		return new ResponseEntity<>(image.getImageFile().getData(), header, HttpStatus.OK);
	}

	// *************************GetAllimages*******************
	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ImageDTO>> getAllImages() {

		List<ImageDTO> allImageDTO = imageService.getAllImages();

		return ResponseEntity.ok(allImageDTO);

	}

	// *****************Delete Images ******************
	@Transactional
	@DeleteMapping("/{id}")
//			@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Response> deleteImageFile(@PathVariable String id) {
		imageService.removeById(id);

		Response response = new Response(ResponseMessage.IMAGE_DELETE_RESPONSE_MESSAGE, true);
		return ResponseEntity.ok(response);
	}

	@Transactional
	@PostMapping("/file")
	public ResponseEntity<String> uploadImageToFIleSystem(@RequestParam("image") MultipartFile file)
			throws IOException {
		String uploadImage = imageService.uploadImageToFileSystem(file);
		new Response(ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE, true);
		return ResponseEntity.ok().body(uploadImage);
	}

//	@Transactional
//	@GetMapping("/add/{imageId}")
//	public ResponseEntity<byte[]> findImageByImageId(@PathVariable String imageId) {
//
//		Image image = imageService.findImageByImageId(imageId);
//		HttpHeaders header = new HttpHeaders();
//		header.setContentType(MediaType.IMAGE_PNG);
//
//		return new ResponseEntity<>(image.getData(), header, HttpStatus.OK);
//	}

}