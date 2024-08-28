package com.example.soundofmeme.service;

import java.util.List;

import com.example.soundofmeme.entity.LikedSongs;

public interface LikedSongsService {
	
	LikedSongs saveLikedSong(LikedSongs song);
	
	LikedSongs DislikeSong(long id);
	
	LikedSongs FindBySongId(long id);

}
