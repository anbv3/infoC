package com.infoc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class UserService {
	
//	@Autowired
//	UserRepository userRepository;
//
//	public Page<User> getUsers(Pageable pageable) {
//		return userRepository.findAll(pageable);
//	}
//
//	public User getUser(Long id) {
//		return userRepository.findOne(id);
//	}
//
//	@Transactional
//	public void delete(Long id) {
//		userRepository.delete(id);		
//	}
//
//	@Transactional
//	public void save(User user) {
//		userRepository.save(user);
//	}
}