package com.inventory.inventory_backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory_backend.dto.LoginRequest;
import com.inventory.inventory_backend.entity.User;
import com.inventory.inventory_backend.repository.UserRepository;
import com.inventory.inventory_backend.security.JwtUtils;
import com.inventory.inventory_backend.service.UserService;

import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    
	private final UserService userService;
    
    private final JwtUtils jwtUtils;
    
    @Value
    ("${app.jwt.expiration}")
    private long jwtExpiration;
    
   
    private final PasswordEncoder passwordEncoder;
    
    private final UserRepository repo;
    
    


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        UserDetails user = userService.loadUserByUsername(request.getUsername());
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String hashed = encoder.encode(request.getPassword());
        Optional<User> userdata = repo.findByUsername(request.getUsername());
        
        if (user != null && userdata.get().getPassword().equalsIgnoreCase(user.getPassword())) {
            String token = jwtUtils.generateToken(user.getUsername());

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) (jwtExpiration / 1000));
            response.addCookie(cookie);

            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out");
    }
}

