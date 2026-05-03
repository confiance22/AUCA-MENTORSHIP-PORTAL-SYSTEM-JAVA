package controller;

import view.MentorProfileModule;
import util.ServiceRegistry;

public class MentorProfileController {

    private MentorProfileModule view;

    public MentorProfileController(MentorProfileModule view) {
        this.view = view;
        initController();
    }

    private void initController() {
        // Initialize listeners and fetch initial data here
    }
}
