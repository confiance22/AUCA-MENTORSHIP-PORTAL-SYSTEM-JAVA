package controller;

import view.ReportModule;
import util.ServiceRegistry;

public class ReportController {

    private ReportModule view;

    public ReportController(ReportModule view) {
        this.view = view;
        initController();
    }

    private void initController() {
        // Initialize listeners and fetch initial data here
    }
}
