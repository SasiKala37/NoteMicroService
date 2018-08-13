package com.bridgelabz.notemicroservice.services;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;

import com.bridgelabz.notemicroservice.exceptions.LabelNameExistedException;
import com.bridgelabz.notemicroservice.exceptions.LabelNotFoundException;
import com.bridgelabz.notemicroservice.exceptions.NoteNotFoundException;
import com.bridgelabz.notemicroservice.exceptions.UnAuthorizedException;
import com.bridgelabz.notemicroservice.exceptions.UserNotFoundException;
import com.bridgelabz.notemicroservice.model.LabelDTO;

/*@FeignClient(name = "usermicroservice")
@RibbonClient(name = "usermicroservice")*/
public interface LabelService {

	/**
	 * @param userId
	 * @param labelName
	 * @return
	 * @throws UserNotFoundException
	 * @throws LabelNameExistedException
	 */
	public String createLabel(String userId, String labelName) throws UserNotFoundException, LabelNameExistedException;

	/**
	 * @param userId
	 * @param labelId
	 * @param noteId
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws LabelNameExistedException
	 * @throws LabelNotFoundException
	 */
	public void addLabel(String userId, String labelId, String noteId) throws UserNotFoundException,
			NoteNotFoundException, UnAuthorizedException, LabelNameExistedException, LabelNotFoundException;

	/**
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 */
	public List<LabelDTO> getAllLabels(String userId) throws UserNotFoundException;

	/**
	 * @param userId
	 * @param labelId
	 * @throws UserNotFoundException
	 * @throws LabelNotFoundException
	 * @throws UnAuthorizedException
	 */
	public void deleteLabel(String userId, String labelId)
			throws UserNotFoundException, LabelNotFoundException, UnAuthorizedException;

	/**
	 * @param userId
	 * @param labelId
	 * @param newLabelName
	 * @throws UserNotFoundException
	 * @throws LabelNotFoundException
	 * @throws UnAuthorizedException
	 */
	public void renameLabel(String userId, String labelId, String newLabelName)
			throws UserNotFoundException, LabelNotFoundException, UnAuthorizedException;

	/**
	 * @param userId
	 * @param noteId
	 * @param labelId
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws LabelNotFoundException
	 */
	public void removeNoteLabel(String userId, String noteId, String labelId)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException, LabelNotFoundException;
}
