package SemanticAnalysis.View;

import SemanticAnalysis.model.Comment;
import SemanticAnalysis.model.Film;
import SemanticAnalysis.repository.FilmRepository;
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

import java.util.List;


@Route("film")
public class FilmView extends VerticalLayout implements HasUrlParameter<Integer>{

    private final FilmRepository filmRepository;
    private VerticalLayout root;

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
        Film film = filmRepository.findById(parameter).get();

        root.add(addFilmLayout(film));
        addComments(film.getComments());
        add(root);
    }

    private void addComments(List<Comment> comments){
        for(Comment comment: comments){
            root.add(addCommentLayout(comment));
        }
    }

    VerticalLayout addFilmLayout(Film film){
        VerticalLayout root = new VerticalLayout();
        HorizontalLayout content = new HorizontalLayout();

        root.add(addHeader(film.getName(), film.getCriticsScore(), film.getViewersScore()));
        content.add(addImg(film.getImgLink()));
        content.add(addDescription(film.getDescription()));

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
        Image image = new Image("src/main/resources/FilmsImgs/" + url, "Img");
        image.setMaxHeight("1000px");
        image.setMaxWidth("700px");

        return image;
    }

    private Label addDescription(String description){
        return new Label(description);
    }

    VerticalLayout addCommentLayout(Comment comment){
        VerticalLayout root = new VerticalLayout();

        root.add(addInfo(comment.getAuthor(), comment.getScore(), comment.getScoreMLT()));
        root.add(new Label(comment.getText()));
        return root;
    }

    private HorizontalLayout addInfo(String name, Integer authorScore, Integer soreMlt){
        HorizontalLayout info = new HorizontalLayout();

        Label nameSpan = new Label(name);
        nameSpan.setWidth("50%");
        Label authorScoreSpan = new Label(String.valueOf(authorScore));
        authorScoreSpan.setWidth("25%");

        Icon icon;
        if(soreMlt==3){
            icon = VaadinIcon.SMILEY_O.create();
        } else if(soreMlt==2){
            icon = VaadinIcon.MEH_O.create();
        } else if(soreMlt==1){
            icon = VaadinIcon.FROWN_O.create();
        } else
            icon = VaadinIcon.CLOSE_SMALL.create();

        info.add(nameSpan, authorScoreSpan, icon);
        return info;
    }

}
