package com.bridgelabz.notemicroservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.notemicroservice.model.Note;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

	Optional<Note> findByNoteId(String noteId);
	
	List<Note> findAllByUserId(String userId);
	
	/*@Query(value = "{noteId:?0, labelList.labelName:?1}")
	List<Note> findByUserIdAndLabelListInLabelName(String userId, String labelNmae);*/
	
	/*@Query(value="{'userId' : ?0,'labelList.labelName': ?1}")
	 List<Note> findAllByQuery(String userId, String labelName);*/
	
}
