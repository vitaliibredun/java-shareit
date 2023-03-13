package ru.practicum.shareit.user.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.UserValidation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserValidation validation;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository repository,
                           @Qualifier("userValidationRepository") UserValidation validation,
                           UserMapper mapper) {
        this.repository = repository;
        this.validation = validation;
        this.mapper = mapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        validation.checkUserData(userDto);
        User user = mapper.toModel(userDto);
        User userFromRepository = repository.save(user);
        return mapper.toDto(userFromRepository);
    }

    @Override
    public UserDto findUser(Integer userId) {
        validation.checkUserExist(userId);
        User userFromStorage = repository.findById(userId).orElseThrow();
        return mapper.toDto(userFromStorage);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Integer userId, UserDto userDto) {
        User user = mapper.toModel(userDto);
        Optional<User> optionalUser = repository.findById(userId);
        if (optionalUser.isPresent()) {
            User userFromRepository = optionalUser.get();
            if (user.getName() != null) {
                userFromRepository.setName(user.getName());
                repository.saveAndFlush(userFromRepository);
            }
            if (user.getEmail() != null) {
                userFromRepository.setEmail(user.getEmail());
                repository.saveAndFlush(userFromRepository);
            }
        }
        return mapper.toDto(optionalUser.orElseThrow());
    }

    @Override
    public void deleteUser(Integer userId) {
        repository.deleteById(userId);
    }
}
