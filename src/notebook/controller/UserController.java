package notebook.controller;

import notebook.model.User;
import notebook.model.repository.GBRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UserController {
    private final GBRepository repository;

    public UserController(GBRepository repository) {
        this.repository = repository;
    }

    public void saveUser(User user) {
        repository.save(user);
    }

    public User createUser(String firstName, String lastName, String phone) {
        return repository.create(firstName, lastName, phone);
    }

    public User readUser(Long userId) throws Exception {
        List<User> users = repository.findAll();
        for (User user : users) {
            if (Objects.equals(user.getId(), userId)) {
                return user;
            }
        }

        throw new RuntimeException("User not found");
    }

    public void updateUser(Long userId, HashMap<String,String> userInfo) {
        //repository.setId(userId);
        //update.setId(Long.parseLong(userId));
        repository.update(userId, userInfo);
    }

    public List<User> readAll(){
        return repository.findAll();
    }

    public void deleteUser(User deletedUser) {

        repository.delete(deletedUser.getId());

    }

}
