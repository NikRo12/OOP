module ru.nsu.romanenko {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens ru.nsu.romanenko to javafx.fxml;
    opens ru.nsu.romanenko.controller to javafx.fxml;
    opens ru.nsu.romanenko.model to com.google.gson;
    exports ru.nsu.romanenko;
}