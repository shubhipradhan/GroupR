package com.som.Group.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.som.Group.dao.FriendDAO;
import com.som.Group.dao.UsersDAO;
import com.som.Group.model.Friend;
import com.som.Group.model.Users;

@RestController
public class LoginController {

	@Autowired 
	UsersDAO usersDAO;
	@Autowired
	FriendDAO friendDAO;

	@GetMapping("/login/{username}/{password}")
	public ResponseEntity<Users> login( @PathVariable("username") String username,@PathVariable("password") String password ,HttpSession session){
		Users users = usersDAO.authuser(username,password);
		if(users==null)
			{	return null;
	}else if(friendDAO.getfriendlist(username)==null){
		session.setAttribute("userLogged", users);
		session.setAttribute("uid", users.getId());
		session.setAttribute("username",users.getUsername());
		users.setStatus('o');
		usersDAO.saveOrUpdate(users);
		Users users1=usersDAO.oneuser(users.getId());
		return new ResponseEntity<Users>(users1,HttpStatus.OK);
	}else{
		session.setAttribute("userLogged", users);
		session.setAttribute("uid", users.getId());
		session.setAttribute("username",users.getUsername());
		users.setStatus('o');
		usersDAO.saveOrUpdate(users);
    	List<Friend> friend=friendDAO.setonline(users.getUsername());
    	for(int i=0;i<friend.size();i++){
    		Friend online=friend.get(i);
    		online.setIsonline('y');
    		friendDAO.saveOrUpdate(online);
    	}
		Users users1=usersDAO.oneuser(users.getId());
		return new ResponseEntity<Users>(users1,HttpStatus.OK);
	}
	}
	@PostMapping("/logout")
	public ResponseEntity<Users> logout(HttpSession session){
		int uid =  (Integer) session.getAttribute("uid");
		Users users =usersDAO.oneuser(uid);
		users.setStatus('N');
		usersDAO.saveOrUpdate(users);
		List<Friend> friend=friendDAO.setonline(users.getUsername());
		for(int i=0;i<friend.size();i++){
    		Friend online=friend.get(i);
    		online.setIsonline('f');
    		friendDAO.saveOrUpdate(online);
    	}
		session.invalidate();
		return new ResponseEntity<Users>(users,HttpStatus.OK);
	}
}
