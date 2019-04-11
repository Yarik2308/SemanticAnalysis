package SemanticAnalysis.View;

import SemanticAnalysis.model.Film;
import SemanticAnalysis.repository.FilmRepository;
import SemanticAnalysis.service.QueryDSLService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route
public class MainView extends VerticalLayout {
    //private final FilmRepository repository;

    private final QueryDSLService service;

    private Grid<Film> grid = new Grid<>(Film.class);

    private final TextField filter = new TextField("", "search");
    private final HorizontalLayout toolbar = new HorizontalLayout(filter);


    @Autowired
    public MainView(QueryDSLService service) {
        this.service = service;

        add(toolbar, grid);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> search(e.getValue()));

        //search("");
    }

    private void search(String text){
        if(text.isEmpty()){
            grid.setVisible(false);
        } else{
          grid.setItems(service.search(text));
        }
    }
}
