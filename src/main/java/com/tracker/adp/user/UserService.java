package com.tracker.adp.user;


import com.tracker.adp.document.DocumentService;
import com.tracker.adp.enums.Role;
import com.tracker.adp.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Data
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DocumentService documentService;

    public User saveUser(UserDTO userDto) {

        User user = new User(userDto.getName(), userDto.getEmail(), userDto.getMobile(), passwordEncoder.encode(userDto.getPassword()), Role.USER);


        return user;
    }


    public User updateUser(UserDTO userDto,  MultipartFile file) {

        Optional<User> userOptional = userRepository.findByEmail(userDto.getEmail());
        User user= userOptional.orElseThrow(()-> new ApplicationException("User Not Found!", HttpStatus.NOT_FOUND));

        user.setMobile(userDto.getMobile());
        user.setOrganization(userDto.getOrganization());
        user.setEducation(userDto.getEducation());
        user.setOccupation(userDto.getOccupation());
        user.setDesignation(userDto.getDesignation());
        user.setAddress(userDto.getAddress());


        user= userRepository.save(user);
        return user;
    }



    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserByPersonalMeetingID(String meetingID) {
        return userRepository.findByPersonalMeetingID(meetingID);
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByEmail(usernameOrEmail);
        return userOptional.orElseThrow(()->new UsernameNotFoundException("Invalid email. User not Found!"));

    }




    public User verifyToken(String token){
        User user = userRepository.findByPasswordResetToken(token).orElseThrow(() -> new ApplicationException("Token didn't match. Please provide valid token", HttpStatus.BAD_REQUEST));
        if (user.getPasswordResetToken() == null || !user.getPasswordResetToken().equals(token))
            throw new ApplicationException("Token is invalid or already used!", HttpStatus.BAD_REQUEST);
        return user;
    }



    private UserDTO mapToDTO(User entity){
        UserDTO userDTO= modelMapper.map(entity, UserDTO.class);
        return userDTO;
    }


}