package sbootproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sbootproject.model.request.UserDetailsRequestModel;
import sbootproject.model.response.OperationStatusModel;
import sbootproject.model.response.RequestOperationStatus;
import sbootproject.model.response.UserRest;
import sbootproject.service.intrf.UserService;
import sbootproject.shared.dto.UserDto;

@RestController   
@RequestMapping(path="/users")
public class UserController {
		
	@Autowired
	UserService userService;	

		@GetMapping
		public String hello() {
			return "Hello from /users";
		}
	
		@GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
		public UserRest getUser(@PathVariable String id) throws Exception {
			UserRest returnValue = new UserRest();
			
			UserDto userDto = userService.getUserByUserId(id);
			BeanUtils.copyProperties(userDto, returnValue);
			
			return returnValue;
		}
		
		
	    @CrossOrigin(origins = "http://localhost:3000")
		@PostMapping
	    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {	
	    	UserRest returnValue = new UserRest();
	    	
	    	ModelMapper modelMapper = new ModelMapper();
	    	UserDto userDto = modelMapper.map(userDetails, UserDto.class);
	    	
	    	UserDto createdUser = userService.createUser(userDto);
	    	returnValue = modelMapper.map(createdUser, UserRest.class);
	    		    	
	    	return returnValue;
	    }
		
		
		@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
		public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
			UserRest returnValue = new UserRest();
			
	    	UserDto userDto = new UserDto();
	        	    	
	    	BeanUtils.copyProperties(userDetails, userDto);
	    		    		        
	    	UserDto updatedUser = userService.updateUser(id, userDto);
	    	
//	        System.out.println(updatedUser.getFirstname());

	    	
	    	BeanUtils.copyProperties(updatedUser, returnValue);
			
//			UserDto userDto = new UserDto();
//			userDto = new ModelMapper().map(userDetails, UserDto.class);
//
//			UserDto updateUser = userService.updateUser(id, userDto);
//			returnValue = new ModelMapper().map(updateUser, UserRest.class);

			return returnValue;
		}
		
		@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
		public OperationStatusModel deleteUser(@PathVariable String id) {
			OperationStatusModel returnValue = new OperationStatusModel();
			returnValue.setOperationName(RequestOperationName.DELETE.name());

			userService.deleteUser(id);

			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
			return returnValue;
		}
		
		@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
		public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
				@RequestParam(value = "limit", defaultValue = "2") int limit) {
			List<UserRest> returnValue = new ArrayList<>();

			List<UserDto> users = userService.getUsers(page, limit);
			
			for (UserDto userDto : users) {
				UserRest userModel = new UserRest();
				BeanUtils.copyProperties(userDto, userModel);
				returnValue.add(userModel);
			}

			return returnValue;
		}

}
