package com.bridgelabz.notemicroservice.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.notemicroservice.exceptions.DateNotProperlySetException;
import com.bridgelabz.notemicroservice.exceptions.LabelNotFoundException;
import com.bridgelabz.notemicroservice.exceptions.NoteCreationException;
import com.bridgelabz.notemicroservice.exceptions.NoteNotFoundException;
import com.bridgelabz.notemicroservice.exceptions.UnAuthorizedException;
import com.bridgelabz.notemicroservice.exceptions.UserNotFoundException;
import com.bridgelabz.notemicroservice.model.CreateNoteDTO;
import com.bridgelabz.notemicroservice.model.Label;
import com.bridgelabz.notemicroservice.model.LabelDTO;
import com.bridgelabz.notemicroservice.model.Note;
import com.bridgelabz.notemicroservice.model.NoteDTO;
import com.bridgelabz.notemicroservice.model.ScrapLinkDTO;
import com.bridgelabz.notemicroservice.model.UpdateNoteDTO;
import com.bridgelabz.notemicroservice.repository.LabelRepository;
import com.bridgelabz.notemicroservice.repository.NoteElasticSearchRepository;
import com.bridgelabz.notemicroservice.repository.NoteRepository;
import com.bridgelabz.notemicroservice.util.Utility;

@Service
public class NoteServiceImplementation implements NoteService {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private NoteElasticSearchRepository noteElasticSearchRepository;

	@Autowired
	private ModelMapper modelMapper;

	private Optional<Note> checkUserNote(String userId, String noteId)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException {

		Optional<Note> note = noteRepository.findByNoteId(noteId);

		if (!note.isPresent()) {
			throw new NoteNotFoundException("note not found exception");
		}

		if (!note.get().getUserId().equals(userId)) {
			throw new UnAuthorizedException("user doesnot has the note vice versa");
		}

		return note;
	}

	@Override
	public NoteDTO createNote(CreateNoteDTO createNoteDTO, String userId) throws UserNotFoundException,
			NoteCreationException, NoteNotFoundException, UnAuthorizedException, LabelNotFoundException, IOException {

		Utility.validateTitleAndDesc(createNoteDTO.getTitle(), createNoteDTO.getDescription());

		Note note = new Note();
		Date date = new Date();
		note.setUserId(userId);
		note.setCreateAt(date);
		note.setUpdateAt(date);
		note.setRemindAt(createNoteDTO.getRemindAt());

		if (createNoteDTO.isArchive()) {
			note.setArchive(true);
		} else {
			note.setArchive(false);
		}

		if (createNoteDTO.isPin()) {
			note.setPin(true);
		} else {
			note.setPin(false);
		}

		if (createNoteDTO.getColor() == "String" || createNoteDTO.getColor().trim().length() == 0
				|| createNoteDTO.getColor() == null) {
			note.setColor("white");
		} else {
			note.setColor(createNoteDTO.getColor());
		}

		note.setTitle(createNoteDTO.getTitle());
		note.setDescription(createNoteDTO.getDescription());

		List<String> newList = createNoteDTO.getLabelList();

		if (newList != null) {
			for (int i = 0; i < newList.size(); i++) {
				Optional<Label> existedLabel = labelRepository.findByLabelNameAndUserId(newList.get(i), userId);

				if (!existedLabel.isPresent()) {
					throw new LabelNotFoundException("User doesnot have labels");
				}

				List<LabelDTO> list = new ArrayList<>();
				if (newList.get(i).trim().length() != 0 && newList.get(i) != null) {
					LabelDTO labelDTO = new LabelDTO();
					labelDTO.setLabelId(existedLabel.get().getLabelId());
					labelDTO.setLabelName(existedLabel.get().getLabelName());
					list.add(labelDTO);
					note.setLabelList(list);
				}
			}

		}
		
		String[] urlLinks = createNoteDTO.getDescription().split(" ");
		System.out.println("length ------"+urlLinks.length);
		
		List<ScrapLinkDTO> listOfScraps = new ArrayList<>();
		
		ScrapLinkDTO scrapLinkDTO = new ScrapLinkDTO();
		
		for (int i = 0; i < urlLinks.length; i++) {
			
			if (Utility.validateUrl(urlLinks[i])) {

				Document doc = Jsoup.connect(urlLinks[i]).userAgent("Jsoup client").timeout(5000).get();

				String title = doc.title();
				scrapLinkDTO.setTitle(title);
				
				String link = doc.baseUri().replace("http://", "");
				scrapLinkDTO.setLink(link);

				Element image = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]").first();
				scrapLinkDTO.setImageLink(image);
						
				listOfScraps.add(scrapLinkDTO);
				note.setListOfLinks(listOfScraps);
			}
		}
		
		

		noteRepository.save(note);
		noteElasticSearchRepository.save(note);

