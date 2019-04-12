package SemanticAnalysis.View;

import SemanticAnalysis.model.Film;
import SemanticAnalysis.model.FilmIndex;
import SemanticAnalysis.model.Genre;
import SemanticAnalysis.service.QueryDSLService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ClickableRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.Router;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route
public class MainView extends VerticalLayout {
    //private final FilmIndexRepository repository;

    private final QueryDSLService service;

    //private Grid<FilmIndex> grid = new Grid<>(FilmIndex.class);
    private VerticalLayout content;

    private final TextField filter = new TextField("", "search");
    //private final HorizontalLayout toolbar = new HorizontalLayout(filter);


    @Autowired
    public MainView(QueryDSLService service) {
        this.service = service;
        content = new VerticalLayout();

//        grid.setColumns("name", "genres", "description");
//        grid.setVisible(false);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.setWidth("80%");
        filter.addValueChangeListener(e -> search(e.getValue()));

        //grid.addItemClickListener(e -> new RouterLink("film", FilmView.class, e.getItem().getId()));
        add(filter, content);
    }

    private void search(String text){
        content.removeAll();

        if(text.isEmpty()){
            content.setVisible(false);
        } else{
            content.setVisible(true);
            for(FilmIndex film: service.search(text)){
                content.add(addFilm(film));
            }
        }
    }


    private HorizontalLayout addFilm(FilmIndex film){
        HorizontalLayout filmLay = new HorizontalLayout();
        VerticalLayout nameAndGenres = new VerticalLayout();

        RouterLink name = new RouterLink(film.getName(), FilmView.class, film.getId());
        //Label name = new Label(film.getName());

        //System.out.println(film.getName());
        Label genres = new Label();

        for(int i = 0; i<film.getGenres().size(); i++){
            genres.add(film.getGenres().get(i));
            if(i!=film.getGenres().size()){
                genres.add(", ");
            } else{
                genres.add(".");
            }
        }

        nameAndGenres.add(name, genres);
        nameAndGenres.setWidth("25%");
        filmLay.add(nameAndGenres);

        VerticalLayout des = new VerticalLayout();
        if(film.getDescription().length()>300) {
            des.add(new Label(film.getDescription().substring(0, 300) + "..."));
        } else {
            des.add(new Label(film.getDescription()));
        }
        
        filmLay.add(des);
        return filmLay;
    }
}