package notebook.model.repository;

import notebook.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface GBRepository {
    List<User> findAll();
    User save(User user);
    User create(String firstName, String lastName, String phone);
    Optional<User> findById(Long id);
    Optional<User> update(Long userId, HashMap<String, String> userInfo);
    boolean delete(Long id);
}
