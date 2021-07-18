package com.example.demo.controllers;
import com.example.demo.model.persistence.entities.Cart;
import com.example.demo.model.persistence.entities.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.dto.CreateUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.splunk.logging.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	public static final Logger log = LoggerFactory.getLogger(UserController.class);


	public UserController(UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}


	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			log.warn("User could not be founded by name: ", username);
			return ResponseEntity.notFound().build();
		} else {
			log.info("User founded by name");
			return ResponseEntity.ok(user);
		}
	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {
		User user = new User();
		user.setUsername(createUserDto.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if (createUserDto.getPassword().length()<7 || !createUserDto.getPassword().equals(createUserDto.getConfirmPassword()) ) {
			log.warn("User passwords could not be confirmed: ", createUserDto.getUsername());
			return ResponseEntity.badRequest().build();
		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserDto.getPassword()));
		userRepository.save(user);
		log.info("User created",  "createUserDto.getUsername()");
		return ResponseEntity.ok(user);
	}

}
