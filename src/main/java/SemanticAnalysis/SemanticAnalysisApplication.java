package SemanticAnalysis;

public class SemanticAnalysisApplication {
    public static void main(String args[]) throws Exception{

        ScoreGetter getter = new ScoreGetter();
        String result = getter.predict("Потрясающей красоты фильм! Роскошные костюмы, красивые актеры (Ашвария Рай!), обалденные интерьеры... Глаз не оторвать.");
        System.out.print(result + "\n");
        result = getter.predict("Не понравился. По-моему, первый Блейд гораздо лучше");
        System.out.print(result + "\n");
        result = getter.predict("На время просмотра сюжет возвратил мне ощущение посещения видеосалона конца 80х начала 90х годов, причем на обсолютно \"дешевый\" фильм.\n");
        System.out.print(result + "\n");
    }
}
