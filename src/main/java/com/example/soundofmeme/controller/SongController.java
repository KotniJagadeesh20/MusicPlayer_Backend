package com.example.soundofmeme.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.soundofmeme.entity.LikedSongs;
import com.example.soundofmeme.entity.Song;
import com.example.soundofmeme.entity.User;
import com.example.soundofmeme.repository.UserRepository;
import com.example.soundofmeme.request.CustomSongRequest;
import com.example.soundofmeme.request.DislikeRequest;
import com.example.soundofmeme.request.LikeRequest;
import com.example.soundofmeme.request.ViewRequest;
import com.example.soundofmeme.service.LikedSongsService;
import com.example.soundofmeme.service.SongService;

@RestController
@RequestMapping("/api/songs")
public class SongController {
	@Autowired
	private SongService songService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private LikedSongsService likeService;

	@PostMapping("/createSong")
	public ResponseEntity<Map<String, Object>> createSong(@RequestHeader("Authorization") String jwt,
			@RequestBody Song song, Principal principal) {

		String email = principal.getName();
		Long userId = userRepo.findUserIdByEmail(email);

		Map<String, Object> responseBody = new HashMap<>();

		if (song.getSongName() == null || song.getSongName().isEmpty() || song.getLyrics() == null
				|| song.getLyrics().isEmpty() || song.getTags() == null || song.getTags().isEmpty()
				|| song.getSongUrl() == null || song.getSongUrl().isEmpty()) {

			responseBody.put("message", "Invalid song data. Title, lyrics, genre, and song URL are required.");
			return ResponseEntity.badRequest().body(responseBody);
		}

		song.setUserId(userId);
		Song savedSong = songService.saveSong(song);
		if (savedSong != null) {
			responseBody.put("Message", "Song Saved successfullyy");
			responseBody.put("SavedSong", savedSong);
			return ResponseEntity.ok(responseBody);
		} else {
			responseBody.put("message", "Failed to save the song.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
		}

	}

	@PostMapping("/createcustomSong")
	public ResponseEntity<?> createCustomSong(@RequestHeader("Authorization") String jwt,
			@RequestBody CustomSongRequest customSongRequest, Principal principal) {

		String email = principal.getName();
		Long userId = userRepo.findUserIdByEmail(email);

		Map<String, Object> responseBody = new HashMap<>();

		if (customSongRequest.getTitle() == null || customSongRequest.getTitle().isEmpty()
				|| customSongRequest.getLyrics() == null || customSongRequest.getLyrics().isEmpty()
				|| customSongRequest.getGenere() == null || customSongRequest.getGenere().isEmpty()) {

			responseBody.put("message", "Invalid song data. Title, lyrics, and Genre are required.");
			return ResponseEntity.badRequest().body(responseBody);
		}

		String title = customSongRequest.getTitle();
		String lyrics = customSongRequest.getLyrics();
		String genre = customSongRequest.getGenere();

		Song song = new Song();
		song.setSongName(title);
		song.setLyrics(lyrics);
		song.setTags(genre);
		song.setUserId(userId);

		Song savedSong = songService.saveSong(song);

		if (savedSong != null) {
			responseBody.put("message", "Song saved successfully.");
			responseBody.put("song", savedSong);
			return ResponseEntity.ok(responseBody);
		} else {
			responseBody.put("message", "Failed to save the song.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
		}
	}

	@GetMapping("/userSongs")
	public ResponseEntity<?> getUserSongs(@RequestHeader("Authorization") String jwt, @RequestParam("userId") long userId) {

		Optional<User> user = userRepo.findById(userId);
		if (user.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
		List<Song> songs = songService.findSongsByUserId(userId);
		return ResponseEntity.ok(songs);
	}

	@GetMapping("/allSongs")
	public ResponseEntity<List<Song>> getAllSongs(@RequestHeader("Authorization") String jwt) {
		List<Song> songs = songService.getAllSongs();
		return ResponseEntity.ok(songs);
	}

	@GetMapping("/getsongbyid")
	public ResponseEntity<Map<String, Object>> getSongById(@RequestHeader("Authorization") String jwt,
			@RequestParam("id") Long id) {

		Map<String, Object> responseBody = new HashMap<>();

		if (id == null) {
			responseBody.put("Message", "unable to find song invalid Songid");
			return ResponseEntity.badRequest().body(responseBody);
		}
		Optional<Song> song = songService.findById(id);
		if (song == null || song.isEmpty()) {
			responseBody.put("Message", "No soung is present with particular id");
			ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
		}
		responseBody.put("Message", "Soung found");
		responseBody.put("Song", song);
		return ResponseEntity.badRequest().body(responseBody);

	}

	@PostMapping("/like")
	public ResponseEntity<?> likeSong(@RequestHeader("Authorization") String jwt, @RequestBody LikeRequest likeRequest,
			Principal principal) {

		long id = likeRequest.getSongId();
		String email = principal.getName();
		Long userId = userRepo.findUserIdByEmail(email);

		Map<String, Object> responseBody = new HashMap<>();

		Optional<Song> optionalSong = songService.findById(id);
		if (!optionalSong.isPresent()) {
			responseBody.put("message", "Song not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
		}

		LikedSongs AlreadyLiked = likeService.FindBySongId(id);

		if (AlreadyLiked != null) {
			responseBody.put("message", "Song arleady liked By you.");
			responseBody.put("song", AlreadyLiked);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
		}

		Song song = optionalSong.get();
		int likes = song.getLikes();
		likes = likes + 1;
		song.setLikes(likes);

		Song updatedSong = songService.saveSong(song);
		if (updatedSong != null) {
			responseBody.put("message", "Song liked successfully.");
			responseBody.put("song", updatedSong);
			LikedSongs likedSong = new LikedSongs();
			likedSong.setImageUrl(song.getImageUrl());
			likedSong.setSongUrl(song.getSongUrl());
			likedSong.setSongid(song.getId());
			likedSong.setLyrics(song.getLyrics());
			likedSong.setTags(song.getTags());
			likedSong.setSongName(song.getSongName());
			likedSong.setUserId(userId);
			LikedSongs SavelikedSong = likeService.saveLikedSong(likedSong);

		}
		return ResponseEntity.ok(responseBody);

	}

	@PostMapping("/dislike")
	public ResponseEntity<?> dislikeSong(@RequestHeader("Authorization") String jwt,
			@RequestBody DislikeRequest dislikeRequest, Principal principal) {

		long id = dislikeRequest.getSongId();

		String email = principal.getName();
		Long userId = userRepo.findUserIdByEmail(email);

		Map<String, Object> responseBody = new HashMap<>();

		Optional<Song> optionalSong = songService.findById(id);
		if (!optionalSong.isPresent()) {

			responseBody.put("message", "Song not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
		}

		Song song = optionalSong.get();

		LikedSongs likedsong = likeService.FindBySongId(id);
		if (likedsong == null) {
			responseBody.put("message", "You already Disliked this song");
			responseBody.put("Song", song);
			return ResponseEntity.badRequest().body(responseBody);
		}
		int dislikes = song.getDislikes();
		int likes = song.getLikes();
		likes -= likes;
		dislikes += 1;
		song.setDislikes(dislikes);

		Song updatedSong = songService.saveSong(song);
		if (updatedSong != null) {
			LikedSongs Disliked = likeService.DislikeSong(id);
		}
		responseBody.put("message", "Song disliked successfully.");
		responseBody.put("song", updatedSong);
		return ResponseEntity.ok(responseBody);

	}

	@PostMapping("/view")
	public ResponseEntity<?> viewSong(@RequestHeader("Authorization") String jwt, @RequestBody ViewRequest viewRequest,
			Principal principal) {

		String email = principal.getName();
		Long userId = userRepo.findUserIdByEmail(email);

		long id = viewRequest.getSongId();

		Optional<Song> optionalSong = songService.findById(id);
		Map<String, Object> responseBody = new HashMap<>();

		if (!optionalSong.isPresent()) {

			responseBody.put("message", "Song not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
		}

		Song song = optionalSong.get();
		
		if (song.isIsviewed()) {
			responseBody.put("message", "You already viewd this song");
			responseBody.put("Song", song);
			return ResponseEntity.badRequest().body(responseBody);
		}

		Integer views = song.getViews();
		  if (views == null) {
		        song.setViews(1);
		    } else {
		        song.setViews(views + 1);
		    }
		song.setIsviewed(true);

		Song viewsupdatedSong = songService.saveSong(song);

		responseBody.put("message", "Song views updated");
		responseBody.put("song", viewsupdatedSong);
		return ResponseEntity.ok(responseBody);
	}

	@PostMapping("/clonesong")
	public ResponseEntity<Map<String, Object>> cloneSong(@RequestParam("file") MultipartFile file,
			@RequestParam("prompt") String prompt, @RequestParam("lyrics") String lyrics, Principal principal) {

		String email = principal.getName();
		Long userId = userRepo.findUserIdByEmail(email);

		Map<String, Object> responseBody = new HashMap<>();

		if (file.isEmpty() || prompt == null || prompt.isEmpty() || lyrics == null || lyrics.isEmpty()) {
			responseBody.put("message", "Invalid data. File, prompt, and lyrics are required.");
			return ResponseEntity.badRequest().body(responseBody);
		}

		Song clonedSong = songService.cloneSong(file, prompt, lyrics);
		if (clonedSong != null) {
			responseBody.put("message", "Song not cloned.");
			responseBody.put("song", clonedSong);
			return ResponseEntity.ok(responseBody);
		}
		clonedSong.setUserId(userId);
		responseBody.put("message", "Song cloned successfully.");
		responseBody.put("song", clonedSong);
		return ResponseEntity.ok(responseBody);

	}

}
