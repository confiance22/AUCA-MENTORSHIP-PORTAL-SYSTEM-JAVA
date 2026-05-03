package controller;

import view.UserModule;
import util.ServiceRegistry;

public class UserController {

    private UserModule view;

    public UserController(UserModule view) {
        this.view = view;
        initController();
    }

    private void initController() {
        // Initialize listeners and fetch initial data here
    }
}
