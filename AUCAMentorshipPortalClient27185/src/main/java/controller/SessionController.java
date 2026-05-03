package controller;

import view.SessionModule;
import util.ServiceRegistry;

public class SessionController {

    private SessionModule view;

    public SessionController(SessionModule view) {
        this.view = view;
        initController();
    }

    private void initController() {
        // Initialize listeners and fetch initial data here
    }
}