		return modelMapper.map(note, NoteDTO.class);

	}

	@Override
	public List<NoteDTO> readNote(String userId) throws NoteNotFoundException, UserNotFoundException {

		List<Note> notes = noteElasticSearchRepository.findAllByUserId(userId);
		List<NoteDTO> listNoteDTO = new ArrayList<>();

		notes.stream().map(streamNotes -> modelMapper.map(streamNotes, NoteDTO.class)).forEach(listNoteDTO::add);

		return listNoteDTO;
	}
	@Override
	public NoteDTO getNote(String userId,String noteId) throws NoteNotFoundException, UserNotFoundException, UnAuthorizedException {
		
		Optional<Note> note=checkUserNote(userId, noteId);
		System.out.println(note.get().getDescription());
		return modelMapper.map(note.get(), NoteDTO.class);
		
	}

	@Override
	public void updateNote(String userId, UpdateNoteDTO updateNoteDTO)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException, NoteCreationException {

		Utility.validateTitleAndDesc(updateNoteDTO.getTitle(), updateNoteDTO.getDescription());
		Optional<Note> optionalNote = checkUserNote(userId, updateNoteDTO.getNoteId());

		if (optionalNote.get().isTrash()) {
			throw new NoteNotFoundException("Note not found");
		}

		Note note = optionalNote.get();

		note.setTitle(updateNoteDTO.getTitle());
		note.setDescription(updateNoteDTO.getDescription());
		note.setUpdateAt(updateNoteDTO.getUpdateAt());

		noteRepository.save(note);
		noteElasticSearchRepository.save(note);

	}

	@Override
	public void deleteNote(String userId, String noteId)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException {

		Optional<Note> optionalNote = checkUserNote(userId, noteId);

		if (optionalNote.get().isTrash()) {
			throw new NoteNotFoundException("note  not found Exception");
		}

		Note note = optionalNote.get();
		note.setTrash(true);

		noteRepository.save(note);
		noteElasticSearchRepository.save(note);

	}

	@Override
	public void trashNote(String userId, String noteId)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException {

		Optional<Note> optionalNote = checkUserNote(userId, noteId);

		if (optionalNote.get().isTrash()) {
			noteRepository.deleteById(noteId);
			noteElasticSearchRepository.deleteById(noteId);
		}
	}

	@Override
	public void restoreNote(String userId, String noteId)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException {

		Optional<Note> optionalNote = checkUserNote(userId, noteId);

		if (optionalNote.get().isTrash()) {
			optionalNote.get().setTrash(false);
			noteRepository.save(optionalNote.get());
			noteElasticSearchRepository.save(optionalNote.get());
		}
	}

	@Override
	public void addReminder(String userId, String noteId, Date remaindAt)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException, DateNotProperlySetException {

		Optional<Note> optionalNote = checkUserNote(userId, noteId);

		if (remaindAt.before(new Date())) {
			throw new DateNotProperlySetException("set the date rather than todays date");
		}

		optionalNote.get().setRemindAt(remaindAt);

		noteRepository.save(optionalNote.get());
		noteElasticSearchRepository.save(optionalNote.get());
	}

	@Override
	public void removeReminder(String userId, String noteId)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException {

		Optional<Note> optionalNote = checkUserNote(userId, noteId);

		optionalNote.get().setRemindAt(null);

		noteRepository.save(optionalNote.get());
		noteElasticSearchRepository.save(optionalNote.get());
	}

	@Override
	public List<NoteDTO> getAllTrashNotes(String userId) throws UserNotFoundException {

		List<Note> optionalNotes = noteElasticSearchRepository.findAllByUserId(userId);
		List<NoteDTO> listNoteDTO = new ArrayList<>();

		for (int i = 0; i < optionalNotes.size(); i++) {
			if (optionalNotes.get(i).isTrash()) {
				listNoteDTO.add(modelMapper.map(optionalNotes.get(i), NoteDTO.class));
			}
		}

		return listNoteDTO;
	}

	@Override
	public void setArchiveNotes(String userId, String noteId, boolean isArchive)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException {

		Optional<Note> optionalNote = checkUserNote(userId, noteId);
		if (optionalNote.get().isPin()) {
			optionalNote.get().setPin(false);
		}
		optionalNote.get().setArchive(isArchive);

		noteRepository.save(optionalNote.get());
		noteElasticSearchRepository.save(optionalNote.get());
	}

	@Override
	public void unArchiveNotes(String userId, String noteId, boolean isArchive)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException {

		Optional<Note> optionalNote = checkUserNote(userId, noteId);

		optionalNote.get().setArchive(isArchive);

		noteRepository.save(optionalNote.get());
		noteElasticSearchRepository.save(optionalNote.get());
	}

	@Override
	public List<NoteDTO> getAllArchiveNotes(String userId) throws UserNotFoundException {

		List<Note> optionalNotes = noteElasticSearchRepository.findAllByUserId(userId);
		List<NoteDTO> listNoteDTO = new ArrayList<>();

		for (int i = 0; i < optionalNotes.size(); i++) {
			if (optionalNotes.get(i).isArchive()) {
				listNoteDTO.add(modelMapper.map(optionalNotes.get(i), NoteDTO.class));
			}
		}

		return listNoteDTO;

	}

	@Override
	public void changeColor(String userId, String noteId, String color)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException {

		Optional<Note> optionalNote = checkUserNote(userId, noteId);

		if (color == null || color.equals("")) {
			optionalNote.get().setColor("white");

		} else {
			optionalNote.get().setColor(color);

		}

		noteRepository.save(optionalNote.get());
		noteElasticSearchRepository.save(optionalNote.get());
	}

	@Override
	public void pinAndUnPinNote(String userId, String noteId, boolean isPin)
			throws UserNotFoundException, NoteNotFoundException, UnAuthorizedException {

		Optional<Note> optionalNote = checkUserNote(userId, noteId);

		if (optionalNote.get().isArchive()) {
			optionalNote.get().setArchive(false);
		}

		if (isPin) {
			optionalNote.get().setPin(true);
		} else {
			optionalNote.get().setPin(false);
		}

		noteRepository.save(optionalNote.get());
		noteElasticSearchRepository.save(optionalNote.get());
	}


}
