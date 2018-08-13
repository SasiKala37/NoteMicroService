package com.bridgelabz.notemicroservice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

import com.bridgelabz.notemicroservice.exceptions.LabelNameExistedException;
import com.bridgelabz.notemicroservice.exceptions.LabelNotFoundException;
import com.bridgelabz.notemicroservice.exceptions.NoteNotFoundException;
import com.bridgelabz.notemicroservice.exceptions.UnAuthorizedException;
import com.bridgelabz.notemicroservice.exceptions.UserNotFoundException;
import com.bridgelabz.notemicroservice.model.Label;
import com.bridgelabz.notemicroservice.model.LabelDTO;
import com.bridgelabz.notemicroservice.model.Note;
import com.bridgelabz.notemicroservice.repository.LabelElasticSearchRepository;
import com.bridgelabz.notemicroservice.repository.LabelRepository;
import com.bridgelabz.notemicroservice.repository.NoteElasticSearchRepository;
import com.bridgelabz.notemicroservice.repository.NoteRepository;
@Service

public class LabelServiceImplementation implements LabelService {

	@Autowired
	private NoteRepository noteRepository;

	

	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private NoteElasticSearchRepository noteElasticSearchRepository;

	@Autowired
	private LabelElasticSearchRepository labelElasticSearchRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public String createLabel(String userId, String labelName) throws UserNotFoundException, LabelNameExistedException {

		Optional<Label> optionalLabel = labelRepository.findByLabelNameAndUserId(labelName, userId);

		if (optionalLabel.isPresent()) {
			throw new LabelNameExistedException("LabelName already exist");
		}

		Label label = new Label();
		label.setLabelName(labelName);
		label.setUserId(userId);
		labelRepository.save(label);
		labelElasticSearchRepository.save(label);

		return labelName;
	}

	List<LabelDTO> labelList = new ArrayList<>();

	private Optional<Note> checkUserNote(String userId, String noteId)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException {

		Optional<Note> note = noteRepository.findById(noteId);

		if (!note.isPresent()) {
			throw new NoteNotFoundException("note not found exception");
		}

		if (!note.get().getUserId().equals(userId)) {
			throw new UnAuthorizedException("user doesnot has the note vice versa");
		}

		return note;
	}

	@Override
	public void addLabel(String userId, String labelId, String noteId) throws UserNotFoundException,
			NoteNotFoundException, UnAuthorizedException, LabelNameExistedException, LabelNotFoundException {

		LabelDTO labelDTO = null;
		Optional<Note> optionalNote = checkUserNote(userId, noteId);

		Optional<Label> optionalLabel = labelElasticSearchRepository.findByLabelIdAndUserId(labelId, userId);
		if (!optionalLabel.isPresent()) {
			throw new LabelNotFoundException("Label not found");
		}

		labelDTO = new LabelDTO();
		labelDTO.setLabelId(optionalLabel.get().getLabelId());
		labelDTO.setLabelName(optionalLabel.get().getLabelName());
		labelList.add(labelDTO);
		optionalNote.get().setLabelList(labelList);

		noteRepository.save(optionalNote.get());
		noteElasticSearchRepository.save(optionalNote.get());
	}

	@Override
	public List<LabelDTO> getAllLabels(String userId) throws UserNotFoundException {

		List<LabelDTO> list = new ArrayList<>();

		List<Label> optionalLabels = labelElasticSearchRepository.findByUserId(userId);

		optionalLabels.stream().map(streamLabels -> modelMapper.map(streamLabels, LabelDTO.class))
				.forEach(mapLabels -> list.add(mapLabels));

		return list;
	}

	@Override
	public void deleteLabel(String userId, String labelId)
			throws UserNotFoundException, LabelNotFoundException, UnAuthorizedException {

		List<Note> optionalNotes = noteElasticSearchRepository.findAllByUserId(userId);

		Optional<Label> optionalLabel = labelElasticSearchRepository.findByLabelIdAndUserId(labelId, userId);

		if (!optionalLabel.isPresent()) {
			throw new LabelNotFoundException("Label not found");
		}
		labelRepository.delete(optionalLabel.get());
		labelElasticSearchRepository.delete(optionalLabel.get());

		for (int i = 0; i < optionalNotes.size(); i++) {

			for (int j = 0; j < optionalNotes.get(i).getLabelList().size(); j++) {

				if (optionalNotes.get(i).getLabelList().get(j).getLabelId().equals(optionalLabel.get().getLabelId())) {

					optionalNotes.get(i).getLabelList().remove(j);
					noteRepository.save(optionalNotes.get(i));
					noteElasticSearchRepository.save(optionalNotes.get(i));
				}
			}
		}

	}

	@Override
	public void renameLabel(String userId, String labelId, String newLabelName)
			throws UserNotFoundException, LabelNotFoundException, UnAuthorizedException {

		List<Note> optionalNotes = noteElasticSearchRepository.findAllByUserId(userId);

		Optional<Label> optionalLabel = labelElasticSearchRepository.findByLabelIdAndUserId(labelId, userId);
		if (!optionalLabel.isPresent()) {
			throw new LabelNotFoundException("Label not found");
		}

		optionalLabel.get().setLabelName(newLabelName);
		labelRepository.save(optionalLabel.get());

		labelElasticSearchRepository.save(optionalLabel.get());

		for (int i = 0; i < optionalNotes.size(); i++) {

			for (int j = 0; j < optionalNotes.get(j).getLabelList().size(); j++) {

				if (optionalNotes.get(j).getLabelList().get(j).getLabelId().equals(optionalLabel.get().getLabelId())) {

					optionalNotes.get(i).getLabelList().get(j).setLabelName(newLabelName);
					noteRepository.save(optionalNotes.get(i));
					noteElasticSearchRepository.save(optionalNotes.get(i));

				}
			}

		}

	}

	@Override
	public void removeNoteLabel(String userId, String noteId, String labelId)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException, LabelNotFoundException {

		Optional<Note> optionalNote = checkUserNote(userId, noteId);
		Optional<Label> optionalLabel = labelElasticSearchRepository.findByLabelIdAndUserId(labelId, userId);
		Note note = optionalNote.get();

		if (!optionalLabel.isPresent()) {
			throw new LabelNotFoundException("Label not found");
		}

		for (int i = 0; i < note.getLabelList().size(); i++) {

			if (note.getLabelList().get(i).getLabelId().equals(optionalLabel.get().getLabelId())) {

				note.getLabelList().remove(i);
				noteRepository.save(note);
				noteElasticSearchRepository.save(note);
				break;
			}
		}

	}

}
