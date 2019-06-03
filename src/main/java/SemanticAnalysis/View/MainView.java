package SemanticAnalysis.View;

import SemanticAnalysis.model.Comment;
import SemanticAnalysis.model.Film;
import SemanticAnalysis.model.FilmIndex;
import SemanticAnalysis.repository.FilmRepository;
import SemanticAnalysis.service.QueryDSLService;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;


@Route
public class MainView extends VerticalLayout {

    private final QueryDSLService service;

    private final FilmRepository filmRepository;

    private VerticalLayout content;

    private final TextField filter = new TextField("", "search");


    @Autowired
    public MainView(QueryDSLService service, FilmRepository repository)  {
        this.service = service;
        this.filmRepository = repository;

        content = new VerticalLayout();


        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.setWidth("80%");
        filter.addValueChangeListener(e -> search(e.getValue()));

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
        RouterLink link = new RouterLink("смотреть отзывы", FilmView.class, film.getId());

        Label genres = new Label();

        for(int i = 0; i<film.getGenres().size(); i++){
            genres.add(film.getGenres().get(i));
            if(i!=film.getGenres().size()-1){
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

        des.add(link);
        des.setWidth("50%");
        filmLay.add(des);

        filmLay.add(addChart(film.getId()));

        return filmLay;
    }

    private Chart addChart(Integer filmId){
        Chart chart = new Chart(ChartType.BAR);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Пропорции мнений");
        YAxis yAxis = new YAxis();
        yAxis.setTitle("");
        conf.addyAxis(yAxis);

        int positive = 0;
        int neutral = 0;
        int bad = 0;

        Film film = filmRepository.findById(filmId).get();
        List<Comment> comments = film.getComments();

        for(Comment comment: comments){
            if(comment.getScoreMLT()==3){
                positive++;
            } else if(comment.getScoreMLT()==2){
                neutral++;
            } else {
                bad++;
            }
        }

        XAxis xAxis = new XAxis();
        xAxis.setCategories("Позитивные", "Нейтральные", "Негативные");
        //xAxis.setTitle("Отзывы");
        conf.addxAxis(xAxis);

        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Позитивные", positive));
        series.add(new DataSeriesItem("Нормальные", neutral));
        series.add(new DataSeriesItem("Плохие", bad));
        series.setName("Пропорции");
        conf.addSeries(series);

        Legend legend = new Legend();
        legend.setEnabled(false);
        conf.setLegend(legend);

        chart.setWidth("25%");

        return chart;
    }
}
