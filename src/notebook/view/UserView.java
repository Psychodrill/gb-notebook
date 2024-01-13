package notebook.view;

import notebook.controller.UserController;
import notebook.model.User;
import notebook.util.Commands;

import java.util.HashMap;

import java.util.Scanner;

public class UserView {
    private final UserController userController;

    public UserView(UserController userController) {
        this.userController = userController;
    }

    public void run(){
        Commands com;

        while (true) {
            String command = prompt("Введите команду: ");
            com = Commands.valueOf(command);
            if (com == Commands.EXIT) return;
            HashMap<String, String> userInfo=null;
            switch (com) {
                case CREATE:
                    // User u = createUser();
                    // userController.saveUser(u);
                    userInfo = getUserInformation();
                    userController.createUser(userInfo.get("Firstname"),userInfo.get("Lastname"), userInfo.get("Phone"));
                    break;
                case READ:
                    // String id = prompt("Идентификатор пользователя: ");
                    // try {
                    //     User user = userController.readUser(Long.parseLong(id));
                    //     System.out.println(user);
                    //     System.out.println();
                    // } catch (Exception e) {
                    //     throw new RuntimeException(e);
                    getUserById();
                    // }
                    break;
                case UPDATE:
                    User editedUser =getUserById();
                    userInfo = getUserInformation();
                    //userController.createUser(userInfo.get("Имя"),userInfo.get("Фамилия"), userInfo.get("Телефон"));
                    userController.updateUser(editedUser.getId(), userInfo);
                    break;
                case LIST:
                    System.out.println(userController.readAll());;
                    break;
                case DELETE:
                    User deletedUser =getUserById();
                    userController.deleteUser(deletedUser);
                    System.out.println("User has been deleted succesfully!");
                    break;
                        
            }
        }
    }

    private String prompt(String message) {
        Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }

    private User getUserById(){
        String userId = prompt("Enter user id: ");
        User user = null;
        try {
            user = userController.readUser(Long.parseLong(userId));
            System.out.println(user);
            System.out.println();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    private HashMap<String, String> getUserInformation() {
        HashMap<String, String> userInfo = new HashMap<String, String>();
        String firstName = prompt("Имя: ");
        String lastName = prompt("Фамилия: ");
        String phone = prompt("Номер телефона: ");
        userInfo.put("Firstname", firstName);
        userInfo.put("Lastname", lastName);
        userInfo.put("Phone",phone);
        return userInfo;
    }
}
