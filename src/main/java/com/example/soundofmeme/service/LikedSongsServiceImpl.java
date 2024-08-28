package com.example.soundofmeme.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.soundofmeme.entity.LikedSongs;
import com.example.soundofmeme.repository.LikedSongsRepository;

@Service
public class LikedSongsServiceImpl implements LikedSongsService {
	
	@Autowired
	private LikedSongsRepository likedRepo;

	@Override
	public LikedSongs saveLikedSong(LikedSongs song) {
		
		long id=song.getSongid();
		
		LikedSongs alreadyliked=likedRepo.findLikedSongBySongid(id);
		
		if(alreadyliked!=null) {
			return null;
		}else {
			return likedRepo.save(song);
		}
			
	}

	@Override
	public LikedSongs DislikeSong(long id) {
		
	   LikedSongs dislikeSong=likedRepo.findLikedSongBySongid(id);
		
		if(dislikeSong!=null) {
			long LikedSongid= dislikeSong.getId();
			
			likedRepo.deleteById(LikedSongid);
			return dislikeSong;
		}else {
			return null;
		}
	}

	@Override
	public LikedSongs FindBySongId(long id) {
		
		LikedSongs song =  likedRepo.findLikedSongBySongid(id);
		
		return song;
	}
	
	

}
