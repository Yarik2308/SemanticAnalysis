package SemanticAnalysis.View;

import SemanticAnalysis.model.Comment;
import SemanticAnalysis.model.Film;
import SemanticAnalysis.repository.FilmRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;


@Route("film")
public class FilmView extends VerticalLayout implements HasUrlParameter<Integer>{

    private final FilmRepository filmRepository;
    private VerticalLayout root;
    private VerticalLayout commentsLayout;

    public FilmView(FilmRepository repository){
        this.filmRepository = repository;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer parameter) {
        if(parameter==null){
            add(new Label("Film Id?"));
            return;
        }
        root = new VerticalLayout();
        commentsLayout = new VerticalLayout();
        Film film = filmRepository.findById(parameter).get();
        root.add(addFilmLayout(film));

        Button button = new Button("Все отзывы");
        button.addClickListener(clickEvent ->addComments(film.getComments()));
        root.add(button);

        addComments(film.getComments());
        root.add(commentsLayout);

        add(root);
    }

    private void addComments(List<Comment> comments){
        commentsLayout.removeAll();
        for(Comment comment: comments){
            commentsLayout.add(addCommentLayout(comment));
        }
    }

    VerticalLayout addFilmLayout(Film film){
        VerticalLayout root = new VerticalLayout();
        HorizontalLayout content = new HorizontalLayout();

        root.add(addHeader(film.getName(), film.getCriticsScore(), film.getViewersScore()));

        VerticalLayout img = new VerticalLayout();
        img.add(addImg(film.getImgLink()));
        img.setWidth("25%");

        VerticalLayout descriptionAndChar = new VerticalLayout();
        descriptionAndChar.add(addDescription(film.getDescription()));
        descriptionAndChar.add(addChart(film.getComments()));
        descriptionAndChar.setWidth("75%");

        content.add(img);
        content.add(descriptionAndChar);

        root.add(content);
        return root;
    }

    private HorizontalLayout addHeader(String name, double criticsScore, double viewersScore ){
        Label nameSpan = new Label(name);
        nameSpan.setWidth("50%");
        Label criticsScoreSpan = new Label("Рейтинг критиков: " + criticsScore);
        criticsScoreSpan.setWidth("25%");
        Label viewersScoreSpan = new Label("Рейтинг зрителей: " + viewersScore);
        viewersScoreSpan.setWidth("25%");
        HorizontalLayout layout = new HorizontalLayout(nameSpan, criticsScoreSpan, viewersScoreSpan);
        layout.setHeightFull();
        return layout;
    }

    private Image addImg(String url){

        Image image = new Image("/FilmsImgs/" + url, "Img");

        image.setSizeFull();

        return image;
    }

    private Label addDescription(String description){
        Label descriptionLabel = new Label(description);
        return descriptionLabel;
    }

    VerticalLayout addCommentLayout(Comment comment){
        VerticalLayout root = new VerticalLayout();

        root.add(addInfo(comment.getAuthor(), comment.getScoreMLT()));
        root.add(new Label(comment.getText()));
        return root;
    }

    private HorizontalLayout addInfo(String name, Integer soreMlt){
        HorizontalLayout info = new HorizontalLayout();

        Label nameSpan = new Label(name);
        //nameSpan.setWidth("50%");
        //Label authorScoreSpan = new Label(String.valueOf(authorScore));
        //authorScoreSpan.setWidth("25%");

        Icon icon;
        if(soreMlt==3){
            icon = VaadinIcon.SMILEY_O.create();
        } else if(soreMlt==2){
            icon = VaadinIcon.MEH_O.create();
        } else if(soreMlt==1){
            icon = VaadinIcon.FROWN_O.create();
        } else
            icon = VaadinIcon.CLOSE_SMALL.create();

        info.add(nameSpan, icon);
        return info;
    }

    private Chart addChart(List<Comment> comments){
        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Пропорция мнений");

        int positive = 0;
        int neutral = 0;
        int bad = 0;

        for(Comment comment: comments){
            if(comment.getScoreMLT()==3){
                positive++;
            } else if(comment.getScoreMLT()==2){
                neutral++;
            } else {
                bad++;
            }
        }

        DataSeries series = new DataSeries();
        DataSeriesItem positiveSeries = new DataSeriesItem("Позитивные: " + positive, positive);
        positiveSeries.setId("pos");
        series.add(positiveSeries);

        DataSeriesItem neutralSeries = new DataSeriesItem("Нормальные: " + neutral, positive);
        neutralSeries.setId("neu");
        series.add(neutralSeries);

        DataSeriesItem badSeries = new DataSeriesItem("Плохие: " + bad, positive);
        badSeries.setId("bad");
        series.add(badSeries);

        conf.addSeries(series);

        chart.addPointClickListener(e -> updateComments(e.getItem().getId(), comments));

        return chart;
    }

    private void updateComments(String id, List<Comment> comments){
        if(id.equals("pos")) {
            List<Comment> posComments = new ArrayList<>();
            for(Comment comment: comments){
                if(comment.getScoreMLT()==3){
                    posComments.add(comment);
                }
            }
            addComments(posComments);
        } else if(id.equals("neu")) {
            List<Comment> neuComments = new ArrayList<>();
            for (Comment comment : comments) {
                if (comment.getScoreMLT() == 2) {
                    neuComments.add(comment);
                }
            }
            addComments(neuComments);
        } else if(id.equals("bad")) {
            List<Comment> badComments = new ArrayList<>();
            for (Comment comment : comments) {
                if (comment.getScoreMLT() == 1) {
                    badComments.add(comment);
                }
            }
            addComments(badComments);
        }
    }

}
