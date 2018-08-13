package com.bridgelabz.notemicroservice.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;

import com.bridgelabz.notemicroservice.exceptions.DateNotProperlySetException;
import com.bridgelabz.notemicroservice.exceptions.LabelNotFoundException;
import com.bridgelabz.notemicroservice.exceptions.NoteCreationException;
import com.bridgelabz.notemicroservice.exceptions.NoteNotFoundException;
import com.bridgelabz.notemicroservice.exceptions.UnAuthorizedException;
import com.bridgelabz.notemicroservice.exceptions.UserNotFoundException;
import com.bridgelabz.notemicroservice.model.CreateNoteDTO;
import com.bridgelabz.notemicroservice.model.NoteDTO;
import com.bridgelabz.notemicroservice.model.UpdateNoteDTO;

/*@FeignClient(name = "usermicroservice")
@RibbonClient(name = "usermicroservice")*/
public interface NoteService {

	/**
	 * @param createNoteDTO
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws UserNotFoundException
	 * @throws NoteCreationException
	 * @throws LabelNotFoundException
	 * @throws IOException 
	 */
	NoteDTO createNote(CreateNoteDTO createNoteDTO, String token) throws NoteNotFoundException, UnAuthorizedException,
			UserNotFoundException, NoteCreationException, LabelNotFoundException, IOException;

	/**
	 * @param token
	 * @param noteId
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws UserNotFoundException
	 */
	void deleteNote(String token, String noteId)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException;

	/**
	 * @param token
	 * @param updateNoteDTO
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws UserNotFoundException
	 * @throws NoteCreationException
	 */
	void updateNote(String token, UpdateNoteDTO updateNoteDTO)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException, NoteCreationException;

	/**
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 */
	List<NoteDTO> readNote(String token) throws NoteNotFoundException, UserNotFoundException;

	public NoteDTO getNote(String userId, String noteId)
			throws NoteNotFoundException, UserNotFoundException, UnAuthorizedException;

	/**
	 * @param token
	 * @param noteId
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws UserNotFoundException
	 */
	void trashNote(String token, String noteId)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException;

	/**
	 * @param token
	 * @param noteId
	 * @param remindAt
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws UserNotFoundException
	 * @throws DateNotProperlySetException
	 */
	void addReminder(String token, String noteId, Date remindAt)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException, DateNotProperlySetException;

	/**
	 * @param token
	 * @param noteId
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws UserNotFoundException
	 */
	void removeReminder(String token, String noteId)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException;

	/**
	 * @param userId
	 * @param noteId
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 */
	void restoreNote(String userId, String noteId)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException;

	/**
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 */
	List<NoteDTO> getAllTrashNotes(String userId) throws UserNotFoundException;

	/**
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 */
	List<NoteDTO> getAllArchiveNotes(String userId) throws UserNotFoundException;

	/**
	 * @param userId
	 * @param noteId
	 * @param isArchive
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 */
	void setArchiveNotes(String userId, String noteId, boolean isArchive)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException;

	/**
	 * @param userId
	 * @param noteId
	 * @param color
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 */
	void changeColor(String userId, String noteId, String color)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException;

	/**
	 * @param userId
	 * @param noteId
	 * @param isArchive
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 */
	void unArchiveNotes(String userId, String noteId, boolean isArchive)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException;

	/**
	 * @param userId
	 * @param noteId
	 * @param isPin
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 */
	void pinAndUnPinNote(String userId, String noteId, boolean isPin)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException;
	

	/**
	 * @param userId
	 * @param labelName
	 * @return
	 * @throws LabelNotFoundException
	 * @throws UserNotFoundException
	 * @throws LabelNameExistedException
	 */
	/*
	 * String createLabel(String userId, String labelName) throws
	 * LabelNotFoundException, UserNotFoundException, LabelNameExistedException;
	 * 
	 *//**
		 * @param userId
		 * @return
		 * @throws UserNotFoundException
		 */
	/*
	 * List<LabelDTO> getAllLabels(String userId) throws UserNotFoundException;
	 * 
	 *//**
		 * @param userId
		 * @param labelName
		 * @throws UserNotFoundException
		 * @throws LabelNotFoundException
		 * @throws UnAuthorizedException
		 * @throws NoteNotFoundException
		 */
	/*
	 * void deleteLabel(String userId, String labelName) throws
	 * UserNotFoundException, LabelNotFoundException, UnAuthorizedException,
	 * NoteNotFoundException;
	 * 
	 *//**
		 * @param userId
		 * @param labelName
		 * @param noteId
		 * @throws UserNotFoundException
		 * @throws NoteNotFoundException
		 * @throws UnAuthorizedException
		 * @throws LabelNameExistedException
		 * @throws LabelNotFoundException
		 */
	/*
	 * void addLabel(String userId, String labelName, String noteId) throws
	 * UserNotFoundException, NoteNotFoundException, UnAuthorizedException,
	 * LabelNameExistedException, LabelNotFoundException;
	 * 
	 *//**
		 * @param userId
		 * @param oldLabelName
		 * @param newLabelName
		 * @throws UserNotFoundException
		 * @throws LabelNotFoundException
		 * @throws UnAuthorizedException
		 */
	/*
	 * void renameLabel(String userId, String oldLabelName, String newLabelName)
	 * throws UserNotFoundException, LabelNotFoundException, UnAuthorizedException;
	 * 
	 *//**
		 * @param userId
		 * @param noteId
		 * @param labelName
		 * @throws UserNotFoundException
		 * @throws NoteNotFoundException
		 * @throws UnAuthorizedException
		 * @throws LabelNotFoundException
		 *//*
			 * void removeNoteLabel(String userId, String noteId, String labelName) throws
			 * UserNotFoundException, NoteNotFoundException, UnAuthorizedException,
			 * LabelNotFoundException;
			 */
}
