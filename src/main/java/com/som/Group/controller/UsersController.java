package com.som.Group.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.som.Group.dao.UsersDAO;
import com.som.Group.model.Users;

@RestController
public class UsersController {

	Logger log = LoggerFactory.getLogger(UsersController.class);
	
	@Autowired
	private UsersDAO usersDAO;

	@GetMapping("/home")
	public String homePage(){
		log.debug("Starting of the method onLoad");
		return "Hello Page ";
	}
	@PostMapping(value = "/register")
	public ResponseEntity<Users> adduser(@RequestBody Users users) {
		System.out.println("hello");
		users.setStatus('n');
		users.setIsonline('N');
		usersDAO.saveOrUpdate(users);
		return new ResponseEntity<Users>(users, HttpStatus.OK);

	}

	@GetMapping(value = "/users")
	public ResponseEntity<List<Users>> listuser() {
		System.out.println("list of users");
		List<Users> users1 = usersDAO.list();
		return new ResponseEntity<List<Users>>(users1, HttpStatus.OK);
	}

	@GetMapping(value = "/oneuser")
	public ResponseEntity<Users> oneuser(HttpSession session) {
		String username = (String) session.getAttribute("username");
		Users oneuser = usersDAO.profileof(username);
		return new ResponseEntity<Users>(oneuser, HttpStatus.OK);
	}
	@PostMapping("/imageUpload")
	@Transactional
	public void ImageUpload(@RequestBody MultipartFile file,HttpSession session) throws IOException {
		
		String username = (String) session.getAttribute("username"); /*Get Logged in Username*/
		Users users=usersDAO.profileof(username);					/*Get user object based on username*/
		System.out.println(file.getContentType()+'\n'+file.getName()+'\n'+file.getSize()+'\n'+file.getOriginalFilename());
		users.setImage(file.getBytes());
		usersDAO.saveOrUpdate(users);
	}

	@GetMapping("/profileimage")
	public ResponseEntity<Users> profileimage(HttpSession session){
		int uid=(Integer) session.getAttribute("uid");
		Users users=usersDAO.oneuser(uid);
		return new ResponseEntity<Users>(users, HttpStatus.OK);
	}
	@GetMapping("/nonfriends")
	public ResponseEntity<List<Users>> nonfriends(HttpSession session){
		int uid=(Integer) session.getAttribute("uid");
		List<Users> nonfriends=usersDAO.nonfriends(uid);
		return new ResponseEntity<List<Users>>(nonfriends,HttpStatus.OK);
	}
}
