package com.example.soundofmeme.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.soundofmeme.entity.Song;
import com.example.soundofmeme.repository.SongRepository;

@Service
public class SongServiceImpl implements SongService{
	
	  @Autowired
	    private SongRepository songRepository;

	 

	  @Override
	    public Song saveSong(Song song) {
	        return songRepository.save(song);
	    }

	    @Override
	    public List<Song> findSongsByUserId(Long userId) {
	        //Pageable pageable = (Pageable) PageRequest.of(page, 20);
	        return songRepository.findByUserId(userId);
	    }

	    @Override
	    public Optional<Song> findById(Long id) {
	        return songRepository.findById(id);
	    }

	    @Override
	    public List<Song> getAllSongs() {
	        return songRepository.findAll();
	    } 
	    
	    @Override
	    public Song cloneSong(MultipartFile file, String prompt, String lyrics) {
	        // Implement your logic for cloning a song here
	        Song clonedSong = new Song();
	        clonedSong.setSongName("Cloned Song from " + prompt);
	        clonedSong.setLyrics(lyrics);
	        clonedSong.setTags("Cloned");

	        // Handle the file and set the URL (assuming you have some logic to store the file and get its URL)
	        String songUrl = storeFileAndGetUrl(file);
	        clonedSong.setSongUrl(songUrl);
	        
			if (clonedSong.getSongName() == null || clonedSong.getSongName().isEmpty()
					|| clonedSong.getLyrics() == null || clonedSong.getLyrics().isEmpty()
					|| clonedSong.getTags() == null || clonedSong.getTags().isEmpty()) {
				return null;
			}
	

	        return songRepository.save(clonedSong);
	    }

	    private String storeFileAndGetUrl(MultipartFile file) {
	        // Implement your file storage logic here
	        // For now, return a dummy URL
	        return "http://example.com/song/" + file.getOriginalFilename();
	    }
	
	
}

