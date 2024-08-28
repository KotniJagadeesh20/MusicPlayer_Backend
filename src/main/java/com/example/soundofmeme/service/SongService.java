package com.example.soundofmeme.service;


	

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.soundofmeme.entity.Song;

	public interface SongService {

	    Song saveSong(Song song);

	    List<Song> findSongsByUserId(Long userId);

	    Optional<Song> findById(Long id);
	    	   
		List<Song> getAllSongs(); 
		
		Song cloneSong(MultipartFile file, String prompt, String lyrics);
	}

