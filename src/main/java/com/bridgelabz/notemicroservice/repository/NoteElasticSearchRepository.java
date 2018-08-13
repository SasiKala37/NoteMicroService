package com.bridgelabz.notemicroservice.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bridgelabz.notemicroservice.model.Note;

public interface NoteElasticSearchRepository extends ElasticsearchRepository<Note, String> {

	List<Note> findAllByUserId(String userId);
}
