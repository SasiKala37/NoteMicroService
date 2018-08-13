package com.bridgelabz.notemicroservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.notemicroservice.exceptions.LabelNameExistedException;
import com.bridgelabz.notemicroservice.exceptions.LabelNotFoundException;
import com.bridgelabz.notemicroservice.exceptions.NoteNotFoundException;
import com.bridgelabz.notemicroservice.exceptions.UnAuthorizedException;
import com.bridgelabz.notemicroservice.exceptions.UserNotFoundException;
import com.bridgelabz.notemicroservice.model.LabelDTO;
import com.bridgelabz.notemicroservice.model.ResponseDTO;
import com.bridgelabz.notemicroservice.services.LabelService;



@RestController
@RequestMapping("/label")
public class LabelController {

	@Autowired
	private LabelService labelService;

	/**
	 * @param userId
	 * @param labelName
	 * @param request
	 * @return
	 * @throws LabelNotFoundException
	 * @throws UserNotFoundException
	 * @throws LabelNameExistedException
	 */
	@PostMapping("/create")
	public ResponseEntity<String> create(@RequestHeader("userId") String userId, @RequestParam("labelName") String labelName)
			throws LabelNotFoundException, UserNotFoundException, LabelNameExistedException {

		String labelname = labelService.createLabel(userId, labelName);

		return new ResponseEntity<>(labelname, HttpStatus.OK);

	}

	/**
	 * @param userId
	 * @param request
	 * @return
	 * @throws UserNotFoundException
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<LabelDTO>> getAll(@RequestHeader("userId") String userId)
			throws UserNotFoundException {

		List<LabelDTO> labelDTOs = labelService.getAllLabels(userId);

		return new ResponseEntity<>(labelDTOs, HttpStatus.OK);
	}

	/**
	 * @param labelId
	 * @param userId
	 * @param request
	 * @return
	 * @throws UserNotFoundException
	 * @throws LabelNotFoundException
	 * @throws UnAuthorizedException
	 * @throws NoteNotFoundException
	 */
	@DeleteMapping("/delete/{labelid}")
	public ResponseEntity<ResponseDTO> delete(@PathVariable("labelId") String labelId,
			@RequestHeader("userId") String userId)
			throws UserNotFoundException, LabelNotFoundException, UnAuthorizedException, NoteNotFoundException {

		labelService.deleteLabel(userId, labelId);

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("delete the label");
		responseDTO.setStatus(1);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);

	}

	/**
	 * @param labelDTO
	 * @param noteId
	 * @param userId
	 * @param request
	 * @return
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws LabelNameExistedException
	 * @throws LabelNotFoundException
	 */
	@PutMapping("/add")
	public ResponseEntity<ResponseDTO> addLabel(@RequestParam String labelId, @RequestParam String noteId,
			@RequestHeader("userId") String userId) throws UserNotFoundException, NoteNotFoundException,
			UnAuthorizedException, LabelNameExistedException, LabelNotFoundException {

		labelService.addLabel(userId, labelId, noteId);

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("add the label successfully");
		responseDTO.setStatus(1);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);

	}

	/**
	 * @param labelId
	 * @param newLabelName
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 * @throws LabelNotFoundException
	 * @throws UnAuthorizedException
	 */
	@PutMapping("/rename/{labelId}")
	public ResponseEntity<ResponseDTO> rename(@PathVariable("labelId") String labelId,
			@RequestParam String newLabelName, @RequestHeader("userId") String userId)
			throws UserNotFoundException, LabelNotFoundException, UnAuthorizedException {

		labelService.renameLabel(userId, labelId, newLabelName);

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("rename label successfully");
		responseDTO.setStatus(1);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param labelId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/removenote/{labelId}")
	public ResponseEntity<ResponseDTO> removeNoteLabel(@RequestParam String noteId,
			@PathVariable("labelId") String labelId, @RequestHeader("userId") String userId) throws Exception {

		labelService.removeNoteLabel(userId, noteId, labelId);

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("remove note label successfully");
		responseDTO.setStatus(1);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

}
