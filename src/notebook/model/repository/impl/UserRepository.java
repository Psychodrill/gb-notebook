package notebook.model.repository.impl;
import notebook.util.DBConnector;
import notebook.util.UserValidator;
import notebook.util.mapper.impl.UserMapper;
import notebook.model.User;
import notebook.model.repository.GBRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class UserRepository implements GBRepository {
    private final UserMapper mapper;
    //private final FileOperation operation;
    private List<User> usersRep;

    public UserRepository() {
        this.mapper = new UserMapper();
        //this.operation = operation;
        this.usersRep = findAll();
    }

    @Override
    public List<User> findAll() {
        List<String> lines = readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    public void userValid(User user){
        UserValidator validator = new UserValidator();
        user.setFirstName(validator.isNameValid(user.getFirstName()));
        user.setLastName(validator.isNameValid(user.getLastName()));
    }

    @Override
    public User save(User user) {
        userValid(user);
        List<User> users = usersRep;
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id){
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        write(users);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(Long userId, HashMap<String,String>userInfo) {
        List<User> users = usersRep;
        User editUser = users.stream()
                .filter(u -> u.getId()
                        .equals(userId))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        if(!userInfo.get("Firstname").isEmpty()){
            editUser.setFirstName(userInfo.get("Firstname"));
        }
        if(!userInfo.get("Lastname").isEmpty()){
            editUser.setLastName(userInfo.get("Lastname"));
        }
        if(!userInfo.get("Phone").isEmpty()){
            editUser.setPhone(userInfo.get("Phone"));
        }
        
        write(users);
        return Optional.of(editUser);
    }

    @Override
    public boolean delete(Long id) {

        List<User> users = usersRep;
        User deletedUser = users.stream()
        .filter(u -> u.getId()
                .equals(id))
        .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        users.remove(deletedUser);
        write(users);
        return false;
    }

    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u: users) {
            lines.add(mapper.toInput(u));
        }
        saveAll(lines);
    }


    private List<String> readAll() {
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(DBConnector.DB_PATH);
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            if (line != null) {
                lines.add(line);
            }
            while (line != null) {
                // считываем остальные строки в цикле
                line = reader.readLine();
                if (line != null) {
                    lines.add(line);
                }
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void saveAll(List<String> data) {
        try (FileWriter writer = new FileWriter(DBConnector.DB_PATH, false)) {
            for (String line : data) {
                // запись всей строки
                writer.write(line);
                // запись по символам
                writer.append('\n');
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public User create(String firstName, String lastName, String phone) {

        User user = new User(firstName, lastName,phone);
        return save(user);
    }


}
