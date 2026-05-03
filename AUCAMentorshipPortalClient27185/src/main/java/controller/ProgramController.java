package controller;

import view.ProgramModule;
import util.ServiceRegistry;

public class ProgramController {

    private ProgramModule view;

    public ProgramController(ProgramModule view) {
        this.view = view;
        initController();
    }

    private void initController() {
        // Initialize listeners and fetch initial data here
    }
}
