package br.com.test.user.service;

import br.com.test.user.dto.UserDto;
import br.com.test.user.model.UserEntity;
import br.com.test.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public void create(final UserDto user){
        UserEntity entity = dtoToEntity(user);
        repository.save(entity);
    }

    public Iterable<UserEntity> getAll(){
        return repository.findAll();
    }

    public UserEntity dtoToEntity(UserDto user){
        UserEntity entity = new UserEntity();
        entity.setAge(user.getAge());
        entity.setName(user.getName());
        return entity;
    }
}
